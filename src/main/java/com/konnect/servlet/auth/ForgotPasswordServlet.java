package com.konnect.servlet.auth;

import com.konnect.dao.UserDAO;
import com.konnect.model.User;
import com.konnect.util.EmailUtil;
import com.konnect.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Servlet for handling forgot password requests
 */
@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    /**
     * Handle GET requests - display forgot password form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
    }

    /**
     * Handle POST requests - process forgot password request
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        if (email != null && !email.isEmpty()) {
            User user = userDAO.getByEmail(email);

            if (user != null) {
                // Generate reset token and expiry time (1 hour from now)
                String resetCode = PasswordUtil.generateVerificationCode(6);
                Timestamp expiryTime = new Timestamp(System.currentTimeMillis() + (60 * 60 * 1000)); // 1 hour

                // Update user with reset token
                if (userDAO.setPasswordResetToken(user.getId(), resetCode, expiryTime)) {
                    // Send password reset email
                    boolean emailSent = EmailUtil.sendPasswordResetEmail(user.getEmail(), resetCode);

                    if (emailSent) {
                        request.setAttribute("message", "Password reset instructions have been sent to your email.");
                        request.setAttribute("messageType", "success");
                        request.getRequestDispatcher("/enter-reset-code.jsp").forward(request, response);
                        return;
                    } else {
                        request.setAttribute("message", "Failed to send password reset email. Please try again later.");
                        request.setAttribute("messageType", "error");
                    }
                } else {
                    request.setAttribute("message", "Failed to process your request. Please try again later.");
                    request.setAttribute("messageType", "error");
                }
            } else {
                // Don't reveal that the email doesn't exist for security reasons
                request.setAttribute("message", "If your email is registered, you will receive password reset instructions.");
                request.setAttribute("messageType", "info");
            }
        } else {
            request.setAttribute("message", "Please provide your email address.");
            request.setAttribute("messageType", "error");
        }

        request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
    }
}
