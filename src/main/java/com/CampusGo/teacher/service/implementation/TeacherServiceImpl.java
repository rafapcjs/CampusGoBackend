package com.CampusGo.teacher.service.implementation;

import com.CampusGo.commons.configs.utils.cloudinary.CloudinaryService;
import com.CampusGo.commons.configs.error.exceptions.AccessDeniedException;
import com.CampusGo.commons.configs.error.exceptions.ConflictException;
import com.CampusGo.commons.configs.error.exceptions.ResourceNotFoundException;
import com.CampusGo.commons.helpers.payloads.ChangePasswordPayload;
 import com.CampusGo.security.persistence.entity.RoleEntity;
import com.CampusGo.security.persistence.entity.RoleEnum;
import com.CampusGo.security.persistence.entity.UserEntity;
import com.CampusGo.security.persistence.repository.RoleRepository;
import com.CampusGo.security.persistence.repository.UserRepository;
import com.CampusGo.security.presentation.dto.AuthResponseDto;
import com.CampusGo.security.util.JwtUtils;
import com.CampusGo.security.util.SecurityUtils;

import com.CampusGo.teacher.persistencie.entity.Teacher;
import com.CampusGo.teacher.persistencie.repository.TeacherRepository;
import com.CampusGo.teacher.presentation.dto.TeacherSessionDto;
import com.CampusGo.teacher.presentation.payload.TeacherPayload;

import com.CampusGo.teacher.presentation.payload.TeacherUpdatePayload;
import com.CampusGo.teacher.service.interfaces.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;


/**
 * Service implementation for managing Teacher-related operations.
 * This service provides methods for registering, updating, and retrieving teacher information.
 * It implements the {@link TeacherService} interface.
 *
 * The service uses repositories to interact with the database for User, Teacher, and Role entities.
 * Additionally, it handles password encoding and JWT token generation.
 *
 * The methods in this service are transactional to ensure data integrity and support rollback in case of errors.
 */
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

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
                .lastName(payload.getLastName())
                .name(payload.getName())
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
    public AuthResponseDto updateTeacher(TeacherUpdatePayload payload) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        UserEntity user = userRepository.findUserEntityByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado."));


        if (!user.getEmail().equals(payload.getEmail()) &&
                userRepository.existsByEmail(payload.getEmail())) {
            throw new ConflictException("El email ya está en uso.");
        }

        if (!user.getPhone().equals(payload.getPhone()) &&
                userRepository.existsByPhone(payload.getPhone())) {
            throw new ConflictException("El teléfono ya está en uso.");
        }

        user.setUsername(payload.getUsername());
        user.setName(payload.getName());
        user.setLastName(payload.getLastName());
        user.setEmail(payload.getEmail());
        user.setPhone(payload.getPhone());


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
    public TeacherSessionDto getCurrentTeacher() {
        String currentUsername = SecurityUtils.getCurrentUsername();

        UserEntity user = userRepository.findUserEntityByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        return TeacherSessionDto.builder()
                .username(user.getUsername())
                .lastName(user.getLastName())
                .name(user.getName())
                .email(user.getEmail())
                .dni(user.getDni())
                .teacherCode(teacher.getTeacherCode())
                .phone(user.getPhone())
                .imageUrl(user.getImageUrl())
                .build();
    }




}