package com.konnect.controller;

import java.io.IOException;

import com.konnect.dao.CampaignDAO;
import com.konnect.model.Campaign;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet for handling campaign operations
 */
@WebServlet("/campaign/*")
public class CampaignServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private CampaignDAO campaignDAO;
    
    public void init() {
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
            // List campaigns
            if (role.equals("business")) {
                // Business sees their own campaigns
                request.setAttribute("campaigns", campaignDAO.getCampaignsByBusinessId(userId));
                request.getRequestDispatcher("/business/campaigns.jsp").forward(request, response);
            } else if (role.equals("creator")) {
                // Creator sees all active campaigns
                request.setAttribute("campaigns", campaignDAO.getActiveCampaigns());
                request.getRequestDispatcher("/creator/campaigns.jsp").forward(request, response);
            } else if (role.equals("admin")) {
                // Admin sees all campaigns
                request.setAttribute("campaigns", campaignDAO.getAllCampaigns());
                request.getRequestDispatcher("/admin/campaigns.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        } else if (pathInfo.startsWith("/view/")) {
            // View campaign details
            try {
                int campaignId = Integer.parseInt(pathInfo.substring(6));
                Campaign campaign = campaignDAO.getCampaignById(campaignId);
                
                if (campaign != null) {
                    request.setAttribute("campaign", campaign);
                    
                    if (role.equals("business")) {
                        // Check if this campaign belongs to the business
                        if (campaign.getBusinessId() == userId) {
                            request.getRequestDispatcher("/business/campaign-details.jsp").forward(request, response);
                        } else {
                            response.sendRedirect(request.getContextPath() + "/business/campaigns.jsp");
                        }
                    } else if (role.equals("creator")) {
                        // Creator can view any active campaign
                        if (campaign.getStatus().equals("active")) {
                            request.getRequestDispatcher("/creator/campaign-details.jsp").forward(request, response);
                        } else {
                            response.sendRedirect(request.getContextPath() + "/creator/campaigns.jsp");
                        }
                    } else if (role.equals("admin")) {
                        // Admin can view any campaign
                        request.getRequestDispatcher("/admin/campaign-details.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/login.jsp");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/" + role + "/campaigns.jsp");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/" + role + "/campaigns.jsp");
            }
        } else if (pathInfo.equals("/create") && role.equals("business")) {
            // Show campaign creation form
            request.getRequestDispatcher("/business/create-campaign.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/edit/") && role.equals("business")) {
            // Show campaign edit form
            try {
                int campaignId = Integer.parseInt(pathInfo.substring(6));
                Campaign campaign = campaignDAO.getCampaignById(campaignId);
                
                if (campaign != null && campaign.getBusinessId() == userId) {
                    request.setAttribute("campaign", campaign);
                    request.getRequestDispatcher("/business/edit-campaign.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/business/campaigns.jsp");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/business/campaigns.jsp");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/" + role + "/campaigns.jsp");
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
        
        if (pathInfo.equals("/create") && role.equals("business")) {
            // Create new campaign
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String budgetStr = request.getParameter("budget");
            String goals = request.getParameter("goals");
            
            // Validate input
            if (title == null || title.trim().isEmpty() || 
                description == null || description.trim().isEmpty() || 
                budgetStr == null || budgetStr.trim().isEmpty() || 
                goals == null || goals.trim().isEmpty()) {
                
                request.setAttribute("error", "All fields are required");
                request.getRequestDispatcher("/business/create-campaign.jsp").forward(request, response);
                return;
            }
            
            // Parse budget
            double budget;
            try {
                budget = Double.parseDouble(budgetStr);
                if (budget <= 0) {
                    request.setAttribute("error", "Budget must be greater than zero");
                    request.getRequestDispatcher("/business/create-campaign.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid budget format");
                request.getRequestDispatcher("/business/create-campaign.jsp").forward(request, response);
                return;
            }
            
            // Create campaign object
            Campaign campaign = new Campaign(userId, title, description, budget, goals);
            
            // Save campaign
            int campaignId = campaignDAO.createCampaign(campaign);
            
            if (campaignId > 0) {
                // Campaign created successfully
                request.setAttribute("success", "Campaign created successfully");
                response.sendRedirect(request.getContextPath() + "/campaign/view/" + campaignId);
            } else {
                // Campaign creation failed
                request.setAttribute("error", "Failed to create campaign");
                request.getRequestDispatcher("/business/create-campaign.jsp").forward(request, response);
            }
        } else if (pathInfo.startsWith("/edit/") && role.equals("business")) {
            // Update existing campaign
            try {
                int campaignId = Integer.parseInt(pathInfo.substring(6));
                Campaign existingCampaign = campaignDAO.getCampaignById(campaignId);
                
                if (existingCampaign != null && existingCampaign.getBusinessId() == userId) {
                    // Get form data
                    String title = request.getParameter("title");
                    String description = request.getParameter("description");
                    String budgetStr = request.getParameter("budget");
                    String goals = request.getParameter("goals");
                    String status = request.getParameter("status");
                    
                    // Validate input
                    if (title == null || title.trim().isEmpty() || 
                        description == null || description.trim().isEmpty() || 
                        budgetStr == null || budgetStr.trim().isEmpty() || 
                        goals == null || goals.trim().isEmpty() || 
                        status == null || status.trim().isEmpty()) {
                        
                        request.setAttribute("error", "All fields are required");
                        request.setAttribute("campaign", existingCampaign);
                        request.getRequestDispatcher("/business/edit-campaign.jsp").forward(request, response);
                        return;
                    }
                    
                    // Parse budget
                    double budget;
                    try {
                        budget = Double.parseDouble(budgetStr);
                        if (budget <= 0) {
                            request.setAttribute("error", "Budget must be greater than zero");
                            request.setAttribute("campaign", existingCampaign);
                            request.getRequestDispatcher("/business/edit-campaign.jsp").forward(request, response);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("error", "Invalid budget format");
                        request.setAttribute("campaign", existingCampaign);
                        request.getRequestDispatcher("/business/edit-campaign.jsp").forward(request, response);
                        return;
                    }
                    
                    // Validate status
                    if (!status.equals("active") && !status.equals("completed") && !status.equals("cancelled")) {
                        request.setAttribute("error", "Invalid status");
                        request.setAttribute("campaign", existingCampaign);
                        request.getRequestDispatcher("/business/edit-campaign.jsp").forward(request, response);
                        return;
                    }
                    
                    // Update campaign object
                    existingCampaign.setTitle(title);
                    existingCampaign.setDescription(description);
                    existingCampaign.setBudget(budget);
                    existingCampaign.setGoals(goals);
                    existingCampaign.setStatus(status);
                    
                    // Save updated campaign
                    boolean updated = campaignDAO.updateCampaign(existingCampaign);
                    
                    if (updated) {
                        // Campaign updated successfully
                        request.setAttribute("success", "Campaign updated successfully");
                        response.sendRedirect(request.getContextPath() + "/campaign/view/" + campaignId);
                    } else {
                        // Campaign update failed
                        request.setAttribute("error", "Failed to update campaign");
                        request.setAttribute("campaign", existingCampaign);
                        request.getRequestDispatcher("/business/edit-campaign.jsp").forward(request, response);
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/business/campaigns.jsp");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/business/campaigns.jsp");
            }
        } else if (pathInfo.startsWith("/delete/") && role.equals("business")) {
            // Delete campaign
            try {
                int campaignId = Integer.parseInt(pathInfo.substring(8));
                
                boolean deleted = campaignDAO.deleteCampaign(campaignId, userId);
                
                if (deleted) {
                    // Campaign deleted successfully
                    request.setAttribute("success", "Campaign deleted successfully");
                } else {
                    // Campaign deletion failed
                    request.setAttribute("error", "Failed to delete campaign");
                }
                
                response.sendRedirect(request.getContextPath() + "/campaign");
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/business/campaigns.jsp");
            }
        } else if (pathInfo.startsWith("/admin-delete/") && role.equals("admin")) {
            // Admin delete campaign
            try {
                int campaignId = Integer.parseInt(pathInfo.substring(13));
                
                boolean deleted = campaignDAO.adminDeleteCampaign(campaignId);
                
                if (deleted) {
                    // Campaign deleted successfully
                    request.setAttribute("success", "Campaign deleted successfully");
                } else {
                    // Campaign deletion failed
                    request.setAttribute("error", "Failed to delete campaign");
                }
                
                response.sendRedirect(request.getContextPath() + "/campaign");
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/campaigns.jsp");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/" + role + "/campaigns.jsp");
        }
    }
}
