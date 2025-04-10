package com.CampusGo.subject.persistencie.entity;

import com.CampusGo.academicPeriod.persistencie.entity.Academic;
import com.CampusGo.schelude.persistencie.entity.Schelude;
import com.CampusGo.teacher.persistencie.entity.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SUBJECT")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer code;

    @Column(length = 50)
    private String name;

    @Column(name = "codePeriodoAca", insertable = false, updatable = false)
    private Integer codePeriodoAca;


    @ManyToOne
    @JoinColumn(name = "codeProfeAsignado", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "codePeriodoAca", referencedColumnName = "code")
    private Academic academic;


    @OneToMany(mappedBy = "subject")
    private List<Schelude> scheludes;

}
