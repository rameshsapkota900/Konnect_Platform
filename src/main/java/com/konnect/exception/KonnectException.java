package com.konnect.exception;

/**
 * Base exception class for all Konnect Platform specific exceptions
 * Provides consistent error handling across the application
 */
public class KonnectException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private final String errorCode;
    private final String userMessage;
    private final boolean isUserFacing;
    
    /**
     * Constructor with message only
     * 
     * @param message Technical error message for logging
     */
    public KonnectException(String message) {
        super(message);
        this.errorCode = "KONNECT_ERROR";
        this.userMessage = "An error occurred. Please try again.";
        this.isUserFacing = false;
    }
    
    /**
     * Constructor with message and cause
     * 
     * @param message Technical error message for logging
     * @param cause Root cause exception
     */
    public KonnectException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "KONNECT_ERROR";
        this.userMessage = "An error occurred. Please try again.";
        this.isUserFacing = false;
    }
    
    /**
     * Full constructor with all parameters
     * 
     * @param message Technical error message for logging
     * @param cause Root cause exception
     * @param errorCode Specific error code for categorization
     * @param userMessage User-friendly error message
     * @param isUserFacing Whether this error should be shown to users
     */
    public KonnectException(String message, Throwable cause, String errorCode, 
                           String userMessage, boolean isUserFacing) {
        super(message, cause);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
        this.isUserFacing = isUserFacing;
    }
    
    /**
     * Constructor for user-facing exceptions
     * 
     * @param message Technical error message for logging
     * @param errorCode Specific error code
     * @param userMessage User-friendly error message
     */
    public KonnectException(String message, String errorCode, String userMessage) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
        this.isUserFacing = true;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getUserMessage() {
        return userMessage;
    }
    
    public boolean isUserFacing() {
        return isUserFacing;
    }
    
    @Override
    public String toString() {
        return String.format("KonnectException[code=%s, userFacing=%s, message=%s]", 
                           errorCode, isUserFacing, getMessage());
    }
}