package com.CampusGo.commons.configs.error.exceptions;




public class NotImplementedException extends RuntimeException {

    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }
}