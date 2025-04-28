package com.CampusGo.grade.service.implementation;

import com.CampusGo.commons.configs.error.exceptions.ResourceNotFoundException;
import com.CampusGo.grade.persistencie.entity.Grade;
import com.CampusGo.grade.persistencie.repository.GradeRepository;
import com.CampusGo.grade.presentation.dto.StudentGradesResponse;
import com.CampusGo.grade.presentation.dto.StudentSubjectGradeResponseDto;
import com.CampusGo.grade.presentation.dto.SubjectGradeResponse;
import com.CampusGo.grade.presentation.payload.UpdateGradeRequest;
import com.CampusGo.grade.service.interfaces.GradeService;
import com.CampusGo.security.util.SecurityUtils;
import com.CampusGo.student.persistencie.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    final private GradeRepository gradeRepository;


    @Override
    @Transactional(readOnly = true)
    public List<StudentGradesResponse> listGradesBySubject() {
        List<Grade> allGrades = gradeRepository.findAllWithStudentAndSubject();

        Map<Student, List<Grade>> grouped =
                allGrades.stream()
                        .collect(Collectors.groupingBy(Grade::getStudent));

        return grouped.entrySet().stream()
                .map(entry -> {
                    var student = entry.getKey();
                    List<SubjectGradeResponse> subjects = entry.getValue().stream()
                            .map(g -> new SubjectGradeResponse(
                                    g.getSubject().getName(),
                                    g.getCorte1(),
                                    g.getCorte2(),
                                    g.getCorte3(),
                                    g.getCorte4()
                            ))
                            .collect(Collectors.toList());

                    return new StudentGradesResponse(
                            student.getUser().getName(),
                            subjects
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentGradesResponse listMyGrades() {
        String username = SecurityUtils.getCurrentUsername();

        List<SubjectGradeResponse> grades = gradeRepository
                .findGradesByUsername(username)
                .stream()
                .map(p -> new SubjectGradeResponse(
                        p.getSubjectName(),
                        p.getCorte1(),
                        p.getCorte2(),
                        p.getCorte3(),
                        p.getCorte4()
                ))
                .collect(Collectors.toList());

        return new StudentGradesResponse(username, grades);
    }

    @Override
    @Transactional
    public void udpateGrade(UpdateGradeRequest request, Integer code) {
        // 1. Obtener el usuario (profesor) en sesión
        String currentTeacherUsername = SecurityUtils.getCurrentUsername();

        // 2. Recuperar la entidad Grade por su código o lanzar 404
        Grade grade = gradeRepository.findByCode(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Código no existe: " + code)
                );

        // 3. Obtener el username del profesor asignado a la materia
        String subjectTeacherUsername = grade.getSubject()
                .getTeacher()
                .getUser()
                .getUsername();  // o getName(), según tu modelo

        // 4. Validar que sea el mismo profesor; si no, lanzar 403
        if (!currentTeacherUsername.equals(subjectTeacherUsername)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No tienes permiso para modificar esta nota"
            );
        }

        // 5. Actualizar las notas
        grade.setCorte1(request.getCorte1());
        grade.setCorte2(request.getCorte2());
        grade.setCorte3(request.getCorte3());
        grade.setCorte4(request.getCorte4());

        // 6. Guardar los cambios
        gradeRepository.save(grade);

    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentSubjectGradeResponseDto> listGradesBySubjectForTeacher(Integer subjectCode) {
        // Obtiene el username del profesor en sesión
        String teacherUsername = SecurityUtils.getCurrentUsername();
        // Llama al repositorio pasando subjectCode y teacherUsername
        return gradeRepository.findGradesBySubjectAndTeacher(subjectCode, teacherUsername);
    }
}


