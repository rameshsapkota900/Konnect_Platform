package com.konnect.controller;

import java.io.IOException;

import com.konnect.dao.UserDAO;
import com.konnect.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet for handling password reset
 */
@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {
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
            // Store email in request for the reset form
            request.setAttribute("email", email);
        }
        
        // Forward to reset password page
        request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get parameters
        String email = request.getParameter("email");
        String resetCode = request.getParameter("code");
        String newPassword = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validate input
        if (email == null || email.trim().isEmpty() || 
            resetCode == null || resetCode.trim().isEmpty() || 
            newPassword == null || newPassword.trim().isEmpty() || 
            confirmPassword == null || confirmPassword.trim().isEmpty()) {
            
            request.setAttribute("error", "All fields are required");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
            return;
        }
        
        // Validate password match
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
            return;
        }
        
        // Generate new salt
        String salt = PasswordUtil.generateSalt();
        
        // Encrypt new password
        String encryptedPassword = PasswordUtil.encryptPassword(newPassword, salt);
        
        // Reset password
        boolean reset = userDAO.resetPassword(email, resetCode, encryptedPassword, salt);
        
        if (reset) {
            // Password reset successful
            request.setAttribute("success", "Password reset successful. You can now login with your new password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            // Password reset failed
            request.setAttribute("error", "Invalid or expired reset code. Please try again.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
        }
    }
}
