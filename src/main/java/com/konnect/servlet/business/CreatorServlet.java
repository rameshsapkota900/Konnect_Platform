package com.konnect.servlet.business;

import com.konnect.dao.CreatorDAO;
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
 * CreatorServlet
 * Handles individual creator profile view for businesses
 */
@WebServlet("/business/creator")
public class CreatorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CreatorDAO creatorDAO;
    
    @Override
    public void init() {
        creatorDAO = new CreatorDAO();
    }
    
    /**
     * Handle GET requests - display creator profile
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in and is a business
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"business".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get creator ID from request parameter
        String creatorIdStr = request.getParameter("id");
        if (creatorIdStr == null || creatorIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/business/creators");
            return;
        }
        
        // Parse creator ID
        int creatorId;
        try {
            creatorId = Integer.parseInt(creatorIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/business/creators");
            return;
        }
        
        // Get creator
        Creator creator = creatorDAO.getByUserId(creatorId);
        if (creator == null) {
            request.setAttribute("error", "Creator not found");
            request.getRequestDispatcher("/business/creators").forward(request, response);
            return;
        }
        
        // Set attributes for the view
        request.setAttribute("creator", creator);
        
        // Forward to creator profile page
        request.getRequestDispatcher("/business/creator-profile.jsp").forward(request, response);
    }
}
