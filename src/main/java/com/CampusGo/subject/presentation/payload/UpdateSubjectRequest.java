package com.CampusGo.subject.presentation.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSubjectRequest {

    @NotBlank(message = "El nombre de la asignatura es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @NotNull(message = "El código del periodo académico es obligatorio")
    @Min(value = 1, message = "El código del periodo académico debe ser mayor que 0")
    private Integer codePeriodoAca;

    @NotBlank(message = "el codigo del teacher es obligatoria")
    @Min(value = 1, message = "El código del periodo académico debe ser mayor que 0")
    private String codeTeacher ;
}
