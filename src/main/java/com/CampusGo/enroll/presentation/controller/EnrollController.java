package com.CampusGo.enroll.presentation.controller;

import com.CampusGo.enroll.presentation.payload.CreateEnrollRequest;
import com.CampusGo.enroll.service.interfaces.EnrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.CampusGo.commons.configs.api.routes.ApiRoutes.ENROLL_REGISTER;

@Tag(name = "Matriculas", description = "Operaciones relacionadas con la gestión de matriculas")
@RestController
@RequiredArgsConstructor
public class EnrollController {

    private final EnrollService enrollService;

    @Operation(
            summary = "Matricular a un estudiante",
            description = "Matricula e inicializa el registro de notas del estudiante"
    )
    @PostMapping(ENROLL_REGISTER)
    public ResponseEntity<Void> createEnroll(@Valid @RequestBody CreateEnrollRequest request) {
        enrollService.createEnroll(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
