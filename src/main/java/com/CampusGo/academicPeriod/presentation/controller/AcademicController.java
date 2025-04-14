package com.CampusGo.academicPeriod.presentation.controller;

import com.CampusGo.academicPeriod.presentation.dto.AcademicResponseDto;
import com.CampusGo.academicPeriod.presentation.payload.CreateAcademicRequest;
import com.CampusGo.academicPeriod.presentation.payload.UpdateAcademicRequest;
import com.CampusGo.academicPeriod.service.interfaces.AcademicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.CampusGo.commons.configs.api.routes.ApiRoutes.*;

@Tag(name = "Periodo-Academico", description = "Operaciones relacionadas con la gestión de periodos academicos")
@RestController
public class AcademicController {

    @Autowired
    private AcademicService academicService;

    @Operation(
            summary = "Lista periodos academicos",
            description = "Lista todos los periodos academicos eexistentes."
    )
    @GetMapping(ACADEMIC_LIST)
    public List<AcademicResponseDto> getAllAcademicPeriods() {
        return academicService.getAllAcademicPeriods();
    }

    @Operation(
            summary = "Crea un periodo academico.",
            description = "Crea el registro de un nuevo periodo academico."
    )
    @PostMapping(ACADEMIC_REGISTER)
    public AcademicResponseDto createAcademic(@RequestBody CreateAcademicRequest request) {
        return academicService.createAcademicPeriod(request);
    }


    @Operation(
            summary = "Actualiza un periodo academico.",
            description = "Modifica los valores de un periodo academico, con base al codigo."
    )
    @PutMapping(ACADEMIC_UPDATE)
    public AcademicResponseDto updateAcademic(@RequestBody UpdateAcademicRequest request) {
        return academicService.updateAcademicPeriod(request);
    }
}
