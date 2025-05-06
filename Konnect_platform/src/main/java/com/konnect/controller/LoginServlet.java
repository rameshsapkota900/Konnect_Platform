package com.konnect.controller;

import java.io.IOException;

import com.konnect.dao.UserDAO;
import com.konnect.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet for handling user login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            
            // Check if we're already on the admin dashboard to prevent loops
            String requestURI = request.getRequestURI();
            String contextPath = request.getContextPath();
            if ("admin".equals(user.getRole()) && 
                requestURI.equals(contextPath + "/admin/dashboard")) {
                // Already on dashboard, just forward to the JSP
                request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
            } else {
                // Redirect based on role
                redirectBasedOnRole(request, response, user.getRole());
            }
        } else {
            // Forward to login page
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get login parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Authenticate user
        User user = userDAO.login(username, password);

        if (user != null) {
            // Create session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            // Redirect based on role
            redirectBasedOnRole(request, response, user.getRole());
        } else {
            // Authentication failed
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    /**
     * Redirect user to appropriate dashboard based on role
     */
    private void redirectBasedOnRole(HttpServletRequest request, HttpServletResponse response, String role) throws IOException {
        String contextPath = request.getContextPath();
        if ("admin".equals(role)) {
            // Ensure we're not already on the admin dashboard to prevent loops
            String requestURI = request.getRequestURI();
            if (!requestURI.equals(contextPath + "/admin/dashboard")) {
                response.sendRedirect(contextPath + "/admin/dashboard");
            } else {
                // Already on dashboard, just forward to the JSP
                try {
					request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        } else {
            // Redirect other users to the general dashboard servlet
            response.sendRedirect(contextPath + "/dashboard");
        }
    }
}
