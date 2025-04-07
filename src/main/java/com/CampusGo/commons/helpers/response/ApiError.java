package com.CampusGo.commons.helpers.response;


import java.time.LocalDateTime;

/**
 * Clase para estructurar los errores de la API.
 */
public class ApiError {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    // Constructor por defecto: inicializa el timestamp
    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor completo
    public ApiError(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Getters y setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}