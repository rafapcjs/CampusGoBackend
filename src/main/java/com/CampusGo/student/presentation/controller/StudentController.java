package com.CampusGo.student.presentation.controller;

import com.CampusGo.commons.configs.api.routes.ApiRoutes;
import com.CampusGo.security.presentation.dto.AuthResponseDto;
import com.CampusGo.student.presentation.payload.StudentPayload;
import com.CampusGo.student.service.interfaces.StudentService;
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

import static com.CampusGo.commons.configs.api.routes.ApiRoutes.STUDENT_BASE;
import static com.CampusGo.commons.configs.api.routes.ApiRoutes.STUDENT_REGISTER;


@Tag(name = "Estudiantes", description = "Operaciones relacionadas con la gestión de estudiantes")
@RestController
@RequiredArgsConstructor
public class StudentController {

    final private StudentService studentService;

    @Operation(
            summary = "Registrar estudiante",
            description = "Permite registrar un nuevo estudiante en la plataforma.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Estudiante registrado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
            }
    )
    @PostMapping(STUDENT_REGISTER)
    public ResponseEntity<AuthResponseDto> registerStudent(@Valid @RequestBody StudentPayload payload) throws URISyntaxException {
        AuthResponseDto response = studentService.registerStudent(payload);
        return ResponseEntity.created(new URI(STUDENT_REGISTER)).build();
    }

    @Operation(
            summary = "Actualizar datos del estudiante",
            description = "Permite al estudiante actualizar su información personal.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Estudiante actualizado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
            }
    )
    @PutMapping(STUDENT_BASE + "/update")
    public ResponseEntity<AuthResponseDto> updateStudent(@Valid @RequestBody StudentPayload payload) throws URISyntaxException {
        AuthResponseDto response = studentService.updateStudent(payload);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener información del estudiante actual",
            description = "Retorna la información del estudiante autenticado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Datos del estudiante encontrados",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StudentPayload.class))),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
            }
    )
    @GetMapping(STUDENT_BASE + "/me")
    public ResponseEntity<StudentPayload> getCurrentStudent() {
        StudentPayload student = studentService.getCurrentStudent();
        return ResponseEntity.ok(student);
    }
}