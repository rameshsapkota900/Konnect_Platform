package com.konnect.model;

import java.sql.Timestamp;

/**
 * User Model Class
 * Base class for all user types in the system
 */
public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String role; // "admin", "creator", or "business"
    private String status; // "active", "pending", "banned", etc.
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String verificationCode;
    private Timestamp verificationExpiry;
    private String resetToken;
    private Timestamp resetTokenExpiry;
    private boolean verified;

    // Default constructor
    public User() {
    }

    // Constructor with essential fields
    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = "pending"; // Changed from "active" to "pending" for email verification
        this.verified = false;
    }

    // Constructor with all fields
    public User(int id, String username, String email, String password, String role,
                String status, Timestamp createdAt, Timestamp updatedAt, String verificationCode,
                Timestamp verificationExpiry, String resetToken, Timestamp resetTokenExpiry,
                boolean verified) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.verificationCode = verificationCode;
        this.verificationExpiry = verificationExpiry;
        this.resetToken = resetToken;
        this.resetTokenExpiry = resetTokenExpiry;
        this.verified = verified;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Timestamp getVerificationExpiry() {
        return verificationExpiry;
    }

    public void setVerificationExpiry(Timestamp verificationExpiry) {
        this.verificationExpiry = verificationExpiry;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Timestamp getResetTokenExpiry() {
        return resetTokenExpiry;
    }

    public void setResetTokenExpiry(Timestamp resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", verified=" + verified +
                '}';
    }
}
