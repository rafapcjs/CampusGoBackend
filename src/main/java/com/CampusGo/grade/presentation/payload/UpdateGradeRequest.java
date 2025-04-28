package com.CampusGo.grade.presentation.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGradeRequest {


@Positive(message = "El corte 1 debe ser mayor a 0")
private float corte1;

    @Positive(message = "El corte 2 debe ser mayor a 0")
    private float corte2;

    @Positive(message = "El corte 3 debe ser mayor a 0")

    private float corte3;

    @Positive(message = "El corte 4 debe ser mayor a 0")

    private float corte4;

}
