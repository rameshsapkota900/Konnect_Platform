package com.konnect.dao;

import com.konnect.config.DatabaseConnection;
import com.konnect.model.Message;
import com.konnect.model.User; // Need User for conversation partners

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDao {

    public boolean sendMessage(Message message) {
        String sql = "INSERT INTO messages (sender_user_id, receiver_user_id, campaign_id, content, is_read) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, message.getSenderUserId());
            stmt.setInt(2, message.getReceiverUserId());
            if (message.getCampaignId() != null) {
                stmt.setInt(3, message.getCampaignId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, message.getContent());
            stmt.setBoolean(5, false); // New messages are unread

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error sending message: " + e.getMessage());
            return false;
        }
    }

    // Get all messages between two users (a specific conversation thread)
    public List<Message> getConversation(int userId1, int userId2) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT m.*, us.username as senderUsername, ur.username as receiverUsername " +
                     "FROM messages m " +
                     "JOIN users us ON m.sender_user_id = us.user_id " +
                     "JOIN users ur ON m.receiver_user_id = ur.user_id " +
                     "WHERE (m.sender_user_id = ? AND m.receiver_user_id = ?) " +
                     "   OR (m.sender_user_id = ? AND m.receiver_user_id = ?) " +
                     "ORDER BY m.sent_at ASC"; // Show oldest first for conversation flow

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId1);
            stmt.setInt(2, userId2);
            stmt.setInt(3, userId2);
            stmt.setInt(4, userId1);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }

            // Optionally mark messages as read for the user fetching them
            // markMessagesAsRead(userId2, userId1); // If userId2 is the one viewing

        } catch (SQLException e) {
            System.err.println("Error getting conversation: " + e.getMessage());
        }
        return messages;
    }

    // Get a list of users the current user has had conversations with
    public List<User> getConversationPartners(int currentUserId) {
        Map<Integer, User> partners = new HashMap<>(); // Use Map to avoid duplicates
        // Find users who sent messages TO the current user OR received messages FROM the current user
        String sql = "SELECT DISTINCT u.* " +
                     "FROM users u " +
                     "JOIN messages m ON (u.user_id = m.sender_user_id AND m.receiver_user_id = ?) " +
                     "                 OR (u.user_id = m.receiver_user_id AND m.sender_user_id = ?) " +
                     "WHERE u.user_id != ? AND u.is_active = TRUE"; // Exclude self, only active users

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentUserId);
            stmt.setInt(2, currentUserId);
            stmt.setInt(3, currentUserId);

            ResultSet rs = stmt.executeQuery();
            UserDao userDao = new UserDao(); // Use UserDao's mapper
            while (rs.next()) {
                 User partner = userDao.mapResultSetToUser(rs); // Reuse User mapping logic
                 if (!partners.containsKey(partner.getUserId())) {
                     partners.put(partner.getUserId(), partner);
                 }
            }
        } catch (SQLException e) {
            System.err.println("Error getting conversation partners: " + e.getMessage());
        }
        return new ArrayList<>(partners.values());
    }


    // Mark messages as read (call this when a user opens a conversation)
    public boolean markMessagesAsRead(int receiverUserId, int senderUserId) {
        String sql = "UPDATE messages SET is_read = TRUE WHERE receiver_user_id = ? AND sender_user_id = ? AND is_read = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, receiverUserId);
            stmt.setInt(2, senderUserId);

            stmt.executeUpdate(); // Don't necessarily need to check affected rows
            return true;
        } catch (SQLException e) {
            System.err.println("Error marking messages as read: " + e.getMessage());
            return false;
        }
    }

     // Count unread messages for a user
     public int countUnreadMessages(int userId) {
        String sql = "SELECT COUNT(*) FROM messages WHERE receiver_user_id = ? AND is_read = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting unread messages: " + e.getMessage());
        }
        return 0;
     }


    private Message mapResultSetToMessage(ResultSet rs) throws SQLException {
        Message msg = new Message();
        msg.setMessageId(rs.getInt("message_id"));
        msg.setSenderUserId(rs.getInt("sender_user_id"));
        msg.setReceiverUserId(rs.getInt("receiver_user_id"));
        msg.setCampaignId(rs.getObject("campaign_id", Integer.class)); // Handle potential NULL
        msg.setContent(rs.getString("content"));
        msg.setSentAt(rs.getTimestamp("sent_at"));
        msg.setRead(rs.getBoolean("is_read"));

        // Map joined fields if present
        if (hasColumn(rs, "senderUsername")) {
            msg.setSenderUsername(rs.getString("senderUsername"));
        }
        if (hasColumn(rs, "receiverUsername")) {
            msg.setReceiverUsername(rs.getString("receiverUsername"));
        }

        return msg;
    }

    // Helper to check if a column exists
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
