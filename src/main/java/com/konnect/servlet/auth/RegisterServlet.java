package com.konnect.servlet.auth;

import com.konnect.dao.UserDAO;
import com.konnect.model.Business;
import com.konnect.model.Creator;
import com.konnect.model.User;
import com.konnect.util.EmailUtil;
import com.konnect.util.PasswordUtil;
import com.konnect.util.InputValidationUtil;
import com.konnect.util.InputValidationUtil.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);
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
        try {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String role = request.getParameter("role");
            
            logger.info("Registration attempt for username: {}, email: {}", username, email);

            // Comprehensive input validation using our new validation utility
            ValidationResult validationResult = InputValidationUtil.validateRegistration(
                username, email, password, confirmPassword, role
            );
            
            if (!validationResult.isValid()) {
                logger.warn("Registration validation failed: {}", validationResult.getMessage());
                request.setAttribute("error", validationResult.getMessage());
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("role", role);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // Additional security checks - sanitize inputs
            username = InputValidationUtil.sanitizeForDatabase(username.trim());
            email = InputValidationUtil.sanitizeForDatabase(email.trim().toLowerCase());
            role = InputValidationUtil.sanitizeForDatabase(role.trim());

        // Validate password strength
        if (!PasswordUtil.isPasswordStrong(password)) {
            request.setAttribute("error", PasswordUtil.getPasswordRequirements());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

            // Check if username or email already exists
            User existingUser = userDAO.getByUsername(username);
            if (existingUser != null) {
                logger.warn("Registration failed - username already exists: {}", username);
                request.setAttribute("error", "Username already exists");
                request.setAttribute("email", email);
                request.setAttribute("role", role);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            existingUser = userDAO.getByEmail(email);
            if (existingUser != null) {
                logger.warn("Registration failed - email already exists: {}", email);
                request.setAttribute("error", "Email already exists");
                request.setAttribute("username", username);
                request.setAttribute("role", role);
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
                logger.warn("Registration failed - invalid role: {}", role);
                request.setAttribute("error", "Invalid role selected");
                request.setAttribute("username", username);
                request.setAttribute("email", email);
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
                logger.info("User registered successfully: {}", email);
                
                // Send verification email
                boolean emailSent = EmailUtil.sendVerificationEmail(email, verificationCode);

                if (emailSent) {
                    // Redirect to verification page
                    request.setAttribute("email", email);
                    request.setAttribute("message", "Registration successful! Please check your email to verify your account.");
                    request.getRequestDispatcher("/verify-email.jsp").forward(request, response);
                } else {
                    // Email sending failed
                    logger.error("Failed to send verification email for user: {}", email);
                    request.setAttribute("error", "Registration successful but failed to send verification email. Please contact support.");
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                }
            } else {
                // Registration failed
                logger.error("Failed to insert user into database: {}", email);
                request.setAttribute("error", "Registration failed. Please try again.");
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("role", role);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
            
        } catch (SecurityException e) {
            logger.error("Security violation during registration: {}", e.getMessage());
            request.setAttribute("error", "Invalid input detected. Please check your information and try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Unexpected error during registration", e);
            request.setAttribute("error", "An unexpected error occurred. Please try again later.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
