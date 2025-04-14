package model;

import java.io.Serializable;

public class Application implements Serializable {
    private int id;
    private int campaignId;
    private int creatorId;
    private String proposal;
    private String status; // "pending", "accepted", "rejected"
    private String createdAt;

    public Application() {
    }

    public Application(int id, int campaignId, int creatorId, String proposal, String status, String createdAt) {
        this.id = id;
        this.campaignId = campaignId;
        this.creatorId = creatorId;
        this.proposal = proposal;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
