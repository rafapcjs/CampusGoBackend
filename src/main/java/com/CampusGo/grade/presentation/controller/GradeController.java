package com.CampusGo.grade.presentation.controller;

import com.CampusGo.grade.presentation.dto.StudentGradesResponse;
import com.CampusGo.grade.presentation.dto.StudentSubjectGradeResponseDto;
import com.CampusGo.grade.presentation.payload.UpdateGradeRequest;
import com.CampusGo.grade.service.interfaces.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.CampusGo.commons.configs.api.routes.ApiRoutes.*;

@RestController
@Tag(name = "Grade", description = "Operaciones relacionadas con la gestión de notas")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PutMapping(GRADE_REGISTER + "/{code}")
    public ResponseEntity<Void> registerGrade(
            @PathVariable Integer code,
            @Valid @RequestBody UpdateGradeRequest request) {

        gradeService.udpateGrade(request, code);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary     = "Obtener mis notas",
            description = "Devuelve la lista de asignaturas con sus cuatro calificaciones para el estudiante actualmente autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notas obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado. El estudiante no ha iniciado sesión o el token es inválido."),
            @ApiResponse(responseCode = "404", description = "No se encontraron notas para el estudiante en sesión")
    })
    @GetMapping(GRADE_MY_GRADES)
    public StudentGradesResponse listMyGrades() {
        return gradeService.listMyGrades();
    }
        @Operation(
                summary     = "Listado de notas por asignatura para el profesor en sesión",
                description = "Retorna el listado de estudiantes y sus cuatro calificaciones para la asignatura especificada, siempre que el profesor autenticado imparta esa materia."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Listado de notas obtenido correctamente"),
                @ApiResponse(responseCode = "401", description = "No autorizado. El profesor no ha iniciado sesión o el token es inválido."),
                @ApiResponse(responseCode = "403", description = "Prohibido. El profesor no imparte esta asignatura."),
                @ApiResponse(responseCode = "404", description = "Asignatura no encontrada o sin registros de notas.")
        })
        @GetMapping(GRADE_LIST_BY_SUBJECT_TEACHER + "/{subjectCode}")
        public ResponseEntity<List<StudentSubjectGradeResponseDto>> listGradesBySubjectForTeacher(
                @PathVariable("subjectCode") Integer subjectCode
        ) {
            List<StudentSubjectGradeResponseDto> grades =
                    gradeService.listGradesBySubjectForTeacher(subjectCode);
            return ResponseEntity.ok(grades);
        }
    }

