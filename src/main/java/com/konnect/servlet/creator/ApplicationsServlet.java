package com.konnect.servlet.creator;

import com.konnect.dao.ApplicationDAO;
import com.konnect.dao.CampaignDAO;
import com.konnect.dao.CreatorDAO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ApplicationsServlet
 * Handles creator's applications list view
 */
@WebServlet("/creator/applications")
public class ApplicationsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ApplicationDAO applicationDAO;
    private CampaignDAO campaignDAO;
    private CreatorDAO creatorDAO;
    
    @Override
    public void init() {
        applicationDAO = new ApplicationDAO();
        campaignDAO = new CampaignDAO();
        creatorDAO = new CreatorDAO();
    }
    
    /**
     * Handle GET requests - display creator's applications
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
            response.sendRedirect(request.getContextPath() + "/creator/profile");
            return;
        }
        
        // Get creator profile ID
        int creatorProfileId = getCreatorProfileId(creator.getId());
        
        // Get creator's applications
        List<Application> applications = applicationDAO.getByCreatorId(creatorProfileId);
        
        // Get campaigns for each application
        Map<Integer, Campaign> campaignMap = new HashMap<>();
        for (Application app : applications) {
            Campaign campaign = campaignDAO.getById(app.getCampaignId());
            if (campaign != null) {
                campaignMap.put(app.getCampaignId(), campaign);
            }
        }
        
        // Set attributes for the view
        request.setAttribute("applications", applications);
        request.setAttribute("campaignMap", campaignMap);
        
        // Forward to applications page
        request.getRequestDispatcher("/creator/applications.jsp").forward(request, response);
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
