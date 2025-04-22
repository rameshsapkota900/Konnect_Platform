package com.konnect.model;

import java.sql.Timestamp;

public class Report {
    private int reportId;
    private int reporterUserId;
    private int reportedUserId;
    private String reason;
    private String details;
    private Timestamp reportedAt;
    private String status; // "new", "resolved"

    // Join fields (optional)
    private String reporterUsername;
    private String reportedUsername;

    // Constructors
    public Report() {}

    // Getters and Setters
    public int getReportId() { return reportId; }
    public void setReportId(int reportId) { this.reportId = reportId; }
    public int getReporterUserId() { return reporterUserId; }
    public void setReporterUserId(int reporterUserId) { this.reporterUserId = reporterUserId; }
    public int getReportedUserId() { return reportedUserId; }
    public void setReportedUserId(int reportedUserId) { this.reportedUserId = reportedUserId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public Timestamp getReportedAt() { return reportedAt; }
    public void setReportedAt(Timestamp reportedAt) { this.reportedAt = reportedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReporterUsername() { return reporterUsername; }
    public void setReporterUsername(String reporterUsername) { this.reporterUsername = reporterUsername; }
    public String getReportedUsername() { return reportedUsername; }
    public void setReportedUsername(String reportedUsername) { this.reportedUsername = reportedUsername; }
}
