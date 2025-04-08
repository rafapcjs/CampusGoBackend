package com.CampusGo.subject.persistencie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(unique = true, nullable = false)
    private Integer codePeriodoAca;

    @Column(unique = true, nullable = false)
    private Integer codeProfeAsignado;

}
