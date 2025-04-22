package com.konnect.dao;

import com.konnect.config.DatabaseConnection;
import com.konnect.model.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDao {

    // Creator applies for a campaign
    public boolean createApplication(Application application) {
        // Ensure it's an actual application, not an invite
        if (!"pending".equalsIgnoreCase(application.getStatus())) {
             application.setStatus("pending"); // Force status for direct applications
        }
        return saveApplication(application);
    }

    // Business invites a creator
     public boolean createInvitation(Application invitation) {
         // Ensure it's marked as an invite
         if (!"invited".equalsIgnoreCase(invitation.getStatus())) {
            invitation.setStatus("invited"); // Force status for invitations
         }
         return saveApplication(invitation);
     }

     // Common method for saving application/invitation
     private boolean saveApplication(Application app) {
        String sql = "INSERT INTO applications (campaign_id, creator_user_id, status, message) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, app.getCampaignId());
            stmt.setInt(2, app.getCreatorUserId());
            stmt.setString(3, app.getStatus());
            // Use null check for message
            if (app.getMessage() != null && !app.getMessage().isEmpty()) {
                stmt.setString(4, app.getMessage());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }


            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            // Handle potential duplicate entry (unique key constraint)
            if (e.getErrorCode() == 1062) { // MySQL code for duplicate entry
                System.err.println("Attempted to create duplicate application/invitation for campaign " + app.getCampaignId() + " and creator " + app.getCreatorUserId());
            } else {
                System.err.println("Error creating application/invitation: " + e.getMessage());
                e.printStackTrace(); // Print stack trace for detailed debugging
            }
            return false;
        }
     }


    // Business accepts/rejects an application
    public boolean updateApplicationStatus(int applicationId, String status, int businessUserId) {
        // Verify the businessUserId owns the campaign associated with this application
        String checkOwnerSql = "SELECT c.business_user_id FROM applications a JOIN campaigns c ON a.campaign_id = c.campaign_id WHERE a.application_id = ?";
        String updateSql = "UPDATE applications SET status = ? WHERE application_id = ?";

        // Validate allowed statuses for business action
        if (!"accepted".equalsIgnoreCase(status) && !"rejected".equalsIgnoreCase(status)) {
            System.err.println("Invalid status for business action: " + status);
            return false;
        }


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkOwnerSql)) {

            checkStmt.setInt(1, applicationId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int ownerId = rs.getInt("business_user_id");
                if (ownerId == businessUserId) {
                    // Owner verified, proceed with update
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setString(1, status);
                        updateStmt.setInt(2, applicationId);
                        int affectedRows = updateStmt.executeUpdate();
                        return affectedRows > 0;
                    }
                } else {
                    System.err.println("Authorization failed: Business user " + businessUserId + " tried to update status for application " + applicationId + " not belonging to them.");
                    return false; // Authorization failed
                }
            } else {
                 System.err.println("Application not found: ID " + applicationId);
                return false; // Application not found
            }

        } catch (SQLException e) {
            System.err.println("Error updating application status: " + e.getMessage());
             e.printStackTrace();
            return false;
        }
    }

     // Creator accepts/rejects an invitation
     public boolean respondToInvitation(int applicationId, String status, int creatorUserId) {
         // Verify the creatorUserId matches the application record
         String sql = "UPDATE applications SET status = ? WHERE application_id = ? AND creator_user_id = ? AND status = 'invited'"; // Only update if currently invited

         try (Connection conn = DatabaseConnection.getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql)) {

             // Allowed responses: 'accepted' or 'rejected'
             if (!"accepted".equalsIgnoreCase(status) && !"rejected".equalsIgnoreCase(status)) {
                 System.err.println("Invalid status for invitation response: " + status);
                 return false;
             }

             stmt.setString(1, status);
             stmt.setInt(2, applicationId);
             stmt.setInt(3, creatorUserId); // Security check

             int affectedRows = stmt.executeUpdate();
             return affectedRows > 0;

         } catch (SQLException e) {
             System.err.println("Error responding to invitation: " + e.getMessage());
             e.printStackTrace();
             return false;
         }
     }


    // Get applications FOR a specific campaign (for Business view)
    public List<Application> findByCampaignId(int campaignId, int businessUserId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT a.*, c.title as campaignTitle, uc.username as creatorUsername, " +
                     "ub.username as businessUsername, ub.user_id as business_user_id " +
                     "FROM applications a " +
                     "JOIN campaigns c ON a.campaign_id = c.campaign_id " +
                     "JOIN users uc ON a.creator_user_id = uc.user_id " +
                     "JOIN users ub ON c.business_user_id = ub.user_id " +
                     "WHERE a.campaign_id = ? AND c.business_user_id = ? " +
                     "ORDER BY a.applied_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, campaignId);
             stmt.setInt(2, businessUserId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                applications.add(mapResultSetToApplication(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding applications by campaign ID: " + e.getMessage());
            e.printStackTrace();
        }
        return applications;
    }

    // Get applications BY a specific creator (for Creator view)
    public List<Application> findByCreatorId(int creatorUserId) {
        List<Application> applications = new ArrayList<>();
         String sql = "SELECT a.*, c.title as campaignTitle, uc.username as creatorUsername, " +
                      "ub.username as businessUsername, ub.user_id as business_user_id " +
                      "FROM applications a " +
                      "JOIN campaigns c ON a.campaign_id = c.campaign_id " +
                      "JOIN users uc ON a.creator_user_id = uc.user_id " +
                      "JOIN users ub ON c.business_user_id = ub.user_id " +
                      "WHERE a.creator_user_id = ? " +
                      "ORDER BY a.applied_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, creatorUserId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                applications.add(mapResultSetToApplication(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding applications by creator ID: " + e.getMessage());
            e.printStackTrace();
        }
        return applications;
    }

     // Get invitations FOR a specific creator (for Creator view)
    public List<Application> findInvitationsByCreatorId(int creatorUserId) {
        List<Application> invitations = new ArrayList<>();
        String sql = "SELECT a.*, c.title as campaignTitle, uc.username as creatorUsername, " +
                     "ub.username as businessUsername, ub.user_id as business_user_id " +
                     "FROM applications a " +
                     "JOIN campaigns c ON a.campaign_id = c.campaign_id " +
                     "JOIN users uc ON a.creator_user_id = uc.user_id " +
                     "JOIN users ub ON c.business_user_id = ub.user_id " +
                     "WHERE a.creator_user_id = ? AND a.status = 'invited' " + // Only fetch invites
                     "ORDER BY a.applied_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, creatorUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                invitations.add(mapResultSetToApplication(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding invitations by creator ID: " + e.getMessage());
            e.printStackTrace();
        }
        return invitations;
    }


    public Application findById(int applicationId) {
         String sql = "SELECT a.*, c.title as campaignTitle, uc.username as creatorUsername, " +
                      "ub.username as businessUsername, ub.user_id as business_user_id " +
                      "FROM applications a " +
                      "JOIN campaigns c ON a.campaign_id = c.campaign_id " +
                      "JOIN users uc ON a.creator_user_id = uc.user_id " +
                      "JOIN users ub ON c.business_user_id = ub.user_id " +
                      "WHERE a.application_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, applicationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToApplication(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding application by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    private Application mapResultSetToApplication(ResultSet rs) throws SQLException {
        Application app = new Application();
        app.setApplicationId(rs.getInt("application_id"));
        app.setCampaignId(rs.getInt("campaign_id"));
        app.setCreatorUserId(rs.getInt("creator_user_id"));
        app.setStatus(rs.getString("status"));
        app.setMessage(rs.getString("message")); // getString handles SQL NULL -> Java null
        app.setAppliedAt(rs.getTimestamp("applied_at"));

        // Map joined fields if present
         if (hasColumn(rs, "campaignTitle")) {
            app.setCampaignTitle(rs.getString("campaignTitle"));
        }
         if (hasColumn(rs, "creatorUsername")) {
            app.setCreatorUsername(rs.getString("creatorUsername"));
        }
        if (hasColumn(rs, "businessUsername")) {
            app.setBusinessUsername(rs.getString("businessUsername"));
        }
        if (hasColumn(rs, "business_user_id")) {
             app.setBusinessUserId(rs.getInt("business_user_id"));
        }

        return app;
    }

     // Helper to check if a column exists using label primarily
    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equalsIgnoreCase(rsmd.getColumnLabel(x))) {
                return true;
            }
        }
        // Fallback check using column name if label doesn't match (less common)
         for (int x = 1; x <= columns; x++) {
              if (columnName.equalsIgnoreCase(rsmd.getColumnName(x))) {
                  System.out.println("Warning: Matched column using getName instead of getLabel for: " + columnName); // Optional warning
                  return true;
              }
         }
        return false;
    }
}
