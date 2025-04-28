package com.CampusGo.subject.service.interfaces;

import com.CampusGo.subject.presentation.dto.SubjectDetailsResponseDto;
import com.CampusGo.subject.presentation.payload.CreateSubjectRequest;
import com.CampusGo.subject.presentation.payload.UpdateSubjectRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubjectService {

    void save (CreateSubjectRequest request);
    Page<SubjectDetailsResponseDto> getSubjectsByOrderName(Pageable pageable);
    Page<SubjectDetailsResponseDto> getSubjectsByOrderCode(Pageable pageable);
    Page<SubjectDetailsResponseDto> geAllSubjectByName(String name, Pageable pageable);
     SubjectDetailsResponseDto findByCode (Integer code);
     void updateSubjectByCodeTeacher(Integer codeSubject , UpdateSubjectRequest request );
    List<SubjectDetailsResponseDto> getSubjectsTheTeacher();
    List<SubjectDetailsResponseDto> getSubjectsTheStudent();


}
