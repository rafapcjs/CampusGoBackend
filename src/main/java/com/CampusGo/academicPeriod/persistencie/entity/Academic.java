package com.CampusGo.academicPeriod.persistencie.entity;

import com.CampusGo.subject.persistencie.entity.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ACADEMIC")
public class Academic {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false)
    private Integer code;

    @Column(nullable = false)
    private Integer nSemestre;

    @Column(nullable = false)
    private Integer anio;

    @OneToMany(mappedBy = "academic")
    private List<Subject> subjects;

}
