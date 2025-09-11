package com.konnect.exception;

/**
 * Exception thrown when database operations fail
 */
public class DatabaseException extends KonnectException {
    
    private static final long serialVersionUID = 1L;
    
    public DatabaseException(String message) {
        super(message, "DB_ERROR", "Database operation failed. Please try again later.");
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause, "DB_ERROR", "Database operation failed. Please try again later.", false);
    }
    
    public DatabaseException(String message, String userMessage) {
        super(message, "DB_ERROR", userMessage);
    }
}