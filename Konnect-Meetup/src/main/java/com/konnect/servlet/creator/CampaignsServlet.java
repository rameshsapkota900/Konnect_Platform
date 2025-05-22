package com.konnect.servlet.creator;

import com.konnect.dao.ApplicationDAO;
import com.konnect.dao.CampaignDAO;
import com.konnect.dao.CreatorDAO;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CampaignsServlet
 * Handles campaign browsing for creators
 */
@WebServlet("/creator/campaigns")
public class CampaignsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CampaignDAO campaignDAO;
    private CreatorDAO creatorDAO;
    private ApplicationDAO applicationDAO;
    
    @Override
    public void init() {
        campaignDAO = new CampaignDAO();
        creatorDAO = new CreatorDAO();
        applicationDAO = new ApplicationDAO();
    }
    
    /**
     * Handle GET requests - display campaigns list
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
        
        // Get filter parameters
        String keyword = request.getParameter("keyword");
        String minBudgetStr = request.getParameter("minBudget");
        String interestFilter = request.getParameter("interest");
        
        // Get all active campaigns
        List<Campaign> allCampaigns = campaignDAO.getAll();
        List<Campaign> filteredCampaigns = new ArrayList<>();
        
        // Apply filters
        double minBudget = 0;
        if (minBudgetStr != null && !minBudgetStr.isEmpty()) {
            try {
                minBudget = Double.parseDouble(minBudgetStr);
            } catch (NumberFormatException e) {
                // Invalid budget, ignore filter
            }
        }
        
        for (Campaign campaign : allCampaigns) {
            // Only show active campaigns
            if (!"active".equals(campaign.getStatus())) {
                continue;
            }
            
            // Apply keyword filter
            if (keyword != null && !keyword.isEmpty()) {
                if (!campaign.getTitle().toLowerCase().contains(keyword.toLowerCase()) && 
                    !campaign.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                    continue;
                }
            }
            
            // Apply budget filter
            if (minBudget > 0 && campaign.getBudget() < minBudget) {
                continue;
            }
            
            // Apply interest filter
            if (interestFilter != null && !interestFilter.isEmpty()) {
                if (campaign.getTargetInterests() == null || 
                    !campaign.getTargetInterests().contains(interestFilter)) {
                    continue;
                }
            }
            
            filteredCampaigns.add(campaign);
        }
        
        // Check if creator meets minimum follower requirements and has already applied
        int creatorProfileId = getCreatorProfileId(creator.getId());
        Map<Integer, Boolean> meetsRequirementMap = new HashMap<>();
        Map<Integer, Boolean> hasAppliedMap = new HashMap<>();
        
        for (Campaign campaign : filteredCampaigns) {
            // Check if creator meets minimum follower requirement
            boolean meetsRequirement = creator.getFollowerCount() >= campaign.getMinFollowers();
            meetsRequirementMap.put(campaign.getId(), meetsRequirement);
            
            // Check if creator has already applied
            boolean hasApplied = applicationDAO.hasApplied(campaign.getId(), creatorProfileId);
            hasAppliedMap.put(campaign.getId(), hasApplied);
        }
        
        // Set attributes for the view
        request.setAttribute("campaigns", filteredCampaigns);
        request.setAttribute("meetsRequirementMap", meetsRequirementMap);
        request.setAttribute("hasAppliedMap", hasAppliedMap);
        request.setAttribute("keyword", keyword);
        request.setAttribute("minBudget", minBudgetStr);
        request.setAttribute("interestFilter", interestFilter);
        
        // Get all unique interests from campaigns for filter dropdown
        List<String> allInterests = new ArrayList<>();
        for (Campaign campaign : allCampaigns) {
            if (campaign.getTargetInterests() != null) {
                for (String interest : campaign.getTargetInterests()) {
                    if (!allInterests.contains(interest)) {
                        allInterests.add(interest);
                    }
                }
            }
        }
        request.setAttribute("allInterests", allInterests);
        
        // Forward to campaigns page
        request.getRequestDispatcher("/creator/campaigns.jsp").forward(request, response);
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
