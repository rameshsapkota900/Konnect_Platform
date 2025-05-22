package com.konnect.servlet.admin;

import com.konnect.dao.BusinessDAO;
import com.konnect.dao.CreatorDAO;
import com.konnect.dao.UserDAO;
import com.konnect.model.Business;
import com.konnect.model.Creator;
import com.konnect.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * AdminUserDetailsServlet
 * Handles user detail view for admins
 */
@WebServlet("/admin/user-details")
public class AdminUserDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    private CreatorDAO creatorDAO;
    private BusinessDAO businessDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
        creatorDAO = new CreatorDAO();
        businessDAO = new BusinessDAO();
    }
    
    /**
     * Handle GET requests - display user details
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
        
        User currentUser = (User) session.getAttribute("user");
        if (!"admin".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get user ID from request parameter
        String userIdStr = request.getParameter("id");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }
        
        // Parse user ID
        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }
        
        // Get user
        User user = userDAO.getById(userId);
        if (user == null) {
            request.setAttribute("error", "User not found");
            request.getRequestDispatcher("/admin/users").forward(request, response);
            return;
        }
        
        // Get role-specific details
        if ("creator".equals(user.getRole())) {
            Creator creator = creatorDAO.getByUserId(userId);
            request.setAttribute("creator", creator);
        } else if ("business".equals(user.getRole())) {
            Business business = businessDAO.getByUserId(userId);
            request.setAttribute("business", business);
        }
        
        // Set user attribute
        request.setAttribute("userDetails", user);
        
        // Forward to user details page
        request.getRequestDispatcher("/admin/user-details.jsp").forward(request, response);
    }
}
