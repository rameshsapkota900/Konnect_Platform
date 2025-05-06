package com.konnect.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filter to prevent unauthorized access to protected resources
 */
@WebFilter(urlPatterns = {"/admin/*", "/creator/*", "/business/*"})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get the requested URI
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Get the current session
        HttpSession session = httpRequest.getSession(false);

        // Check if user is logged in
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        // Check if the request is for a protected resource
        boolean isAdminResource = requestURI.startsWith(contextPath + "/admin/");
        boolean isCreatorResource = requestURI.contains("/creator/");
        boolean isBusinessResource = requestURI.contains("/business/");

        if (isLoggedIn) {
            // Get user role
            String role = (String) session.getAttribute("role");

            // Check if role is null
            if (role == null) {
                // Invalid session state, clear it and redirect to login
                session.invalidate();
                httpResponse.sendRedirect(contextPath + "/login");
                return;
            }

            // Check if user has appropriate role for the requested resource
            if ((isAdminResource && "admin".equals(role)) ||
                (isCreatorResource && "creator".equals(role)) ||
                (isBusinessResource && "business".equals(role))) {
                // User has appropriate role, continue with the request
                chain.doFilter(request, response);
            } else {
                // User does not have appropriate role, redirect to home page
                httpResponse.sendRedirect(contextPath + "/index.jsp");
            }
        } else {
            // User is not logged in, redirect to login page
            httpResponse.sendRedirect(contextPath + "/login");
        }
    }

    @Override
    public void destroy() {
        // Cleanup code
    }
}
