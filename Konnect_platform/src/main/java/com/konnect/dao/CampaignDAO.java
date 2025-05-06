package com.konnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.konnect.model.Campaign;
import com.konnect.util.DBConnection;

/**
 * Data Access Object for Campaign-related database operations
 */
public class CampaignDAO {

    /**
     * Create a new campaign
     * @param campaign Campaign object with campaign details
     * @return Campaign ID if successful, -1 if failed
     */
    public int createCampaign(Campaign campaign) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int campaignId = -1;

        try {
            conn = DBConnection.getConnection();

            String sql = "INSERT INTO campaigns (business_id, title, description, budget, goals) VALUES (?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, campaign.getBusinessId());
            pstmt.setString(2, campaign.getTitle());
            pstmt.setString(3, campaign.getDescription());
            pstmt.setDouble(4, campaign.getBudget());
            pstmt.setString(5, campaign.getGoals());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    campaignId = rs.getInt(1);
                    campaign.setCampaignId(campaignId);
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

        return campaignId;
    }

    /**
     * Get campaign by ID
     * @param campaignId Campaign ID
     * @return Campaign object if found, null otherwise
     */
    public Campaign getCampaignById(int campaignId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Campaign campaign = null;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT c.*, u.username FROM campaigns c " +
                         "JOIN users u ON c.business_id = u.user_id " +
                         "WHERE c.campaign_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, campaignId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                campaign = new Campaign();
                campaign.setCampaignId(rs.getInt("campaign_id"));
                campaign.setBusinessId(rs.getInt("business_id"));
                campaign.setBusinessUsername(rs.getString("username")); // Set business username
                campaign.setTitle(rs.getString("title"));
                campaign.setDescription(rs.getString("description"));
                campaign.setBudget(rs.getDouble("budget"));
                campaign.setGoals(rs.getString("goals"));
                campaign.setStatus(rs.getString("status"));
                campaign.setCreatedAt(rs.getTimestamp("created_at"));
                campaign.setUpdatedAt(rs.getTimestamp("updated_at"));
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

        return campaign;
    }

    /**
     * Get all campaigns
     * @return List of all campaigns
     */
    public List<Campaign> getAllCampaigns() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Campaign> campaigns = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT c.*, u.username FROM campaigns c " +
                         "JOIN users u ON c.business_id = u.user_id " +
                         "ORDER BY c.created_at DESC";

            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Campaign campaign = new Campaign();
                campaign.setCampaignId(rs.getInt("campaign_id"));
                campaign.setBusinessId(rs.getInt("business_id"));
                campaign.setBusinessUsername(rs.getString("username")); // Set business username
                campaign.setTitle(rs.getString("title"));
                campaign.setDescription(rs.getString("description"));
                campaign.setBudget(rs.getDouble("budget"));
                campaign.setGoals(rs.getString("goals"));
                campaign.setStatus(rs.getString("status"));
                campaign.setCreatedAt(rs.getTimestamp("created_at"));
                campaign.setUpdatedAt(rs.getTimestamp("updated_at"));

                campaigns.add(campaign);
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

        return campaigns;
    }

    /**
     * Get campaigns by business ID
     * @param businessId Business ID
     * @return List of campaigns for the business
     */
    public List<Campaign> getCampaignsByBusinessId(int businessId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Campaign> campaigns = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT c.*, u.username FROM campaigns c " +
                         "JOIN users u ON c.business_id = u.user_id " +
                         "WHERE c.business_id = ? ORDER BY c.created_at DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, businessId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Campaign campaign = new Campaign();
                campaign.setCampaignId(rs.getInt("campaign_id"));
                campaign.setBusinessId(rs.getInt("business_id"));
                campaign.setBusinessUsername(rs.getString("username")); // Set business username
                campaign.setTitle(rs.getString("title"));
                campaign.setDescription(rs.getString("description"));
                campaign.setBudget(rs.getDouble("budget"));
                campaign.setGoals(rs.getString("goals"));
                campaign.setStatus(rs.getString("status"));
                campaign.setCreatedAt(rs.getTimestamp("created_at"));
                campaign.setUpdatedAt(rs.getTimestamp("updated_at"));

                campaigns.add(campaign);
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

        return campaigns;
    }

    /**
     * Get active campaigns
     * @return List of active campaigns
     */
    public List<Campaign> getActiveCampaigns() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Campaign> campaigns = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT c.*, u.username FROM campaigns c " +
                         "JOIN users u ON c.business_id = u.user_id " +
                         "WHERE c.status = 'active' " +
                         "ORDER BY c.created_at DESC";

            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Campaign campaign = new Campaign();
                campaign.setCampaignId(rs.getInt("campaign_id"));
                campaign.setBusinessId(rs.getInt("business_id"));
                campaign.setBusinessUsername(rs.getString("username")); // Set business username
                campaign.setTitle(rs.getString("title"));
                campaign.setDescription(rs.getString("description"));
                campaign.setBudget(rs.getDouble("budget"));
                campaign.setGoals(rs.getString("goals"));
                campaign.setStatus(rs.getString("status"));
                campaign.setCreatedAt(rs.getTimestamp("created_at"));
                campaign.setUpdatedAt(rs.getTimestamp("updated_at"));

                campaigns.add(campaign);
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

        return campaigns;
    }

    /**
     * Update campaign
     * @param campaign Campaign object with updated details
     * @return true if successful, false otherwise
     */
    public boolean updateCampaign(Campaign campaign) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsUpdated = 0;

        try {
            conn = DBConnection.getConnection();

            String sql = "UPDATE campaigns SET title = ?, description = ?, budget = ?, goals = ?, status = ? " +
                         "WHERE campaign_id = ? AND business_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, campaign.getTitle());
            pstmt.setString(2, campaign.getDescription());
            pstmt.setDouble(3, campaign.getBudget());
            pstmt.setString(4, campaign.getGoals());
            pstmt.setString(5, campaign.getStatus());
            pstmt.setInt(6, campaign.getCampaignId());
            pstmt.setInt(7, campaign.getBusinessId());

            rowsUpdated = pstmt.executeUpdate();

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
     * Delete campaign
     * @param campaignId Campaign ID
     * @param businessId Business ID (for security check)
     * @return true if successful, false otherwise
     */
    public boolean deleteCampaign(int campaignId, int businessId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsDeleted = 0;

        try {
            conn = DBConnection.getConnection();

            String sql = "DELETE FROM campaigns WHERE campaign_id = ? AND business_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, campaignId);
            pstmt.setInt(2, businessId);

            rowsDeleted = pstmt.executeUpdate();

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

        return rowsDeleted > 0;
    }

    /**
     * Admin delete campaign
     * @param campaignId Campaign ID
     * @return true if successful, false otherwise
     */
    public boolean adminDeleteCampaign(int campaignId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsDeleted = 0;

        try {
            conn = DBConnection.getConnection();

            String sql = "DELETE FROM campaigns WHERE campaign_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, campaignId);

            rowsDeleted = pstmt.executeUpdate();

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

        return rowsDeleted > 0;
    }

    /**
     * Count total campaigns
     * @return Total number of campaigns
     */
    public int countTotalCampaigns() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT COUNT(*) FROM campaigns";

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
     * Count active campaigns
     * @return Number of active campaigns
     */
    public int countActiveCampaigns() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT COUNT(*) FROM campaigns WHERE status = 'active'";

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
     * Count campaigns by business
     * @param businessId Business ID
     * @return Number of campaigns for the business
     */
    public int countCampaignsByBusiness(int businessId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT COUNT(*) FROM campaigns WHERE business_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, businessId);

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
