package model;

import java.io.Serializable;

public class Campaign implements Serializable {
    private int id;
    private int businessId;
    private String title;
    private String description;
    private double budget;
    private String requirements;
    private String deadline;
    private String status; // "active", "completed", "cancelled"
    private String imagePath;
    private String createdAt;

    public Campaign() {
    }

    public Campaign(int id, int businessId, String title, String description, double budget,
                   String requirements, String deadline, String status, String imagePath, String createdAt) {
        this.id = id;
        this.businessId = businessId;
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.requirements = requirements;
        this.deadline = deadline;
        this.status = status;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
