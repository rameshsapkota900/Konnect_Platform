package com.konnect.controller;

import java.io.IOException;

import com.konnect.dao.UserDAO;
import com.konnect.model.User;
import com.konnect.util.EmailUtil;
import com.konnect.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet for handling user registration
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private UserDAO userDAO;
    
    public void init() {
        userDAO = new UserDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward to registration page
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get registration parameters
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
        
        // Validate role
        if (!role.equals("creator") && !role.equals("business")) {
            request.setAttribute("error", "Invalid role selected");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        // Check if username already exists
        if (userDAO.isUsernameExists(username)) {
            request.setAttribute("error", "Username already exists");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        // Check if email already exists
        if (userDAO.isEmailExists(email)) {
            request.setAttribute("error", "Email already exists");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        // Generate salt for password encryption
        String salt = PasswordUtil.generateSalt();
        
        // Encrypt password
        String encryptedPassword = PasswordUtil.encryptPassword(password, salt);
        
        // Create user object
        User user = new User(username, email, encryptedPassword, salt, role);
        
        // Register user
        int userId = userDAO.registerUser(user);
        
        if (userId > 0) {
            // Registration successful
            
            // Send verification email
            boolean emailSent = EmailUtil.sendVerificationEmail(user.getEmail(), user.getVerificationCode());
            
            if (emailSent) {
                // Redirect to verification page
                request.setAttribute("success", "Registration successful! Please check your email to verify your account.");
                response.sendRedirect("verify?email=" + user.getEmail());
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
