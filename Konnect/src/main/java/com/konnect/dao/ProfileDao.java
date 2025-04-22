package com.konnect.dao;

import com.konnect.config.DatabaseConnection;
import com.konnect.model.Profile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileDao {

    public Profile findByUserId(int userId) {
        String sql = "SELECT p.*, u.username FROM profiles p JOIN users u ON p.user_id = u.user_id WHERE p.user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProfile(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding profile by user ID: " + e.getMessage());
        }
        return null; // Or return an empty profile object
    }

     // Added method to create an empty profile upon creator registration
     public boolean createEmptyProfile(int userId) {
         // Check if profile already exists for this user
         if (findByUserId(userId) != null) {
             System.out.println("Profile already exists for user ID: " + userId);
             return true; // Already exists, consider it a success in this context
         }

         String sql = "INSERT INTO profiles (user_id, full_name, bio, social_media_links, follower_count, niche, pricing_info, media_kit_path) VALUES (?, '', '', '', 0, '', '', NULL)";
         try (Connection conn = DatabaseConnection.getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql)) {

             stmt.setInt(1, userId);
             int affectedRows = stmt.executeUpdate();
             return affectedRows > 0;

         } catch (SQLException e) {
             System.err.println("Error creating empty profile for user ID " + userId + ": " + e.getMessage());
             return false;
         }
     }


    public boolean saveOrUpdateProfile(Profile profile) {
        // Check if profile exists
        Profile existingProfile = findByUserId(profile.getUserId());
        String sql;
        if (existingProfile == null) {
            // Insert new profile
            sql = "INSERT INTO profiles (user_id, full_name, bio, social_media_links, follower_count, niche, pricing_info, media_kit_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            // Update existing profile
             sql = "UPDATE profiles SET full_name = ?, bio = ?, social_media_links = ?, follower_count = ?, niche = ?, pricing_info = ?, media_kit_path = ? WHERE user_id = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

             stmt.setString(1, profile.getFullName());
             stmt.setString(2, profile.getBio());
             stmt.setString(3, profile.getSocialMediaLinks());
             stmt.setInt(4, profile.getFollowerCount());
             stmt.setString(5, profile.getNiche());
             stmt.setString(6, profile.getPricingInfo());
             stmt.setString(7, profile.getMediaKitPath()); // Allow null if not updated/set
             stmt.setInt(8, profile.getUserId()); // This is the WHERE clause for UPDATE, and the first param for INSERT

             if (existingProfile == null) {
                 // Set user_id for INSERT statement (parameter index 1)
                 stmt.setInt(1, profile.getUserId());
                 stmt.setString(2, profile.getFullName());
                 stmt.setString(3, profile.getBio());
                 stmt.setString(4, profile.getSocialMediaLinks());
                 stmt.setInt(5, profile.getFollowerCount());
                 stmt.setString(6, profile.getNiche());
                 stmt.setString(7, profile.getPricingInfo());
                 stmt.setString(8, profile.getMediaKitPath()); // Parameter index 8 for INSERT
             }


            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error saving/updating profile: " + e.getMessage());
            return false;
        }
    }

    // Method for businesses to search creators
    public List<Profile> searchCreators(String keyword, String nicheFilter, int minFollowers) {
        List<Profile> profiles = new ArrayList<>();
        // Base query joining profiles and active creator users
        StringBuilder sqlBuilder = new StringBuilder("SELECT p.*, u.username FROM profiles p ");
        sqlBuilder.append("JOIN users u ON p.user_id = u.user_id ");
        sqlBuilder.append("WHERE u.role = 'creator' AND u.is_active = TRUE ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sqlBuilder.append("AND (p.full_name LIKE ? OR p.bio LIKE ? OR p.niche LIKE ? OR u.username LIKE ?) ");
            String likeKeyword = "%" + keyword.trim() + "%";
            params.add(likeKeyword);
            params.add(likeKeyword);
            params.add(likeKeyword);
            params.add(likeKeyword);
        }
        if (nicheFilter != null && !nicheFilter.trim().isEmpty()) {
            sqlBuilder.append("AND p.niche LIKE ? ");
             params.add("%" + nicheFilter.trim() + "%");
        }
        if (minFollowers > 0) {
            sqlBuilder.append("AND p.follower_count >= ? ");
            params.add(minFollowers);
        }

        sqlBuilder.append("ORDER BY p.follower_count DESC, u.username ASC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            // System.out.println("Executing SQL: " + stmt); // For debugging

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                profiles.add(mapResultSetToProfile(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error searching creators: " + e.getMessage());
        }
        return profiles;
    }

     public String getMediaKitPath(int userId) {
        String sql = "SELECT media_kit_path FROM profiles WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("media_kit_path");
            }
        } catch (SQLException e) {
            System.err.println("Error getting media kit path: " + e.getMessage());
        }
        return null;
    }


    private Profile mapResultSetToProfile(ResultSet rs) throws SQLException {
        Profile profile = new Profile();
        profile.setProfileId(rs.getInt("profile_id"));
        profile.setUserId(rs.getInt("user_id"));
        profile.setFullName(rs.getString("full_name"));
        profile.setBio(rs.getString("bio"));
        profile.setSocialMediaLinks(rs.getString("social_media_links"));
        profile.setFollowerCount(rs.getInt("follower_count"));
        profile.setNiche(rs.getString("niche"));
        profile.setPricingInfo(rs.getString("pricing_info"));
        profile.setMediaKitPath(rs.getString("media_kit_path"));

        // Optionally map joined fields if present
        if (hasColumn(rs, "username")) {
            profile.setUsername(rs.getString("username"));
        }

        return profile;
    }

     // Helper to check if a column exists in ResultSet to avoid errors when not joining
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
