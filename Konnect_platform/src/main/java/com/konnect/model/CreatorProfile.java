package com.konnect.model;

import java.sql.Timestamp;

/**
 * CreatorProfile model class for Konnect platform
 */
public class CreatorProfile {
    private int profileId;
    private int userId;
    private String fullName;
    private String bio;
    private String instagramLink;
    private String youtubeLink;
    private String tiktokLink;
    private int instagramFollowers;
    private int youtubeFollowers;
    private int tiktokFollowers;
    private double postPrice;
    private double storyPrice;
    private double videoPrice;
    private String interests;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields for joining with user table
    private String username;
    private String email;
    private String status;
    
    // Default constructor
    public CreatorProfile() {
    }
    
    // Constructor with essential fields
    public CreatorProfile(int userId, String fullName, String bio) {
        this.userId = userId;
        this.fullName = fullName;
        this.bio = bio;
    }
    
    // Full constructor
    public CreatorProfile(int profileId, int userId, String fullName, String bio, String instagramLink,
                         String youtubeLink, String tiktokLink, int instagramFollowers, int youtubeFollowers,
                         int tiktokFollowers, double postPrice, double storyPrice, double videoPrice,
                         String interests, Timestamp createdAt, Timestamp updatedAt) {
        this.profileId = profileId;
        this.userId = userId;
        this.fullName = fullName;
        this.bio = bio;
        this.instagramLink = instagramLink;
        this.youtubeLink = youtubeLink;
        this.tiktokLink = tiktokLink;
        this.instagramFollowers = instagramFollowers;
        this.youtubeFollowers = youtubeFollowers;
        this.tiktokFollowers = tiktokFollowers;
        this.postPrice = postPrice;
        this.storyPrice = storyPrice;
        this.videoPrice = videoPrice;
        this.interests = interests;
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
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getInstagramLink() {
        return instagramLink;
    }
    
    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }
    
    public String getYoutubeLink() {
        return youtubeLink;
    }
    
    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }
    
    public String getTiktokLink() {
        return tiktokLink;
    }
    
    public void setTiktokLink(String tiktokLink) {
        this.tiktokLink = tiktokLink;
    }
    
    public int getInstagramFollowers() {
        return instagramFollowers;
    }
    
    public void setInstagramFollowers(int instagramFollowers) {
        this.instagramFollowers = instagramFollowers;
    }
    
    public int getYoutubeFollowers() {
        return youtubeFollowers;
    }
    
    public void setYoutubeFollowers(int youtubeFollowers) {
        this.youtubeFollowers = youtubeFollowers;
    }
    
    public int getTiktokFollowers() {
        return tiktokFollowers;
    }
    
    public void setTiktokFollowers(int tiktokFollowers) {
        this.tiktokFollowers = tiktokFollowers;
    }
    
    public double getPostPrice() {
        return postPrice;
    }
    
    public void setPostPrice(double postPrice) {
        this.postPrice = postPrice;
    }
    
    public double getStoryPrice() {
        return storyPrice;
    }
    
    public void setStoryPrice(double storyPrice) {
        this.storyPrice = storyPrice;
    }
    
    public double getVideoPrice() {
        return videoPrice;
    }
    
    public void setVideoPrice(double videoPrice) {
        this.videoPrice = videoPrice;
    }
    
    public String getInterests() {
        return interests;
    }
    
    public void setInterests(String interests) {
        this.interests = interests;
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
    
    // Helper methods
    public int getTotalFollowers() {
        return instagramFollowers + youtubeFollowers + tiktokFollowers;
    }
    
    @Override
    public String toString() {
        return "CreatorProfile [profileId=" + profileId + ", userId=" + userId + 
               ", fullName=" + fullName + ", totalFollowers=" + getTotalFollowers() + "]";
    }
}
