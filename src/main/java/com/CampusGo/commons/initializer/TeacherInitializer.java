package com.CampusGo.commons.initializer;

import com.CampusGo.commons.configs.error.exceptions.ConflictException;
import com.CampusGo.security.persistence.entity.RoleEntity;
import com.CampusGo.security.persistence.entity.RoleEnum;
import com.CampusGo.security.persistence.entity.UserEntity;
import com.CampusGo.security.persistence.repository.RoleRepository;
import com.CampusGo.security.persistence.repository.UserRepository;
import com.CampusGo.teacher.persistencie.entity.Teacher;
import com.CampusGo.teacher.persistencie.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


@Component
@RequiredArgsConstructor
public class TeacherInitializer {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String TEACHER_USERNAME = "matricula";
    private static final String TEACHER_DNI = "11111111";
    private static final String TEACHER_EMAIL = "teacher@campusgo.com";
    private static final String TEACHER_CODE = "teacher-default-001";
    private static final String DEFAULT_PASSWORD = "matricula123";
    private static final String PHONE_NUMBER = "3000000000";

    @Transactional
    public void createTeacherForCreationsSubjects() {
        // Buscar el rol TEACHER ya gestionado en la base de datos
        RoleEntity teacherRole = roleRepository.findByRoleEnum(RoleEnum.TEACHER)
                .orElseThrow(() -> new RuntimeException("Rol TEACHER no encontrado."));

        // Validar existencia del usuario
        if (userRepository.existsByUsername(TEACHER_USERNAME)) {
            System.out.println("El usuario 'teacher' ya existe. Se omite la creación.");
            return;
        }

        // Validar existencia del código de docente
        if (teacherRepository.existsByTeacherCode(TEACHER_CODE)) {
            System.out.println("El código del docente 'teacher-default-001' ya está en uso. Se omite la creación.");
            return;
        }

        // Crear usuario y asignarle el rol TEACHER
        UserEntity user = UserEntity.builder()
                .username(TEACHER_USERNAME)
                .dni(TEACHER_DNI)
                .name(TEACHER_USERNAME)
                .lastName("default")
                .email(TEACHER_EMAIL)
                .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                .phone(PHONE_NUMBER)
                .roles(Collections.singleton(teacherRole))
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .build();

        UserEntity savedUser = userRepository.save(user);

        // Crear y guardar el docente (Teacher) vinculado al usuario
        Teacher teacher = Teacher.builder()
                .user(savedUser)
                .teacherCode(TEACHER_CODE)
                .build();

        teacherRepository.save(teacher);
        System.out.println("Docente 'teacher' creado correctamente.");
    }
}