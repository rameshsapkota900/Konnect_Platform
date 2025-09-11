package com.konnect.dao;

import com.konnect.model.User;
import com.konnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO Class
 * Handles database operations for User objects
 */
public class UserDAO {

    /**
     * Insert a new user into the database
     * @param user User object to insert
     * @return generated user ID if successful, -1 otherwise
     */
    public int insert(User user) {
        String sql = "INSERT INTO users (username, email, password, role, status, verified, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, NOW())";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int userId = -1;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getStatus());
            pstmt.setBoolean(6, user.isVerified());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);
                    user.setId(userId);
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

        return userId;
    }

    /**
     * Get a user by ID
     * @param id User ID
     * @return User object if found, null otherwise
     */
    public User getById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = mapResultSetToUser(rs);
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

        return user;
    }

    /**
     * Get a user by email
     * @param email User email
     * @return User object if found, null otherwise
     */
    public User getByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = mapResultSetToUser(rs);
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

        return user;
    }

    /**
     * Get a user by username
     * @param username Username
     * @return User object if found, null otherwise
     */
    public User getByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = mapResultSetToUser(rs);
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

        return user;
    }

    /**
     * Update a user in the database
     * @param user User object to update
     * @return true if successful, false otherwise
     */
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, " +
                     "role = ?, status = ?, verified = ?, updated_at = NOW() " +
                     "WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getStatus());
            pstmt.setBoolean(6, user.isVerified());
            pstmt.setInt(7, user.getId());

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
     * Get all users
     * @return List of all users
     */
    public List<User> getAll() {
        String sql = "SELECT * FROM users";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                users.add(user);
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

        return users;
    }

    /**
     * Get all users by role
     * @param role User role
     * @return List of users with the specified role
     */
    public List<User> getAllByRole(String role) {
        String sql = "SELECT * FROM users WHERE role = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                users.add(user);
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

        return users;
    }

    /**
     * Get users by list of IDs
     * @param userIds List of user IDs
     * @return List of users with the specified IDs
     */
    public List<User> getByIds(List<Integer> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return new ArrayList<>();
        }

        // Build the SQL query with placeholders for each ID
        StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE id IN (");
        for (int i = 0; i < userIds.size(); i++) {
            sql.append(i > 0 ? ", ?" : "?");
        }
        sql.append(")");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql.toString());

            // Set parameters for each ID
            for (int i = 0; i < userIds.size(); i++) {
                pstmt.setInt(i + 1, userIds.get(i));
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                users.add(user);
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

        return users;
    }

    /**
     * Map a ResultSet to a User object
     * @param rs ResultSet containing user data
     * @return User object
     * @throws SQLException if a database access error occurs
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Set verified field - this column exists in our current schema
        user.setVerified(rs.getBoolean("verified"));

        // Note: verification_code, verification_expiry, reset_token, reset_token_expiry 
        // columns don't exist in the current database schema, so we're not trying to access them

        return user;
    }

    /**
     * Get a user by verification code
     * Note: verification_code column doesn't exist in current database schema
     * @param code Verification code
     * @return User object if found, null otherwise
     */
    public User getByVerificationCode(String code) {
        // Since verification_code column doesn't exist in current database schema,
        // this method always returns null
        return null;
    }

    /**
     * Get a user by reset token
     * @param token Reset token
     * @return User object if found, null otherwise
     */
    public User getByResetToken(String token) {
        String sql = "SELECT * FROM users WHERE reset_token = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, token);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = mapResultSetToUser(rs);
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

        return user;
    }

    /**
     * Update user verification status
     * @param userId User ID
     * @param verified Verification status
     * @return true if successful, false otherwise
     */
    public boolean updateVerificationStatus(int userId, boolean verified) {
        String sql = "UPDATE users SET verified = ?, status = ?, updated_at = NOW() WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setBoolean(1, verified);
            pstmt.setString(2, verified ? "active" : "pending");
            pstmt.setInt(3, userId);

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
     * Set password reset token for a user
     * @param userId User ID
     * @param resetToken Reset token
     * @param resetTokenExpiry Reset token expiry
     * @return true if successful, false otherwise
     */
    public boolean setPasswordResetToken(int userId, String resetToken, Timestamp resetTokenExpiry) {
        String sql = "UPDATE users SET reset_token = ?, reset_token_expiry = ?, updated_at = NOW() WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, resetToken);
            pstmt.setTimestamp(2, resetTokenExpiry);
            pstmt.setInt(3, userId);

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
     * Update user password
     * @param userId User ID
     * @param newPassword New password (already hashed)
     * @return true if successful, false otherwise
     */
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ?, reset_token = NULL, reset_token_expiry = NULL, updated_at = NOW() WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);

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
     * Authenticate a user with email and password
     * @param email User email
     * @param password User password (plain text)
     * @return User object if authentication successful, null otherwise
     */
    public User authenticate(String email, String password) {
        User user = getByEmail(email);

        if (user != null && com.konnect.util.PasswordUtil.verifyPassword(password, user.getPassword())) {
            // Check if user is verified and active
            if (user.isVerified() && "active".equals(user.getStatus())) {
                return user;
            }
        }

        return null;
    }
    
    /**
     * Update a user's password
     * @param userId User ID
     * @param newPassword New hashed password
     * @return true if update successful, false otherwise
     */
    public boolean updateUserPassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ?, updated_at = NOW() WHERE id = ?";
        
        try (Connection conn = com.konnect.config.DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Get total count of users
     * @return Total number of users
     */
    public int getTotalUserCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
        
        try (Connection conn = com.konnect.config.DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    
    /**
     * Get count of users with legacy SHA-256 passwords
     * @return Number of users with legacy passwords
     */
    public int getLegacyPasswordCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE password NOT LIKE '$2a$%'";
        
        try (Connection conn = com.konnect.config.DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
}
