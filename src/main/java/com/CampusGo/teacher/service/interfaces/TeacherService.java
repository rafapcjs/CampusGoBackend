package com.CampusGo.teacher.service.interfaces;

import com.CampusGo.commons.helpers.payloads.ChangePasswordPayload;
import com.CampusGo.security.presentation.dto.AuthResponseDto;
import com.CampusGo.student.presentation.payload.StudentPayload;
import com.CampusGo.teacher.presentation.dto.TeacherSessionDto;
import com.CampusGo.teacher.presentation.payload.TeacherPayload;
import com.CampusGo.teacher.presentation.payload.TeacherUpdatePayload;

public interface TeacherService {
    AuthResponseDto registerTeacher(TeacherPayload payload);
     AuthResponseDto updateTeacher(TeacherUpdatePayload payload);
    TeacherSessionDto getCurrentTeacher();
    void updatePasswordTeacher(ChangePasswordPayload payload );

}
