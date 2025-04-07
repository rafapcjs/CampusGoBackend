package com.CampusGo.teacher.presentation.controller;

 import com.CampusGo.security.presentation.dto.AuthResponseDto;
  import com.CampusGo.teacher.presentation.payload.TeacherPayload;
import com.CampusGo.teacher.service.interfaces.TeacherService;
 import io.swagger.v3.oas.annotations.Operation;
 import io.swagger.v3.oas.annotations.media.Content;
 import io.swagger.v3.oas.annotations.media.Schema;
 import io.swagger.v3.oas.annotations.responses.ApiResponse;
 import io.swagger.v3.oas.annotations.tags.Tag;
 import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static com.CampusGo.commons.configs.api.routes.ApiRoutes.*;

@Tag(name = "Docentes", description = "Operaciones relacionadas con la gestión de docentes")
@RestController
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @Operation(
            summary = "Registrar docente",
            description = "Registra un nuevo docente en la plataforma.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Docente registrado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
            }
    )
    @PostMapping(TEACHER_REGISTER)
    public ResponseEntity<AuthResponseDto> registerTeacher(@Valid @RequestBody TeacherPayload payload) throws URISyntaxException {
        AuthResponseDto response = teacherService.registerTeacher(payload);
        return ResponseEntity.created(new URI(TEACHER_REGISTER)).build();
    }

    @Operation(
            summary = "Actualizar datos del docente",
            description = "Permite al docente actualizar su información personal.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Docente actualizado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
            }
    )
    @PutMapping(TEACHER_BASE + "/update")
    public ResponseEntity<AuthResponseDto> updateTeacher(@Valid @RequestBody TeacherPayload payload) throws URISyntaxException {
        AuthResponseDto response = teacherService.updateTeacher(payload);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener información del docente actual",
            description = "Devuelve los datos del docente autenticado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Datos del docente encontrados",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TeacherPayload.class))),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
            }
    )
    @GetMapping(TEACHER_BASE + "/me")
    public ResponseEntity<TeacherPayload> getCurrentTeacher() {
        TeacherPayload teacher = teacherService.getCurrentTeacher();
        return ResponseEntity.ok(teacher);
    }
}