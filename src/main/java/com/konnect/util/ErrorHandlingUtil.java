package com.konnect.util;

import com.konnect.exception.KonnectException;
import com.konnect.servlet.admin.AdminErrorMonitoringServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.UUID;

/**
 * ErrorHandlingUtil - Comprehensive error handling and reporting utility
 * Provides consistent error handling, logging, and user-friendly error responses
 */
public class ErrorHandlingUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingUtil.class);
    
    /**
     * Handle exception in servlet context with proper logging and user response
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param exception Exception to handle
     * @param context Context description (e.g., "user registration", "login attempt")
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    public static void handleServletException(HttpServletRequest request, HttpServletResponse response, 
                                            Exception exception, String context) throws ServletException, IOException {
        String errorId = generateErrorId();
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = getClientIpAddress(request);
        
        // Log the full exception details
        logger.error("Error ID: {} | Context: {} | IP: {} | User-Agent: {} | Exception: {}", 
                    errorId, context, ipAddress, userAgent, exception.getMessage(), exception);
        
        // Track error for monitoring dashboard
        trackErrorForMonitoring(exception);
        
        String userMessage;
        String errorPage;
        int statusCode;
        
        if (exception instanceof KonnectException) {
            KonnectException konnectEx = (KonnectException) exception;
            userMessage = konnectEx.isUserFacing() ? konnectEx.getUserMessage() : 
                         "An unexpected error occurred. Please try again later.";
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            errorPage = "/error.jsp";
        } else if (exception instanceof SecurityException) {
            userMessage = "Security violation detected. Please check your input and try again.";
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            errorPage = "/error.jsp";
        } else if (exception instanceof SQLException) {
            userMessage = "Database operation failed. Please try again later.";
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            errorPage = "/error.jsp";
        } else {
            userMessage = "An unexpected error occurred. Please try again later.";
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            errorPage = "/error.jsp";
        }
        
        // Set error attributes for JSP
        request.setAttribute("errorId", errorId);
        request.setAttribute("errorMessage", userMessage);
        request.setAttribute("errorContext", context);
        request.setAttribute("statusCode", statusCode);
        
        // Forward to error page or send JSON response
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            sendJsonErrorResponse(response, statusCode, userMessage, errorId);
        } else {
            response.setStatus(statusCode);
            request.getRequestDispatcher(errorPage).forward(request, response);
        }
    }
    
    /**
     * Send JSON error response for AJAX requests
     * 
     * @param response HttpServletResponse
     * @param statusCode HTTP status code
     * @param message Error message
     * @param errorId Unique error identifier
     * @throws IOException if I/O error occurs
     */
    public static void sendJsonErrorResponse(HttpServletResponse response, int statusCode, 
                                           String message, String errorId) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print("{");
        out.print("\"success\": false,");
        out.print("\"error\": \"" + escapeJson(message) + "\",");
        out.print("\"errorId\": \"" + errorId + "\",");
        out.print("\"timestamp\": \"" + java.time.Instant.now().toString() + "\"");
        out.print("}");
        out.flush();
    }
    
    /**
     * Log and return user-friendly error message for DAO operations
     * 
     * @param operation Description of the operation that failed
     * @param exception Exception that occurred
     * @param entityId ID of the entity being operated on (optional)
     * @return User-friendly error message
     */
    public static String handleDaoException(String operation, Exception exception, Object entityId) {
        String errorId = generateErrorId();
        String entityInfo = entityId != null ? " for entity ID: " + entityId : "";
        
        logger.error("Error ID: {} | DAO Operation: {} {} | Exception: {}", 
                    errorId, operation, entityInfo, exception.getMessage(), exception);
        
        if (exception instanceof SQLException) {
            SQLException sqlEx = (SQLException) exception;
            logger.error("SQL Error Code: {} | SQL State: {}", sqlEx.getErrorCode(), sqlEx.getSQLState());
            
            // Check for specific SQL error types
            if (sqlEx.getSQLState() != null) {
                switch (sqlEx.getSQLState().substring(0, 2)) {
                    case "23": // Integrity constraint violation
                        return "The operation conflicts with existing data. Please check your input.";
                    case "28": // Invalid authorization
                        return "Database access denied. Please contact support.";
                    case "42": // Syntax error or access rule violation
                        return "Invalid database operation. Please contact support.";
                    default:
                        return "Database operation failed. Please try again later.";
                }
            }
        }
        
        return "Operation failed. Please try again later. Error ID: " + errorId;
    }
    
    /**
     * Validate and throw appropriate exception for business logic violations
     * 
     * @param condition Boolean condition to check
     * @param errorMessage Error message if condition fails
     * @param userMessage User-friendly message
     * @throws com.konnect.exception.BusinessLogicException if condition is false
     */
    public static void validateBusinessRule(boolean condition, String errorMessage, String userMessage) 
            throws com.konnect.exception.BusinessLogicException {
        if (!condition) {
            logger.warn("Business rule violation: {}", errorMessage);
            throw new com.konnect.exception.BusinessLogicException(errorMessage, userMessage);
        }
    }
    
    /**
     * Log performance warning if operation takes too long
     * 
     * @param operation Operation description
     * @param startTime Start time in milliseconds
     * @param warningThreshold Warning threshold in milliseconds
     */
    public static void logPerformanceWarning(String operation, long startTime, long warningThreshold) {
        long duration = System.currentTimeMillis() - startTime;
        if (duration > warningThreshold) {
            logger.warn("Performance Warning: {} took {}ms (threshold: {}ms)", 
                       operation, duration, warningThreshold);
        }
    }
    
    /**
     * Get the real client IP address from request headers
     * 
     * @param request HttpServletRequest
     * @return Client IP address
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {
            "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", 
            "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", 
            "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", 
            "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"
        };
        
        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // Handle comma-separated IPs (X-Forwarded-For can contain multiple IPs)
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                return ip;
            }
        }
        
        return request.getRemoteAddr();
    }
    
    /**
     * Generate unique error ID for tracking
     * 
     * @return Unique error identifier
     */
    public static String generateErrorId() {
        return "ERR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Escape JSON string values
     * 
     * @param value String to escape
     * @return Escaped string
     */
    private static String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    /**
     * Create a detailed error report for debugging
     * 
     * @param exception Exception to analyze
     * @param context Additional context information
     * @return Detailed error report
     */
    public static String createErrorReport(Exception exception, String context) {
        StringBuilder report = new StringBuilder();
        report.append("=== ERROR REPORT ===\n");
        report.append("Error ID: ").append(generateErrorId()).append("\n");
        report.append("Timestamp: ").append(java.time.Instant.now().toString()).append("\n");
        report.append("Context: ").append(context).append("\n");
        report.append("Exception Type: ").append(exception.getClass().getSimpleName()).append("\n");
        report.append("Message: ").append(exception.getMessage()).append("\n");
        
        if (exception instanceof SQLException) {
            SQLException sqlEx = (SQLException) exception;
            report.append("SQL Error Code: ").append(sqlEx.getErrorCode()).append("\n");
            report.append("SQL State: ").append(sqlEx.getSQLState()).append("\n");
        }
        
        report.append("Stack Trace:\n");
        for (StackTraceElement element : exception.getStackTrace()) {
            report.append("  at ").append(element.toString()).append("\n");
        }
        
        if (exception.getCause() != null) {
            report.append("Caused by: ").append(exception.getCause().toString()).append("\n");
        }
        
        report.append("=== END REPORT ===\n");
        return report.toString();
    }
    
    /**
     * Track error for monitoring dashboard
     */
    private static void trackErrorForMonitoring(Exception exception) {
        try {
            String exceptionType = exception.getClass().getSimpleName().toLowerCase();
            
            if (exceptionType.contains("authentication") || exceptionType.contains("login") || 
                exceptionType.contains("password") || exceptionType.contains("credential")) {
                AdminErrorMonitoringServlet.incrementAuthenticationErrors();
            } else if (exceptionType.contains("sql") || exceptionType.contains("database") || 
                      exceptionType.contains("connection")) {
                AdminErrorMonitoringServlet.incrementDatabaseErrors();
            } else if (exceptionType.contains("validation") || exceptionType.contains("input") || 
                      exceptionType.contains("format") || exceptionType.contains("illegal")) {
                AdminErrorMonitoringServlet.incrementValidationErrors();
            } else {
                AdminErrorMonitoringServlet.incrementSystemErrors();
            }
        } catch (Exception e) {
            // Fail silently for monitoring to not affect main application flow
            logger.debug("Failed to update error monitoring statistics", e);
        }
    }
}