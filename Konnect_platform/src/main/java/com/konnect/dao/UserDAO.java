package com.konnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.konnect.model.User;
import com.konnect.util.DBConnection;
import com.konnect.util.PasswordUtil;

/**
 * Data Access Object for User-related database operations
 */
public class UserDAO {

    /**
     * Register a new user
     * @param user User object with registration details
     * @return User ID if successful, -1 if failed
     */
    public int registerUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int userId = -1;

        try {
            conn = DBConnection.getConnection();
            // Generate verification code
            String verificationCode = generateRandomCode();

            String sql = "INSERT INTO users (username, email, password, salt, role, verification_code) VALUES (?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword()); // Already encrypted
            pstmt.setString(4, user.getSalt());
            pstmt.setString(5, user.getRole());
            pstmt.setString(6, verificationCode);

            // Set verification code in user object
            user.setVerificationCode(verificationCode);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);
                    user.setUserId(userId);
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

        return userId;
    }

    /**
     * Authenticate user login
     * @param username Username or email
     * @param password Password
     * @return User object if authentication successful, null otherwise
     */
    public User login(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        // Special case for admin login
        if ("admin".equals(username) && "admin123".equals(password)) {
            try {
                conn = DBConnection.getConnection();
                String adminSql = "SELECT * FROM users WHERE username = 'admin' AND role = 'admin'";
                pstmt = conn.prepareStatement(adminSql);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setSalt(rs.getString("salt"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setVerified(rs.getBoolean("verified"));
                    user.setVerificationCode(rs.getString("verification_code"));
                    user.setResetCode(rs.getString("reset_code"));
                    user.setResetCodeExpiry(rs.getTimestamp("reset_code_expiry"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return user;
                } else {
                    // If admin user doesn't exist in the database, create it
                    String insertSql = "INSERT INTO users (username, email, password, salt, role, status, verified) " +
                                      "VALUES ('admin', 'admin@konnect.com', ?, '', 'admin', 'active', TRUE)";
                    pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    pstmt.setString(1, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"); // SHA-256 hash of "admin123"

                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        rs = pstmt.getGeneratedKeys();
                        if (rs.next()) {
                            user = new User();
                            user.setUserId(rs.getInt(1));
                            user.setUsername("admin");
                            user.setEmail("admin@konnect.com");
                            user.setPassword("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
                            user.setSalt("");
                            user.setRole("admin");
                            user.setStatus("active");
                            user.setVerified(true);
                            return user;
                        }
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

            // If we reach here, we couldn't find or create the admin user in the database
            // Create a temporary admin user object to allow login
            user = new User();
            user.setUserId(1);
            user.setUsername("admin");
            user.setEmail("admin@konnect.com");
            user.setPassword("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"); // Store hashed password
            user.setSalt("");
            user.setRole("admin");
            user.setStatus("active");
            user.setVerified(true);
            return user;
        }

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND status = 'active' AND verified = TRUE";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, username); // Allow login with email too

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Get the stored password and salt
                String storedPassword = rs.getString("password");
                String salt = rs.getString("salt");

                // Verify the password
                if (PasswordUtil.verifyPassword(password, storedPassword, salt)) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(storedPassword); // Store encrypted password
                    user.setSalt(salt);
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setVerified(rs.getBoolean("verified"));
                    user.setVerificationCode(rs.getString("verification_code"));
                    user.setResetCode(rs.getString("reset_code"));
                    user.setResetCodeExpiry(rs.getTimestamp("reset_code_expiry"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
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

        return user;
    }

    /**
     * Check if username already exists
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    public boolean isUsernameExists(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
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

        return exists;
    }

    /**
     * Check if email already exists
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    public boolean isEmailExists(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
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

        return exists;
    }

    /**
     * Get user by ID
     * @param userId User ID
     * @return User object if found, null otherwise
     */
    public User getUserById(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE user_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setSalt(rs.getString("salt"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setVerified(rs.getBoolean("verified"));
                user.setVerificationCode(rs.getString("verification_code"));
                user.setResetCode(rs.getString("reset_code"));
                user.setResetCodeExpiry(rs.getTimestamp("reset_code_expiry"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
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

        return user;
    }

    /**
     * Get user by email
     * @param email User email
     * @return User object if found, null otherwise
     */
    public User getUserByEmail(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE email = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setSalt(rs.getString("salt"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setVerified(rs.getBoolean("verified"));
                user.setVerificationCode(rs.getString("verification_code"));
                user.setResetCode(rs.getString("reset_code"));
                user.setResetCodeExpiry(rs.getTimestamp("reset_code_expiry"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
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

        return user;
    }

    /**
     * Verify user email with verification code
     * @param email User email
     * @param verificationCode Verification code
     * @return true if verification successful, false otherwise
     */
    public boolean verifyEmail(String email, String verificationCode) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsUpdated = 0;

        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE users SET verified = TRUE WHERE email = ? AND verification_code = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, verificationCode);

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
     * Generate and store password reset code
     * @param email User email
     * @return Reset code if successful, null otherwise
     */
    public String generateResetCode(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsUpdated = 0;
        String resetCode = null;

        try {
            conn = DBConnection.getConnection();

            // Generate reset code
            resetCode = generateRandomCode();

            // Set expiry time (24 hours from now)
            Timestamp expiryTime = new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

            String sql = "UPDATE users SET reset_code = ?, reset_code_expiry = ? WHERE email = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, resetCode);
            pstmt.setTimestamp(2, expiryTime);
            pstmt.setString(3, email);

            rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated == 0) {
                resetCode = null; // Email not found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resetCode = null;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resetCode;
    }

    /**
     * Reset password using reset code
     * @param email User email
     * @param resetCode Reset code
     * @param newPassword New password (already encrypted)
     * @param newSalt New salt
     * @return true if reset successful, false otherwise
     */
    public boolean resetPassword(String email, String resetCode, String newPassword, String newSalt) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int rowsUpdated = 0;

        try {
            conn = DBConnection.getConnection();

            // First check if reset code is valid and not expired
            String checkSql = "SELECT * FROM users WHERE email = ? AND reset_code = ? AND reset_code_expiry > ?";

            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, email);
            pstmt.setString(2, resetCode);
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Reset code is valid, update password
                rs.close();
                pstmt.close();

                String updateSql = "UPDATE users SET password = ?, salt = ?, reset_code = NULL, reset_code_expiry = NULL WHERE email = ?";

                pstmt = conn.prepareStatement(updateSql);
                pstmt.setString(1, newPassword);
                pstmt.setString(2, newSalt);
                pstmt.setString(3, email);

                rowsUpdated = pstmt.executeUpdate();
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

        return rowsUpdated > 0;
    }

    /**
     * Generate a random 6-digit code
     * @return Random 6-digit code
     */
    private String generateRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6-digit code
        return String.valueOf(code);
    }

    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users ORDER BY created_at DESC";

            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setSalt(rs.getString("salt"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setVerified(rs.getBoolean("verified"));
                user.setVerificationCode(rs.getString("verification_code"));
                user.setResetCode(rs.getString("reset_code"));
                user.setResetCodeExpiry(rs.getTimestamp("reset_code_expiry"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));

                users.add(user);
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

        return users;
    }

    /**
     * Get users by role
     * @param role User role
     * @return List of users with the given role
     */
    public List<User> getUsersByRole(String role) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE role = ? ORDER BY created_at DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setSalt(rs.getString("salt"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setVerified(rs.getBoolean("verified"));
                user.setVerificationCode(rs.getString("verification_code"));
                user.setResetCode(rs.getString("reset_code"));
                user.setResetCodeExpiry(rs.getTimestamp("reset_code_expiry"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));

                users.add(user);
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

        return users;
    }

    /**
     * Get users by status
     * @param status User status
     * @return List of users with the given status
     */
    public List<User> getUsersByStatus(String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE status = ? ORDER BY created_at DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setSalt(rs.getString("salt"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setVerified(rs.getBoolean("verified"));
                user.setVerificationCode(rs.getString("verification_code"));
                user.setResetCode(rs.getString("reset_code"));
                user.setResetCodeExpiry(rs.getTimestamp("reset_code_expiry"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));

                users.add(user);
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

        return users;
    }

    /**
     * Get users by role and status
     * @param role User role
     * @param status User status
     * @return List of users with the given role and status
     */
    public List<User> getUsersByRoleAndStatus(String role, String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE role = ? AND status = ? ORDER BY created_at DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role);
            pstmt.setString(2, status);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setSalt(rs.getString("salt"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setVerified(rs.getBoolean("verified"));
                user.setVerificationCode(rs.getString("verification_code"));
                user.setResetCode(rs.getString("reset_code"));
                user.setResetCodeExpiry(rs.getTimestamp("reset_code_expiry"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));

                users.add(user);
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

        return users;
    }

    /**
     * Update user status (ban/unban)
     * @param userId User ID
     * @param status New status (active/banned)
     * @return true if successful, false otherwise
     */
    public boolean updateUserStatus(int userId, String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsUpdated = 0;

        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE users SET status = ? WHERE user_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, userId);

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
     * Count total users
     * @return Total number of users
     */
    public int countTotalUsers() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM users";

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
     * Count users by role
     * @param role User role
     * @return Number of users with the given role
     */
    public int countUsersByRole(String role) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM users WHERE role = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role);

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
     * Count users by status
     * @param status User status
     * @return Number of users with the given status
     */
    public int countUsersByStatus(String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM users WHERE status = ?";

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
