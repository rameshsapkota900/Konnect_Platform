package com.konnect.filter;

import com.konnect.util.InputValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SecurityInputFilter - Automatic input validation and sanitization for all requests
 * Provides comprehensive protection against XSS, SQL injection, and other attacks
 */
@WebFilter(filterName = "SecurityInputFilter", urlPatterns = {"/*"})
public class SecurityInputFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityInputFilter.class);
    
    // Parameters that should be excluded from XSS sanitization (passwords, etc.)
    private static final String[] EXCLUDED_PARAMS = {
        "password", "confirmPassword", "currentPassword", "newPassword"
    };
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("SecurityInputFilter initialized - protecting against XSS and injection attacks");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Skip filtering for static resources
        String requestURI = httpRequest.getRequestURI();
        if (isStaticResource(requestURI)) {
            chain.doFilter(request, response);
            return;
        }
        
        try {
            // Wrap the request to sanitize parameters
            SanitizedHttpServletRequestWrapper wrappedRequest = 
                new SanitizedHttpServletRequestWrapper(httpRequest);
            
            // Log potential security threats
            logSecurityThreats(wrappedRequest);
            
            // Continue with the filtered request
            chain.doFilter(wrappedRequest, response);
            
        } catch (SecurityException e) {
            logger.error("Security threat detected and blocked: {}", e.getMessage());
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input detected");
        } catch (Exception e) {
            logger.error("Error in security filter", e);
            chain.doFilter(request, response);
        }
    }
    
    @Override
    public void destroy() {
        logger.info("SecurityInputFilter destroyed");
    }
    
    /**
     * Check if the request is for a static resource that doesn't need filtering
     */
    private boolean isStaticResource(String requestURI) {
        return requestURI.matches(".+\\.(css|js|jpg|jpeg|png|gif|ico|svg|woff|woff2|ttf|eot)$");
    }
    
    /**
     * Log potential security threats found in the request
     */
    private void logSecurityThreats(HttpServletRequest request) {
        // Check for potential threats in parameters
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            
            if (paramValues != null) {
                for (String paramValue : paramValues) {
                    // Skip validation for email parameters as they are handled separately
                    if (isEmailParameter(paramName)) {
                        continue;
                    }
                    
                    if (InputValidationUtil.containsSQLInjection(paramValue)) {
                        logger.warn("SQL injection attempt detected - Parameter: {}, IP: {}, User-Agent: {}", 
                                   paramName, request.getRemoteAddr(), request.getHeader("User-Agent"));
                    }
                }
            }
        }
    }
    
    /**
     * Check if parameter is an email field
     */
    private static boolean isEmailParameter(String paramName) {
        return paramName != null && (
            paramName.toLowerCase().contains("email") ||
            paramName.toLowerCase().equals("username") ||
            paramName.toLowerCase().equals("user")
        );
    }
    
    /**
     * Custom HttpServletRequestWrapper that sanitizes input parameters
     */
    private static class SanitizedHttpServletRequestWrapper extends HttpServletRequestWrapper {
        
        private final Map<String, String[]> sanitizedParameters;
        
        public SanitizedHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
            this.sanitizedParameters = sanitizeParameters(request);
        }
        
        @Override
        public String getParameter(String name) {
            String[] values = getParameterValues(name);
            return (values != null && values.length > 0) ? values[0] : null;
        }
        
        @Override
        public String[] getParameterValues(String name) {
            return sanitizedParameters.get(name);
        }
        
        @Override
        public Map<String, String[]> getParameterMap() {
            return Collections.unmodifiableMap(sanitizedParameters);
        }
        
        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(sanitizedParameters.keySet());
        }
        
        /**
         * Sanitize all request parameters
         */
        private Map<String, String[]> sanitizeParameters(HttpServletRequest request) {
            Map<String, String[]> sanitizedMap = new ConcurrentHashMap<>();
            
            Map<String, String[]> originalParams = request.getParameterMap();
            
            for (Map.Entry<String, String[]> entry : originalParams.entrySet()) {
                String paramName = entry.getKey();
                String[] paramValues = entry.getValue();
                
                if (paramValues != null) {
                    String[] sanitizedValues = new String[paramValues.length];
                    
                    for (int i = 0; i < paramValues.length; i++) {
                        String value = paramValues[i];
                        
                        if (value != null) {
                            // Skip SQL injection check for email parameters as they are handled separately
                            if (!isEmailParameter(paramName) && InputValidationUtil.containsSQLInjection(value)) {
                                throw new SecurityException("SQL injection attempt detected in parameter: " + paramName);
                            }
                            
                            // Sanitize for XSS (except for password fields)
                            if (!Arrays.asList(EXCLUDED_PARAMS).contains(paramName)) {
                                sanitizedValues[i] = InputValidationUtil.sanitizeXSS(value);
                            } else {
                                // For passwords, just remove null bytes and control characters
                                sanitizedValues[i] = InputValidationUtil.sanitizeForDatabase(value);
                            }
                        } else {
                            sanitizedValues[i] = null;
                        }
                    }
                    
                    sanitizedMap.put(paramName, sanitizedValues);
                } else {
                    sanitizedMap.put(paramName, null);
                }
            }
            
            return sanitizedMap;
        }
    }
}