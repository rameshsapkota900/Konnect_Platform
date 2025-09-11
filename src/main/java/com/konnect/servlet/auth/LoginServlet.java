package com.konnect.servlet.auth;

import com.konnect.dao.UserDAO;
import com.konnect.model.User;
import com.konnect.util.PasswordUtil;
import com.konnect.util.PasswordMigrationService;
import com.konnect.util.InputValidationUtil;
import com.konnect.util.InputValidationUtil.ValidationResult;
import com.konnect.util.ErrorHandlingUtil;
import com.konnect.exception.AuthenticationException;
import com.konnect.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * LoginServlet
 * Handles user login requests
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    
    private UserDAO userDAO;
    private PasswordMigrationService migrationService;

    @Override
    public void init() {
        userDAO = new UserDAO();
        migrationService = new PasswordMigrationService();
        logger.info("LoginServlet initialized with password migration support");
    }

    /**
     * Handle GET requests - display login form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // User is already logged in, redirect based on role
            User user = (User) session.getAttribute("user");
            redirectBasedOnRole(user, response);
            return;
        }

        // Check if user just verified their email
        String verified = request.getParameter("verified");
        if (verified != null && verified.equals("true")) {
            request.setAttribute("message", "Your email has been verified successfully. You can now login.");
            request.setAttribute("messageType", "success");
        }

        // Forward to login page
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * Handle POST requests - process login form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
            // Enhanced input validation with proper exceptions
            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                logger.warn("Login attempt with empty credentials from IP: {}", 
                           ErrorHandlingUtil.getClientIpAddress(request));
                throw new AuthenticationException("Empty credentials provided", 
                                                "Email and password are required");
            }
            
            // Validate email format
            ValidationResult emailValidation = InputValidationUtil.validateEmail(email);
            if (!emailValidation.isValid()) {
                logger.warn("Login attempt with invalid email format: {} from IP: {}", 
                           email, ErrorHandlingUtil.getClientIpAddress(request));
                throw new AuthenticationException("Invalid email format: " + email, 
                                                "Please enter a valid email address");
            }
            
            // Sanitize inputs
            email = InputValidationUtil.sanitizeForDatabase(email.trim().toLowerCase());
            password = InputValidationUtil.sanitizeForDatabase(password);

            // Authenticate user with enhanced error handling
            User user;
            try {
                user = userDAO.getByEmail(email);
            } catch (Exception e) {
                throw new DatabaseException("Failed to retrieve user by email: " + email, e);
            }

            if (user != null && PasswordUtil.verifyPassword(password, user.getPassword())) {
                // Check if user is banned
                if ("banned".equals(user.getStatus())) {
                    logger.warn("Banned user attempted login: {}", email);
                    throw new AuthenticationException("Banned user login attempt: " + email, 
                                                    "Your account has been banned. Please contact support.");
                }

                // Check if user is verified
                if (!user.isVerified()) {
                    logger.info("Unverified user attempted login: {}", email);
                    request.setAttribute("email", email);
                    request.setAttribute("message", "Your account is not verified. Please check your email for verification instructions or request a new verification code.");
                    request.setAttribute("messageType", "warning");
                    request.getRequestDispatcher("/verify-email.jsp").forward(request, response);
                    return;
                }

                // Migrate password if using legacy hash
                if (PasswordUtil.isLegacyHash(user.getPassword())) {
                    try {
                        boolean migrated = migrationService.migrateUserPassword(user.getId(), password);
                        if (migrated) {
                            logger.info("Successfully migrated legacy password for user: {}", user.getEmail());
                        } else {
                            logger.warn("Failed to migrate legacy password for user: {}", user.getEmail());
                        }
                    } catch (Exception e) {
                        logger.error("Error during password migration for user: {}", user.getEmail(), e);
                        // Continue with login even if migration fails
                    }
                }

                // Create session and store user
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userRole", user.getRole());

                logger.info("User {} logged in successfully from IP: {}", user.getEmail(), request.getRemoteAddr());

                // Log performance and redirect based on role
                ErrorHandlingUtil.logPerformanceWarning("User login", startTime, 2000);
                redirectBasedOnRole(user, response);
            } else {
                // Authentication failed
                logger.warn("Failed login attempt for email: {} from IP: {}", 
                           email, ErrorHandlingUtil.getClientIpAddress(request));
                throw new AuthenticationException("Invalid credentials for: " + email, 
                                                "Invalid email or password");
            }
            
        } catch (AuthenticationException | DatabaseException e) {
            // Handle our custom exceptions with proper error responses
            ErrorHandlingUtil.handleServletException(request, response, e, "user login");
        } catch (SecurityException e) {
            // Handle security violations
            ErrorHandlingUtil.handleServletException(request, response, e, "login security check");
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            ErrorHandlingUtil.handleServletException(request, response, e, "unexpected login error");
        }
    }

    /**
     * Redirect user based on their role
     * @param user User to redirect
     * @param response HttpServletResponse to send redirect
     * @throws IOException if an I/O error occurs
     */
    private void redirectBasedOnRole(User user, HttpServletResponse response) throws IOException {
        String role = user.getRole();

        switch (role) {
            case "admin":
                response.sendRedirect("admin/dashboard");
                break;
            case "creator":
                response.sendRedirect("creator/dashboard");
                break;
            case "business":
                response.sendRedirect("business/dashboard");
                break;
            default:
                response.sendRedirect("index.jsp");
                break;
        }
    }
}
