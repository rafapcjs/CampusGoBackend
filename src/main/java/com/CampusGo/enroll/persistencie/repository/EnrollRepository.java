package com.CampusGo.enroll.persistencie.repository;

import com.CampusGo.enroll.persistencie.entity.Enroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollRepository extends JpaRepository<Enroll, Integer> {
    Optional<Enroll> findTopByOrderByCodeDesc();

    boolean existsByStudentIdAndSubjectCode(Long studentId, Integer subjectCode);

    Optional<Enroll> findByCode(Integer code);


}