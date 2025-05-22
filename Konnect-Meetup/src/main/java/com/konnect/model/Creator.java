package com.konnect.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * Creator Model Class
 * Represents a content creator user in the system
 */
public class Creator extends User {
    private String bio;
    private int followerCount;
    private String instagramLink;
    private String tiktokLink;
    private String youtubeLink;
    private double pricingPerPost;
    private List<String> interests;

    // Default constructor
    public Creator() {
        super();
        setRole("creator");
    }

    // Constructor with essential fields
    public Creator(String username, String email, String password) {
        super(username, email, password, "creator");
    }

    // Constructor with all User fields and Creator-specific fields
    public Creator(int id, String username, String email, String password, String status,
                  Timestamp createdAt, Timestamp updatedAt, String bio, int followerCount,
                  String instagramLink, String tiktokLink, String youtubeLink,
                  double pricingPerPost, List<String> interests) {
        super(id, username, email, password, "creator", status, createdAt, updatedAt, null, null, null, null, true);
        this.bio = bio;
        this.followerCount = followerCount;
        this.instagramLink = instagramLink;
        this.tiktokLink = tiktokLink;
        this.youtubeLink = youtubeLink;
        this.pricingPerPost = pricingPerPost;
        this.interests = interests;
    }

    // Constructor with all User fields and Creator-specific fields including verification fields
    public Creator(int id, String username, String email, String password, String status,
                  Timestamp createdAt, Timestamp updatedAt, String verificationCode,
                  Timestamp verificationExpiry, String resetToken, Timestamp resetTokenExpiry,
                  boolean verified, String bio, int followerCount,
                  String instagramLink, String tiktokLink, String youtubeLink,
                  double pricingPerPost, List<String> interests) {
        super(id, username, email, password, "creator", status, createdAt, updatedAt,
              verificationCode, verificationExpiry, resetToken, resetTokenExpiry, verified);
        this.bio = bio;
        this.followerCount = followerCount;
        this.instagramLink = instagramLink;
        this.tiktokLink = tiktokLink;
        this.youtubeLink = youtubeLink;
        this.pricingPerPost = pricingPerPost;
        this.interests = interests;
    }

    // Getters and Setters
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public String getTiktokLink() {
        return tiktokLink;
    }

    public void setTiktokLink(String tiktokLink) {
        this.tiktokLink = tiktokLink;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public double getPricingPerPost() {
        return pricingPerPost;
    }

    public void setPricingPerPost(double pricingPerPost) {
        this.pricingPerPost = pricingPerPost;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    @Override
    public String toString() {
        return "Creator{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", bio='" + bio + '\'' +
                ", followerCount=" + followerCount +
                ", instagramLink='" + instagramLink + '\'' +
                ", tiktokLink='" + tiktokLink + '\'' +
                ", youtubeLink='" + youtubeLink + '\'' +
                ", pricingPerPost=" + pricingPerPost +
                ", interests=" + interests +
                '}';
    }
}
