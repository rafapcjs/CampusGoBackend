package com.CampusGo.student.persistencie.repository;

import com.CampusGo.student.persistencie.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Repository
public interface StudentRepository  extends JpaRepository <Student, Long> {
    boolean existsByStudentCode(String studentCode);
    Optional<Student> findByUserId(Long userId);

}
