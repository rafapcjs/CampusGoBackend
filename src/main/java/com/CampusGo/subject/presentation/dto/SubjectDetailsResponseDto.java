package com.CampusGo.subject.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDetailsResponseDto {
    private Integer codigoAsignatura;
    private String nombreAsignatura;
    private String periodoAcademico;
    private Integer codigoAcademico;
    private String codigoProfesor;
    private String nombreProfesor;
    private String apellidoProfesor;

}
