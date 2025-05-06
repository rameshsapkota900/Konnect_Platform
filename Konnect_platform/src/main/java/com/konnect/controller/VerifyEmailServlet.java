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
 * Servlet for handling email verification
 */
@WebServlet("/verify")
public class VerifyEmailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private UserDAO userDAO;
    
    public void init() {
        userDAO = new UserDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get email from parameter
        String email = request.getParameter("email");
        
        if (email != null && !email.trim().isEmpty()) {
            // Store email in request for the verification form
            request.setAttribute("email", email);
        }
        
        // Forward to verification page
        request.getRequestDispatcher("/verify.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get parameters
        String email = request.getParameter("email");
        String verificationCode = request.getParameter("code");
        
        // Validate input
        if (email == null || email.trim().isEmpty() || 
            verificationCode == null || verificationCode.trim().isEmpty()) {
            
            request.setAttribute("error", "Email and verification code are required");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/verify.jsp").forward(request, response);
            return;
        }
        
        // Verify email
        boolean verified = userDAO.verifyEmail(email, verificationCode);
        
        if (verified) {
            // Verification successful
            User user = userDAO.getUserByEmail(email);
            
            if (user != null) {
                // Create session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());
                
                // Set success message
                request.setAttribute("success", "Email verified successfully! You are now logged in.");
                
                // Redirect based on role
                if (user.getRole().equals("creator")) {
                    response.sendRedirect("creator/dashboard.jsp");
                } else if (user.getRole().equals("business")) {
                    response.sendRedirect("business/dashboard.jsp");
                } else {
                    response.sendRedirect("index.jsp");
                }
            } else {
                // User not found (shouldn't happen)
                request.setAttribute("error", "User not found. Please try again.");
                request.setAttribute("email", email);
                request.getRequestDispatcher("/verify.jsp").forward(request, response);
            }
        } else {
            // Verification failed
            request.setAttribute("error", "Invalid verification code. Please try again.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/verify.jsp").forward(request, response);
        }
    }
}
