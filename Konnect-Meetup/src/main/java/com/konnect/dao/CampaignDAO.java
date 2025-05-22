package com.konnect.dao;

import com.konnect.model.Campaign;
import com.konnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CampaignDAO Class
 * Handles database operations for Campaign objects
 */
public class CampaignDAO {
    
    /**
     * Insert a new campaign into the database
     * @param campaign Campaign object to insert
     * @return generated campaign ID if successful, -1 otherwise
     */
    public int insert(Campaign campaign) {
        String sql = "INSERT INTO campaigns (business_id, title, description, budget, " +
                     "start_date, end_date, requirements, status, min_followers) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int campaignId = -1;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, campaign.getBusinessId());
            pstmt.setString(2, campaign.getTitle());
            pstmt.setString(3, campaign.getDescription());
            pstmt.setDouble(4, campaign.getBudget());
            pstmt.setDate(5, campaign.getStartDate());
            pstmt.setDate(6, campaign.getEndDate());
            pstmt.setString(7, campaign.getRequirements());
            pstmt.setString(8, campaign.getStatus());
            pstmt.setInt(9, campaign.getMinFollowers());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    campaignId = rs.getInt(1);
                    campaign.setId(campaignId);
                    
                    // Insert target interests if any
                    if (campaign.getTargetInterests() != null && !campaign.getTargetInterests().isEmpty()) {
                        insertTargetInterests(campaignId, campaign.getTargetInterests(), conn);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return campaignId;
    }
    
    /**
     * Insert campaign target interests
     * @param campaignId Campaign ID
     * @param interests List of interests
     * @param conn Database connection
     * @throws SQLException if a database access error occurs
     */
    private void insertTargetInterests(int campaignId, List<String> interests, Connection conn) throws SQLException {
        String sql = "INSERT INTO campaign_interests (campaign_id, interest) VALUES (?, ?)";
        
        PreparedStatement pstmt = null;
        
        try {
            pstmt = conn.prepareStatement(sql);
            
            for (String interest : interests) {
                pstmt.setInt(1, campaignId);
                pstmt.setString(2, interest);
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }
    
    /**
     * Get a campaign by ID
     * @param id Campaign ID
     * @return Campaign object if found, null otherwise
     */
    public Campaign getById(int id) {
        String sql = "SELECT * FROM campaigns WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Campaign campaign = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                campaign = mapResultSetToCampaign(rs);
                
                // Get campaign target interests
                List<String> interests = getCampaignInterests(id, conn);
                campaign.setTargetInterests(interests);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return campaign;
    }
    
    /**
     * Get campaign interests
     * @param campaignId Campaign ID
     * @param conn Database connection
     * @return List of interests
     * @throws SQLException if a database access error occurs
     */
    private List<String> getCampaignInterests(int campaignId, Connection conn) throws SQLException {
        String sql = "SELECT interest FROM campaign_interests WHERE campaign_id = ?";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> interests = new ArrayList<>();
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, campaignId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                interests.add(rs.getString("interest"));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        
        return interests;
    }
    
    /**
     * Get all campaigns
     * @return List of all campaigns
     */
    public List<Campaign> getAll() {
        String sql = "SELECT * FROM campaigns ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Campaign> campaigns = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Campaign campaign = mapResultSetToCampaign(rs);
                campaigns.add(campaign);
            }
            
            // Get interests for each campaign
            for (Campaign campaign : campaigns) {
                List<String> interests = getCampaignInterests(campaign.getId(), conn);
                campaign.setTargetInterests(interests);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
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
    public List<Campaign> getByBusinessId(int businessId) {
        String sql = "SELECT * FROM campaigns WHERE business_id = ? ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Campaign> campaigns = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, businessId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Campaign campaign = mapResultSetToCampaign(rs);
                campaigns.add(campaign);
            }
            
            // Get interests for each campaign
            for (Campaign campaign : campaigns) {
                List<String> interests = getCampaignInterests(campaign.getId(), conn);
                campaign.setTargetInterests(interests);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return campaigns;
    }
    
    /**
     * Update a campaign
     * @param campaign Campaign object to update
     * @return true if successful, false otherwise
     */
    public boolean update(Campaign campaign) {
        String sql = "UPDATE campaigns SET title = ?, description = ?, budget = ?, " +
                     "start_date = ?, end_date = ?, requirements = ?, status = ?, " +
                     "min_followers = ?, updated_at = NOW() WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, campaign.getTitle());
            pstmt.setString(2, campaign.getDescription());
            pstmt.setDouble(3, campaign.getBudget());
            pstmt.setDate(4, campaign.getStartDate());
            pstmt.setDate(5, campaign.getEndDate());
            pstmt.setString(6, campaign.getRequirements());
            pstmt.setString(7, campaign.getStatus());
            pstmt.setInt(8, campaign.getMinFollowers());
            pstmt.setInt(9, campaign.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Delete existing interests
                deleteCampaignInterests(campaign.getId(), conn);
                
                // Insert new interests
                if (campaign.getTargetInterests() != null && !campaign.getTargetInterests().isEmpty()) {
                    insertTargetInterests(campaign.getId(), campaign.getTargetInterests(), conn);
                }
                
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return success;
    }
    
    /**
     * Delete campaign interests
     * @param campaignId Campaign ID
     * @param conn Database connection
     * @throws SQLException if a database access error occurs
     */
    private void deleteCampaignInterests(int campaignId, Connection conn) throws SQLException {
        String sql = "DELETE FROM campaign_interests WHERE campaign_id = ?";
        
        PreparedStatement pstmt = null;
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, campaignId);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }
    
    /**
     * Delete a campaign
     * @param campaignId Campaign ID
     * @return true if successful, false otherwise
     */
    public boolean delete(int campaignId) {
        String sql = "DELETE FROM campaigns WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            
            // First delete campaign interests
            deleteCampaignInterests(campaignId, conn);
            
            // Then delete the campaign
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, campaignId);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return success;
    }
    
    /**
     * Map a ResultSet to a Campaign object
     * @param rs ResultSet containing campaign data
     * @return Campaign object
     * @throws SQLException if a database access error occurs
     */
    private Campaign mapResultSetToCampaign(ResultSet rs) throws SQLException {
        Campaign campaign = new Campaign();
        
        campaign.setId(rs.getInt("id"));
        campaign.setBusinessId(rs.getInt("business_id"));
        campaign.setTitle(rs.getString("title"));
        campaign.setDescription(rs.getString("description"));
        campaign.setBudget(rs.getDouble("budget"));
        campaign.setStartDate(rs.getDate("start_date"));
        campaign.setEndDate(rs.getDate("end_date"));
        campaign.setRequirements(rs.getString("requirements"));
        campaign.setStatus(rs.getString("status"));
        campaign.setMinFollowers(rs.getInt("min_followers"));
        campaign.setCreatedAt(rs.getTimestamp("created_at"));
        campaign.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return campaign;
    }
}
