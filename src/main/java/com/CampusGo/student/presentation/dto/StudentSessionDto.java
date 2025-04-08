package com.CampusGo.student.presentation.dto;

import lombok.Builder;

@Builder
public record StudentSessionDto (
        String username,
        String name,
        String lastName,
        String dni,
        String email,
        String phone,
        String studentCode
) {}