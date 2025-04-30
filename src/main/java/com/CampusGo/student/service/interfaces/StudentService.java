package com.CampusGo.student.service.interfaces;

import com.CampusGo.security.presentation.dto.AuthResponseDto;
import com.CampusGo.commons.helpers.payloads.ChangePasswordPayload;
import com.CampusGo.student.presentation.dto.StudentSessionDto;
import com.CampusGo.student.presentation.payload.StudentPayload;
import com.CampusGo.student.presentation.payload.StudentUpdatePayload;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {
    AuthResponseDto registerStudent(StudentPayload studentPayload);
     AuthResponseDto updateStudent(StudentUpdatePayload payload);
     StudentSessionDto getCurrentStudent();

}
