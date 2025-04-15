package model;

import java.io.Serializable;

public class Business implements Serializable {
    private int id;
    private int userId;
    private String companyName;
    private String description;
    private String website;
    private String industry;
    private String logoPath;

    public Business() {
    }

    public Business(int id, int userId, String companyName, String description, 
                   String website, String industry, String logoPath) {
        this.id = id;
        this.userId = userId;
        this.companyName = companyName;
        this.description = description;
        this.website = website;
        this.industry = industry;
        this.logoPath = logoPath;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
}
