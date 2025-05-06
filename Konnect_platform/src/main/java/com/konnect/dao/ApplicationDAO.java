package com.konnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.konnect.model.Application;
import com.konnect.util.DBConnection;

/**
 * Data Access Object for Application-related database operations
 */
public class ApplicationDAO {

    /**
     * Create a new application
     * @param application Application object with application details
     * @return Application ID if successful, -1 if failed
     */
    public int createApplication(Application application) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int applicationId = -1;

        try {
            conn = DBConnection.getConnection();

            // Check if creator has already applied to this campaign
            if (hasCreatorApplied(application.getCampaignId(), application.getCreatorId())) {
                return -1; // Already applied
            }

            String sql = "INSERT INTO applications (campaign_id, creator_id, message) VALUES (?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, application.getCampaignId());
            pstmt.setInt(2, application.getCreatorId());
            pstmt.setString(3, application.getMessage());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    applicationId = rs.getInt(1);
                    application.setApplicationId(applicationId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return applicationId;
    }

    /**
     * Check if creator has already applied to a campaign
     * @param campaignId Campaign ID
     * @param creatorId Creator ID
     * @return true if already applied, false otherwise
     */
    public boolean hasCreatorApplied(int campaignId, int creatorId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean hasApplied = false;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT COUNT(*) FROM applications WHERE campaign_id = ? AND creator_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, campaignId);
            pstmt.setInt(2, creatorId);

            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                hasApplied = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return hasApplied;
    }

    /**
     * Get application by ID
     * @param applicationId Application ID
     * @return Application object if found, null otherwise
     */
    public Application getApplicationById(int applicationId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Application application = null;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT a.*, c.title as campaign_title, c.business_id, u.username as creator_username, " +
                         "(SELECT username FROM users WHERE user_id = c.business_id) as business_username " +
                         "FROM applications a " +
                         "JOIN campaigns c ON a.campaign_id = c.campaign_id " +
                         "JOIN users u ON a.creator_id = u.user_id " +
                         "WHERE a.application_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, applicationId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                application = new Application();
                application.setApplicationId(rs.getInt("application_id"));
                application.setCampaignId(rs.getInt("campaign_id"));
                application.setCreatorId(rs.getInt("creator_id"));
                application.setMessage(rs.getString("message"));
                application.setStatus(rs.getString("status"));
                application.setCreatedAt(rs.getTimestamp("created_at"));
                application.setUpdatedAt(rs.getTimestamp("updated_at"));
                application.setCampaignTitle(rs.getString("campaign_title"));
                application.setBusinessId(rs.getInt("business_id"));
                application.setBusinessUsername(rs.getString("business_username"));
                application.setCreatorUsername(rs.getString("creator_username"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return application;
    }

    /**
     * Get applications by campaign ID
     * @param campaignId Campaign ID
     * @return List of applications for the campaign
     */
    public List<Application> getApplicationsByCampaignId(int campaignId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Application> applications = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT a.*, u.username as creator_username " +
                         "FROM applications a " +
                         "JOIN users u ON a.creator_id = u.user_id " +
                         "WHERE a.campaign_id = ? " +
                         "ORDER BY a.created_at DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, campaignId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Application application = new Application();
                application.setApplicationId(rs.getInt("application_id"));
                application.setCampaignId(rs.getInt("campaign_id"));
                application.setCreatorId(rs.getInt("creator_id"));
                application.setMessage(rs.getString("message"));
                application.setStatus(rs.getString("status"));
                application.setCreatedAt(rs.getTimestamp("created_at"));
                application.setUpdatedAt(rs.getTimestamp("updated_at"));
                application.setCreatorUsername(rs.getString("creator_username"));

                applications.add(application);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return applications;
    }

    /**
     * Get applications by creator ID
     * @param creatorId Creator ID
     * @return List of applications by the creator
     */
    public List<Application> getApplicationsByCreatorId(int creatorId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Application> applications = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT a.*, c.title as campaign_title, c.business_id, u.username as business_username " +
                         "FROM applications a " +
                         "JOIN campaigns c ON a.campaign_id = c.campaign_id " +
                         "JOIN users u ON c.business_id = u.user_id " +
                         "WHERE a.creator_id = ? " +
                         "ORDER BY a.created_at DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, creatorId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Application application = new Application();
                application.setApplicationId(rs.getInt("application_id"));
                application.setCampaignId(rs.getInt("campaign_id"));
                application.setCreatorId(rs.getInt("creator_id"));
                application.setMessage(rs.getString("message"));
                application.setStatus(rs.getString("status"));
                application.setCreatedAt(rs.getTimestamp("created_at"));
                application.setUpdatedAt(rs.getTimestamp("updated_at"));
                application.setCampaignTitle(rs.getString("campaign_title"));
                application.setBusinessId(rs.getInt("business_id"));
                application.setBusinessUsername(rs.getString("business_username"));

                applications.add(application);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return applications;
    }

    /**
     * Get applications by business ID
     * @param businessId Business ID
     * @return List of applications for the business's campaigns
     */
    public List<Application> getApplicationsByBusinessId(int businessId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Application> applications = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT a.*, c.title as campaign_title, u.username as creator_username " +
                         "FROM applications a " +
                         "JOIN campaigns c ON a.campaign_id = c.campaign_id " +
                         "JOIN users u ON a.creator_id = u.user_id " +
                         "WHERE c.business_id = ? " +
                         "ORDER BY a.created_at DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, businessId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Application application = new Application();
                application.setApplicationId(rs.getInt("application_id"));
                application.setCampaignId(rs.getInt("campaign_id"));
                application.setCreatorId(rs.getInt("creator_id"));
                application.setMessage(rs.getString("message"));
                application.setStatus(rs.getString("status"));
                application.setCreatedAt(rs.getTimestamp("created_at"));
                application.setUpdatedAt(rs.getTimestamp("updated_at"));
                application.setCampaignTitle(rs.getString("campaign_title"));
                application.setCreatorUsername(rs.getString("creator_username"));

                applications.add(application);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return applications;
    }

    /**
     * Update application status
     * @param applicationId Application ID
     * @param status New status (approved/rejected)
     * @param businessId Business ID (for security check)
     * @return true if successful, false otherwise
     */
    public boolean updateApplicationStatus(int applicationId, String status, int businessId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsUpdated = 0;

        try {
            conn = DBConnection.getConnection();

            // First, check if the application belongs to a campaign owned by this business
            String checkSql = "SELECT COUNT(*) FROM applications a " +
                             "JOIN campaigns c ON a.campaign_id = c.campaign_id " +
                             "WHERE a.application_id = ? AND c.business_id = ?";

            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, applicationId);
            checkStmt.setInt(2, businessId);

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Application belongs to this business, proceed with update
                String sql = "UPDATE applications SET status = ? WHERE application_id = ?";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, status);
                pstmt.setInt(2, applicationId);

                rowsUpdated = pstmt.executeUpdate();
            }

            rs.close();
            checkStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rowsUpdated > 0;
    }

    /**
     * Count total applications
     * @return Total number of applications
     */
    public int countTotalApplications() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT COUNT(*) FROM applications";

            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return count;
    }

    /**
     * Count applications by status
     * @param status Application status
     * @return Number of applications with the given status
     */
    public int countApplicationsByStatus(String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT COUNT(*) FROM applications WHERE status = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return count;
    }
}
