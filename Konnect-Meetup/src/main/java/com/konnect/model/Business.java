package com.konnect.model;

import java.sql.Timestamp;

/**
 * Business Model Class
 * Represents a business user in the system
 */
public class Business extends User {
    private String companyName;
    private String industry;
    private String description;
    private String website;
    private String contactPhone;

    // Default constructor
    public Business() {
        super();
        setRole("business");
    }

    // Constructor with essential fields
    public Business(String username, String email, String password) {
        super(username, email, password, "business");
    }

    // Constructor with all User fields and Business-specific fields
    public Business(int id, String username, String email, String password, String status,
                   Timestamp createdAt, Timestamp updatedAt, String companyName,
                   String industry, String description, String website, String contactPhone) {
        super(id, username, email, password, "business", status, createdAt, updatedAt, null, null, null, null, true);
        this.companyName = companyName;
        this.industry = industry;
        this.description = description;
        this.website = website;
        this.contactPhone = contactPhone;
    }

    // Constructor with all User fields and Business-specific fields including verification fields
    public Business(int id, String username, String email, String password, String status,
                   Timestamp createdAt, Timestamp updatedAt, String verificationCode,
                   Timestamp verificationExpiry, String resetToken, Timestamp resetTokenExpiry,
                   boolean verified, String companyName, String industry, String description,
                   String website, String contactPhone) {
        super(id, username, email, password, "business", status, createdAt, updatedAt,
              verificationCode, verificationExpiry, resetToken, resetTokenExpiry, verified);
        this.companyName = companyName;
        this.industry = industry;
        this.description = description;
        this.website = website;
        this.contactPhone = contactPhone;
    }

    // Getters and Setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @Override
    public String toString() {
        return "Business{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", companyName='" + companyName + '\'' +
                ", industry='" + industry + '\'' +
                ", description='" + description + '\'' +
                ", website='" + website + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                '}';
    }
}
