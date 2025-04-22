package com.konnect.model;

import java.sql.Timestamp;

public class Application {
    private int applicationId;
    private int campaignId;
    private int creatorUserId;
    private String status; // "pending", "accepted", "rejected", "invited"
    private String message;
    private Timestamp appliedAt;

    // Join fields (optional, useful for display)
    private String campaignTitle;
    private String creatorUsername;
    private String businessUsername; // User who owns the campaign
    private int businessUserId; // <<--- ADD THIS FIELD ---

    // Constructors
    public Application() {}

    // Getters and Setters
    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }
    public int getCampaignId() { return campaignId; }
    public void setCampaignId(int campaignId) { this.campaignId = campaignId; }
    public int getCreatorUserId() { return creatorUserId; }
    public void setCreatorUserId(int creatorUserId) { this.creatorUserId = creatorUserId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Timestamp getAppliedAt() { return appliedAt; }
    public void setAppliedAt(Timestamp appliedAt) { this.appliedAt = appliedAt; }

    public String getCampaignTitle() { return campaignTitle; }
    public void setCampaignTitle(String campaignTitle) { this.campaignTitle = campaignTitle; }
    public String getCreatorUsername() { return creatorUsername; }
    public void setCreatorUsername(String creatorUsername) { this.creatorUsername = creatorUsername; }
    public String getBusinessUsername() { return businessUsername; }
    public void setBusinessUsername(String businessUsername) { this.businessUsername = businessUsername; }

    // --- ADD GETTER AND SETTER for businessUserId ---
    public int getBusinessUserId() { return businessUserId; }
    public void setBusinessUserId(int businessUserId) { this.businessUserId = businessUserId; }
    // --- END ADDITION ---
}
