package com.CampusGo.teacher.presentation.dto;

import lombok.Builder;

@Builder
public record TeacherSessionDto(
        String username,
        String name,
        String lastName,
        String dni,
        String email,
        String phone,
        String teacherCode
) {}