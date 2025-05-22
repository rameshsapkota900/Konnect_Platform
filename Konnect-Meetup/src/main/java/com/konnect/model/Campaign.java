package com.konnect.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Campaign Model Class
 * Represents a marketing campaign created by a business
 */
public class Campaign {
    private int id;
    private int businessId;
    private String title;
    private String description;
    private double budget;
    private Date startDate;
    private Date endDate;
    private String requirements;
    private String status; // "active", "completed", "cancelled"
    private List<String> targetInterests;
    private int minFollowers;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Default constructor
    public Campaign() {
    }
    
    // Constructor with essential fields
    public Campaign(int businessId, String title, String description, double budget, 
                   Date startDate, Date endDate, String requirements) {
        this.businessId = businessId;
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requirements = requirements;
        this.status = "active";
    }
    
    // Constructor with all fields
    public Campaign(int id, int businessId, String title, String description, double budget, 
                   Date startDate, Date endDate, String requirements, String status, 
                   List<String> targetInterests, int minFollowers, Timestamp createdAt, 
                   Timestamp updatedAt) {
        this.id = id;
        this.businessId = businessId;
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requirements = requirements;
        this.status = status;
        this.targetInterests = targetInterests;
        this.minFollowers = minFollowers;
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
    
    public int getBusinessId() {
        return businessId;
    }
    
    public void setBusinessId(int businessId) {
        this.businessId = businessId;
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
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public String getRequirements() {
        return requirements;
    }
    
    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<String> getTargetInterests() {
        return targetInterests;
    }
    
    public void setTargetInterests(List<String> targetInterests) {
        this.targetInterests = targetInterests;
    }
    
    public int getMinFollowers() {
        return minFollowers;
    }
    
    public void setMinFollowers(int minFollowers) {
        this.minFollowers = minFollowers;
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
        return "Campaign{" +
                "id=" + id +
                ", businessId=" + businessId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", budget=" + budget +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                '}';
    }
}
