package com.konnect.dao;

import com.konnect.model.Message;
import com.konnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MessageDAO Class
 * Handles database operations for Message objects
 */
public class MessageDAO {
    
    /**
     * Insert a new message into the database
     * @param message Message object to insert
     * @return generated message ID if successful, -1 otherwise
     */
    public int insert(Message message) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, content, is_read) " +
                     "VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int messageId = -1;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, message.getSenderId());
            pstmt.setInt(2, message.getReceiverId());
            pstmt.setString(3, message.getContent());
            pstmt.setBoolean(4, message.isRead());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    messageId = rs.getInt(1);
                    message.setId(messageId);
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
        
        return messageId;
    }
    
    /**
     * Get a message by ID
     * @param id Message ID
     * @return Message object if found, null otherwise
     */
    public Message getById(int id) {
        String sql = "SELECT * FROM messages WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Message message = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                message = mapResultSetToMessage(rs);
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
        
        return message;
    }
    
    /**
     * Get messages between two users
     * @param user1Id First user ID
     * @param user2Id Second user ID
     * @return List of messages between the users
     */
    public List<Message> getConversation(int user1Id, int user2Id) {
        String sql = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) " +
                     "OR (sender_id = ? AND receiver_id = ?) ORDER BY created_at ASC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, user1Id);
            pstmt.setInt(2, user2Id);
            pstmt.setInt(3, user2Id);
            pstmt.setInt(4, user1Id);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Message message = mapResultSetToMessage(rs);
                messages.add(message);
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
        
        return messages;
    }
    
    /**
     * Get all conversations for a user
     * @param userId User ID
     * @return List of unique user IDs the user has conversations with
     */
    public List<Integer> getUserConversations(int userId) {
        String sql = "SELECT DISTINCT " +
                     "CASE WHEN sender_id = ? THEN receiver_id ELSE sender_id END AS other_user_id " +
                     "FROM messages WHERE sender_id = ? OR receiver_id = ? " +
                     "ORDER BY other_user_id";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Integer> conversationUserIds = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                conversationUserIds.add(rs.getInt("other_user_id"));
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
        
        return conversationUserIds;
    }
    
    /**
     * Get unread messages count for a user
     * @param userId User ID
     * @return Number of unread messages
     */
    public int getUnreadCount(int userId) {
        String sql = "SELECT COUNT(*) FROM messages WHERE receiver_id = ? AND is_read = FALSE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int unreadCount = 0;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                unreadCount = rs.getInt(1);
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
        
        return unreadCount;
    }
    
    /**
     * Mark messages as read
     * @param senderId Sender ID
     * @param receiverId Receiver ID
     * @return true if successful, false otherwise
     */
    public boolean markAsRead(int senderId, int receiverId) {
        String sql = "UPDATE messages SET is_read = TRUE " +
                     "WHERE sender_id = ? AND receiver_id = ? AND is_read = FALSE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, senderId);
            pstmt.setInt(2, receiverId);
            
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
     * Map a ResultSet to a Message object
     * @param rs ResultSet containing message data
     * @return Message object
     * @throws SQLException if a database access error occurs
     */
    private Message mapResultSetToMessage(ResultSet rs) throws SQLException {
        Message message = new Message();
        
        message.setId(rs.getInt("id"));
        message.setSenderId(rs.getInt("sender_id"));
        message.setReceiverId(rs.getInt("receiver_id"));
        message.setContent(rs.getString("content"));
        message.setRead(rs.getBoolean("is_read"));
        message.setCreatedAt(rs.getTimestamp("created_at"));
        
        return message;
    }
}
