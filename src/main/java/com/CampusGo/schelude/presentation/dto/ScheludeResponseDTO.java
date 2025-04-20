package com.CampusGo.schelude.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheludeResponseDTO {
    private Integer code;
    private Integer codeAsignatureFk;
    private Integer dia;
    private String horaInicial;
    private String horaFinal;
}
