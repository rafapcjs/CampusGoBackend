package com.CampusGo.enroll.service.interfaces;

import com.CampusGo.enroll.presentation.dto.EnrollInfoDTO;
import com.CampusGo.enroll.presentation.payload.BulkEnrollRequest;
import com.CampusGo.enroll.presentation.payload.CreateEnrollRequest;

import java.time.LocalDate;
import java.util.List;

public interface EnrollService {
    void createEnroll(CreateEnrollRequest request);

    void createBulkEnroll(BulkEnrollRequest request);

    void deleteEnrollByCode(Integer code);

    List<EnrollInfoDTO> getAllEnrollInfo();

    List<EnrollInfoDTO> getAllEnrollInfoByStudentId(String studentId);

    List<EnrollInfoDTO> getAllEnrollInfoBySubjectCode(String subjectCode);

    List<EnrollInfoDTO> getAllEnrollInfoByRegisterDate(String registerDate);




}
