package com.CampusGo.student.service.interfaces;

import com.CampusGo.security.presentation.dto.AuthResponseDto;
import com.CampusGo.student.presentation.payload.StudentPayload;

public interface StudentService {
    AuthResponseDto registerStudent(StudentPayload studentPayload);
     AuthResponseDto updateStudent(StudentPayload payload);
    StudentPayload getCurrentStudent();

}
