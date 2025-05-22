package com.konnect.model;

import java.sql.Timestamp;

/**
 * Application Model Class
 * Represents an application from a creator to a campaign
 */
public class Application {
    private int id;
    private int campaignId;
    private int creatorId;
    private String message;
    private String status; // "pending", "approved", "rejected", "completed"
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Default constructor
    public Application() {
    }
    
    // Constructor with essential fields
    public Application(int campaignId, int creatorId, String message) {
        this.campaignId = campaignId;
        this.creatorId = creatorId;
        this.message = message;
        this.status = "pending";
    }
    
    // Constructor with all fields
    public Application(int id, int campaignId, int creatorId, String message, 
                      String status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.campaignId = campaignId;
        this.creatorId = creatorId;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getCampaignId() {
        return campaignId;
    }
    
    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }
    
    public int getCreatorId() {
        return creatorId;
    }
    
    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", campaignId=" + campaignId +
                ", creatorId=" + creatorId +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
