package com.CampusGo.enroll.presentation.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateEnrollRequest {


    @NotNull
    private Integer codAsignatureFk;

    @NotNull
    private Long codEstudianteFk; // CAMBIADO A LONG

}
