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

/**
 * ApplicationServlet
 * Handles creator applications to campaigns
 */
@WebServlet("/creator/apply")
public class ApplicationServlet extends HttpServlet {
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
     * Handle GET requests - display application form
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

        // Get campaign ID from request parameter
        String campaignIdStr = request.getParameter("campaignId");
        if (campaignIdStr == null || campaignIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/creator/campaigns");
            return;
        }

        // Parse campaign ID
        int campaignId;
        try {
            campaignId = Integer.parseInt(campaignIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/creator/campaigns");
            return;
        }

        // Get campaign details
        Campaign campaign = campaignDAO.getById(campaignId);
        if (campaign == null) {
            response.sendRedirect(request.getContextPath() + "/creator/campaigns");
            return;
        }

        // Check if campaign is active
        if (!"active".equals(campaign.getStatus())) {
            request.setAttribute("error", "This campaign is no longer active");
            request.getRequestDispatcher("/creator/campaigns").forward(request, response);
            return;
        }

        // Get creator profile
        Creator creator = creatorDAO.getByUserId(user.getId());
        if (creator == null) {
            response.sendRedirect(request.getContextPath() + "/creator/profile");
            return;
        }

        // Check if creator meets the minimum follower requirement
        if (creator.getFollowerCount() < campaign.getMinFollowers()) {
            request.setAttribute("error", "You don't meet the minimum follower requirement for this campaign");
            request.getRequestDispatcher("/creator/campaigns").forward(request, response);
            return;
        }

        // Check if creator has already applied
        int creatorProfileId = getCreatorProfileId(creator.getId());
        if (applicationDAO.hasApplied(campaignId, creatorProfileId)) {
            request.setAttribute("error", "You have already applied to this campaign");
            request.getRequestDispatcher("/creator/campaigns").forward(request, response);
            return;
        }

        // Set attributes for the view
        request.setAttribute("campaign", campaign);
        request.setAttribute("creator", creator);

        // Forward to application form
        request.getRequestDispatcher("/creator/apply.jsp").forward(request, response);
    }

    /**
     * Handle POST requests - process application form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

        // Get form data
        String campaignIdStr = request.getParameter("campaignId");
        String message = request.getParameter("message");

        // Validate input
        if (campaignIdStr == null || campaignIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/creator/campaigns");
            return;
        }

        if (message == null || message.trim().isEmpty()) {
            request.setAttribute("error", "Message is required");
            doGet(request, response);
            return;
        }

        // Parse campaign ID
        int campaignId;
        try {
            campaignId = Integer.parseInt(campaignIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/creator/campaigns");
            return;
        }

        // Get campaign details
        Campaign campaign = campaignDAO.getById(campaignId);
        if (campaign == null) {
            response.sendRedirect(request.getContextPath() + "/creator/campaigns");
            return;
        }

        // Check if campaign is active
        if (!"active".equals(campaign.getStatus())) {
            request.setAttribute("error", "This campaign is no longer active");
            request.getRequestDispatcher("/creator/campaigns").forward(request, response);
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

        // Check if creator has already applied
        if (applicationDAO.hasApplied(campaignId, creatorProfileId)) {
            request.setAttribute("error", "You have already applied to this campaign");
            request.getRequestDispatcher("/creator/campaigns").forward(request, response);
            return;
        }

        // Create application object
        Application application = new Application();
        application.setCampaignId(campaignId);
        application.setCreatorId(creatorProfileId);
        application.setMessage(message);
        application.setStatus("pending");

        // Save application
        int applicationId = applicationDAO.insert(application);

        if (applicationId > 0) {
            // Set success message
            request.setAttribute("success", "Application submitted successfully");

            // Redirect to applications page
            response.sendRedirect(request.getContextPath() + "/creator/applications");
        } else {
            // Set error message
            request.setAttribute("error", "Failed to submit application. Please try again.");

            // Forward back to application form
            doGet(request, response);
        }
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
