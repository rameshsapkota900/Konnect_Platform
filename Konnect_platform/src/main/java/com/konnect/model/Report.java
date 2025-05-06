package com.konnect.model;

import java.sql.Timestamp;

/**
 * Report model class for Konnect platform
 */
public class Report {
    private int reportId;
    private int reporterId;
    private int reportedId;
    private String reason;
    private String description;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Additional fields for joining with other tables
    private String reporterUsername;
    private String reportedUsername;
    private String reporterRole;
    private String reportedRole;

    // Default constructor
    public Report() {
    }

    // Constructor with essential fields
    public Report(int reporterId, int reportedId, String reason, String description) {
        this.reporterId = reporterId;
        this.reportedId = reportedId;
        this.reason = reason;
        this.description = description;
        this.status = "pending";
    }

    // Full constructor
    public Report(int reportId, int reporterId, int reportedId, String reason, String description,
                 String status, Timestamp createdAt, Timestamp updatedAt) {
        this.reportId = reportId;
        this.reporterId = reporterId;
        this.reportedId = reportedId;
        this.reason = reason;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getReporterId() {
        return reporterId;
    }

    public void setReporterId(int reporterId) {
        this.reporterId = reporterId;
    }

    public int getReportedId() {
        return reportedId;
    }

    public void setReportedId(int reportedId) {
        this.reportedId = reportedId;
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

    public String getReporterUsername() {
        return reporterUsername;
    }

    public void setReporterUsername(String reporterUsername) {
        this.reporterUsername = reporterUsername;
    }

    public String getReportedUsername() {
        return reportedUsername;
    }

    public void setReportedUsername(String reportedUsername) {
        this.reportedUsername = reportedUsername;
    }

    public String getReporterRole() {
        return reporterRole;
    }

    public void setReporterRole(String reporterRole) {
        this.reporterRole = reporterRole;
    }

    public String getReportedRole() {
        return reportedRole;
    }

    public void setReportedRole(String reportedRole) {
        this.reportedRole = reportedRole;
    }

    /**
     * Alias for getReportedId() to maintain compatibility with JSP files
     * @return The ID of the reported user
     */
    public int getReportedUserId() {
        return reportedId;
    }

    /**
     * Alias for setReportedId() to maintain compatibility with JSP files
     * @param reportedUserId The ID of the reported user
     */
    public void setReportedUserId(int reportedUserId) {
        this.reportedId = reportedUserId;
    }

    @Override
    public String toString() {
        return "Report [reportId=" + reportId + ", reporterId=" + reporterId +
               ", reportedId=" + reportedId + ", reason=" + reason +
               ", description=" + description + ", status=" + status + "]";
    }
}
