package com.CampusGo.subject.presentation.controller;

import com.CampusGo.commons.helpers.pageable.PageableUtil;
import com.CampusGo.subject.presentation.dto.SubjectDetailsResponseDto;
import com.CampusGo.subject.presentation.payload.CreateSubjectRequest;
import com.CampusGo.subject.presentation.payload.UpdateSubjectRequest;
import com.CampusGo.subject.service.interfaces.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.CampusGo.commons.configs.api.routes.ApiRoutes.*;

@RestController
@Tag(name = "Subjects", description = "Operaciones relacionadas con asignaturas")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @Operation(summary = "Crear un nuevo subject",
            description = "Registra una nueva asignatura en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Asignatura creada."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @PostMapping(SUBJECT_REGISTER)
    public ResponseEntity<Void> registerSubject(@RequestBody CreateSubjectRequest request)
            throws URISyntaxException {
        subjectService.save(request);
        return ResponseEntity.created(new URI(SUBJECT_REGISTER)).build();
    }

    @Operation(summary = "Listar subjects por nombre",
            description = "Devuelve asignaturas paginadas filtradas por nombre.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado correcto."),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
    })
    @GetMapping(SUBJECT_LIST_BY_ORDER_NAME)
    public ResponseEntity<Page<SubjectDetailsResponseDto>> getAllSubjectsByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);
        Page<SubjectDetailsResponseDto> subjects = subjectService.getSubjectsByOrderName(pageable);
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Listar subjects por código",
            description = "Devuelve asignaturas paginadas filtradas por código.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado correcto."),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
    })
    @GetMapping(SUBJECT_LIST_BY_ORDER_CODE)
    public ResponseEntity<Page<SubjectDetailsResponseDto>> getAllSubjectsByCode(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "code") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);
        Page<SubjectDetailsResponseDto> subjects = subjectService.getSubjectsByOrderCode(pageable);
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Asignaturas del profesor autenticado",
            description = "Devuelve las asignaturas que imparte el profesor en sesión.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado correcto."),
            @ApiResponse(responseCode = "401", description = "No autorizado."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
    })
    @GetMapping(SUBJECT_MY_TEACHER)
    public ResponseEntity<List<SubjectDetailsResponseDto>> getSubjectsOfTeacher() {
        List<SubjectDetailsResponseDto> subjects = subjectService.getSubjectsTheTeacher();
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Asignaturas del estudiante autenticado",
            description = "Devuelve las asignaturas inscritas por el estudiante en sesión.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado correcto."),
            @ApiResponse(responseCode = "401", description = "No autorizado."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
    })
    @GetMapping(SUBJECT_MY_STUDENT)
    public ResponseEntity<List<SubjectDetailsResponseDto>> getSubjectsOfStudent() {
        List<SubjectDetailsResponseDto> subjects = subjectService.getSubjectsTheStudent();
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Buscar subjects por nombre",
            description = "Devuelve asignaturas paginadas según el nombre.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado correcto."),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
    })
    @GetMapping(SUBJECT_SEARCH_NAME + "/{name}")
    public ResponseEntity<Page<SubjectDetailsResponseDto>> searchSubjectsByName(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "code") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);
        Page<SubjectDetailsResponseDto> subjects = subjectService.geAllSubjectByName(name, pageable);
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Obtener subject por código",
            description = "Busca y devuelve una asignatura específica por su código.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Asignatura encontrada."),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
    })
    @GetMapping(SUBJECT_SEARCH_CODE + "/{code}")
    public ResponseEntity<SubjectDetailsResponseDto> getSubjectByCode(
            @PathVariable Integer code) {

        SubjectDetailsResponseDto subject = subjectService.findByCode(code);
        return ResponseEntity.ok(subject);
    }

    @Operation(summary = "Actualizar profesor de subject",
            description = "Actualiza el profesor asignado a la asignatura indicada.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Actualización exitosa."),
            @ApiResponse(responseCode = "404", description = "Asignatura o profesor no encontrados."),
            @ApiResponse(responseCode = "403", description = "Sin permiso."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
    })
    @PutMapping(SUBJECT_UPDATE + "/{codeSubject}")
    public ResponseEntity<Void> updateTeacherInSubject(
            @PathVariable Integer codeSubject,
            @RequestBody UpdateSubjectRequest request) {

        subjectService.updateSubjectByCodeTeacher(codeSubject, request);
        return ResponseEntity.noContent().build();
    }
}







