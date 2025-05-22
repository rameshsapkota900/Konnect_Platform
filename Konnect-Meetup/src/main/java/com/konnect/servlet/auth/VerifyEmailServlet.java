package com.konnect.servlet.auth;

import com.konnect.dao.UserDAO;
import com.konnect.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Servlet for handling email verification
 */
@WebServlet(name = "VerifyEmailServlet", urlPatterns = {"/verify-email"})
public class VerifyEmailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    /**
     * Handle GET requests - process verification code or display verification form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code != null && !code.isEmpty()) {
            User user = userDAO.getByVerificationCode(code);

            if (user != null) {
                // Check if verification code is still valid
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if (user.getVerificationExpiry() != null && now.before(user.getVerificationExpiry())) {
                    // Valid code, verify the user
                    user.setVerified(true);
                    user.setStatus("active");
                    user.setVerificationCode(null);
                    user.setVerificationExpiry(null);

                    if (userDAO.update(user)) {
                        request.setAttribute("message", "Your email has been verified successfully. You can now login.");
                        request.setAttribute("messageType", "success");
                        // Redirect to login page after successful verification
                        response.sendRedirect(request.getContextPath() + "/login?verified=true");
                        return;
                    } else {
                        request.setAttribute("message", "Failed to verify your email. Please try again.");
                        request.setAttribute("messageType", "error");
                    }
                } else {
                    request.setAttribute("message", "Verification code has expired. Please request a new one.");
                    request.setAttribute("messageType", "error");
                    request.setAttribute("expired", true);
                    request.setAttribute("email", user.getEmail());
                }
            } else {
                request.setAttribute("message", "Invalid verification code. Please check and try again.");
                request.setAttribute("messageType", "error");
            }
        }

        request.getRequestDispatcher("/verify-email.jsp").forward(request, response);
    }

    /**
     * Handle POST requests - resend verification email
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        if (email != null && !email.isEmpty()) {
            User user = userDAO.getByEmail(email);

            if (user != null && !user.isVerified()) {
                // Generate new verification code and send email
                String verificationCode = com.konnect.util.PasswordUtil.generateVerificationCode(6);
                Timestamp expiryTime = new Timestamp(System.currentTimeMillis() + (24 * 60 * 60 * 1000)); // 24 hours

                user.setVerificationCode(verificationCode);
                user.setVerificationExpiry(expiryTime);

                if (userDAO.update(user)) {
                    boolean emailSent = com.konnect.util.EmailUtil.sendVerificationEmail(user.getEmail(), verificationCode);

                    if (emailSent) {
                        request.setAttribute("message", "A new verification email has been sent. Please check your inbox.");
                        request.setAttribute("messageType", "success");
                    } else {
                        request.setAttribute("message", "Failed to send verification email. Please try again later.");
                        request.setAttribute("messageType", "error");
                    }
                } else {
                    request.setAttribute("message", "Failed to update verification code. Please try again.");
                    request.setAttribute("messageType", "error");
                }
            } else if (user != null && user.isVerified()) {
                request.setAttribute("message", "This email is already verified. You can login now.");
                request.setAttribute("messageType", "info");
            } else {
                request.setAttribute("message", "Email not found. Please register first.");
                request.setAttribute("messageType", "error");
            }
        } else {
            request.setAttribute("message", "Please provide your email address.");
            request.setAttribute("messageType", "error");
        }

        request.getRequestDispatcher("/verify-email.jsp").forward(request, response);
    }
}
