package com.CampusGo.schelude.presentation.payload;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateScheludeRequest {

    @NotNull
    private Integer codeAsignatureFk;

    @NotNull
    @Min(value = 0, message = "El día debe estar entre 0 y 7")
    @Max(value = 7, message = "El día debe estar entre 0 y 7")
    private Integer dia;

    @NotBlank
    private String horaInicial;

    @NotBlank
    private String horaFinal;

}
