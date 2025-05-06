package com.konnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.konnect.model.Message;
import com.konnect.util.DBConnection;

/**
 * Data Access Object for Message-related database operations
 */
public class MessageDAO {
    
    /**
     * Send a new message
     * @param message Message object with message details
     * @return Message ID if successful, -1 if failed
     */
    public int sendMessage(Message message) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int messageId = -1;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "INSERT INTO messages (sender_id, receiver_id, content) VALUES (?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, message.getSenderId());
            pstmt.setInt(2, message.getReceiverId());
            pstmt.setString(3, message.getContent());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    messageId = rs.getInt(1);
                    message.setMessageId(messageId);
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
        
        return messageId;
    }
    
    /**
     * Get message by ID
     * @param messageId Message ID
     * @return Message object if found, null otherwise
     */
    public Message getMessageById(int messageId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Message message = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT m.*, u1.username as sender_username, u1.role as sender_role, " +
                         "u2.username as receiver_username, u2.role as receiver_role " +
                         "FROM messages m " +
                         "JOIN users u1 ON m.sender_id = u1.user_id " +
                         "JOIN users u2 ON m.receiver_id = u2.user_id " +
                         "WHERE m.message_id = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, messageId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                message = new Message();
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderId(rs.getInt("sender_id"));
                message.setReceiverId(rs.getInt("receiver_id"));
                message.setContent(rs.getString("content"));
                message.setRead(rs.getBoolean("is_read"));
                message.setCreatedAt(rs.getTimestamp("created_at"));
                message.setSenderUsername(rs.getString("sender_username"));
                message.setReceiverUsername(rs.getString("receiver_username"));
                message.setSenderRole(rs.getString("sender_role"));
                message.setReceiverRole(rs.getString("receiver_role"));
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
        
        return message;
    }
    
    /**
     * Get conversation between two users
     * @param userId1 First user ID
     * @param userId2 Second user ID
     * @return List of messages between the two users
     */
    public List<Message> getConversation(int userId1, int userId2) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT m.*, u1.username as sender_username, u1.role as sender_role, " +
                         "u2.username as receiver_username, u2.role as receiver_role " +
                         "FROM messages m " +
                         "JOIN users u1 ON m.sender_id = u1.user_id " +
                         "JOIN users u2 ON m.receiver_id = u2.user_id " +
                         "WHERE (m.sender_id = ? AND m.receiver_id = ?) OR (m.sender_id = ? AND m.receiver_id = ?) " +
                         "ORDER BY m.created_at ASC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId1);
            pstmt.setInt(2, userId2);
            pstmt.setInt(3, userId2);
            pstmt.setInt(4, userId1);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Message message = new Message();
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderId(rs.getInt("sender_id"));
                message.setReceiverId(rs.getInt("receiver_id"));
                message.setContent(rs.getString("content"));
                message.setRead(rs.getBoolean("is_read"));
                message.setCreatedAt(rs.getTimestamp("created_at"));
                message.setSenderUsername(rs.getString("sender_username"));
                message.setReceiverUsername(rs.getString("receiver_username"));
                message.setSenderRole(rs.getString("sender_role"));
                message.setReceiverRole(rs.getString("receiver_role"));
                
                messages.add(message);
            }
            
            // Mark messages as read
            markMessagesAsRead(userId2, userId1);
            
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
        
        return messages;
    }
    
    /**
     * Get user conversations
     * @param userId User ID
     * @return List of users with whom the user has conversations
     */
    public List<Integer> getUserConversations(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Integer> conversationUsers = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT DISTINCT " +
                         "CASE WHEN sender_id = ? THEN receiver_id ELSE sender_id END AS other_user_id " +
                         "FROM messages " +
                         "WHERE sender_id = ? OR receiver_id = ? " +
                         "ORDER BY other_user_id";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                conversationUsers.add(rs.getInt("other_user_id"));
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
        
        return conversationUsers;
    }
    
    /**
     * Mark messages as read
     * @param senderId Sender ID
     * @param receiverId Receiver ID
     * @return true if successful, false otherwise
     */
    public boolean markMessagesAsRead(int senderId, int receiverId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsUpdated = 0;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "UPDATE messages SET is_read = TRUE " +
                         "WHERE sender_id = ? AND receiver_id = ? AND is_read = FALSE";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, senderId);
            pstmt.setInt(2, receiverId);
            
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
     * Count unread messages
     * @param userId User ID
     * @return Number of unread messages for the user
     */
    public int countUnreadMessages(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT COUNT(*) FROM messages WHERE receiver_id = ? AND is_read = FALSE";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
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
