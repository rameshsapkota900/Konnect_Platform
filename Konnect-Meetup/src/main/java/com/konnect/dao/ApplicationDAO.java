package com.konnect.dao;

import com.konnect.model.Application;
import com.konnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ApplicationDAO Class
 * Handles database operations for Application objects
 */
public class ApplicationDAO {
    
    /**
     * Insert a new application into the database
     * @param application Application object to insert
     * @return generated application ID if successful, -1 otherwise
     */
    public int insert(Application application) {
        String sql = "INSERT INTO applications (campaign_id, creator_id, message, status) " +
                     "VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int applicationId = -1;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, application.getCampaignId());
            pstmt.setInt(2, application.getCreatorId());
            pstmt.setString(3, application.getMessage());
            pstmt.setString(4, application.getStatus());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    applicationId = rs.getInt(1);
                    application.setId(applicationId);
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
        
        return applicationId;
    }
    
    /**
     * Get an application by ID
     * @param id Application ID
     * @return Application object if found, null otherwise
     */
    public Application getById(int id) {
        String sql = "SELECT * FROM applications WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Application application = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                application = mapResultSetToApplication(rs);
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
        
        return application;
    }
    
    /**
     * Get applications by campaign ID
     * @param campaignId Campaign ID
     * @return List of applications for the campaign
     */
    public List<Application> getByCampaignId(int campaignId) {
        String sql = "SELECT * FROM applications WHERE campaign_id = ? ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Application> applications = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, campaignId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Application application = mapResultSetToApplication(rs);
                applications.add(application);
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
        
        return applications;
    }
    
    /**
     * Get applications by creator ID
     * @param creatorId Creator ID
     * @return List of applications by the creator
     */
    public List<Application> getByCreatorId(int creatorId) {
        String sql = "SELECT * FROM applications WHERE creator_id = ? ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Application> applications = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, creatorId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Application application = mapResultSetToApplication(rs);
                applications.add(application);
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
        
        return applications;
    }
    
    /**
     * Check if a creator has already applied to a campaign
     * @param campaignId Campaign ID
     * @param creatorId Creator ID
     * @return true if already applied, false otherwise
     */
    public boolean hasApplied(int campaignId, int creatorId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE campaign_id = ? AND creator_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean hasApplied = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, campaignId);
            pstmt.setInt(2, creatorId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                hasApplied = rs.getInt(1) > 0;
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
        
        return hasApplied;
    }
    
    /**
     * Update application status
     * @param applicationId Application ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateStatus(int applicationId, String status) {
        String sql = "UPDATE applications SET status = ?, updated_at = NOW() WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, status);
            pstmt.setInt(2, applicationId);
            
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
     * Delete an application
     * @param applicationId Application ID
     * @return true if successful, false otherwise
     */
    public boolean delete(int applicationId) {
        String sql = "DELETE FROM applications WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, applicationId);
            
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
     * Map a ResultSet to an Application object
     * @param rs ResultSet containing application data
     * @return Application object
     * @throws SQLException if a database access error occurs
     */
    private Application mapResultSetToApplication(ResultSet rs) throws SQLException {
        Application application = new Application();
        
        application.setId(rs.getInt("id"));
        application.setCampaignId(rs.getInt("campaign_id"));
        application.setCreatorId(rs.getInt("creator_id"));
        application.setMessage(rs.getString("message"));
        application.setStatus(rs.getString("status"));
        application.setCreatedAt(rs.getTimestamp("created_at"));
        application.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return application;
    }
}
