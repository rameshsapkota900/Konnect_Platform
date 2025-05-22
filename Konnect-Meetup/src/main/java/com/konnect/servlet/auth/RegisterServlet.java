package com.konnect.servlet.auth;

import com.konnect.dao.UserDAO;
import com.konnect.model.Business;
import com.konnect.model.Creator;
import com.konnect.model.User;
import com.konnect.util.EmailUtil;
import com.konnect.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * RegisterServlet
 * Handles user registration requests
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    /**
     * Handle GET requests - display registration form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to registration page
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    /**
     * Handle POST requests - process registration form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String role = request.getParameter("role");

        // Validate input
        if (username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {

            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Validate password match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Check if username or email already exists
        User existingUser = userDAO.getByUsername(username);
        if (existingUser != null) {
            request.setAttribute("error", "Username already exists");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        existingUser = userDAO.getByEmail(email);
        if (existingUser != null) {
            request.setAttribute("error", "Email already exists");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Hash the password
        String hashedPassword = PasswordUtil.hashPassword(password);

        // Generate verification code and expiry time (24 hours from now)
        String verificationCode = PasswordUtil.generateVerificationCode(6);
        Timestamp expiryTime = new Timestamp(System.currentTimeMillis() + (24 * 60 * 60 * 1000));

        // Create user based on role
        User user;
        if ("creator".equals(role)) {
            user = new Creator(username, email, hashedPassword);
        } else if ("business".equals(role)) {
            user = new Business(username, email, hashedPassword);
        } else {
            request.setAttribute("error", "Invalid role selected");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Set verification details
        user.setVerificationCode(verificationCode);
        user.setVerificationExpiry(expiryTime);
        user.setVerified(false);
        user.setStatus("pending");

        // Insert user into database
        int userId = userDAO.insert(user);

        if (userId > 0) {
            // Send verification email
            boolean emailSent = EmailUtil.sendVerificationEmail(email, verificationCode);

            if (emailSent) {
                // Redirect to verification page
                request.setAttribute("email", email);
                request.setAttribute("message", "Registration successful! Please check your email to verify your account.");
                request.getRequestDispatcher("/verify-email.jsp").forward(request, response);
            } else {
                // Email sending failed
                request.setAttribute("error", "Registration successful but failed to send verification email. Please contact support.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } else {
            // Registration failed
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
