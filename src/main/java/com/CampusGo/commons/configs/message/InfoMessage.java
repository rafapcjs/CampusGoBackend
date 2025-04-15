package com.CampusGo.commons.configs.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class InfoMessage {
    private String message;
    private LocalDateTime timestamp;
}