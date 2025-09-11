package com.konnect.model;

import java.sql.Timestamp;

/**
 * Report Model Class
 * Represents a report submitted by a user
 */
public class Report {
    private int id;
    private int reporterId;
    private int reportedUserId;
    private String reason;
    private String description;
    private String status; // "pending", "resolved", "dismissed"
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Default constructor
    public Report() {
    }
    
    // Constructor with essential fields
    public Report(int reporterId, int reportedUserId, String reason, String description) {
        this.reporterId = reporterId;
        this.reportedUserId = reportedUserId;
        this.reason = reason;
        this.description = description;
        this.status = "pending";
    }
    
    // Constructor with all fields
    public Report(int id, int reporterId, int reportedUserId, String reason, 
                 String description, String status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.reporterId = reporterId;
        this.reportedUserId = reportedUserId;
        this.reason = reason;
        this.description = description;
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
    
    public int getReporterId() {
        return reporterId;
    }
    
    public void setReporterId(int reporterId) {
        this.reporterId = reporterId;
    }
    
    public int getReportedUserId() {
        return reportedUserId;
    }
    
    public void setReportedUserId(int reportedUserId) {
        this.reportedUserId = reportedUserId;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
        return "Report{" +
                "id=" + id +
                ", reporterId=" + reporterId +
                ", reportedUserId=" + reportedUserId +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
