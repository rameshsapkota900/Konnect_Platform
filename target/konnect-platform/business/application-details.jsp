<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.Application" %>
<%@ page import="com.konnect.model.Campaign" %>
<%@ page import="com.konnect.model.Creator" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Application Details - Konnect</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Konnect</h1>
            <p>Application Details</p>

            <nav>
                <ul>
                    <li><a href="<%= request.getContextPath() %>/business/dashboard">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/profile">Profile</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/campaigns" class="active">My Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/creators">Browse Creators</a></li>
                    <li><a href="<%= request.getContextPath() %>/messages">Messages</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <main>
            <%
                Application app = (Application)request.getAttribute("application");
                Campaign campaign = (Campaign)request.getAttribute("campaign");
                Creator creator = (Creator)request.getAttribute("creator");
            %>

            <section class="application-details-section">
                <div class="section-header">
                    <h2>Application Details</h2>
                    <a href="<%= request.getContextPath() %>/campaign?id=<%= campaign.getId() %>" class="btn btn-secondary">Back to Campaign</a>
                </div>

                <% if(request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <% if(request.getAttribute("success") != null) { %>
                    <div class="success-message">
                        <%= request.getAttribute("success") %>
                    </div>
                <% } %>

                <div class="application-details-grid">
                    <div class="application-info-card">
                        <h3>Application Information</h3>
                        <div class="info-item">
                            <span class="label">Status:</span>
                            <span class="value status-<%= app.getStatus().toLowerCase() %>"><%= app.getStatus() %></span>
                        </div>
                        <div class="info-item">
                            <span class="label">Applied On:</span>
                            <span class="value"><%= app.getCreatedAt() %></span>
                        </div>
                        <div class="info-item">
                            <span class="label">Last Updated:</span>
                            <span class="value"><%= app.getUpdatedAt() != null ? app.getUpdatedAt() : "N/A" %></span>
                        </div>

                        <div class="application-message-box">
                            <h4>Creator's Message:</h4>
                            <p><%= app.getMessage() %></p>
                        </div>

                        <% if ("pending".equals(app.getStatus())) { %>
                            <div class="application-actions">
                                <a href="<%= request.getContextPath() %>/business/application?action=approve&id=<%= app.getId() %>" class="btn btn-primary">Approve</a>
                                <a href="<%= request.getContextPath() %>/business/application?action=reject&id=<%= app.getId() %>" class="btn btn-secondary" onclick="return confirm('Are you sure you want to reject this application?')">Reject</a>
                            </div>
                        <% } else if ("accepted".equals(app.getStatus())) { %>
                            <div class="application-actions">
                                <a href="<%= request.getContextPath() %>/business/application?action=complete&id=<%= app.getId() %>" class="btn btn-primary">Mark as Completed</a>
                            </div>
                        <% } %>
                    </div>

                    <div class="creator-info-card">
                        <h3>Creator Information</h3>
                        <% if (creator != null) { %>
                            <div class="creator-profile">
                                <div class="info-item">
                                    <span class="label">Username:</span>
                                    <span class="value"><%= creator.getUsername() %></span>
                                </div>
                                <div class="info-item">
                                    <span class="label">Email:</span>
                                    <span class="value"><%= creator.getEmail() %></span>
                                </div>
                                <div class="info-item">
                                    <span class="label">Followers:</span>
                                    <span class="value"><%= creator.getFollowerCount() %></span>
                                </div>
                                <div class="info-item">
                                    <span class="label">Pricing:</span>
                                    <span class="value">$<%= creator.getPricingPerPost() %> per post</span>
                                </div>

                                <div class="creator-bio">
                                    <h4>Bio:</h4>
                                    <p><%= creator.getBio() != null ? creator.getBio() : "No bio provided" %></p>
                                </div>

                                <div class="creator-social">
                                    <h4>Social Media:</h4>
                                    <ul>
                                        <% if (creator.getInstagramLink() != null && !creator.getInstagramLink().isEmpty()) { %>
                                            <li><strong>Instagram:</strong> <a href="<%= creator.getInstagramLink() %>" target="_blank"><%= creator.getInstagramLink() %></a></li>
                                        <% } %>
                                        <% if (creator.getTiktokLink() != null && !creator.getTiktokLink().isEmpty()) { %>
                                            <li><strong>TikTok:</strong> <a href="<%= creator.getTiktokLink() %>" target="_blank"><%= creator.getTiktokLink() %></a></li>
                                        <% } %>
                                        <% if (creator.getYoutubeLink() != null && !creator.getYoutubeLink().isEmpty()) { %>
                                            <li><strong>YouTube:</strong> <a href="<%= creator.getYoutubeLink() %>" target="_blank"><%= creator.getYoutubeLink() %></a></li>
                                        <% } %>
                                    </ul>
                                </div>

                                <div class="creator-interests">
                                    <h4>Interests:</h4>
                                    <% if (creator.getInterests() != null && !creator.getInterests().isEmpty()) { %>
                                        <div class="interests-tags">
                                            <% for (String interest : creator.getInterests()) { %>
                                                <span class="interest-tag"><%= interest %></span>
                                            <% } %>
                                        </div>
                                    <% } else { %>
                                        <p>No interests specified</p>
                                    <% } %>
                                </div>

                                <div class="creator-actions">
                                    <a href="<%= request.getContextPath() %>/messages?userId=<%= creator.getId() %>" class="btn btn-primary">Message Creator</a>
                                    <a href="<%= request.getContextPath() %>/business/creator?id=<%= creator.getId() %>" class="btn btn-secondary">View Full Profile</a>
                                </div>
                            </div>
                        <% } else { %>
                            <p>Creator information not available.</p>
                        <% } %>
                    </div>

                    <div class="campaign-info-card">
                        <h3>Campaign Information</h3>
                        <div class="info-item">
                            <span class="label">Title:</span>
                            <span class="value"><%= campaign.getTitle() %></span>
                        </div>
                        <div class="info-item">
                            <span class="label">Budget:</span>
                            <span class="value">$<%= campaign.getBudget() %></span>
                        </div>
                        <div class="info-item">
                            <span class="label">Duration:</span>
                            <span class="value"><%= campaign.getStartDate() %> to <%= campaign.getEndDate() %></span>
                        </div>
                        <div class="info-item">
                            <span class="label">Status:</span>
                            <span class="value status-<%= campaign.getStatus().toLowerCase() %>"><%= campaign.getStatus() %></span>
                        </div>

                        <div class="campaign-description">
                            <h4>Description:</h4>
                            <p><%= campaign.getDescription() %></p>
                        </div>

                        <% if (campaign.getRequirements() != null && !campaign.getRequirements().isEmpty()) { %>
                            <div class="campaign-requirements">
                                <h4>Requirements:</h4>
                                <p><%= campaign.getRequirements() %></p>
                            </div>
                        <% } %>

                        <div class="campaign-actions">
                            <a href="<%= request.getContextPath() %>/campaign?id=<%= campaign.getId() %>" class="btn btn-primary">View Campaign</a>
                        </div>
                    </div>
                </div>
            </section>
        </main>

        <footer>
            <p>&copy; 2025 Konnect. All rights reserved.</p>
        </footer>
    </div>
</body>
</html>
