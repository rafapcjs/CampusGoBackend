package com.CampusGo.schelude.presentation.controller;

import com.CampusGo.schelude.presentation.dto.ListOrderScheludeDTO;
import com.CampusGo.schelude.presentation.dto.ScheludeResponseDTO;
import com.CampusGo.schelude.presentation.payload.CreateScheludeRequest;
import com.CampusGo.schelude.service.interfaces.ScheludeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.CampusGo.commons.configs.api.routes.ApiRoutes.*;

@Tag(name = "Horarios", description = "Operaciones relacionadas con la gestión de horarios")
@RestController
@RequiredArgsConstructor
public class ScheludeController {

    private final ScheludeService scheludeService;


    // Controlador para la creación de un horario
    @Operation(
            summary = "Registar Horario",
            description = "Crea registro de horario por asignatura con base a las referencias y restrinciones."
    )
    @PostMapping(SCHELUDE_REGISTER)
    public ResponseEntity<ScheludeResponseDTO> createSchelude(@Valid @RequestBody CreateScheludeRequest request) {

        return ResponseEntity.ok(scheludeService.createSchelude(request));
    }


    // Controlador para listar los horarios de forma ascendente por dia
    @Operation(
            summary = "Listar todos los horarios",
            description = "El metodo lista todos los horarios disponibles de forma ascendete por dia"
    )
    @GetMapping(SCHELUDE_LIST_ORDER)
    public ResponseEntity<List<ListOrderScheludeDTO>> listAllOrderSchelude() {
        return ResponseEntity.ok(scheludeService.listAllOrderSchelude());
    }


    // Controlador para listar los horarios de un estudiante por su ID
    @Operation(
            summary = "Listar horarios por estudiante",
            description = "Lista todos los horarios existentes para un estudiante ordenados por día."
    )
    @GetMapping(SCHELUDE_LIST_BY_STUDENT)
    public ResponseEntity<List<ListOrderScheludeDTO>> getScheludeByStudent(
            @RequestParam Integer studentId
    ) {
        List<ListOrderScheludeDTO> result = scheludeService.getOrderedScheludeByStudent(studentId);
        return ResponseEntity.ok(result);
    }


}
