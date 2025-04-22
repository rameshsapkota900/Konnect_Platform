package com.konnect.model;

import java.sql.Timestamp;

public class Message {
    private int messageId;
    private int senderUserId;
    private int receiverUserId;
    private Integer campaignId; // Can be null if general chat
    private String content;
    private Timestamp sentAt;
    private boolean read;

    // Join fields (optional)
    private String senderUsername;
    private String receiverUsername;

    // Constructors
    public Message() {}

    // Getters and Setters
    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }
    public int getSenderUserId() { return senderUserId; }
    public void setSenderUserId(int senderUserId) { this.senderUserId = senderUserId; }
    public int getReceiverUserId() { return receiverUserId; }
    public void setReceiverUserId(int receiverUserId) { this.receiverUserId = receiverUserId; }
    public Integer getCampaignId() { return campaignId; }
    public void setCampaignId(Integer campaignId) { this.campaignId = campaignId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Timestamp getSentAt() { return sentAt; }
    public void setSentAt(Timestamp sentAt) { this.sentAt = sentAt; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public String getSenderUsername() { return senderUsername; }
    public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }
    public String getReceiverUsername() { return receiverUsername; }
    public void setReceiverUsername(String receiverUsername) { this.receiverUsername = receiverUsername; }
}
