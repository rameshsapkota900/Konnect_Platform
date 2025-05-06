package com.konnect.model;

import java.sql.Timestamp;

/**
 * Message model class for Konnect platform
 */
public class Message {
    private int messageId;
    private int senderId;
    private int receiverId;
    private String content;
    private boolean isRead;
    private Timestamp createdAt;
    
    // Additional fields for joining with other tables
    private String senderUsername;
    private String receiverUsername;
    private String senderRole;
    private String receiverRole;
    
    // Default constructor
    public Message() {
    }
    
    // Constructor with essential fields
    public Message(int senderId, int receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.isRead = false;
    }
    
    // Full constructor
    public Message(int messageId, int senderId, int receiverId, String content, 
                  boolean isRead, Timestamp createdAt) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }
    
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
    
    public int getSenderId() {
        return senderId;
    }
    
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    
    public int getReceiverId() {
        return receiverId;
    }
    
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getSenderUsername() {
        return senderUsername;
    }
    
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
    
    public String getReceiverUsername() {
        return receiverUsername;
    }
    
    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }
    
    public String getSenderRole() {
        return senderRole;
    }
    
    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }
    
    public String getReceiverRole() {
        return receiverRole;
    }
    
    public void setReceiverRole(String receiverRole) {
        this.receiverRole = receiverRole;
    }
    
    @Override
    public String toString() {
        return "Message [messageId=" + messageId + ", senderId=" + senderId + 
               ", receiverId=" + receiverId + ", isRead=" + isRead + "]";
    }
}
