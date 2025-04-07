package com.CampusGo.security.persistence.repository;

import com.CampusGo.security.persistence.entity.RoleEntity;
import com.CampusGo.security.persistence.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    boolean existsByRoleEnum(RoleEnum roleEnum);
    Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum);

    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);
}
