package com.CampusGo.academicPeriod.presentation.payload;

import lombok.Data;

@Data
public class CreateAcademicRequest {
    private Integer nSemestre;
    private Integer anio;
}
