package com.konnect.exception;

/**
 * Exception thrown for authentication and authorization failures
 */
public class AuthenticationException extends KonnectException {
    
    private static final long serialVersionUID = 1L;
    
    public AuthenticationException(String message) {
        super(message, "AUTH_ERROR", "Authentication failed. Please check your credentials.");
    }
    
    public AuthenticationException(String message, String userMessage) {
        super(message, "AUTH_ERROR", userMessage);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause, "AUTH_ERROR", "Authentication failed. Please try again.", false);
    }
}