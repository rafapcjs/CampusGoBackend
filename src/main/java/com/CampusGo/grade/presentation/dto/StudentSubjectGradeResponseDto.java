package com.CampusGo.grade.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentSubjectGradeResponseDto {
    /**
     * Código del estudiante.
     */
    private final Integer studentCode;

    /**
     * Nombre completo del estudiante.
     */
    private final String studentName;

    /**
     * Nota del primer corte.
     */
    private final float corte1;

    /**
     * Nota del segundo corte.
     */
    private final float corte2;

    /**
     * Nota del tercer corte.
     */
    private final float corte3;

    /**
     * Nota del cuarto corte.
     */
    private final float corte4;
}