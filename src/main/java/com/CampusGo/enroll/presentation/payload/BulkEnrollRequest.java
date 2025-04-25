package com.CampusGo.enroll.presentation.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BulkEnrollRequest {

    @NotNull
    private Long codEstudianteFk;

    @NotNull
    private List<Integer> codAsignatureFks;
}
