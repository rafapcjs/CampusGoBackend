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
import com.CampusGo.commons.helpers.payloads.ChangePasswordPayload;
import com.CampusGo.student.presentation.dto.StudentSessionDto;
import com.CampusGo.student.presentation.payload.StudentPayload;
import com.CampusGo.student.presentation.payload.StudentUpdatePayload;
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
                .lastName(payload.getLastName())
                .name(payload.getName())
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
    public AuthResponseDto updateStudent(StudentUpdatePayload payload) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        // Buscar el usuario autenticado
        UserEntity user = userRepository.findUserEntityByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        // Verificar que el estudiante exista
        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado."));

        // Validar email duplicado
        if (!user.getEmail().equals(payload.getEmail()) &&
                userRepository.existsByEmail(payload.getEmail())) {
            throw new ConflictException("El email ya está en uso.");
        }

        // Validar teléfono duplicado
        if (!user.getPhone().equals(payload.getPhone()) &&
                userRepository.existsByPhone(payload.getPhone())) {
            throw new ConflictException("El teléfono ya está en uso.");
        }

        // Actualizar los datos del usuario
        user.setUsername(payload.getUsername());
        user.setName(payload.getName());
        user.setLastName(payload.getLastName());
        user.setEmail(payload.getEmail());
        user.setPhone(payload.getPhone());

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
    public StudentSessionDto getCurrentStudent() {
        String currentUsername = SecurityUtils.getCurrentUsername();

        UserEntity user = userRepository.findUserEntityByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        return StudentSessionDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .lastName(user.getLastName())
                .name(user.getName())
                .dni(user.getDni())
                .phone(user.getPhone())
                .studentCode(student.getStudentCode())
                .build();
    }


    @Override
    @Transactional
    public void updatePasswordStudent(ChangePasswordPayload payload) {
        // 1. Obtener el nombre de usuario autenticado
        String currentUsername = SecurityUtils.getCurrentUsername();

        // 2. Buscar el usuario por su nombre de usuario
        UserEntity user = userRepository.findUserEntityByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        // 3. Verificar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(payload.getOldPassword(), user.getPassword())) {
            throw new ConflictException("La contraseña actual no es correcta.");
        }

        // 4. Validar que la nueva contraseña y su confirmación coincidan
        if (!payload.getNewPassword().equals(payload.getConfirmNewPassword())) {
            throw new ConflictException("La nueva contraseña y su confirmación no coinciden.");
        }

        // 5. Verificar que la nueva contraseña no sea igual a la actual
        if (passwordEncoder.matches(payload.getNewPassword(), user.getPassword())) {
            throw new ConflictException("La nueva contraseña no puede ser igual a la actual.");
        }

        // 6. Codificar y actualizar la nueva contraseña
        user.setPassword(passwordEncoder.encode(payload.getNewPassword()));
        userRepository.save(user);
    }


}