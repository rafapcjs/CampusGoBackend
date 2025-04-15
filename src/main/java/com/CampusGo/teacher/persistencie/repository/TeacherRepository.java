package com.CampusGo.teacher.persistencie.repository;

import com.CampusGo.teacher.persistencie.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository  extends JpaRepository<Teacher, Long> {
    boolean existsByTeacherCode(String teacherCode);
    Optional<Teacher> findByUserId(Long userId);
    Optional <Teacher> findByTeacherCode(String teacherCode);

}