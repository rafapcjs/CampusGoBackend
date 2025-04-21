package com.CampusGo.schelude.presentation.payload;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class UpdateScheludeRequest {

    @NotNull(message = "El código del horario es obligatorio")
    private Integer code;

    @NotNull(message = "El día es obligatorio")
    @Min(value = 0, message = "El día debe estar entre 0 y 6")
    @Max(value = 6, message = "El día debe estar entre 0 y 6")
    private Integer dia;

    @NotBlank(message = "La hora inicial es obligatoria")
    private String horaInicial;

    @NotBlank(message = "La hora final es obligatoria")
    private String horaFinal;
}
