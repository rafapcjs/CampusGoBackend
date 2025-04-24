package com.CampusGo.enroll.presentation.controller;

import com.CampusGo.enroll.presentation.payload.BulkEnrollRequest;
import com.CampusGo.enroll.presentation.payload.CreateEnrollRequest;
import com.CampusGo.enroll.service.interfaces.EnrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.CampusGo.commons.configs.api.routes.ApiRoutes.*;

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

    @Operation(
            summary = "Matricular varias asignaturas a un estudiante",
            description = "Matricula e inicializa el registro multiple de notas de un estudiante"
    )
    @PostMapping(ENROLL_MULTI_REGISTER)
    public ResponseEntity<Void> createBulkEnroll(@Valid @RequestBody BulkEnrollRequest request) {
        enrollService.createBulkEnroll(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Eliminar registro de matricula por codigo",
            description = "Eliminar registro de matricula y su notas por codigo de matricula"
    )
    @DeleteMapping(ENROLL_DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEnroll(@RequestParam("code") Integer code) {
        enrollService.deleteEnrollByCode(code);
    }



}
