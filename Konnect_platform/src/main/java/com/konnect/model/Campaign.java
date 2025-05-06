package com.konnect.model;

import java.sql.Timestamp;

/**
 * Campaign model class for Konnect platform
 */
public class Campaign {
    private int campaignId;
    private int businessId;
    private String businessUsername; // Added for displaying business username
    private String title;
    private String description;
    private double budget;
    private String goals;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public Campaign() {
    }

    // Constructor with essential fields
    public Campaign(int businessId, String title, String description, double budget, String goals) {
        this.businessId = businessId;
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.goals = goals;
        this.status = "active";
    }

    // Full constructor
    public Campaign(int campaignId, int businessId, String title, String description, double budget,
                   String goals, String status, Timestamp createdAt, Timestamp updatedAt) {
        this.campaignId = campaignId;
        this.businessId = businessId;
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.goals = goals;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getBusinessUsername() {
        return businessUsername;
    }

    public void setBusinessUsername(String businessUsername) {
        this.businessUsername = businessUsername;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
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
        return "Campaign [campaignId=" + campaignId + ", businessId=" + businessId +
               ", businessUsername=" + businessUsername + ", title=" + title +
               ", budget=" + budget + ", status=" + status + "]";
    }
}
