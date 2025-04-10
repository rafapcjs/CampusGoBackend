package com.CampusGo.grade.persistencie.entity;

import com.CampusGo.student.persistencie.entity.Student;
import com.CampusGo.subject.persistencie.entity.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "GRADE")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false)
    private Integer code;

    @ManyToOne
    @JoinColumn(name = "codAsignatureFk", referencedColumnName = "code", nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "codEstudianteFk", nullable = false)
    private Student student;

    @Column
    private float corte1;

    @Column
    private float corte2;

    @Column
    private float corte3;

    @Column
    private float corte4;



}
