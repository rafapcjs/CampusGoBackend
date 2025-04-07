package com.CampusGo.security.persistence.entity;

 import com.CampusGo.security.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        createRoleIfNotFound(RoleEnum.TEACHER);
        createRoleIfNotFound(RoleEnum.STUDENT);
    }

    private void createRoleIfNotFound(RoleEnum roleEnum) {
        boolean exists = roleRepository.existsByRoleEnum(roleEnum);
        if (!exists) {
            RoleEntity roleEntity = RoleEntity.builder()
                    .roleEnum(roleEnum)
                    .build();
            roleRepository.save(roleEntity);
            System.out.println("Role created: " + roleEnum.name());
        }
    }
}