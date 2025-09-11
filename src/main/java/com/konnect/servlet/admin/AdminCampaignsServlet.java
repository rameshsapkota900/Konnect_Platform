package com.konnect.servlet.admin;

import com.konnect.dao.BusinessDAO;
import com.konnect.dao.CampaignDAO;
import com.konnect.model.Business;
import com.konnect.model.Campaign;
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
 * AdminCampaignsServlet
 * Handles campaign moderation for admins
 */
@WebServlet("/admin/campaigns")
public class AdminCampaignsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CampaignDAO campaignDAO;
    private BusinessDAO businessDAO;
    
    @Override
    public void init() {
        campaignDAO = new CampaignDAO();
        businessDAO = new BusinessDAO();
    }
    
    /**
     * Handle GET requests - display campaigns list
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
        String statusFilter = request.getParameter("status");
        String searchQuery = request.getParameter("search");
        
        // Get all campaigns
        List<Campaign> campaigns = campaignDAO.getAll();
        
        // Apply filters
        if (statusFilter != null && !statusFilter.isEmpty()) {
            campaigns.removeIf(c -> !c.getStatus().equals(statusFilter));
        }
        
        if (searchQuery != null && !searchQuery.isEmpty()) {
            campaigns.removeIf(c -> 
                !c.getTitle().toLowerCase().contains(searchQuery.toLowerCase()) && 
                !c.getDescription().toLowerCase().contains(searchQuery.toLowerCase())
            );
        }
        
        // Get business details for each campaign
        Map<Integer, Business> businessMap = new HashMap<>();
        for (Campaign campaign : campaigns) {
            int businessProfileId = campaign.getBusinessId();
            Business business = getBusinessByProfileId(businessProfileId);
            if (business != null) {
                businessMap.put(campaign.getId(), business);
            }
        }
        
        // Set attributes for the view
        request.setAttribute("campaigns", campaigns);
        request.setAttribute("businessMap", businessMap);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("searchQuery", searchQuery);
        
        // Forward to campaigns page
        request.getRequestDispatcher("/admin/campaigns.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - update campaign status
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
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get form data
        String campaignIdStr = request.getParameter("campaignId");
        String action = request.getParameter("action");
        
        if (campaignIdStr == null || campaignIdStr.trim().isEmpty() || action == null || action.trim().isEmpty()) {
            request.setAttribute("error", "Invalid request");
            doGet(request, response);
            return;
        }
        
        // Parse campaign ID
        int campaignId;
        try {
            campaignId = Integer.parseInt(campaignIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid campaign ID");
            doGet(request, response);
            return;
        }
        
        // Get campaign
        Campaign campaign = campaignDAO.getById(campaignId);
        if (campaign == null) {
            request.setAttribute("error", "Campaign not found");
            doGet(request, response);
            return;
        }
        
        // Update campaign status based on action
        boolean success = false;
        String successMessage = "";
        
        if ("activate".equals(action)) {
            campaign.setStatus("active");
            success = campaignDAO.update(campaign);
            successMessage = "Campaign activated successfully";
        } else if ("cancel".equals(action)) {
            campaign.setStatus("cancelled");
            success = campaignDAO.update(campaign);
            successMessage = "Campaign cancelled successfully";
        } else if ("delete".equals(action)) {
            success = campaignDAO.delete(campaignId);
            successMessage = "Campaign deleted successfully";
        } else {
            request.setAttribute("error", "Invalid action");
            doGet(request, response);
            return;
        }
        
        if (success) {
            request.setAttribute("success", successMessage);
        } else {
            request.setAttribute("error", "Failed to update campaign");
        }
        
        // Redirect back to campaigns page
        doGet(request, response);
    }
    
    /**
     * Get business by profile ID
     */
    private Business getBusinessByProfileId(int profileId) {
        String sql = "SELECT u.* FROM users u JOIN business_profiles b ON u.id = b.user_id WHERE b.id = ?";
        
        java.sql.Connection conn = null;
        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;
        Business business = null;
        
        try {
            conn = com.konnect.util.DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, profileId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("id");
                business = businessDAO.getByUserId(userId);
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
        
        return business;
    }
}
