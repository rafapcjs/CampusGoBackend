package com.CampusGo.academicPeriod.service.interfaces;

import com.CampusGo.academicPeriod.presentation.dto.AcademicResponseDto;
import com.CampusGo.academicPeriod.presentation.payload.CreateAcademicRequest;
import com.CampusGo.academicPeriod.presentation.payload.UpdateAcademicRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AcademicService {
    ResponseEntity<?> getAllAcademicPeriods();
    AcademicResponseDto createAcademicPeriod(CreateAcademicRequest request);
    AcademicResponseDto updateAcademicPeriod(UpdateAcademicRequest request);
}