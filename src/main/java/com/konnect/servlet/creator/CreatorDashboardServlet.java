package com.konnect.servlet.creator;

import com.konnect.dao.ApplicationDAO;
import com.konnect.dao.CampaignDAO;
import com.konnect.dao.CreatorDAO;
import com.konnect.dao.MessageDAO;
import com.konnect.model.Application;
import com.konnect.model.Campaign;
import com.konnect.model.Creator;
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
 * CreatorDashboardServlet
 * Handles creator dashboard requests
 */
@WebServlet("/creator/dashboard")
public class CreatorDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CreatorDAO creatorDAO;
    private CampaignDAO campaignDAO;
    private ApplicationDAO applicationDAO;
    private MessageDAO messageDAO;
    
    @Override
    public void init() {
        creatorDAO = new CreatorDAO();
        campaignDAO = new CampaignDAO();
        applicationDAO = new ApplicationDAO();
        messageDAO = new MessageDAO();
    }
    
    /**
     * Handle GET requests - display creator dashboard
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
        
        // Get creator profile
        Creator creator = creatorDAO.getByUserId(user.getId());
        if (creator == null) {
            // Creator profile not found, redirect to profile creation
            response.sendRedirect(request.getContextPath() + "/creator/profile");
            return;
        }
        
        // Get creator's applications
        int creatorProfileId = getCreatorProfileId(creator.getId());
        List<Application> applications = applicationDAO.getByCreatorId(creatorProfileId);
        
        // Get active campaigns
        List<Campaign> campaigns = campaignDAO.getAll();
        
        // Get unread messages count
        int unreadMessages = messageDAO.getUnreadCount(user.getId());
        
        // Set attributes for the dashboard
        request.setAttribute("creator", creator);
        request.setAttribute("applications", applications);
        request.setAttribute("campaigns", campaigns);
        request.setAttribute("unreadMessages", unreadMessages);
        
        // Forward to dashboard page
        request.getRequestDispatcher("/creator/dashboard.jsp").forward(request, response);
    }
    
    /**
     * Get creator profile ID by user ID
     * @param userId User ID
     * @return Creator profile ID if found, -1 otherwise
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
