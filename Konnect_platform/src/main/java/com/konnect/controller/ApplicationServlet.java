package com.konnect.controller;

import java.io.IOException;

import com.konnect.dao.ApplicationDAO;
import com.konnect.dao.CampaignDAO;
import com.konnect.model.Application;
import com.konnect.model.Campaign;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet for handling campaign application operations
 */
@WebServlet("/application/*")
public class ApplicationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private ApplicationDAO applicationDAO;
    private CampaignDAO campaignDAO;
    
    public void init() {
        applicationDAO = new ApplicationDAO();
        campaignDAO = new CampaignDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Get user role and ID
        String role = (String) session.getAttribute("role");
        int userId = (int) session.getAttribute("userId");
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // List applications
            if (role.equals("creator")) {
                // Creator sees their own applications
                request.setAttribute("applications", applicationDAO.getApplicationsByCreatorId(userId));
                request.getRequestDispatcher("/creator/applications.jsp").forward(request, response);
            } else if (role.equals("business")) {
                // Business sees applications for their campaigns
                request.setAttribute("applications", applicationDAO.getApplicationsByBusinessId(userId));
                request.getRequestDispatcher("/business/applications.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else if (pathInfo.startsWith("/view/")) {
            // View application details
            try {
                int applicationId = Integer.parseInt(pathInfo.substring(6));
                Application application = applicationDAO.getApplicationById(applicationId);
                
                if (application != null) {
                    // Get the campaign
                    Campaign campaign = campaignDAO.getCampaignById(application.getCampaignId());
                    
                    if (campaign != null) {
                        request.setAttribute("application", application);
                        request.setAttribute("campaign", campaign);
                        
                        if (role.equals("creator") && application.getCreatorId() == userId) {
                            // Creator viewing their own application
                            request.getRequestDispatcher("/creator/application-details.jsp").forward(request, response);
                        } else if (role.equals("business") && campaign.getBusinessId() == userId) {
                            // Business viewing application for their campaign
                            request.getRequestDispatcher("/business/application-details.jsp").forward(request, response);
                        } else {
                            response.sendRedirect(request.getContextPath() + "/application");
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + "/application");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/application");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/application");
            }
        } else if (pathInfo.startsWith("/apply/") && role.equals("creator")) {
            // Show application form
            try {
                int campaignId = Integer.parseInt(pathInfo.substring(7));
                Campaign campaign = campaignDAO.getCampaignById(campaignId);
                
                if (campaign != null && campaign.getStatus().equals("active")) {
                    // Check if creator has already applied
                    if (applicationDAO.hasCreatorApplied(campaignId, userId)) {
                        request.setAttribute("error", "You have already applied to this campaign");
                        response.sendRedirect(request.getContextPath() + "/campaign/view/" + campaignId);
                        return;
                    }
                    
                    request.setAttribute("campaign", campaign);
                    request.getRequestDispatcher("/creator/apply.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/campaign");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/campaign");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/application");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Get user role and ID
        String role = (String) session.getAttribute("role");
        int userId = (int) session.getAttribute("userId");
        
        if (pathInfo.startsWith("/apply/") && role.equals("creator")) {
            // Submit application
            try {
                int campaignId = Integer.parseInt(pathInfo.substring(7));
                Campaign campaign = campaignDAO.getCampaignById(campaignId);
                
                if (campaign != null && campaign.getStatus().equals("active")) {
                    // Check if creator has already applied
                    if (applicationDAO.hasCreatorApplied(campaignId, userId)) {
                        request.setAttribute("error", "You have already applied to this campaign");
                        response.sendRedirect(request.getContextPath() + "/campaign/view/" + campaignId);
                        return;
                    }
                    
                    // Get form data
                    String message = request.getParameter("message");
                    
                    // Validate input
                    if (message == null || message.trim().isEmpty()) {
                        request.setAttribute("error", "Message is required");
                        request.setAttribute("campaign", campaign);
                        request.getRequestDispatcher("/creator/apply.jsp").forward(request, response);
                        return;
                    }
                    
                    // Create application object
                    Application application = new Application(campaignId, userId, message);
                    
                    // Save application
                    int applicationId = applicationDAO.createApplication(application);
                    
                    if (applicationId > 0) {
                        // Application submitted successfully
                        request.setAttribute("success", "Application submitted successfully");
                        response.sendRedirect(request.getContextPath() + "/application/view/" + applicationId);
                    } else {
                        // Application submission failed
                        request.setAttribute("error", "Failed to submit application");
                        request.setAttribute("campaign", campaign);
                        request.getRequestDispatcher("/creator/apply.jsp").forward(request, response);
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/campaign");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/campaign");
            }
        } else if (pathInfo.startsWith("/update/") && role.equals("business")) {
            // Update application status
            try {
                int applicationId = Integer.parseInt(pathInfo.substring(8));
                String status = request.getParameter("status");
                
                // Validate status
                if (status == null || (!status.equals("approved") && !status.equals("rejected"))) {
                    response.sendRedirect(request.getContextPath() + "/application/view/" + applicationId);
                    return;
                }
                
                // Update application status
                boolean updated = applicationDAO.updateApplicationStatus(applicationId, status, userId);
                
                if (updated) {
                    // Status updated successfully
                    request.setAttribute("success", "Application " + status + " successfully");
                } else {
                    // Status update failed
                    request.setAttribute("error", "Failed to update application status");
                }
                
                response.sendRedirect(request.getContextPath() + "/application/view/" + applicationId);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/application");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/application");
        }
    }
}
