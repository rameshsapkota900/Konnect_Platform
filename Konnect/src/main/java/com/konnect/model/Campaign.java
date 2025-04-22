package com.konnect.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Campaign {
    private int campaignId;
    private int businessUserId;
    private String title;
    private String description;
    private String requirements;
    private BigDecimal budget;
    private String productImagePath; // Relative path
    private String status; // "active", "completed", "archived"
    private Timestamp createdAt;

    // Join field (optional)
    private String businessUsername;

    // Constructors
    public Campaign() {}

    // Getters and Setters
    public int getCampaignId() { return campaignId; }
    public void setCampaignId(int campaignId) { this.campaignId = campaignId; }
    public int getBusinessUserId() { return businessUserId; }
    public void setBusinessUserId(int businessUserId) { this.businessUserId = businessUserId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public String getProductImagePath() { return productImagePath; }
    public void setProductImagePath(String productImagePath) { this.productImagePath = productImagePath; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getBusinessUsername() { return businessUsername; }
    public void setBusinessUsername(String businessUsername) { this.businessUsername = businessUsername; }
}
