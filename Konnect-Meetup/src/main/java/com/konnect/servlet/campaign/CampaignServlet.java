package com.konnect.servlet.campaign;

import com.konnect.dao.ApplicationDAO;
import com.konnect.dao.BusinessDAO;
import com.konnect.dao.CampaignDAO;
import com.konnect.dao.CreatorDAO;
import com.konnect.model.Application;
import com.konnect.model.Business;
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
import java.util.List;

/**
 * CampaignServlet
 * Handles campaign details view
 */
@WebServlet("/campaign")
public class CampaignServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CampaignDAO campaignDAO;
    private BusinessDAO businessDAO;
    private ApplicationDAO applicationDAO;
    private CreatorDAO creatorDAO;

    @Override
    public void init() {
        campaignDAO = new CampaignDAO();
        businessDAO = new BusinessDAO();
        applicationDAO = new ApplicationDAO();
        creatorDAO = new CreatorDAO();
    }

    /**
     * Handle GET requests - display campaign details
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Get campaign ID from request parameter
        String campaignIdStr = request.getParameter("id");
        if (campaignIdStr == null || campaignIdStr.trim().isEmpty()) {
            // Redirect based on user role
            redirectBasedOnRole(user, response, request);
            return;
        }

        // Parse campaign ID
        int campaignId;
        try {
            campaignId = Integer.parseInt(campaignIdStr);
        } catch (NumberFormatException e) {
            // Invalid campaign ID, redirect based on user role
            redirectBasedOnRole(user, response, request);
            return;
        }

        // Get campaign details
        Campaign campaign = campaignDAO.getById(campaignId);
        if (campaign == null) {
            // Campaign not found, redirect based on user role
            redirectBasedOnRole(user, response, request);
            return;
        }

        // Get business details
        Business business = null;
        List<Business> businesses = businessDAO.getAllByRole("business");
        for (Business b : businesses) {
            int businessProfileId = businessDAO.getBusinessProfileId(b.getId());
            if (businessProfileId == campaign.getBusinessId()) {
                business = b;
                break;
            }
        }

        // Set attributes for the view
        request.setAttribute("campaign", campaign);
        request.setAttribute("business", business);

        // If user is a creator, check if they have already applied
        if ("creator".equals(user.getRole())) {
            Creator creator = creatorDAO.getByUserId(user.getId());
            if (creator != null) {
                int creatorProfileId = getCreatorProfileId(creator.getId());
                boolean hasApplied = applicationDAO.hasApplied(campaignId, creatorProfileId);
                request.setAttribute("hasApplied", hasApplied);

                // Check if creator meets the minimum follower requirement
                boolean meetsRequirement = creator.getFollowerCount() >= campaign.getMinFollowers();
                request.setAttribute("meetsRequirement", meetsRequirement);
            }
        }
        // If user is the business owner of this campaign, get applications
        else if ("business".equals(user.getRole())) {
            int businessProfileId = businessDAO.getBusinessProfileId(user.getId());
            if (businessProfileId == campaign.getBusinessId()) {
                List<Application> applications = applicationDAO.getByCampaignId(campaignId);
                request.setAttribute("applications", applications);

                // Get creator details for each application
                for (Application app : applications) {
                    Creator creator = getCreatorByProfileId(app.getCreatorId());
                    if (creator != null) {
                        request.setAttribute("creator_" + app.getId(), creator);
                    }
                }
            }
        }

        // Forward to campaign details page
        request.getRequestDispatcher("/campaign.jsp").forward(request, response);
    }

    /**
     * Redirect user based on their role
     */
    private void redirectBasedOnRole(User user, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String role = user.getRole();
        String contextPath = request.getContextPath();

        switch (role) {
            case "admin":
                response.sendRedirect(contextPath + "/admin/campaigns");
                break;
            case "creator":
                response.sendRedirect(contextPath + "/creator/campaigns");
                break;
            case "business":
                response.sendRedirect(contextPath + "/business/campaigns");
                break;
            default:
                response.sendRedirect(contextPath + "/index.jsp");
                break;
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

    /**
     * Get creator by profile ID
     */
    private Creator getCreatorByProfileId(int profileId) {
        String sql = "SELECT u.* FROM users u JOIN creator_profiles c ON u.id = c.user_id WHERE c.id = ?";

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
                int userId = rs.getInt("id");
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
