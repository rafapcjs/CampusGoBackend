package com.CampusGo.subject.service.implementation;

import com.CampusGo.academicPeriod.persistencie.entity.Academic;
import com.CampusGo.academicPeriod.persistencie.repository.AcademicRepository;
import com.CampusGo.commons.configs.error.exceptions.ResourceNotFoundException;
import com.CampusGo.security.util.SecurityUtils;
import com.CampusGo.subject.persistencie.entity.Subject;
import com.CampusGo.subject.persistencie.repository.SubjectRepository;
import com.CampusGo.subject.presentation.dto.SubjectDetailsResponseDto;
import com.CampusGo.subject.presentation.payload.CreateSubjectRequest;
import com.CampusGo.subject.presentation.payload.UpdateSubjectRequest;
import com.CampusGo.subject.service.interfaces.SubjectService;
import com.CampusGo.teacher.persistencie.entity.Teacher;
import com.CampusGo.teacher.persistencie.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl  implements SubjectService {


    final  private SubjectRepository subjectRepository;
    final  private AcademicRepository academicRepository;
    final   private TeacherRepository teacherRepository;
    final private ModelMapper modelMapper;
    @Override
    @Transactional
    public void save(CreateSubjectRequest request) {
        Academic academic = academicRepository.findByCode(request.getCodePeriodoAca()).orElseThrow(()-> new ResourceNotFoundException("Academic with code " + request.getCodePeriodoAca() + " not found"));
        Teacher teacher = teacherRepository.findByTeacherCode("teacher-default-001").orElseThrow(()-> new ResourceNotFoundException("Teacher with code " + request.getCodePeriodoAca() + " not found"));

        // Generar un código único aleatorio para la asignatura
        Integer subjectCode = generateRandomCode();

        // Crear una nueva instancia de Subject
        Subject subject = new Subject();

        // Asignar los valores del request a la entidad subject
        subject.setName(request.getName());
        subject.setAcademic(academic);
        subject.setTeacher(teacher);
        subject.setCode(subjectCode);  // Asignar el código aleatorio

        // Guardar la asignatura
        subjectRepository.save(subject);

    }

    @Override
    @Transactional(readOnly = true)

    public Page<SubjectDetailsResponseDto> getSubjectsByOrderName(Pageable pageable) {
        return subjectRepository.findAllSubjectDetailsOrderedByName(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubjectDetailsResponseDto> getSubjectsByOrderCode(Pageable pageable) {
        return subjectRepository.findAllSubjectDetailsOrderedByCode(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubjectDetailsResponseDto> geAllSubjectByName(String name, Pageable pageable) {
        return subjectRepository.findBySubjectNameContainingIgnoreCase(name,pageable);
    }




    @Override
    @Transactional(readOnly = true)

    public SubjectDetailsResponseDto findByCode(Integer code) {
        return subjectRepository.findSubjectDetailsByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Subject with code " + code + " not found"));
    }

    @Override
    @Transactional
    public void updateSubjectByCodeTeacher(Integer codeSubject, UpdateSubjectRequest request) {
        Subject subject = subjectRepository.findByCode(codeSubject)
                .orElseThrow(() -> new ResourceNotFoundException("Subject with code " + codeSubject + " not found"));

        Teacher teacher = teacherRepository.findByTeacherCode(request.getCodeTeacher())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher with code " + request.getCodeTeacher() + " not found"));

        Academic academic = academicRepository.findByCode(request.getCodePeriodoAca())
                .orElseThrow(() -> new ResourceNotFoundException("Academic with code " + request.getCodePeriodoAca() + " not found"));

        subject.setName(request.getName());
        subject.setAcademic(academic);
        subject.setTeacher(teacher);

    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectDetailsResponseDto> getSubjectsTheTeacher() {
        return subjectRepository.getSubjectsTheTeacher(
                SecurityUtils.getCurrentUsername()
        );
    }

    @Override
    public List<SubjectDetailsResponseDto> getSubjectsTheStudent() {
        return subjectRepository.getSubjectsTheTeacher(
                SecurityUtils.getCurrentUsername()
        );
    }

    private Integer generateRandomCode() {
        // Genera un código aleatorio entre 100000 y 999999
        int randomCode = (int) (Math.random() * 900000) + 100000;

        // Verificar que el código no exista ya en la base de datos (opcional, para garantizar unicidad)
        while (subjectRepository.existsByCode(randomCode)) {
            randomCode = (int) (Math.random() * 900000) + 100000;
        }

        return randomCode;
    }

}
