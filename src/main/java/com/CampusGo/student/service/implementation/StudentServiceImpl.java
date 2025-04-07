package com.CampusGo.student.service.implementation;

import com.CampusGo.commons.configs.error.exceptions.ConflictException;
import com.CampusGo.commons.configs.error.exceptions.ResourceNotFoundException;
import com.CampusGo.security.persistence.entity.RoleEntity;
import com.CampusGo.security.persistence.entity.RoleEnum;
import com.CampusGo.security.persistence.entity.UserEntity;
import com.CampusGo.security.persistence.repository.RoleRepository;
import com.CampusGo.security.persistence.repository.UserRepository;
import com.CampusGo.security.presentation.dto.AuthResponseDto;
import com.CampusGo.security.util.JwtUtils;
import com.CampusGo.security.util.SecurityUtils;
import com.CampusGo.student.persistencie.entity.Student;
import com.CampusGo.student.persistencie.repository.StudentRepository;
import com.CampusGo.student.presentation.payload.StudentPayload;
import com.CampusGo.student.service.interfaces.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    @Override
    @Transactional
    public AuthResponseDto registerStudent(StudentPayload payload) {
        // Validaciones para evitar duplicados
        if (userRepository.existsByUsername(payload.getUsername())) {
            throw new ConflictException("El nombre de usuario ya existe");
        }

        if (userRepository.existsByDni(payload.getDni())) {
            throw new ConflictException("El DNI ya está registrado");
        }

        if (userRepository.existsByEmail(payload.getEmail())) {
            throw new ConflictException("El correo ya está registrado");
        }

        if (studentRepository.existsByStudentCode(payload.getStudentCode())) {
            throw new ConflictException("El código de estudiante ya está en uso");
        }

        // Buscar rol de estudiante
        RoleEntity studentRole = roleRepository.findByRoleEnum(RoleEnum.STUDENT)
                .orElseThrow(() -> new RuntimeException("Rol STUDENT no encontrado."));

        // Crear usuario
        UserEntity user = UserEntity.builder()
                .username(payload.getUsername())
                .dni(payload.getDni())
                .email(payload.getEmail())
                .password(passwordEncoder.encode(payload.getPassword()))
                .phone(payload.getPhone())
                .roles(Collections.singleton(studentRole))
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .build();

        UserEntity savedUser = userRepository.save(user);

        // Crear estudiante
        Student student = Student.builder()
                .user(savedUser)
                .studentCode(payload.getStudentCode())
                .build();

        studentRepository.save(student);

        // Generar token JWT
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getUsername(),
                savedUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT"))
        );

        String token = jwtUtils.createToken(authentication);

        return new AuthResponseDto(savedUser.getUsername(), "Estudiante registrado correctamente", token, true);
    }

    @Override
    @Transactional
    public AuthResponseDto updateStudent(StudentPayload payload) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        // Buscar el usuario autenticado
        UserEntity user = userRepository.findUserEntityByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        // Verificar que el estudiante exista
        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado."));

        // Verificar si los datos a actualizar ya existen para otro usuario
        if (!user.getDni().equals(payload.getDni()) &&
                userRepository.existsByDni(payload.getDni())) {
            throw new ConflictException("El DNI ya está en uso.");
        }

        if (!user.getEmail().equals(payload.getEmail()) &&
                userRepository.existsByEmail(payload.getEmail())) {
            throw new ConflictException("El email ya está en uso.");
        }

        if (!user.getPhone().equals(payload.getPhone()) &&
                userRepository.existsByPhone(payload.getPhone())) {
            throw new ConflictException("El teléfono ya está en uso.");
        }

        // Actualizar los datos del usuario
        user.setUsername(payload.getUsername());
        user.setDni(payload.getDni());
        user.setEmail(payload.getEmail());
        user.setPhone(payload.getPhone());

        // Si la contraseña es diferente, actualizarla
        if (!payload.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(payload.getPassword()));
        }

        userRepository.save(user);

        // Actualizar el código del estudiante
        student.setStudentCode(payload.getStudentCode());
        studentRepository.save(student);

        return new AuthResponseDto(
                user.getUsername(),
                "Estudiante actualizado correctamente.",
                null,
                true
        );
    }

    @Override
    @Transactional
    public StudentPayload getCurrentStudent() {
        String currentUsername = SecurityUtils.getCurrentUsername();

        UserEntity user = userRepository.findUserEntityByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        return StudentPayload.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .dni(user.getDni())
                .phone(user.getPhone())
                .studentCode(student.getStudentCode())
                .build();
    }

}