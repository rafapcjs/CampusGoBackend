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
import static com.CampusGo.commons.configs.api.routes.ApiRoutes.*;
@RestController
@RequiredArgsConstructor
@Tag(name = "Subject")
public class SubjectController {

      final private  SubjectService subjectService;


    @Operation(summary = "Crear un nuevo subject",
            description = "Registra una nueva asignatura en el sistema. Se espera que todos los parámetros sean válidos.",
            tags = {"Subjects"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro exitoso. La asignatura ha sido creada con éxito."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos. Asegúrese de que todos los parámetros sean correctos."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor. No se pudo procesar la solicitud.")
    })
    @PostMapping(SUBJECT_REGISTER)
    public ResponseEntity<?> registerSubject(@RequestBody CreateSubjectRequest request) throws URISyntaxException {
        subjectService.save(request);
        return ResponseEntity.created(new URI(SUBJECT_REGISTER)).build();
    }

    @Operation(summary = "Obtener todos los subjects ordenados por nombre",
            description = "Devuelve una lista paginada de asignaturas ordenadas por el nombre, con detalles adicionales.",
            tags = {"Subjects"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud exitosa. Retorna la lista paginada de asignaturas."),
            @ApiResponse(responseCode = "400", description = "Parámetros de solicitud inválidos."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @GetMapping(SUBJECT_LIST_BY_ORDER_NAME)
    public ResponseEntity<Page<SubjectDetailsResponseDto>> getAllSubjectByOrderName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);
        Page<SubjectDetailsResponseDto> subjects = subjectService.getSubjectsByOrderName(pageable);
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Obtener todos los subjects ordenados por código",
            description = "Devuelve una lista paginada de asignaturas ordenadas por el código, con detalles adicionales.",
            tags = {"Subjects"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud exitosa. Retorna la lista paginada de asignaturas."),
            @ApiResponse(responseCode = "400", description = "Parámetros de solicitud inválidos."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @GetMapping(SUBJECT_LIST_BY_ORDER_CODE)
    public Page<SubjectDetailsResponseDto> getAllSubjectByOrderCode(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "code") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);
        return subjectService.getSubjectsByOrderCode(pageable);
    }

    @Operation(summary = "Buscar asignaturas por nombre",
            description = "Devuelve una lista paginada de asignaturas filtradas por el nombre (coincidencia parcial).",
            tags = {"Subjects"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud exitosa. Retorna las asignaturas encontradas."),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @GetMapping(SUBJECT_SEARCH_NAME + "/{name}")
    public ResponseEntity<Page<SubjectDetailsResponseDto>> searchSubjectsByName(
            @PathVariable("name") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "code") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);
        Page<SubjectDetailsResponseDto> subjects = subjectService.geAllSubjectByName(name, pageable);
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Obtener una asignatura por su código",
            description = "Busca y devuelve una asignatura específica mediante su código.",
            tags = {"Subjects"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Asignatura encontrada. Devuelve los detalles de la asignatura."),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @GetMapping(SUBJECT_SEARCH_CODE + "/{code}")
    public ResponseEntity<SubjectDetailsResponseDto> getSubjectByCode(@PathVariable Integer code) {
        SubjectDetailsResponseDto subjectDetailsResponseDto = subjectService.findByCode(code);
        return ResponseEntity.ok(subjectDetailsResponseDto);
    }

    @Operation(summary = "Actualizar el profesor asignado a una asignatura",
            description = "Permite actualizar el profesor asignado a una asignatura específica mediante su código.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Actualización exitosa. El profesor ha sido asignado correctamente."),
            @ApiResponse(responseCode = "404", description = "Asignatura o profesor no encontrados."),
            @ApiResponse(responseCode = "402", description = "Requiere pago o permisos adicionales.")
    })
    @PutMapping(SUBJECT_UPDATE + "/{codeSubject}")
    public ResponseEntity<Void> updateTeacherInSubject(
            @Parameter(description = "Código de la asignatura a actualizar") @PathVariable Integer codeSubject,
            @Parameter(description = "Informacion a actualizar en caso que decida o solo profesor") @RequestBody UpdateSubjectRequest request) {

        subjectService.updateSubjectByCodeTeacher(codeSubject, request);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    }