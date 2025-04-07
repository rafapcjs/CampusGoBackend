package com.CampusGo.teacher.service.interfaces;

import com.CampusGo.security.presentation.dto.AuthResponseDto;
import com.CampusGo.student.presentation.payload.StudentPayload;
import com.CampusGo.teacher.presentation.payload.TeacherPayload;

public interface TeacherService {
    AuthResponseDto registerTeacher(TeacherPayload payload);
     AuthResponseDto updateTeacher(TeacherPayload payload);
    TeacherPayload getCurrentTeacher();

}
