package com.CampusGo.academicPeriod.presentation.payload;

import lombok.Data;

@Data
public class UpdateAcademicRequest {
    private Integer code;
    private Integer nSemestre;
    private Integer anio;
}
