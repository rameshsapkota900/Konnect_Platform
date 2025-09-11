package com.konnect.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * SQLSecurityUtil - Utility for secure database operations
 * Provides enhanced protection against SQL injection attacks
 */
public class SQLSecurityUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(SQLSecurityUtil.class);
    
    /**
     * Safely set string parameter in PreparedStatement
     * 
     * @param stmt PreparedStatement
     * @param parameterIndex Parameter index (1-based)
     * @param value String value to set
     * @throws SQLException if database error occurs
     */
    public static void setSecureString(PreparedStatement stmt, int parameterIndex, String value) throws SQLException {
        if (value == null) {
            stmt.setNull(parameterIndex, Types.VARCHAR);
        } else {
            // Validate input for potential threats
            if (InputValidationUtil.containsSQLInjection(value)) {
                logger.error("SQL injection attempt blocked in parameter {}: {}", 
                           parameterIndex, value.length() > 50 ? value.substring(0, 50) + "..." : value);
                throw new SecurityException("Invalid input detected in parameter " + parameterIndex);
            }
            
            // Sanitize and set the parameter
            String sanitized = InputValidationUtil.sanitizeForDatabase(value);
            stmt.setString(parameterIndex, sanitized);
        }
    }
    
    /**
     * Safely set integer parameter in PreparedStatement
     * 
     * @param stmt PreparedStatement
     * @param parameterIndex Parameter index (1-based)
     * @param value Integer value to set
     * @throws SQLException if database error occurs
     */
    public static void setSecureInt(PreparedStatement stmt, int parameterIndex, Integer value) throws SQLException {
        if (value == null) {
            stmt.setNull(parameterIndex, Types.INTEGER);
        } else {
            stmt.setInt(parameterIndex, value);
        }
    }
    
    /**
     * Safely set email parameter with validation
     * 
     * @param stmt PreparedStatement
     * @param parameterIndex Parameter index (1-based)
     * @param email Email value to set
     * @throws SQLException if database error occurs
     * @throws SecurityException if email is invalid
     */
    public static void setSecureEmail(PreparedStatement stmt, int parameterIndex, String email) throws SQLException {
        if (email == null) {
            stmt.setNull(parameterIndex, Types.VARCHAR);
        } else {
            // Validate email format
            InputValidationUtil.ValidationResult validation = InputValidationUtil.validateEmail(email);
            if (!validation.isValid()) {
                logger.error("Invalid email format in parameter {}: {}", parameterIndex, email);
                throw new SecurityException("Invalid email format");
            }
            
            // Sanitize and set
            String sanitized = InputValidationUtil.sanitizeForDatabase(email.toLowerCase().trim());
            stmt.setString(parameterIndex, sanitized);
        }
    }
    
    /**
     * Safely build LIKE query parameter with wildcards
     * 
     * @param searchTerm Search term to escape
     * @param position Where to place wildcards (BOTH, START, END, NONE)
     * @return Escaped search term with wildcards
     */
    public static String buildSecureLikePattern(String searchTerm, WildcardPosition position) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return "%";
        }
        
        // Validate input
        if (InputValidationUtil.containsSQLInjection(searchTerm)) {
            logger.error("SQL injection attempt in search term: {}", searchTerm);
            throw new SecurityException("Invalid search term");
        }
        
        // Sanitize
        String sanitized = InputValidationUtil.sanitizeForDatabase(searchTerm);
        
        // Escape LIKE special characters
        sanitized = sanitized.replace("\\", "\\\\")
                           .replace("%", "\\%")
                           .replace("_", "\\_");
        
        // Add wildcards based on position
        switch (position) {
            case BOTH:
                return "%" + sanitized + "%";
            case START:
                return "%" + sanitized;
            case END:
                return sanitized + "%";
            case NONE:
            default:
                return sanitized;
        }
    }
    
    /**
     * Validate and sanitize ORDER BY clause
     * 
     * @param orderBy Order by clause
     * @param allowedColumns List of allowed column names
     * @return Sanitized ORDER BY clause
     */
    public static String buildSecureOrderBy(String orderBy, String[] allowedColumns) {
        if (orderBy == null || orderBy.trim().isEmpty()) {
            return "id ASC"; // Default safe ordering
        }
        
        // Check for SQL injection
        if (InputValidationUtil.containsSQLInjection(orderBy)) {
            logger.error("SQL injection attempt in ORDER BY clause: {}", orderBy);
            throw new SecurityException("Invalid ORDER BY clause");
        }
        
        // Parse and validate order by
        String[] parts = orderBy.trim().split("\\s+");
        if (parts.length == 0 || parts.length > 2) {
            throw new SecurityException("Invalid ORDER BY format");
        }
        
        String column = parts[0].toLowerCase();
        String direction = parts.length > 1 ? parts[1].toUpperCase() : "ASC";
        
        // Validate column name
        boolean columnAllowed = false;
        for (String allowedColumn : allowedColumns) {
            if (allowedColumn.toLowerCase().equals(column)) {
                columnAllowed = true;
                break;
            }
        }
        
        if (!columnAllowed) {
            logger.error("Unauthorized column in ORDER BY: {}", column);
            throw new SecurityException("Unauthorized column name");
        }
        
        // Validate direction
        if (!"ASC".equals(direction) && !"DESC".equals(direction)) {
            throw new SecurityException("Invalid sort direction");
        }
        
        return column + " " + direction;
    }
    
    /**
     * Validate LIMIT clause parameters
     * 
     * @param limit Limit value
     * @param offset Offset value
     * @return Validated limit and offset array [limit, offset]
     */
    public static int[] validateLimitOffset(Integer limit, Integer offset) {
        int validLimit = 10; // Default limit
        int validOffset = 0; // Default offset
        
        if (limit != null) {
            if (limit < 1 || limit > 1000) { // Reasonable limits
                throw new SecurityException("Invalid limit value: " + limit);
            }
            validLimit = limit;
        }
        
        if (offset != null) {
            if (offset < 0) {
                throw new SecurityException("Invalid offset value: " + offset);
            }
            validOffset = offset;
        }
        
        return new int[]{validLimit, validOffset};
    }
    
    /**
     * Enum for wildcard positions in LIKE queries
     */
    public enum WildcardPosition {
        BOTH, START, END, NONE
    }
}