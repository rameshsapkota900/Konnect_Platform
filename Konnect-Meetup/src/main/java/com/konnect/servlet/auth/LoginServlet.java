package com.konnect.servlet.auth;

import com.konnect.dao.UserDAO;
import com.konnect.model.User;
import com.konnect.util.PasswordUtil;

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
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
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
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate input
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email and password are required");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Authenticate user
        User user = userDAO.getByEmail(email);

        if (user != null && PasswordUtil.verifyPassword(password, user.getPassword())) {
            // Check if user is banned
            if ("banned".equals(user.getStatus())) {
                request.setAttribute("error", "Your account has been banned. Please contact support.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            // Check if user is verified
            if (!user.isVerified()) {
                request.setAttribute("email", email);
                request.setAttribute("message", "Your account is not verified. Please check your email for verification instructions or request a new verification code.");
                request.setAttribute("messageType", "warning");
                request.getRequestDispatcher("/verify-email.jsp").forward(request, response);
                return;
            }

            // Create session and store user
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole());

            // Redirect based on role
            redirectBasedOnRole(user, response);
        } else {
            // Authentication failed
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
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
