package com.CampusGo.grade.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectGradeResponse {
    private String subjectName;
    private float corte1;
    private float corte2;
    private float corte3;
    private float corte4;
}
