package com.CampusGo.schelude.presentation.controller;

import com.CampusGo.commons.configs.message.InfoMessage;
import com.CampusGo.schelude.presentation.dto.ListOrderScheludeDTO;
import com.CampusGo.schelude.presentation.dto.ScheludeResponseDTO;
import com.CampusGo.schelude.presentation.payload.CreateScheludeRequest;
import com.CampusGo.schelude.presentation.payload.UpdateScheludeRequest;
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
            summary = "Listar horario de estudiante mediante su ID",
            description = "Lista todos los horarios existentes para un estudiante ordenados por día."
    )
    @GetMapping(SCHELUDE_LIST_BY_STUDENT)
    public ResponseEntity<List<ListOrderScheludeDTO>> getScheludeByStudent() {
        List<ListOrderScheludeDTO> result = scheludeService.getOrderedScheludeByStudent();
        return ResponseEntity.ok(result);
    }


    // Controlador para actualizar un registro por su code
    @Operation(
            summary = "Actualizar horario proporcionado codigo",
            description = "Actualiza los valores (dia, horaIncial y horaFinal) de un horario identificado por su código"
    )
    @PutMapping(SCHELUDE_UPDATE)
    public ResponseEntity<ScheludeResponseDTO> updateSchelude(@Valid @RequestBody UpdateScheludeRequest request) {
        return ResponseEntity.ok(scheludeService.updateSchelude(request));
    }

    // Controlador para eliminar  un horario por su code
    @Operation(
            summary = "Eliminar horario proporcionado codigo",
            description = "Elimina un horario existente proporcionando su código"
    )
    @DeleteMapping(SCHELUDE_DELETE)
    public ResponseEntity<InfoMessage> deleteSchelude(@RequestParam Integer code) {
        InfoMessage message = scheludeService.deleteScheludeByCode(code);
        return ResponseEntity.ok(message);
    }




}
