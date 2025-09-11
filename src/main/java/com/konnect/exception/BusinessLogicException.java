package com.konnect.exception;

/**
 * Exception thrown for business logic violations
 */
public class BusinessLogicException extends KonnectException {
    
    private static final long serialVersionUID = 1L;
    
    public BusinessLogicException(String message) {
        super(message, "BUSINESS_ERROR", "Operation not allowed. Please check your request.");
    }
    
    public BusinessLogicException(String message, String userMessage) {
        super(message, "BUSINESS_ERROR", userMessage);
    }
    
    public BusinessLogicException(String message, Throwable cause, String userMessage) {
        super(message, cause, "BUSINESS_ERROR", userMessage, true);
    }
}