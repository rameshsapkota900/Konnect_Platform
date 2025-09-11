package com.konnect.dao;

import com.konnect.model.Creator;
import com.konnect.model.User;
import com.konnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CreatorDAO Class
 * Handles database operations for Creator objects
 */
public class CreatorDAO {
    // We don't need UserDAO anymore since we're not creating/updating user records

    public CreatorDAO() {
        // No initialization needed
    }

    /**
     * Insert a new creator profile into the database for an existing user
     * @param creator Creator object to insert
     * @return true if successful, false otherwise
     */
    public boolean insert(Creator creator) {
        // For a new creator profile, we don't insert a new user
        // We just create a profile for an existing user
        int userId = creator.getId();

        if (userId <= 0) {
            return false;
        }

        // Insert the creator profile
        String sql = "INSERT INTO creator_profiles (user_id, bio, follower_count, instagram_link, " +
                     "tiktok_link, youtube_link, pricing_per_post) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, userId);
            pstmt.setString(2, creator.getBio());
            pstmt.setInt(3, creator.getFollowerCount());
            pstmt.setString(4, creator.getInstagramLink());
            pstmt.setString(5, creator.getTiktokLink());
            pstmt.setString(6, creator.getYoutubeLink());
            pstmt.setDouble(7, creator.getPricingPerPost());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int creatorProfileId = rs.getInt(1);

                    // Insert interests if any
                    if (creator.getInterests() != null && !creator.getInterests().isEmpty()) {
                        insertInterests(creatorProfileId, creator.getInterests(), conn);
                    }

                    success = true;
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

        return success;
    }

    /**
     * Insert creator interests
     * @param creatorProfileId Creator profile ID
     * @param interests List of interests
     * @param conn Database connection
     * @throws SQLException if a database access error occurs
     */
    private void insertInterests(int creatorProfileId, List<String> interests, Connection conn) throws SQLException {
        String sql = "INSERT INTO creator_interests (creator_id, interest) VALUES (?, ?)";

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);

            for (String interest : interests) {
                pstmt.setInt(1, creatorProfileId);
                pstmt.setString(2, interest);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    /**
     * Get a creator by user ID
     * @param userId User ID
     * @return Creator object if found, null otherwise
     */
    public Creator getByUserId(int userId) {
        String sql = "SELECT c.*, u.* FROM creator_profiles c " +
                     "JOIN users u ON c.user_id = u.id " +
                     "WHERE u.id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Creator creator = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                creator = mapResultSetToCreator(rs);

                // Get creator interests
                List<String> interests = getCreatorInterests(rs.getInt("c.id"), conn);
                creator.setInterests(interests);
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

        return creator;
    }

    /**
     * Get creator interests
     * @param creatorProfileId Creator profile ID
     * @param conn Database connection
     * @return List of interests
     * @throws SQLException if a database access error occurs
     */
    private List<String> getCreatorInterests(int creatorProfileId, Connection conn) throws SQLException {
        String sql = "SELECT interest FROM creator_interests WHERE creator_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> interests = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, creatorProfileId);

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
     * Update a creator profile
     * @param creator Creator object to update
     * @return true if successful, false otherwise
     */
    public boolean update(Creator creator) {
        // We don't need to update the user record for profile updates
        // Just update the creator profile

        // Then update the creator profile
        String sql = "UPDATE creator_profiles SET bio = ?, follower_count = ?, " +
                     "instagram_link = ?, tiktok_link = ?, youtube_link = ?, " +
                     "pricing_per_post = ?, updated_at = NOW() " +
                     "WHERE user_id = ?"; // user_id is the foreign key to users.id

        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, creator.getBio());
            pstmt.setInt(2, creator.getFollowerCount());
            pstmt.setString(3, creator.getInstagramLink());
            pstmt.setString(4, creator.getTiktokLink());
            pstmt.setString(5, creator.getYoutubeLink());
            pstmt.setDouble(6, creator.getPricingPerPost());
            // Use the creator's ID (which is the user ID) for the WHERE clause
            pstmt.setInt(7, creator.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Get creator profile ID
                int creatorProfileId = getCreatorProfileId(creator.getId(), conn);

                if (creatorProfileId > 0) {
                    // Delete existing interests
                    deleteCreatorInterests(creatorProfileId, conn);

                    // Insert new interests
                    if (creator.getInterests() != null && !creator.getInterests().isEmpty()) {
                        insertInterests(creatorProfileId, creator.getInterests(), conn);
                    }

                    success = true;
                }
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
     * Get creator profile ID by user ID
     * @param userId User ID
     * @param conn Database connection
     * @return Creator profile ID if found, -1 otherwise
     * @throws SQLException if a database access error occurs
     */
    private int getCreatorProfileId(int userId, Connection conn) throws SQLException {
        String sql = "SELECT id FROM creator_profiles WHERE user_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int creatorProfileId = -1;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                creatorProfileId = rs.getInt("id");
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }

        return creatorProfileId;
    }

    /**
     * Delete creator interests
     * @param creatorProfileId Creator profile ID
     * @param conn Database connection
     * @throws SQLException if a database access error occurs
     */
    private void deleteCreatorInterests(int creatorProfileId, Connection conn) throws SQLException {
        String sql = "DELETE FROM creator_interests WHERE creator_id = ?";

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, creatorProfileId);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    /**
     * Map a ResultSet to a Creator object
     * @param rs ResultSet containing creator data
     * @return Creator object
     * @throws SQLException if a database access error occurs
     */
    private Creator mapResultSetToCreator(ResultSet rs) throws SQLException {
        Creator creator = new Creator();

        // Set User properties
        creator.setId(rs.getInt("u.id"));
        creator.setUsername(rs.getString("u.username"));
        creator.setEmail(rs.getString("u.email"));
        creator.setPassword(rs.getString("u.password"));
        creator.setRole(rs.getString("u.role"));
        creator.setStatus(rs.getString("u.status"));
        creator.setCreatedAt(rs.getTimestamp("u.created_at"));
        creator.setUpdatedAt(rs.getTimestamp("u.updated_at"));

        // Set Creator properties
        creator.setBio(rs.getString("c.bio"));
        creator.setFollowerCount(rs.getInt("c.follower_count"));
        creator.setInstagramLink(rs.getString("c.instagram_link"));
        creator.setTiktokLink(rs.getString("c.tiktok_link"));
        creator.setYoutubeLink(rs.getString("c.youtube_link"));
        creator.setPricingPerPost(rs.getDouble("c.pricing_per_post"));

        return creator;
    }
}
