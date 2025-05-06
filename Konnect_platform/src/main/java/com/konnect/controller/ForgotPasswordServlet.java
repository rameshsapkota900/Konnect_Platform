package com.konnect.controller;

import java.io.IOException;

import com.konnect.dao.UserDAO;
import com.konnect.model.User;
import com.konnect.util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet for handling forgot password requests
 */
@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to forgot password page
        request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get email parameter
        String email = request.getParameter("email");

        // Validate input
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            return;
        }

        // Check if email exists
        User user = userDAO.getUserByEmail(email);

        if (user != null) {
            // Generate reset code
            String resetCode = userDAO.generateResetCode(email);

            if (resetCode != null) {
                // Send reset email
                boolean emailSent = EmailUtil.sendPasswordResetEmail(email, resetCode);

                if (emailSent) {
                    // Redirect to reset password page with success message
                    request.setAttribute("success", "Password reset instructions have been sent to your email address: " + email);
                    request.setAttribute("email", email);
                    request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
                } else {
                    // Email sending failed
                    request.setAttribute("error", "Failed to send reset email. Please try again or contact support.");
                    request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                }
            } else {
                // Failed to generate reset code
                request.setAttribute("error", "Failed to process your request. Please try again.");
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            }
        } else {
            // Email not found - show clear error message
            request.setAttribute("error", "The email address '" + email + "' is not registered in our system. Please check your email or register for a new account.");
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
        }
    }
}
