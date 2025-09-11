package com.konnect.filter;

import com.konnect.util.ErrorHandlingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GlobalErrorHandlerFilter - Catches and handles all unhandled exceptions
 * Provides consistent error handling across the entire application
 */
@WebFilter(filterName = "GlobalErrorHandlerFilter", urlPatterns = {"/*"})
public class GlobalErrorHandlerFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorHandlerFilter.class);
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("GlobalErrorHandlerFilter initialized - providing application-wide error handling");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        try {
            // Continue with the filter chain
            chain.doFilter(request, response);
            
        } catch (Exception e) {
            // Handle any unhandled exceptions
            handleGlobalException(httpRequest, httpResponse, e);
        }
    }
    
    @Override
    public void destroy() {
        logger.info("GlobalErrorHandlerFilter destroyed");
    }
    
    /**
     * Handle global exceptions that weren't caught by individual servlets
     */
    private void handleGlobalException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        try {
            String requestURI = request.getRequestURI();
            String method = request.getMethod();
            String context = String.format("%s %s", method, requestURI);
            
            logger.error("Unhandled exception in global filter for {}: {}", context, exception.getMessage(), exception);
            
            // Use our error handling utility
            ErrorHandlingUtil.handleServletException(request, response, exception, context);
            
        } catch (Exception e) {
            // Last resort error handling
            logger.error("Critical error in global error handler", e);
            
            try {
                if (!response.isCommitted()) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("text/html;charset=UTF-8");
                    response.getWriter().println(
                        "<html><body>" +
                        "<h1>Critical System Error</h1>" +
                        "<p>A critical error occurred. Please contact system administrator.</p>" +
                        "<p>Error ID: " + ErrorHandlingUtil.generateErrorId() + "</p>" +
                        "</body></html>"
                    );
                }
            } catch (IOException ioEx) {
                logger.error("Failed to send error response", ioEx);
            }
        }
    }
}