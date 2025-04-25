package com.CampusGo.enroll.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollInfoDTO {
    private Integer code;
    private Integer codAsignatureFk;
    private String nameAsignature;
    private Long codEstudianteFk;
    private String fullName;
    private Timestamp fechaRegistra;
}