package com.CampusGo.commons.initializer;

 import com.CampusGo.security.persistence.entity.RoleEntity;
 import com.CampusGo.security.persistence.entity.RoleEnum;
 import com.CampusGo.security.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
 import org.springframework.core.annotation.Order;
 import org.springframework.stereotype.Component;
 import org.springframework.transaction.annotation.Transactional;
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final TeacherInitializer teacherInitializer;

    @Override
    @Transactional
    public void run(String... args) {
        // Crear roles si no existen y forzar el flush para persistirlos inmediatamente
        createRoleIfNotFound(RoleEnum.TEACHER);
        createRoleIfNotFound(RoleEnum.STUDENT);

        // Crear el docente ya que ahora el rol TEACHER existe y está gestionado
        teacherInitializer.createTeacherForCreationsSubjects();
    }

    private void createRoleIfNotFound(RoleEnum roleEnum) {
        if (!roleRepository.existsByRoleEnum(roleEnum)) {
            RoleEntity roleEntity = RoleEntity.builder()
                    .roleEnum(roleEnum)
                    .build();
            // Guardamos y forzamos el flush para que quede en el contexto de persistencia
            roleRepository.saveAndFlush(roleEntity);
            System.out.println("Role created: " + roleEnum.name());
        } else {
            System.out.println("Role already exists: " + roleEnum.name());
        }
    }
}