package com.CampusGo.security.presentation.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "status", "jwt"})
public record AuthResponseDto(
        String username,
        String message,
        String jwt,
        Boolean status) {
}
