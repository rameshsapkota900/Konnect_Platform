package com.konnect.filter;

import com.konnect.model.User;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@WebFilter("/*") // Apply to all requests initially, specific paths checked inside
public class AuthenticationFilter implements Filter {

    // Pages accessible without login
    private static final Set<String> PUBLIC_URIS = new HashSet<>(Arrays.asList(
            "/login.jsp", "/register.jsp", "/LoginServlet", "/RegisterServlet", "/css/", "/js/", "/index.jsp"
    ));

    // Prefixes for role-based access
    private static final String ADMIN_PREFIX = "/admin";
    private static final String CREATOR_PREFIX = "/creator";
    private static final String BUSINESS_PREFIX = "/business";
     // Common areas accessible when logged in (e.g., Logout, common Servlets)
    private static final String COMMON_PREFIX = "/common"; // Like ReportServlet
    private static final String LOGOUT_SERVLET = "/LogoutServlet";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // Don't create session if not exists

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String relativeURI = requestURI.substring(contextPath.length());

        // Check if the requested URI is public or static resource
        boolean isPublicResource = PUBLIC_URIS.stream().anyMatch(relativeURI::startsWith) || relativeURI.equals("/");


        if (isPublicResource) {
            // Allow access to public pages/resources
             // Special case: if logged in and accessing login/register, redirect to dashboard
            if ((relativeURI.equals("/login.jsp") || relativeURI.equals("/register.jsp") || relativeURI.equals("/LoginServlet") || relativeURI.equals("/RegisterServlet")) && session != null && session.getAttribute("user") != null) {
                 User user = (User) session.getAttribute("user");
                 redirectToDashboard(user.getRole(), httpResponse, contextPath);
                 return;
             }
            chain.doFilter(request, response); // Continue processing
            return;
        }

        // If not public, check for active session and user object
        if (session == null || session.getAttribute("user") == null) {
            // Not logged in, redirect to login page
            System.out.println("AuthFilter: No session or user found for " + relativeURI + ". Redirecting to login.");
            httpResponse.sendRedirect(contextPath + "/login.jsp?message=Please login first");
            return;
        }

        // User is logged in, check role-based access
        User user = (User) session.getAttribute("user");
        String userRole = user.getRole(); // "admin", "creator", "business"

        boolean authorized = false;

        // Allow access to common logged-in areas like logout or reporting
        if (relativeURI.startsWith(COMMON_PREFIX) || relativeURI.equals(LOGOUT_SERVLET)) {
             authorized = true;
        }
        // Check role-specific areas
        else if ("admin".equals(userRole) && relativeURI.startsWith(ADMIN_PREFIX)) {
            authorized = true;
        } else if ("creator".equals(userRole) && relativeURI.startsWith(CREATOR_PREFIX)) {
            authorized = true;
        } else if ("business".equals(userRole) && relativeURI.startsWith(BUSINESS_PREFIX)) {
            authorized = true;
        }
         // Allow logged-in users to access the root (index.jsp) which should redirect them
        else if (relativeURI.equals("/") || relativeURI.equals("/index.jsp")) {
             authorized = true; // index.jsp will handle redirection
        }

        if (authorized) {
             // User is authorized for this resource
             // System.out.println("AuthFilter: User '" + user.getUsername() + "' (" + userRole + ") authorized for " + relativeURI);
            chain.doFilter(request, response);
        } else {
            // User is logged in but trying to access unauthorized area
            System.out.println("AuthFilter: User '" + user.getUsername() + "' (" + userRole + ") UNAUTHORIZED for " + relativeURI + ". Redirecting to their dashboard.");
            // Redirect to their appropriate dashboard or an error page
             redirectToDashboard(userRole, httpResponse, contextPath);
           // httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
        }
    }

    private void redirectToDashboard(String role, HttpServletResponse response, String contextPath) throws IOException {
        String dashboardUrl;
        switch (role) {
            case "admin":
                dashboardUrl = contextPath + "/admin/dashboard";
                break;
            case "creator":
                dashboardUrl = contextPath + "/creator/dashboard";
                break;
            case "business":
                dashboardUrl = contextPath + "/business/dashboard";
                break;
            default:
                dashboardUrl = contextPath + "/login.jsp"; // Fallback
        }
        response.sendRedirect(dashboardUrl);
    }


    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
