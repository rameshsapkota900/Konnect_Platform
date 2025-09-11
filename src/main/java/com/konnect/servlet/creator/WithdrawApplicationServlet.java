package com.konnect.servlet.creator;

import com.konnect.dao.ApplicationDAO;
import com.konnect.dao.CreatorDAO;
import com.konnect.model.Application;
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
 * WithdrawApplicationServlet
 * Handles application withdrawal for creators
 */
@WebServlet("/creator/withdraw")
public class WithdrawApplicationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ApplicationDAO applicationDAO;
    private CreatorDAO creatorDAO;
    
    @Override
    public void init() {
        applicationDAO = new ApplicationDAO();
        creatorDAO = new CreatorDAO();
    }
    
    /**
     * Handle GET requests - process application withdrawal
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in and is a creator
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"creator".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get application ID from request parameter
        String applicationIdStr = request.getParameter("id");
        if (applicationIdStr == null || applicationIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/creator/applications");
            return;
        }
        
        // Parse application ID
        int applicationId;
        try {
            applicationId = Integer.parseInt(applicationIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/creator/applications");
            return;
        }
        
        // Get application
        Application application = applicationDAO.getById(applicationId);
        if (application == null) {
            request.setAttribute("error", "Application not found");
            request.getRequestDispatcher("/creator/applications").forward(request, response);
            return;
        }
        
        // Get creator profile
        Creator creator = creatorDAO.getByUserId(user.getId());
        if (creator == null) {
            response.sendRedirect(request.getContextPath() + "/creator/profile");
            return;
        }
        
        // Get creator profile ID
        int creatorProfileId = getCreatorProfileId(creator.getId());
        
        // Check if application belongs to this creator
        if (application.getCreatorId() != creatorProfileId) {
            request.setAttribute("error", "You don't have permission to withdraw this application");
            request.getRequestDispatcher("/creator/applications").forward(request, response);
            return;
        }
        
        // Check if application is in pending status
        if (!"pending".equals(application.getStatus())) {
            request.setAttribute("error", "Only pending applications can be withdrawn");
            request.getRequestDispatcher("/creator/applications").forward(request, response);
            return;
        }
        
        // Delete application
        boolean success = applicationDAO.delete(applicationId);
        
        if (success) {
            request.setAttribute("success", "Application withdrawn successfully");
        } else {
            request.setAttribute("error", "Failed to withdraw application");
        }
        
        // Redirect back to applications page
        response.sendRedirect(request.getContextPath() + "/creator/applications");
    }
    
    /**
     * Get creator profile ID by user ID
     */
    private int getCreatorProfileId(int userId) {
        String sql = "SELECT id FROM creator_profiles WHERE user_id = ?";
        
        java.sql.Connection conn = null;
        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;
        int creatorProfileId = -1;
        
        try {
            conn = com.konnect.util.DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                creatorProfileId = rs.getInt("id");
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) com.konnect.util.DBConnection.closeConnection(conn);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        
        return creatorProfileId;
    }
}
