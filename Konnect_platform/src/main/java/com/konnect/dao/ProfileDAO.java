package com.konnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.konnect.model.BusinessProfile;
import com.konnect.model.CreatorProfile;
import com.konnect.util.DBConnection;

/**
 * Data Access Object for Profile-related database operations
 */
public class ProfileDAO {
    
    /**
     * Create a new creator profile
     * @param profile CreatorProfile object with profile details
     * @return Profile ID if successful, -1 if failed
     */
    public int createCreatorProfile(CreatorProfile profile) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int profileId = -1;
        
        try {
            conn = DBConnection.getConnection();
            
            // Check if profile already exists
            if (getCreatorProfileByUserId(profile.getUserId()) != null) {
                // Update existing profile instead
                return updateCreatorProfile(profile) ? profile.getProfileId() : -1;
            }
            
            String sql = "INSERT INTO creator_profiles (user_id, full_name, bio, instagram_link, youtube_link, " +
                         "tiktok_link, instagram_followers, youtube_followers, tiktok_followers, " +
                         "post_price, story_price, video_price, interests) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, profile.getUserId());
            pstmt.setString(2, profile.getFullName());
            pstmt.setString(3, profile.getBio());
            pstmt.setString(4, profile.getInstagramLink());
            pstmt.setString(5, profile.getYoutubeLink());
            pstmt.setString(6, profile.getTiktokLink());
            pstmt.setInt(7, profile.getInstagramFollowers());
            pstmt.setInt(8, profile.getYoutubeFollowers());
            pstmt.setInt(9, profile.getTiktokFollowers());
            pstmt.setDouble(10, profile.getPostPrice());
            pstmt.setDouble(11, profile.getStoryPrice());
            pstmt.setDouble(12, profile.getVideoPrice());
            pstmt.setString(13, profile.getInterests());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    profileId = rs.getInt(1);
                    profile.setProfileId(profileId);
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
        
        return profileId;
    }
    
    /**
     * Create a new business profile
     * @param profile BusinessProfile object with profile details
     * @return Profile ID if successful, -1 if failed
     */
    public int createBusinessProfile(BusinessProfile profile) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int profileId = -1;
        
        try {
            conn = DBConnection.getConnection();
            
            // Check if profile already exists
            if (getBusinessProfileByUserId(profile.getUserId()) != null) {
                // Update existing profile instead
                return updateBusinessProfile(profile) ? profile.getProfileId() : -1;
            }
            
            String sql = "INSERT INTO business_profiles (user_id, business_name, business_description, website, industry) " +
                         "VALUES (?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, profile.getUserId());
            pstmt.setString(2, profile.getBusinessName());
            pstmt.setString(3, profile.getBusinessDescription());
            pstmt.setString(4, profile.getWebsite());
            pstmt.setString(5, profile.getIndustry());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    profileId = rs.getInt(1);
                    profile.setProfileId(profileId);
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
        
        return profileId;
    }
    
    /**
     * Get creator profile by user ID
     * @param userId User ID
     * @return CreatorProfile object if found, null otherwise
     */
    public CreatorProfile getCreatorProfileByUserId(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        CreatorProfile profile = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT cp.*, u.username, u.email, u.status FROM creator_profiles cp " +
                         "JOIN users u ON cp.user_id = u.user_id " +
                         "WHERE cp.user_id = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                profile = new CreatorProfile();
                profile.setProfileId(rs.getInt("profile_id"));
                profile.setUserId(rs.getInt("user_id"));
                profile.setFullName(rs.getString("full_name"));
                profile.setBio(rs.getString("bio"));
                profile.setInstagramLink(rs.getString("instagram_link"));
                profile.setYoutubeLink(rs.getString("youtube_link"));
                profile.setTiktokLink(rs.getString("tiktok_link"));
                profile.setInstagramFollowers(rs.getInt("instagram_followers"));
                profile.setYoutubeFollowers(rs.getInt("youtube_followers"));
                profile.setTiktokFollowers(rs.getInt("tiktok_followers"));
                profile.setPostPrice(rs.getDouble("post_price"));
                profile.setStoryPrice(rs.getDouble("story_price"));
                profile.setVideoPrice(rs.getDouble("video_price"));
                profile.setInterests(rs.getString("interests"));
                profile.setCreatedAt(rs.getTimestamp("created_at"));
                profile.setUpdatedAt(rs.getTimestamp("updated_at"));
                profile.setUsername(rs.getString("username"));
                profile.setEmail(rs.getString("email"));
                profile.setStatus(rs.getString("status"));
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
        
        return profile;
    }
    
    /**
     * Get business profile by user ID
     * @param userId User ID
     * @return BusinessProfile object if found, null otherwise
     */
    public BusinessProfile getBusinessProfileByUserId(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BusinessProfile profile = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT bp.*, u.username, u.email, u.status FROM business_profiles bp " +
                         "JOIN users u ON bp.user_id = u.user_id " +
                         "WHERE bp.user_id = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                profile = new BusinessProfile();
                profile.setProfileId(rs.getInt("profile_id"));
                profile.setUserId(rs.getInt("user_id"));
                profile.setBusinessName(rs.getString("business_name"));
                profile.setBusinessDescription(rs.getString("business_description"));
                profile.setWebsite(rs.getString("website"));
                profile.setIndustry(rs.getString("industry"));
                profile.setCreatedAt(rs.getTimestamp("created_at"));
                profile.setUpdatedAt(rs.getTimestamp("updated_at"));
                profile.setUsername(rs.getString("username"));
                profile.setEmail(rs.getString("email"));
                profile.setStatus(rs.getString("status"));
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
        
        return profile;
    }
    
    /**
     * Get all creator profiles
     * @return List of all creator profiles
     */
    public List<CreatorProfile> getAllCreatorProfiles() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<CreatorProfile> profiles = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT cp.*, u.username, u.email, u.status FROM creator_profiles cp " +
                         "JOIN users u ON cp.user_id = u.user_id " +
                         "WHERE u.status = 'active' " +
                         "ORDER BY cp.created_at DESC";
            
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                CreatorProfile profile = new CreatorProfile();
                profile.setProfileId(rs.getInt("profile_id"));
                profile.setUserId(rs.getInt("user_id"));
                profile.setFullName(rs.getString("full_name"));
                profile.setBio(rs.getString("bio"));
                profile.setInstagramLink(rs.getString("instagram_link"));
                profile.setYoutubeLink(rs.getString("youtube_link"));
                profile.setTiktokLink(rs.getString("tiktok_link"));
                profile.setInstagramFollowers(rs.getInt("instagram_followers"));
                profile.setYoutubeFollowers(rs.getInt("youtube_followers"));
                profile.setTiktokFollowers(rs.getInt("tiktok_followers"));
                profile.setPostPrice(rs.getDouble("post_price"));
                profile.setStoryPrice(rs.getDouble("story_price"));
                profile.setVideoPrice(rs.getDouble("video_price"));
                profile.setInterests(rs.getString("interests"));
                profile.setCreatedAt(rs.getTimestamp("created_at"));
                profile.setUpdatedAt(rs.getTimestamp("updated_at"));
                profile.setUsername(rs.getString("username"));
                profile.setEmail(rs.getString("email"));
                profile.setStatus(rs.getString("status"));
                
                profiles.add(profile);
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
        
        return profiles;
    }
    
    /**
     * Get creator profiles by interests
     * @param interests Comma-separated list of interests
     * @return List of creator profiles matching the interests
     */
    public List<CreatorProfile> getCreatorProfilesByInterests(String interests) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<CreatorProfile> profiles = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            
            // Split interests and create SQL LIKE conditions
            String[] interestArray = interests.split(",");
            StringBuilder likeConditions = new StringBuilder();
            
            for (int i = 0; i < interestArray.length; i++) {
                if (i > 0) {
                    likeConditions.append(" OR ");
                }
                likeConditions.append("cp.interests LIKE ?");
            }
            
            String sql = "SELECT cp.*, u.username, u.email, u.status FROM creator_profiles cp " +
                         "JOIN users u ON cp.user_id = u.user_id " +
                         "WHERE u.status = 'active' AND (" + likeConditions.toString() + ") " +
                         "ORDER BY cp.created_at DESC";
            
            pstmt = conn.prepareStatement(sql);
            
            // Set parameters for LIKE conditions
            for (int i = 0; i < interestArray.length; i++) {
                pstmt.setString(i + 1, "%" + interestArray[i].trim() + "%");
            }
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                CreatorProfile profile = new CreatorProfile();
                profile.setProfileId(rs.getInt("profile_id"));
                profile.setUserId(rs.getInt("user_id"));
                profile.setFullName(rs.getString("full_name"));
                profile.setBio(rs.getString("bio"));
                profile.setInstagramLink(rs.getString("instagram_link"));
                profile.setYoutubeLink(rs.getString("youtube_link"));
                profile.setTiktokLink(rs.getString("tiktok_link"));
                profile.setInstagramFollowers(rs.getInt("instagram_followers"));
                profile.setYoutubeFollowers(rs.getInt("youtube_followers"));
                profile.setTiktokFollowers(rs.getInt("tiktok_followers"));
                profile.setPostPrice(rs.getDouble("post_price"));
                profile.setStoryPrice(rs.getDouble("story_price"));
                profile.setVideoPrice(rs.getDouble("video_price"));
                profile.setInterests(rs.getString("interests"));
                profile.setCreatedAt(rs.getTimestamp("created_at"));
                profile.setUpdatedAt(rs.getTimestamp("updated_at"));
                profile.setUsername(rs.getString("username"));
                profile.setEmail(rs.getString("email"));
                profile.setStatus(rs.getString("status"));
                
                profiles.add(profile);
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
        
        return profiles;
    }
    
    /**
     * Update creator profile
     * @param profile CreatorProfile object with updated details
     * @return true if successful, false otherwise
     */
    public boolean updateCreatorProfile(CreatorProfile profile) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsUpdated = 0;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "UPDATE creator_profiles SET full_name = ?, bio = ?, instagram_link = ?, youtube_link = ?, " +
                         "tiktok_link = ?, instagram_followers = ?, youtube_followers = ?, tiktok_followers = ?, " +
                         "post_price = ?, story_price = ?, video_price = ?, interests = ? " +
                         "WHERE user_id = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, profile.getFullName());
            pstmt.setString(2, profile.getBio());
            pstmt.setString(3, profile.getInstagramLink());
            pstmt.setString(4, profile.getYoutubeLink());
            pstmt.setString(5, profile.getTiktokLink());
            pstmt.setInt(6, profile.getInstagramFollowers());
            pstmt.setInt(7, profile.getYoutubeFollowers());
            pstmt.setInt(8, profile.getTiktokFollowers());
            pstmt.setDouble(9, profile.getPostPrice());
            pstmt.setDouble(10, profile.getStoryPrice());
            pstmt.setDouble(11, profile.getVideoPrice());
            pstmt.setString(12, profile.getInterests());
            pstmt.setInt(13, profile.getUserId());
            
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
     * Update business profile
     * @param profile BusinessProfile object with updated details
     * @return true if successful, false otherwise
     */
    public boolean updateBusinessProfile(BusinessProfile profile) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsUpdated = 0;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "UPDATE business_profiles SET business_name = ?, business_description = ?, website = ?, industry = ? " +
                         "WHERE user_id = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, profile.getBusinessName());
            pstmt.setString(2, profile.getBusinessDescription());
            pstmt.setString(3, profile.getWebsite());
            pstmt.setString(4, profile.getIndustry());
            pstmt.setInt(5, profile.getUserId());
            
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
     * Count total creator profiles
     * @return Total number of creator profiles
     */
    public int countCreatorProfiles() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT COUNT(*) FROM creator_profiles";
            
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
     * Count total business profiles
     * @return Total number of business profiles
     */
    public int countBusinessProfiles() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT COUNT(*) FROM business_profiles";
            
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
}
