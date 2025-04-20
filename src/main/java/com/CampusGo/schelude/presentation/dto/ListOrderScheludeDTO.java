package com.CampusGo.schelude.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListOrderScheludeDTO {
    private Integer code;
    private Integer codeSubject;
    private String name;
    private String dia;
    private String horaInicial;
    private String horaFinal;
    private String nameTeacher;
}
