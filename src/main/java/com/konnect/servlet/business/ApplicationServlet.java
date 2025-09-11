package com.konnect.servlet.business;

import com.konnect.dao.ApplicationDAO;
import com.konnect.dao.BusinessDAO;
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

/**
 * ApplicationServlet
 * Handles application management for businesses
 */
@WebServlet("/business/application")
public class ApplicationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ApplicationDAO applicationDAO;
    private CampaignDAO campaignDAO;
    private BusinessDAO businessDAO;
    private CreatorDAO creatorDAO;

    @Override
    public void init() {
        applicationDAO = new ApplicationDAO();
        campaignDAO = new CampaignDAO();
        businessDAO = new BusinessDAO();
        creatorDAO = new CreatorDAO();
    }

    /**
     * Handle GET requests - process application actions
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

        // Get parameters
        String action = request.getParameter("action");
        String applicationIdStr = request.getParameter("id");

        if (action == null || action.trim().isEmpty() || applicationIdStr == null || applicationIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/business/campaigns");
            return;
        }

        // Parse application ID
        int applicationId;
        try {
            applicationId = Integer.parseInt(applicationIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/business/campaigns");
            return;
        }

        // Get application
        Application application = applicationDAO.getById(applicationId);
        if (application == null) {
            request.setAttribute("error", "Application not found");
            request.getRequestDispatcher("/business/campaigns").forward(request, response);
            return;
        }

        // Get campaign
        Campaign campaign = campaignDAO.getById(application.getCampaignId());
        if (campaign == null) {
            request.setAttribute("error", "Campaign not found");
            request.getRequestDispatcher("/business/campaigns").forward(request, response);
            return;
        }

        // Check if campaign belongs to this business
        if (campaign.getBusinessId() != businessProfileId) {
            request.setAttribute("error", "You don't have permission to manage this application");
            request.getRequestDispatcher("/business/campaigns").forward(request, response);
            return;
        }

        // Process action
        boolean success = false;
        String successMessage = "";

        if ("approve".equals(action)) {
            success = applicationDAO.updateStatus(applicationId, "accepted");
            successMessage = "Application approved successfully";
        } else if ("reject".equals(action)) {
            success = applicationDAO.updateStatus(applicationId, "rejected");
            successMessage = "Application rejected successfully";
        } else if ("complete".equals(action)) {
            success = applicationDAO.updateStatus(applicationId, "completed");
            successMessage = "Application marked as completed successfully";
        } else {
            request.setAttribute("error", "Invalid action");
            request.getRequestDispatcher("/business/campaigns").forward(request, response);
            return;
        }

        if (success) {
            request.setAttribute("success", successMessage);
        } else {
            request.setAttribute("error", "Failed to update application status");
        }

        // Redirect back to campaign page
        response.sendRedirect(request.getContextPath() + "/campaign?id=" + application.getCampaignId());
    }

    /**
     * Handle POST requests - view application details
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

        // Get application ID
        String applicationIdStr = request.getParameter("applicationId");
        if (applicationIdStr == null || applicationIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/business/campaigns");
            return;
        }

        // Parse application ID
        int applicationId;
        try {
            applicationId = Integer.parseInt(applicationIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/business/campaigns");
            return;
        }

        // Get application
        Application application = applicationDAO.getById(applicationId);
        if (application == null) {
            request.setAttribute("error", "Application not found");
            request.getRequestDispatcher("/business/campaigns").forward(request, response);
            return;
        }

        // Get campaign
        Campaign campaign = campaignDAO.getById(application.getCampaignId());
        if (campaign == null) {
            request.setAttribute("error", "Campaign not found");
            request.getRequestDispatcher("/business/campaigns").forward(request, response);
            return;
        }

        // Check if campaign belongs to this business
        if (campaign.getBusinessId() != businessProfileId) {
            request.setAttribute("error", "You don't have permission to view this application");
            request.getRequestDispatcher("/business/campaigns").forward(request, response);
            return;
        }

        // Get creator
        Creator creator = getCreatorByProfileId(application.getCreatorId());

        // Set attributes for the view
        request.setAttribute("application", application);
        request.setAttribute("campaign", campaign);
        request.setAttribute("creator", creator);

        // Forward to application details page
        request.getRequestDispatcher("/business/application-details.jsp").forward(request, response);
    }

    /**
     * Get creator by profile ID
     */
    private Creator getCreatorByProfileId(int profileId) {
        String sql = "SELECT user_id FROM creator_profiles WHERE id = ?";

        java.sql.Connection conn = null;
        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;
        Creator creator = null;

        try {
            conn = com.konnect.util.DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, profileId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                creator = creatorDAO.getByUserId(userId);
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

        return creator;
    }
}
