package com.konnect.controller.auth;

import com.konnect.dao.UserDao;
import com.konnect.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password"); // Will be hashed by DAO
        String confirmPassword = request.getParameter("confirmPassword");
        String role = request.getParameter("role"); // "creator" or "business"

        String redirectUrl = request.getContextPath();
        String errorMessage = null;

        // Basic Validation
        if (username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.isEmpty() ||
            confirmPassword == null || confirmPassword.isEmpty() ||
            role == null || (!role.equals("creator") && !role.equals("business"))) {
            errorMessage = "All fields are required and role must be valid.";
        } else if (!password.equals(confirmPassword)) {
            errorMessage = "Passwords do not match.";
        } else if (password.length() < 6) { // Simple password length check
             errorMessage = "Password must be at least 6 characters long.";
        }

        if (errorMessage != null) {
             System.out.println("Registration failed (validation): " + errorMessage);
             response.sendRedirect(redirectUrl + "/register.jsp?error=" + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
            return;
        }

        UserDao userDao = new UserDao();
        User newUser = new User();
        newUser.setUsername(username.trim());
        newUser.setEmail(email.trim());
        newUser.setPasswordHash(password); // Pass plain password, DAO will hash it
        newUser.setRole(role);

        boolean success = userDao.createUser(newUser);

        if (success) {
             System.out.println("Registration successful for " + username);
            // Redirect to login page with a success message
             response.sendRedirect(redirectUrl + "/login.jsp?message=Registration successful! Please login.");
        } else {
            // Check if it failed due to duplicate username/email (DAO might log details)
            // We need a way to distinguish this from other DB errors. Let's assume duplicate for now.
             errorMessage = "Username or Email already exists. Please choose different credentials.";
             System.out.println("Registration failed (duplicate/DB error) for " + username);
             response.sendRedirect(redirectUrl + "/register.jsp?error=" + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
        }
    }

     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect GET requests to register page
        response.sendRedirect(request.getContextPath() + "/register.jsp");
    }
}
