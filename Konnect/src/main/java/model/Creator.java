package model;
import java.io.Serializable;

public class Creator implements Serializable {
    private int id;
    private int userId;
    private String fullName;
    private String bio;
    private String socialMediaLinks;
    private int followerCount;
    private double pricing;
    private String mediaKitPath;
    private String niche;

    public Creator() {
    }

    public Creator(int id, int userId, String fullName, String bio, String socialMediaLinks, 
                  int followerCount, double pricing, String mediaKitPath, String niche) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.bio = bio;
        this.socialMediaLinks = socialMediaLinks;
        this.followerCount = followerCount;
        this.pricing = pricing;
        this.mediaKitPath = mediaKitPath;
        this.niche = niche;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSocialMediaLinks() {
        return socialMediaLinks;
    }

    public void setSocialMediaLinks(String socialMediaLinks) {
        this.socialMediaLinks = socialMediaLinks;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public double getPricing() {
        return pricing;
    }

    public void setPricing(double pricing) {
        this.pricing = pricing;
    }

    public String getMediaKitPath() {
        return mediaKitPath;
    }

    public void setMediaKitPath(String mediaKitPath) {
        this.mediaKitPath = mediaKitPath;
    }

    public String getNiche() {
        return niche;
    }

    public void setNiche(String niche) {
        this.niche = niche;
    }
}
