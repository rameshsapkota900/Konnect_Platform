package com.konnect.dao;

import com.konnect.config.DatabaseConnection;
import com.konnect.model.Campaign;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CampaignDao {

    public boolean createCampaign(Campaign campaign) {
        String sql = "INSERT INTO campaigns (business_user_id, title, description, requirements, budget, product_image_path, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, campaign.getBusinessUserId());
            stmt.setString(2, campaign.getTitle());
            stmt.setString(3, campaign.getDescription());
            stmt.setString(4, campaign.getRequirements());
            stmt.setBigDecimal(5, campaign.getBudget());
            stmt.setString(6, campaign.getProductImagePath());
            stmt.setString(7, campaign.getStatus() != null ? campaign.getStatus() : "active"); // Default status

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error creating campaign: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCampaign(Campaign campaign) {
         String sql = "UPDATE campaigns SET title = ?, description = ?, requirements = ?, budget = ?, product_image_path = ?, status = ? WHERE campaign_id = ? AND business_user_id = ?"; // Ensure owner matches
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, campaign.getTitle());
            stmt.setString(2, campaign.getDescription());
            stmt.setString(3, campaign.getRequirements());
            stmt.setBigDecimal(4, campaign.getBudget());
            stmt.setString(5, campaign.getProductImagePath());
            stmt.setString(6, campaign.getStatus());
            stmt.setInt(7, campaign.getCampaignId());
            stmt.setInt(8, campaign.getBusinessUserId()); // Security check

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating campaign: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCampaign(int campaignId, int businessUserId) {
        // Also delete related applications? Or handle via DB constraints (ON DELETE CASCADE)
        // Let's assume ON DELETE CASCADE handles applications.
        String sql = "DELETE FROM campaigns WHERE campaign_id = ? AND business_user_id = ?"; // Ensure owner matches
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, campaignId);
            stmt.setInt(2, businessUserId); // Security check

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting campaign: " + e.getMessage());
            return false;
        }
    }

    public Campaign findById(int campaignId) {
        String sql = "SELECT c.*, u.username as businessUsername FROM campaigns c JOIN users u ON c.business_user_id = u.user_id WHERE c.campaign_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, campaignId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCampaign(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding campaign by ID: " + e.getMessage());
        }
        return null;
    }

     public List<Campaign> findByBusinessId(int businessUserId) {
        List<Campaign> campaigns = new ArrayList<>();
        String sql = "SELECT c.*, u.username as businessUsername FROM campaigns c JOIN users u ON c.business_user_id = u.user_id WHERE c.business_user_id = ? ORDER BY c.created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, businessUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                campaigns.add(mapResultSetToCampaign(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding campaigns by business ID: " + e.getMessage());
        }
        return campaigns;
    }

    // Get active campaigns for creators to view/apply
     public List<Campaign> getActiveCampaigns(Integer excludeCreatorId) { // excludeCreatorId to filter out campaigns creator already applied to/invited for
        List<Campaign> campaigns = new ArrayList<>();
         StringBuilder sqlBuilder = new StringBuilder(
             "SELECT c.*, u.username as businessUsername FROM campaigns c " +
             "JOIN users u ON c.business_user_id = u.user_id " +
             "WHERE c.status = 'active' "
         );

         if (excludeCreatorId != null && excludeCreatorId > 0) {
             sqlBuilder.append("AND c.campaign_id NOT IN (SELECT campaign_id FROM applications WHERE creator_user_id = ?) ");
         }

         sqlBuilder.append("ORDER BY c.created_at DESC");


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

             if (excludeCreatorId != null && excludeCreatorId > 0) {
                stmt.setInt(1, excludeCreatorId);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                campaigns.add(mapResultSetToCampaign(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching active campaigns: " + e.getMessage());
        }
        return campaigns;
    }

     // For Admin view
    public List<Campaign> getAllCampaigns() {
        List<Campaign> campaigns = new ArrayList<>();
        String sql = "SELECT c.*, u.username as businessUsername FROM campaigns c JOIN users u ON c.business_user_id = u.user_id ORDER BY c.created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                campaigns.add(mapResultSetToCampaign(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all campaigns: " + e.getMessage());
        }
        return campaigns;
    }

     public String getProductImagePath(int campaignId) {
        String sql = "SELECT product_image_path FROM campaigns WHERE campaign_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, campaignId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("product_image_path");
            }
        } catch (SQLException e) {
            System.err.println("Error getting product image path: " + e.getMessage());
        }
        return null;
    }

     public long countCampaigns() {
        String sql = "SELECT COUNT(*) FROM campaigns";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting campaigns: " + e.getMessage());
        }
        return 0;
    }

    private Campaign mapResultSetToCampaign(ResultSet rs) throws SQLException {
        Campaign campaign = new Campaign();
        campaign.setCampaignId(rs.getInt("campaign_id"));
        campaign.setBusinessUserId(rs.getInt("business_user_id"));
        campaign.setTitle(rs.getString("title"));
        campaign.setDescription(rs.getString("description"));
        campaign.setRequirements(rs.getString("requirements"));
        campaign.setBudget(rs.getBigDecimal("budget"));
        campaign.setProductImagePath(rs.getString("product_image_path"));
        campaign.setStatus(rs.getString("status"));
        campaign.setCreatedAt(rs.getTimestamp("created_at"));

        // Map joined fields if present
         if (hasColumn(rs, "businessUsername")) {
            campaign.setBusinessUsername(rs.getString("businessUsername"));
        }

        return campaign;
    }

    // Helper to check if a column exists
    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equalsIgnoreCase(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }
}
