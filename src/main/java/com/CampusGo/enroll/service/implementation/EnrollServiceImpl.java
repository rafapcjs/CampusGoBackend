package com.CampusGo.enroll.service.implementation;

import com.CampusGo.commons.configs.error.exceptions.ConflictException;
import com.CampusGo.commons.configs.error.exceptions.ResourceNotFoundException;
import com.CampusGo.enroll.persistencie.entity.Enroll;
import com.CampusGo.enroll.persistencie.repository.EnrollRepository;
import com.CampusGo.enroll.presentation.dto.EnrollInfoDTO;
import com.CampusGo.enroll.presentation.payload.BulkEnrollRequest;
import com.CampusGo.enroll.presentation.payload.CreateEnrollRequest;
import com.CampusGo.enroll.service.interfaces.EnrollService;
import com.CampusGo.grade.persistencie.entity.Grade;
import com.CampusGo.grade.persistencie.repository.GradeRepository;
import com.CampusGo.student.persistencie.entity.Student;
import com.CampusGo.student.persistencie.repository.StudentRepository;
import com.CampusGo.subject.persistencie.entity.Subject;
import com.CampusGo.subject.persistencie.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollServiceImpl implements EnrollService {

    private final EnrollRepository enrollRepository;
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;


    // Servicio para el registro de matricula de un estudiante x asignatura

    @Override
    @Transactional
    public void createEnroll(CreateEnrollRequest request) {
        Student student = studentRepository.findById(request.getCodEstudianteFk())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + request.getCodEstudianteFk()));

        Subject subject = subjectRepository.findByCode(request.getCodAsignatureFk())
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada con código: " + request.getCodAsignatureFk()));

        // Validar si ya existe matrícula para este estudiante y asignatura
        boolean exists = enrollRepository.existsByStudentIdAndSubjectCode(student.getId(), subject.getCode());
        if (exists) {
            throw new ConflictException("El estudiante con ID " + student.getId() +
                    " ya está matriculado en la asignatura con código " + subject.getCode());
        }

        // Crear Enroll
        Enroll enroll = new Enroll();
        enroll.setCode(getNextEnrollCode());
        enroll.setFechaRegistra(LocalDateTime.now());
        enroll.setStudent(student);
        enroll.setSubject(subject);
        enrollRepository.save(enroll);

        // Crear Grade
        Grade grade = new Grade();
        grade.setCode(getNextGradeCode());
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setCorte1(0.0f);
        grade.setCorte2(0.0f);
        grade.setCorte3(0.0f);
        grade.setCorte4(0.0f);
        gradeRepository.save(grade);
    }


    private Integer getNextEnrollCode() {
        return enrollRepository.findTopByOrderByCodeDesc()
                .map(e -> e.getCode() + 1)
                .orElse(1);
    }

    private Integer getNextGradeCode() {
        return gradeRepository.findTopByOrderByCodeDesc()
                .map(g -> g.getCode() + 1)
                .orElse(1);
    }



    // Servicio para el registro de varias matriculas x asignatura

    @Override
    @Transactional
    public void createBulkEnroll(BulkEnrollRequest request) {
        Student student = studentRepository.findById(request.getCodEstudianteFk())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + request.getCodEstudianteFk()));

        List<Integer> asignaturasExistentes = request.getCodAsignatureFks().stream()
                .filter(codeAsignature ->
                        enrollRepository.existsByStudentIdAndSubjectCode(student.getId(), codeAsignature))
                .toList();

        if (!asignaturasExistentes.isEmpty()) {
            throw new ConflictException("El estudiante con ID " + student.getId() +
                    " ya está matriculado en las siguientes asignaturas con código: " + asignaturasExistentes);
        }

        for (Integer codeAsignature : request.getCodAsignatureFks()) {
            Subject subject = subjectRepository.findByCode(codeAsignature)
                    .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada con código: " + codeAsignature));

            // Crear Enroll
            Enroll enroll = new Enroll();
            enroll.setCode(getNextEnrollCode());
            enroll.setFechaRegistra(LocalDateTime.now());
            enroll.setStudent(student);
            enroll.setSubject(subject);
            enrollRepository.save(enroll);

            // Crear Grade
            Grade grade = new Grade();
            grade.setCode(getNextGradeCode());
            grade.setStudent(student);
            grade.setSubject(subject);
            grade.setCorte1(0.0f);
            grade.setCorte2(0.0f);
            grade.setCorte3(0.0f);
            grade.setCorte4(0.0f);
            gradeRepository.save(grade);
        }
    }


    // Servicio para eliminación de matricula y su registro de notas

    @Override
    @Transactional
    public void deleteEnrollByCode(Integer code) {
        Enroll enroll = enrollRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró matrícula con el código: " + code));

        Long studentId = enroll.getStudent().getId();
        Integer subjectCode = enroll.getSubject().getCode();

        // Eliminar Grade asociado
        Optional<Grade> optionalGrade = gradeRepository.findByStudentIdAndSubjectCode(studentId, subjectCode);
        optionalGrade.ifPresent(gradeRepository::delete);

        // Eliminar Enroll
        enrollRepository.delete(enroll);
    }



    // Servicio para listar todas la matriculas

    @Override
    public List<EnrollInfoDTO> getAllEnrollInfo() {
        List<Object[]> results = enrollRepository.findAllEnrollInfoRaw();

        return results.stream()
                .map(row -> new EnrollInfoDTO(
                        (Integer) row[0],                             // code
                        (Integer) row[1],                             // codAsignatureFk
                        (String) row[2],                              // nameAsignature
                        ((Number) row[3]).longValue(),                // codEstudianteFk (cast a Long)
                        (String) row[4],                              // fullName
                        (Timestamp) row[5]                            // fechaRegistra
                ))
                .collect(Collectors.toList());
    }




}

