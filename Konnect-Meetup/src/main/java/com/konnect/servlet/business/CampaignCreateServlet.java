package com.konnect.servlet.business;

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
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CampaignCreateServlet
 * Handles campaign creation
 */
@WebServlet("/business/campaign-create")
public class CampaignCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BusinessDAO businessDAO;
    private CampaignDAO campaignDAO;
    
    @Override
    public void init() {
        businessDAO = new BusinessDAO();
        campaignDAO = new CampaignDAO();
    }
    
    /**
     * Handle GET requests - display campaign creation form
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
        
        // Forward to campaign creation page
        request.getRequestDispatcher("/business/campaign-create.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - process campaign creation form submission
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
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String budgetStr = request.getParameter("budget");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String requirements = request.getParameter("requirements");
        String minFollowersStr = request.getParameter("minFollowers");
        String[] targetInterestsArray = request.getParameterValues("targetInterests");
        
        // Validate input
        if (title == null || title.trim().isEmpty()) {
            request.setAttribute("error", "Title is required");
            doGet(request, response);
            return;
        }
        
        if (description == null || description.trim().isEmpty()) {
            request.setAttribute("error", "Description is required");
            doGet(request, response);
            return;
        }
        
        if (budgetStr == null || budgetStr.trim().isEmpty()) {
            request.setAttribute("error", "Budget is required");
            doGet(request, response);
            return;
        }
        
        if (startDateStr == null || startDateStr.trim().isEmpty()) {
            request.setAttribute("error", "Start date is required");
            doGet(request, response);
            return;
        }
        
        if (endDateStr == null || endDateStr.trim().isEmpty()) {
            request.setAttribute("error", "End date is required");
            doGet(request, response);
            return;
        }
        
        // Parse numeric values
        double budget;
        int minFollowers = 0;
        
        try {
            budget = Double.parseDouble(budgetStr);
            if (budget <= 0) {
                request.setAttribute("error", "Budget must be a positive number");
                doGet(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Budget must be a valid number");
            doGet(request, response);
            return;
        }
        
        if (minFollowersStr != null && !minFollowersStr.trim().isEmpty()) {
            try {
                minFollowers = Integer.parseInt(minFollowersStr);
                if (minFollowers < 0) {
                    request.setAttribute("error", "Minimum followers must be a positive number");
                    doGet(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Minimum followers must be a valid number");
                doGet(request, response);
                return;
            }
        }
        
        // Parse dates
        Date startDate;
        Date endDate;
        
        try {
            startDate = Date.valueOf(startDateStr);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid start date format");
            doGet(request, response);
            return;
        }
        
        try {
            endDate = Date.valueOf(endDateStr);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid end date format");
            doGet(request, response);
            return;
        }
        
        // Validate date range
        if (endDate.before(startDate)) {
            request.setAttribute("error", "End date must be after start date");
            doGet(request, response);
            return;
        }
        
        // Create campaign object
        Campaign campaign = new Campaign();
        campaign.setBusinessId(businessProfileId);
        campaign.setTitle(title);
        campaign.setDescription(description);
        campaign.setBudget(budget);
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);
        campaign.setRequirements(requirements);
        campaign.setStatus("active");
        campaign.setMinFollowers(minFollowers);
        
        // Set target interests
        List<String> targetInterests = new ArrayList<>();
        if (targetInterestsArray != null) {
            targetInterests = Arrays.asList(targetInterestsArray);
        }
        campaign.setTargetInterests(targetInterests);
        
        // Save campaign
        int campaignId = campaignDAO.insert(campaign);
        
        if (campaignId > 0) {
            // Set success message
            request.setAttribute("success", "Campaign created successfully");
            
            // Redirect to campaigns page
            response.sendRedirect(request.getContextPath() + "/business/campaigns");
        } else {
            // Set error message
            request.setAttribute("error", "Failed to create campaign. Please try again.");
            
            // Forward back to campaign creation page
            doGet(request, response);
        }
    }
}
