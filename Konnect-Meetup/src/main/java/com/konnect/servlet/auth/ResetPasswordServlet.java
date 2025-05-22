package com.konnect.servlet.auth;

import com.konnect.dao.UserDAO;
import com.konnect.model.User;
import com.konnect.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Servlet for handling password reset
 */
@WebServlet(name = "ResetPasswordServlet", urlPatterns = {"/reset-password"})
public class ResetPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    /**
     * Handle GET requests - display reset password form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code != null && !code.isEmpty()) {
            User user = userDAO.getByResetToken(code);

            if (user != null) {
                // Check if reset token is still valid
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if (user.getResetTokenExpiry() != null && now.before(user.getResetTokenExpiry())) {
                    // Valid token, allow password reset
                    request.setAttribute("validToken", true);
                    request.setAttribute("resetCode", code);
                } else {
                    request.setAttribute("message", "Password reset link has expired. Please request a new one.");
                    request.setAttribute("messageType", "error");
                }
            } else {
                request.setAttribute("message", "Invalid password reset code.");
                request.setAttribute("messageType", "error");
            }
        }

        request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
    }

    /**
     * Handle POST requests - process password reset
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (code != null && !code.isEmpty() && password != null && !password.isEmpty() && confirmPassword != null && !confirmPassword.isEmpty()) {
            if (!password.equals(confirmPassword)) {
                request.setAttribute("message", "Passwords do not match.");
                request.setAttribute("messageType", "error");
                request.setAttribute("validToken", true);
                request.setAttribute("resetCode", code);
                request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
                return;
            }

            User user = userDAO.getByResetToken(code);

            if (user != null) {
                // Check if reset token is still valid
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if (user.getResetTokenExpiry() != null && now.before(user.getResetTokenExpiry())) {
                    // Hash the new password
                    String hashedPassword = PasswordUtil.hashPassword(password);

                    // Update user password and clear reset token
                    if (userDAO.updatePassword(user.getId(), hashedPassword)) {
                        request.setAttribute("message", "Your password has been reset successfully. You can now login with your new password.");
                        request.setAttribute("messageType", "success");
                    } else {
                        request.setAttribute("message", "Failed to reset your password. Please try again.");
                        request.setAttribute("messageType", "error");
                        request.setAttribute("validToken", true);
                        request.setAttribute("resetCode", code);
                    }
                } else {
                    request.setAttribute("message", "Password reset link has expired. Please request a new one.");
                    request.setAttribute("messageType", "error");
                }
            } else {
                request.setAttribute("message", "Invalid password reset code.");
                request.setAttribute("messageType", "error");
            }
        } else {
            request.setAttribute("message", "Please provide all required information.");
            request.setAttribute("messageType", "error");
            if (code != null && !code.isEmpty()) {
                request.setAttribute("validToken", true);
                request.setAttribute("resetCode", code);
            }
        }

        request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
    }
}
