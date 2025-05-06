package com.konnect.model;

import java.sql.Timestamp;

/**
 * Application model class for Konnect platform
 */
public class Application {
    private int applicationId;
    private int campaignId;
    private int creatorId;
    private String message;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Additional fields for joining with other tables
    private String campaignTitle;
    private String creatorUsername;
    private String businessUsername;
    private int businessId;

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

    // Full constructor
    public Application(int applicationId, int campaignId, int creatorId, String message,
                      String status, Timestamp createdAt, Timestamp updatedAt) {
        this.applicationId = applicationId;
        this.campaignId = campaignId;
        this.creatorId = creatorId;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
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

    public String getCampaignTitle() {
        return campaignTitle;
    }

    public void setCampaignTitle(String campaignTitle) {
        this.campaignTitle = campaignTitle;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public String getBusinessUsername() {
        return businessUsername;
    }

    public void setBusinessUsername(String businessUsername) {
        this.businessUsername = businessUsername;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    @Override
    public String toString() {
        return "Application [applicationId=" + applicationId + ", campaignId=" + campaignId +
               ", creatorId=" + creatorId + ", businessId=" + businessId +
               ", status=" + status + "]";
    }
}
