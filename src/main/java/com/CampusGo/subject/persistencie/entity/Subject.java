package com.CampusGo.subject.persistencie.entity;

import com.CampusGo.academicPeriod.persistencie.entity.Academic;
import com.CampusGo.schelude.persistencie.entity.Schelude;
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
    private Integer id;

    @Column(unique = true, nullable = false)
    private Integer code;

    @Column(length = 50)
    private String name;

    @Column(name = "codePeriodoAca", insertable = false, updatable = false)
    private Integer codePeriodoAca;


    @Column(nullable = false)
    private Integer codeProfeAsignado;

    @ManyToOne
    @JoinColumn(name = "codePeriodoAca", referencedColumnName = "code")
    private Academic academic;


    @OneToMany(mappedBy = "subject")
    private List<Schelude> scheludes;

}
