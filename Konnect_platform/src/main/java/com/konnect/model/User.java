package com.konnect.model;

import java.sql.Timestamp;

/**
 * User model class for Konnect platform
 */
public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private String salt;
    private String role;
    private String status;
    private boolean verified;
    private String verificationCode;
    private String resetCode;
    private Timestamp resetCodeExpiry;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public User() {
    }

    // Constructor with essential fields
    public User(String username, String email, String password, String salt, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.role = role;
        this.status = "active";
        this.verified = false;
    }

    // Full constructor
    public User(int userId, String username, String email, String password, String salt, String role,
                String status, boolean verified, String verificationCode, String resetCode,
                Timestamp resetCodeExpiry, Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.role = role;
        this.status = status;
        this.verified = verified;
        this.verificationCode = verificationCode;
        this.resetCode = resetCode;
        this.resetCodeExpiry = resetCodeExpiry;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public Timestamp getResetCodeExpiry() {
        return resetCodeExpiry;
    }

    public void setResetCodeExpiry(Timestamp resetCodeExpiry) {
        this.resetCodeExpiry = resetCodeExpiry;
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
        return "User [userId=" + userId + ", username=" + username + ", email=" + email +
               ", role=" + role + ", status=" + status + "]";
    }
}
