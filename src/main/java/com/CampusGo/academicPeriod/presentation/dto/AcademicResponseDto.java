package com.CampusGo.academicPeriod.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AcademicResponseDto {
    private Integer code;
    private Integer nSemestre;
    private Integer anio;
}
