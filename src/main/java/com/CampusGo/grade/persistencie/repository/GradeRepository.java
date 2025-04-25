package com.CampusGo.grade.persistencie.repository;

import com.CampusGo.grade.persistencie.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {
    Optional<Grade> findTopByOrderByCodeDesc();

    Optional<Grade> findByStudentIdAndSubjectCode(Long studentId, Integer subjectCode);

}
