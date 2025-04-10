package com.CampusGo.enroll.persistencie.entity;

import com.CampusGo.student.persistencie.entity.Student;
import com.CampusGo.subject.persistencie.entity.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ENROLL")
public class Enroll {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false)
    private Integer code;

    @Column(nullable = false)
    private LocalDateTime fechaRegistra;

    @ManyToOne
    @JoinColumn(name = "codEstudianteFk",nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "codAsignatureFk",referencedColumnName = "code",nullable = false)
    private Subject subject;
}
