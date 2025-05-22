package com.konnect.servlet.business;

import com.konnect.dao.ApplicationDAO;
import com.konnect.dao.BusinessDAO;
import com.konnect.dao.CampaignDAO;
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
 * CampaignsServlet
 * Handles campaign management for businesses
 */
@WebServlet("/business/campaigns")
public class CampaignsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CampaignDAO campaignDAO;
    private BusinessDAO businessDAO;
    private ApplicationDAO applicationDAO;
    
    @Override
    public void init() {
        campaignDAO = new CampaignDAO();
        businessDAO = new BusinessDAO();
        applicationDAO = new ApplicationDAO();
    }
    
    /**
     * Handle GET requests - display business campaigns
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
        
        // Get business profile ID
        int businessProfileId = businessDAO.getBusinessProfileId(user.getId());
        if (businessProfileId == -1) {
            // Business profile not found, redirect to profile creation
            response.sendRedirect(request.getContextPath() + "/business/profile");
            return;
        }
        
        // Get filter parameter
        String statusFilter = request.getParameter("status");
        
        // Get business campaigns
        List<Campaign> campaigns = campaignDAO.getByBusinessId(businessProfileId);
        
        // Get application counts for each campaign
        Map<Integer, Integer> applicationCounts = new HashMap<>();
        Map<Integer, Integer> pendingCounts = new HashMap<>();
        Map<Integer, Integer> approvedCounts = new HashMap<>();
        Map<Integer, Integer> completedCounts = new HashMap<>();
        
        for (Campaign campaign : campaigns) {
            List<com.konnect.model.Application> applications = applicationDAO.getByCampaignId(campaign.getId());
            
            int totalCount = applications.size();
            int pendingCount = 0;
            int approvedCount = 0;
            int completedCount = 0;
            
            for (com.konnect.model.Application app : applications) {
                if ("pending".equals(app.getStatus())) {
                    pendingCount++;
                } else if ("approved".equals(app.getStatus())) {
                    approvedCount++;
                } else if ("completed".equals(app.getStatus())) {
                    completedCount++;
                }
            }
            
            applicationCounts.put(campaign.getId(), totalCount);
            pendingCounts.put(campaign.getId(), pendingCount);
            approvedCounts.put(campaign.getId(), approvedCount);
            completedCounts.put(campaign.getId(), completedCount);
        }
        
        // Set attributes for the view
        request.setAttribute("campaigns", campaigns);
        request.setAttribute("applicationCounts", applicationCounts);
        request.setAttribute("pendingCounts", pendingCounts);
        request.setAttribute("approvedCounts", approvedCounts);
        request.setAttribute("completedCounts", completedCounts);
        request.setAttribute("statusFilter", statusFilter);
        
        // Forward to campaigns page
        request.getRequestDispatcher("/business/campaigns.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - update campaign status
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
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
        
        // Get business profile ID
        int businessProfileId = businessDAO.getBusinessProfileId(user.getId());
        if (businessProfileId == -1) {
            // Business profile not found, redirect to profile creation
            response.sendRedirect(request.getContextPath() + "/business/profile");
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
        
        // Check if campaign belongs to this business
        if (campaign.getBusinessId() != businessProfileId) {
            request.setAttribute("error", "You don't have permission to modify this campaign");
            doGet(request, response);
            return;
        }
        
        // Update campaign status based on action
        String newStatus = null;
        if ("cancel".equals(action)) {
            newStatus = "cancelled";
        } else if ("complete".equals(action)) {
            newStatus = "completed";
        } else if ("activate".equals(action)) {
            newStatus = "active";
        }
        
        if (newStatus != null) {
            campaign.setStatus(newStatus);
            boolean success = campaignDAO.update(campaign);
            
            if (success) {
                request.setAttribute("success", "Campaign status updated successfully");
            } else {
                request.setAttribute("error", "Failed to update campaign status");
            }
        } else {
            request.setAttribute("error", "Invalid action");
        }
        
        // Redirect back to campaigns page
        doGet(request, response);
    }
}
