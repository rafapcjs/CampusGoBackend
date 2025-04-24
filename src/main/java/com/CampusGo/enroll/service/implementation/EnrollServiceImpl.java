package com.CampusGo.enroll.service.implementation;

import com.CampusGo.commons.configs.error.exceptions.ResourceNotFoundException;
import com.CampusGo.enroll.persistencie.entity.Enroll;
import com.CampusGo.enroll.persistencie.repository.EnrollRepository;
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

import java.time.LocalDateTime;

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
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        Subject subject = subjectRepository.findByCode(request.getCodAsignatureFk())
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));

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
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        for (Integer codeAsignature : request.getCodAsignatureFks()) {
            Subject subject = subjectRepository.findByCode(codeAsignature)
                    .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada: " + codeAsignature));

            // Enroll
            Enroll enroll = new Enroll();
            enroll.setCode(getNextEnrollCode());
            enroll.setFechaRegistra(LocalDateTime.now());
            enroll.setStudent(student);
            enroll.setSubject(subject);
            enrollRepository.save(enroll);

            // Grade
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

}
