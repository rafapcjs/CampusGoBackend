package com.CampusGo.academicPeriod.persistencie.repository;

import com.CampusGo.academicPeriod.persistencie.entity.Academic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicRepository extends JpaRepository<Academic, Integer> {

    // Método para buscar un periodo académico por su code (único)
    Optional<Academic> findByCode(Integer code);

}
