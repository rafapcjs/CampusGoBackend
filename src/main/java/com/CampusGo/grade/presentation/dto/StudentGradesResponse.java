package com.CampusGo.grade.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentGradesResponse {
    private String studentName;
    private List<SubjectGradeResponse> grades;
}