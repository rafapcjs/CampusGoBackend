package com.CampusGo.schelude.presentation.controller;

import com.CampusGo.schelude.presentation.dto.ScheludeResponseDTO;
import com.CampusGo.schelude.presentation.payload.CreateScheludeRequest;
import com.CampusGo.schelude.service.interfaces.ScheludeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.CampusGo.commons.configs.api.routes.ApiRoutes.SCHELUDE_REGISTER;

@Tag(name = "Horarios", description = "Operaciones relacionadas con la gestión de horarios")
@RestController
@RequiredArgsConstructor
public class ScheludeController {

    private final ScheludeService scheludeService;


    @Operation(
            summary = "Registar Horario",
            description = "Crea registro de horario por asignatura con base a las referencias y restrinciones."
    )
    @PostMapping(SCHELUDE_REGISTER)
    public ResponseEntity<ScheludeResponseDTO> createSchelude(@Valid @RequestBody CreateScheludeRequest request) {

        return ResponseEntity.ok(scheludeService.createSchelude(request));
    }
}
