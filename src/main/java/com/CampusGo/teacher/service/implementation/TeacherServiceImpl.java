package com.CampusGo.teacher.service.implementation;

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
import com.CampusGo.teacher.persistencie.entity.Teacher;
import com.CampusGo.teacher.persistencie.repository.TeacherRepository;
import com.CampusGo.teacher.presentation.payload.TeacherPayload;

import com.CampusGo.teacher.service.interfaces.TeacherService;
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
public class TeacherServiceImpl implements TeacherService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public AuthResponseDto registerTeacher(TeacherPayload payload) {
        if (userRepository.existsByUsername(payload.getUsername())) {
            throw new ConflictException("El nombre de usuario ya existe");
        }

        if (userRepository.existsByDni(payload.getDni())) {
            throw new ConflictException("El DNI ya está registrado");
        }

        if (userRepository.existsByEmail(payload.getEmail())) {
            throw new ConflictException("El correo ya está registrado");
        }

        if (teacherRepository.existsByTeacherCode(payload.getTeacherCode())) {
            throw new ConflictException("El código del docente ya está en uso");
        }

        RoleEntity teacherRole = roleRepository.findByRoleEnum(RoleEnum.TEACHER)
                .orElseThrow(() -> new RuntimeException("Rol TEACHER no encontrado."));

        UserEntity user = UserEntity.builder()
                .username(payload.getUsername())
                .dni(payload.getDni())
                .email(payload.getEmail())
                .password(passwordEncoder.encode(payload.getPassword()))
                .phone(payload.getPhone())
                .roles(Collections.singleton(teacherRole))
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .build();

        UserEntity savedUser = userRepository.save(user);

        Teacher teacher = Teacher.builder()
                .user(savedUser)
                .teacherCode(payload.getTeacherCode())
                .build();

        teacherRepository.save(teacher);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getUsername(),
                savedUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_TEACHER"))
        );

        String token = jwtUtils.createToken(authentication);

        return new AuthResponseDto(savedUser.getUsername(), "Docente registrado correctamente", token, true);
    }

    @Override
    @Transactional
    public AuthResponseDto updateTeacher(TeacherPayload payload) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        UserEntity user = userRepository.findUserEntityByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado."));

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

        user.setUsername(payload.getUsername());
        user.setDni(payload.getDni());
        user.setEmail(payload.getEmail());
        user.setPhone(payload.getPhone());

        if (!payload.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(payload.getPassword()));
        }

        userRepository.save(user);

        teacher.setTeacherCode(payload.getTeacherCode());
        teacherRepository.save(teacher);

        return new AuthResponseDto(
                user.getUsername(),
                "Docente actualizado correctamente.",
                null,
                true
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherPayload getCurrentTeacher() {
        String currentUsername = SecurityUtils.getCurrentUsername();

        UserEntity user = userRepository.findUserEntityByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        return TeacherPayload.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .dni(user.getDni())
                .teacherCode(teacher.getTeacherCode())
                .phone(user.getPhone())
                .build();
    }
}