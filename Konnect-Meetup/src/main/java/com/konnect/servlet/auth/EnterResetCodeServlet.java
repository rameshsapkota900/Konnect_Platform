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
 * Servlet for handling reset code verification
 */
@WebServlet(name = "EnterResetCodeServlet", urlPatterns = {"/enter-reset-code"})
public class EnterResetCodeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    /**
     * Handle POST requests - verify reset code
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resetCode = request.getParameter("resetCode");

        if (resetCode != null && !resetCode.isEmpty()) {
            User user = userDAO.getByResetToken(resetCode);

            if (user != null) {
                // Check if reset token is still valid
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if (user.getResetTokenExpiry() != null && now.before(user.getResetTokenExpiry())) {
                    // Valid token, redirect to reset password page
                    response.sendRedirect(request.getContextPath() + "/reset-password?code=" + resetCode);
                    return;
                } else {
                    request.setAttribute("message", "Password reset code has expired. Please request a new one.");
                    request.setAttribute("messageType", "error");
                }
            } else {
                request.setAttribute("message", "Invalid reset code. Please check and try again.");
                request.setAttribute("messageType", "error");
            }
        } else {
            request.setAttribute("message", "Please enter the reset code.");
            request.setAttribute("messageType", "error");
        }

        request.getRequestDispatcher("/enter-reset-code.jsp").forward(request, response);
    }
} 