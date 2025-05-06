package com.konnect.model;

import java.sql.Timestamp;

/**
 * BusinessProfile model class for Konnect platform
 */
public class BusinessProfile {
    private int profileId;
    private int userId;
    private String businessName;
    private String businessDescription;
    private String website;
    private String industry;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields for joining with user table
    private String username;
    private String email;
    private String status;
    
    // Default constructor
    public BusinessProfile() {
    }
    
    // Constructor with essential fields
    public BusinessProfile(int userId, String businessName, String businessDescription, String industry) {
        this.userId = userId;
        this.businessName = businessName;
        this.businessDescription = businessDescription;
        this.industry = industry;
    }
    
    // Full constructor
    public BusinessProfile(int profileId, int userId, String businessName, String businessDescription,
                          String website, String industry, Timestamp createdAt, Timestamp updatedAt) {
        this.profileId = profileId;
        this.userId = userId;
        this.businessName = businessName;
        this.businessDescription = businessDescription;
        this.website = website;
        this.industry = industry;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getProfileId() {
        return profileId;
    }
    
    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getBusinessName() {
        return businessName;
    }
    
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    
    public String getBusinessDescription() {
        return businessDescription;
    }
    
    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
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
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "BusinessProfile [profileId=" + profileId + ", userId=" + userId + 
               ", businessName=" + businessName + ", industry=" + industry + "]";
    }
}
