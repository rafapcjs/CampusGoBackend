package com.CampusGo.subject.service.interfaces;

import com.CampusGo.subject.presentation.dto.SubjectDetailsResponseDto;
import com.CampusGo.subject.presentation.payload.CreateSubjectRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubjectService {

    void save (CreateSubjectRequest request);
    Page<SubjectDetailsResponseDto> getSubjectsByOrderName(Pageable pageable);
    Page<SubjectDetailsResponseDto> getSubjectsByOrderCode(Pageable pageable);
    Page<SubjectDetailsResponseDto> geAllSubjectByName(String name, Pageable pageable);
     SubjectDetailsResponseDto findByCode (Integer code);
     void updateSubjectByCodeTeacher( Integer codeSubject ,String codeTeacher );
}
