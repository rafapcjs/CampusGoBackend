package com.CampusGo.commons.configs.error.exceptions;

public class AccessDeniedException extends org.springframework.security.access.AccessDeniedException {

    public AccessDeniedException(String message) {
        super(message);
    }
}