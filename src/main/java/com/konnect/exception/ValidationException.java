package com.konnect.exception;

/**
 * Exception thrown for input validation failures
 */
public class ValidationException extends KonnectException {
    
    private static final long serialVersionUID = 1L;
    
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR", message);
    }
    
    public ValidationException(String message, String userMessage) {
        super(message, "VALIDATION_ERROR", userMessage);
    }
}