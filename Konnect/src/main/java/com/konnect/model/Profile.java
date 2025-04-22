package com.konnect.model;

public class Profile {
    private int profileId;
    private int userId;
    private String fullName;
    private String bio;
    private String socialMediaLinks; // Could be JSON or comma-separated string
    private int followerCount;
    private String niche;
    private String pricingInfo;
    private String mediaKitPath; // Relative path

    // Join field (optional, useful for display)
    private String username;

    // Constructors
    public Profile() {}

    // Getters and Setters
    public int getProfileId() { return profileId; }
    public void setProfileId(int profileId) { this.profileId = profileId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getSocialMediaLinks() { return socialMediaLinks; }
    public void setSocialMediaLinks(String socialMediaLinks) { this.socialMediaLinks = socialMediaLinks; }
    public int getFollowerCount() { return followerCount; }
    public void setFollowerCount(int followerCount) { this.followerCount = followerCount; }
    public String getNiche() { return niche; }
    public void setNiche(String niche) { this.niche = niche; }
    public String getPricingInfo() { return pricingInfo; }
    public void setPricingInfo(String pricingInfo) { this.pricingInfo = pricingInfo; }
    public String getMediaKitPath() { return mediaKitPath; }
    public void setMediaKitPath(String mediaKitPath) { this.mediaKitPath = mediaKitPath; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

}
