package com.CampusGo.academicPeriod.service.interfaces;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ACADEMIC")
public class AcademicService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false)
    private Integer code;

    @Column(length = 10)
    private String nSemestre;

    @Column(unique = true, nullable = false)
    private Integer anio;






}
