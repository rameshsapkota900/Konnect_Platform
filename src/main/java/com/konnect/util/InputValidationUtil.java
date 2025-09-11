package com.konnect.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Arrays;
import java.util.List;

/**
 * InputValidationUtil - Comprehensive input validation and sanitization utility
 * Provides protection against SQL injection, XSS, and other security threats
 */
public class InputValidationUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(InputValidationUtil.class);
    
    // Regular expression patterns for validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,30}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9]{10,15}$"
    );
    
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9\\s]+$"
    );
    
    private static final Pattern SAFE_TEXT_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9\\s.,!?()-]+$"
    );
    
    // SQL injection patterns to detect
    private static final List<String> SQL_INJECTION_PATTERNS = Arrays.asList(
        "('|(\\-\\-)|;|\\||\\*|%|char|nchar|varchar|nvarchar|alter|begin|cast|create|cursor|declare|delete|drop|end|exec|execute|fetch|insert|kill|open|select|sys|sysobjects|syscolumns|table|update|union|xp_)"
    );
    
    // XSS patterns to detect and sanitize
    private static final List<String> XSS_PATTERNS = Arrays.asList(
        "<script", "</script>", "<iframe", "</iframe>", "javascript:", "vbscript:",
        "onload=", "onerror=", "onclick=", "onmouseover=", "onfocus=", "onblur=",
        "<object", "</object>", "<embed", "</embed>", "<applet", "</applet>"
    );
    
    /**
     * Validate email address format
     * 
     * @param email Email to validate
     * @return ValidationResult with success/failure and message
     */
    public static ValidationResult validateEmail(String email) {
        if (isNullOrEmpty(email)) {
            return ValidationResult.failure("Email cannot be empty");
        }
        
        if (email.length() > 100) {
            return ValidationResult.failure("Email must be less than 100 characters");
        }
        
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return ValidationResult.failure("Invalid email format");
        }
        
        return ValidationResult.success();
    }
    
    /**
     * Validate username format
     * 
     * @param username Username to validate
     * @return ValidationResult with success/failure and message
     */
    public static ValidationResult validateUsername(String username) {
        if (isNullOrEmpty(username)) {
            return ValidationResult.failure("Username cannot be empty");
        }
        
        if (username.length() < 3 || username.length() > 30) {
            return ValidationResult.failure("Username must be between 3 and 30 characters");
        }
        
        if (!USERNAME_PATTERN.matcher(username.trim()).matches()) {
            return ValidationResult.failure("Username can only contain letters, numbers, and underscores");
        }
        
        return ValidationResult.success();
    }
    
    /**
     * Validate phone number format
     * 
     * @param phone Phone number to validate
     * @return ValidationResult with success/failure and message
     */
    public static ValidationResult validatePhone(String phone) {
        if (isNullOrEmpty(phone)) {
            return ValidationResult.failure("Phone number cannot be empty");
        }
        
        // Remove spaces and dashes for validation
        String cleanPhone = phone.replaceAll("[\\s-]", "");
        
        if (!PHONE_PATTERN.matcher(cleanPhone).matches()) {
            return ValidationResult.failure("Invalid phone number format");
        }
        
        return ValidationResult.success();
    }
    
    /**
     * Validate general text input
     * 
     * @param text Text to validate
     * @param maxLength Maximum allowed length
     * @param allowEmpty Whether empty values are allowed
     * @return ValidationResult with success/failure and message
     */
    public static ValidationResult validateText(String text, int maxLength, boolean allowEmpty) {
        if (isNullOrEmpty(text)) {
            if (allowEmpty) {
                return ValidationResult.success();
            } else {
                return ValidationResult.failure("Text cannot be empty");
            }
        }
        
        if (text.length() > maxLength) {
            return ValidationResult.failure("Text must be less than " + maxLength + " characters");
        }
        
        return ValidationResult.success();
    }
    
    /**
     * Validate numeric input
     * 
     * @param value Value to validate
     * @param min Minimum allowed value
     * @param max Maximum allowed value
     * @return ValidationResult with success/failure and message
     */
    public static ValidationResult validateNumeric(String value, int min, int max) {
        if (isNullOrEmpty(value)) {
            return ValidationResult.failure("Numeric value cannot be empty");
        }
        
        try {
            int numValue = Integer.parseInt(value.trim());
            if (numValue < min || numValue > max) {
                return ValidationResult.failure("Value must be between " + min + " and " + max);
            }
            return ValidationResult.success();
        } catch (NumberFormatException e) {
            return ValidationResult.failure("Invalid numeric value");
        }
    }
    
    /**
     * Sanitize input to prevent XSS attacks
     * 
     * @param input Input to sanitize
     * @return Sanitized input
     */
    public static String sanitizeXSS(String input) {
        if (isNullOrEmpty(input)) {
            return input;
        }
        
        String sanitized = input;
        
        // HTML encode dangerous characters
        sanitized = sanitized.replace("&", "&amp;")
                           .replace("<", "&lt;")
                           .replace(">", "&gt;")
                           .replace("\"", "&quot;")
                           .replace("'", "&#x27;")
                           .replace("/", "&#x2F;");
        
        // Remove dangerous patterns
        for (String pattern : XSS_PATTERNS) {
            sanitized = sanitized.replaceAll("(?i)" + Pattern.quote(pattern), "");
        }
        
        return sanitized.trim();
    }
    
    /**
     * Check for potential SQL injection patterns
     * 
     * @param input Input to check
     * @return true if potential SQL injection detected
     */
    public static boolean containsSQLInjection(String input) {
        if (isNullOrEmpty(input)) {
            return false;
        }
        
        // Skip SQL injection check for valid email addresses
        if (EMAIL_PATTERN.matcher(input.trim()).matches()) {
            return false;
        }
        
        String lowercaseInput = input.toLowerCase();
        
        for (String pattern : SQL_INJECTION_PATTERNS) {
            Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            if (compiledPattern.matcher(lowercaseInput).find()) {
                logger.warn("Potential SQL injection detected in input: {}", 
                           input.length() > 50 ? input.substring(0, 50) + "..." : input);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Sanitize input for database operations
     * 
     * @param input Input to sanitize
     * @return Sanitized input safe for database operations
     */
    public static String sanitizeForDatabase(String input) {
        if (isNullOrEmpty(input)) {
            return input;
        }
        
        // Check for SQL injection
        if (containsSQLInjection(input)) {
            logger.warn("SQL injection attempt blocked");
            throw new SecurityException("Invalid input detected");
        }
        
        // Basic sanitization - remove null bytes and control characters
        return input.replaceAll("\\x00", "")
                   .replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "")
                   .trim();
    }
    
    /**
     * Validate and sanitize search query
     * 
     * @param query Search query to validate
     * @return ValidationResult with sanitized query
     */
    public static ValidationResult validateSearchQuery(String query) {
        if (isNullOrEmpty(query)) {
            return ValidationResult.failure("Search query cannot be empty");
        }
        
        if (query.length() > 100) {
            return ValidationResult.failure("Search query must be less than 100 characters");
        }
        
        if (containsSQLInjection(query)) {
            return ValidationResult.failure("Invalid search query");
        }
        
        String sanitized = sanitizeXSS(query);
        return ValidationResult.success(sanitized);
    }
    
    /**
     * Comprehensive input validation for user registration
     * 
     * @param username Username
     * @param email Email
     * @param password Password
     * @param confirmPassword Confirm password
     * @param role User role
     * @return ValidationResult with detailed error messages
     */
    public static ValidationResult validateRegistration(String username, String email, 
                                                      String password, String confirmPassword, String role) {
        StringBuilder errors = new StringBuilder();
        
        // Validate username
        ValidationResult usernameResult = validateUsername(username);
        if (!usernameResult.isValid()) {
            errors.append("Username: ").append(usernameResult.getMessage()).append(". ");
        }
        
        // Validate email
        ValidationResult emailResult = validateEmail(email);
        if (!emailResult.isValid()) {
            errors.append("Email: ").append(emailResult.getMessage()).append(". ");
        }
        
        // Validate password
        if (!PasswordUtil.isPasswordStrong(password)) {
            errors.append("Password: ").append(PasswordUtil.getPasswordRequirements()).append(". ");
        }
        
        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            errors.append("Passwords do not match. ");
        }
        
        // Validate role
        if (!Arrays.asList("creator", "business", "admin").contains(role)) {
            errors.append("Invalid role selected. ");
        }
        
        if (errors.length() > 0) {
            return ValidationResult.failure(errors.toString().trim());
        }
        
        return ValidationResult.success();
    }
    
    /**
     * Utility method to check if string is null or empty
     * 
     * @param str String to check
     * @return true if null or empty
     */
    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * ValidationResult class to encapsulate validation results
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String message;
        private final String sanitizedValue;
        
        private ValidationResult(boolean valid, String message, String sanitizedValue) {
            this.valid = valid;
            this.message = message;
            this.sanitizedValue = sanitizedValue;
        }
        
        public static ValidationResult success() {
            return new ValidationResult(true, null, null);
        }
        
        public static ValidationResult success(String sanitizedValue) {
            return new ValidationResult(true, null, sanitizedValue);
        }
        
        public static ValidationResult failure(String message) {
            return new ValidationResult(false, message, null);
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String getSanitizedValue() {
            return sanitizedValue;
        }
    }
}