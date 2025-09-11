package com.konnect.servlet.admin;

import com.konnect.dao.UserDAO;
import com.konnect.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * AdminUsersServlet
 * Handles user management for admins
 */
@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
    }
    
    /**
     * Handle GET requests - display users list
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in and is an admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get filter parameters
        String roleFilter = request.getParameter("role");
        String statusFilter = request.getParameter("status");
        String searchQuery = request.getParameter("search");
        
        // Get all users
        List<User> allUsers = userDAO.getAll();
        
        // Apply filters
        if (roleFilter != null && !roleFilter.isEmpty()) {
            allUsers.removeIf(u -> !u.getRole().equals(roleFilter));
        }
        
        if (statusFilter != null && !statusFilter.isEmpty()) {
            allUsers.removeIf(u -> !u.getStatus().equals(statusFilter));
        }
        
        if (searchQuery != null && !searchQuery.isEmpty()) {
            allUsers.removeIf(u -> 
                !u.getUsername().toLowerCase().contains(searchQuery.toLowerCase()) && 
                !u.getEmail().toLowerCase().contains(searchQuery.toLowerCase())
            );
        }
        
        // Set attributes for the view
        request.setAttribute("users", allUsers);
        request.setAttribute("roleFilter", roleFilter);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("searchQuery", searchQuery);
        
        // Forward to users page
        request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - update user status
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in and is an admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        if (!"admin".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get form data
        String userIdStr = request.getParameter("userId");
        String action = request.getParameter("action");
        
        if (userIdStr == null || userIdStr.trim().isEmpty() || action == null || action.trim().isEmpty()) {
            request.setAttribute("error", "Invalid request");
            doGet(request, response);
            return;
        }
        
        // Parse user ID
        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid user ID");
            doGet(request, response);
            return;
        }
        
        // Get user
        User user = userDAO.getById(userId);
        if (user == null) {
            request.setAttribute("error", "User not found");
            doGet(request, response);
            return;
        }
        
        // Prevent admin from changing their own status
        if (user.getId() == currentUser.getId()) {
            request.setAttribute("error", "You cannot change your own status");
            doGet(request, response);
            return;
        }
        
        // Update user status based on action
        boolean success = false;
        String successMessage = "";
        
        if ("ban".equals(action)) {
            user.setStatus("banned");
            success = userDAO.update(user);
            successMessage = "User banned successfully";
        } else if ("unban".equals(action)) {
            user.setStatus("active");
            success = userDAO.update(user);
            successMessage = "User unbanned successfully";
        } else {
            request.setAttribute("error", "Invalid action");
            doGet(request, response);
            return;
        }
        
        if (success) {
            request.setAttribute("success", successMessage);
        } else {
            request.setAttribute("error", "Failed to update user status");
        }
        
        // Redirect back to users page
        doGet(request, response);
    }
}
