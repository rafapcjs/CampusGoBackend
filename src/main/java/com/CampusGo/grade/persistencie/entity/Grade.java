package com.CampusGo.grade.persistencie.entity;

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

    @Column
    private Integer codAsignatureFk;

    @Column(nullable = false)
    private Integer codEstudianteFk;

    @Column
    private float corte1;

    @Column
    private float corte2;

    @Column
    private float corte3;

    @Column
    private float corte4;



}
