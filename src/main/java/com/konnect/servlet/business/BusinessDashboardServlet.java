package com.konnect.servlet.business;

import com.konnect.dao.ApplicationDAO;
import com.konnect.dao.BusinessDAO;
import com.konnect.dao.CampaignDAO;
import com.konnect.dao.MessageDAO;
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
 * BusinessDashboardServlet
 * Handles business dashboard requests
 */
@WebServlet("/business/dashboard")
public class BusinessDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BusinessDAO businessDAO;
    private CampaignDAO campaignDAO;
    private ApplicationDAO applicationDAO;
    private MessageDAO messageDAO;
    
    @Override
    public void init() {
        businessDAO = new BusinessDAO();
        campaignDAO = new CampaignDAO();
        applicationDAO = new ApplicationDAO();
        messageDAO = new MessageDAO();
    }
    
    /**
     * Handle GET requests - display business dashboard
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
        
        // Get business profile
        Business business = businessDAO.getByUserId(user.getId());
        if (business == null) {
            // Business profile not found, redirect to profile creation
            response.sendRedirect(request.getContextPath() + "/business/profile");
            return;
        }
        
        // Get business profile ID
        int businessProfileId = businessDAO.getBusinessProfileId(user.getId());
        
        // Get business campaigns
        List<Campaign> campaigns = campaignDAO.getByBusinessId(businessProfileId);
        
        // Get application counts for each campaign
        Map<Integer, Integer> applicationCounts = new HashMap<>();
        for (Campaign campaign : campaigns) {
            List<com.konnect.model.Application> applications = applicationDAO.getByCampaignId(campaign.getId());
            applicationCounts.put(campaign.getId(), applications.size());
        }
        
        // Get unread messages count
        int unreadMessages = messageDAO.getUnreadCount(user.getId());
        
        // Set attributes for the dashboard
        request.setAttribute("business", business);
        request.setAttribute("campaigns", campaigns);
        request.setAttribute("applicationCounts", applicationCounts);
        request.setAttribute("unreadMessages", unreadMessages);
        
        // Forward to dashboard page
        request.getRequestDispatcher("/business/dashboard.jsp").forward(request, response);
    }
}
