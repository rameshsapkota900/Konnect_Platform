<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Creator" %>
<%@ page import="com.konnect.model.Campaign" %>
<%@ page import="com.konnect.model.Application" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Creator Dashboard - Konnect</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
        <header>
        <div class="header-row">
            <a href="<%= request.getContextPath() %>/" class="header-logo">Konnect</a>
            <nav>
                <ul class="nav-list">
                    <li><a href="<%= request.getContextPath() %>/creator/dashboard" class="active">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/creator/profile">Profile</a></li>
                    <li><a href="<%= request.getContextPath() %>/creator/campaigns">Browse Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/creator/applications">My Applications</a></li>
                    <li><a href="<%= request.getContextPath() %>/messages">Messages
                        <% if ((Integer)request.getAttribute("unreadMessages") > 0) { %>
                            <span class="badge"><%= request.getAttribute("unreadMessages") %></span>
                        <% } %>
                    </a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Creator Dashboard</div>
        </header>

        <main>
            <%
                Creator creator = (Creator)request.getAttribute("creator");
                List<Application> applications = (List<Application>)request.getAttribute("applications");
                List<Campaign> campaigns = (List<Campaign>)request.getAttribute("campaigns");
            int applicationCount = 0;
            int campaignCount = 0;
            %>

            <section class="dashboard-welcome">
                <h2>Welcome, <%= creator.getUsername() %>!</h2>
                <p>This is your creator dashboard where you can manage your profile, browse campaigns, and track your applications.</p>
            </section>

        <div class="stats-overview">
            <h3>Creator Overview</h3>
            <div class="stats-grid">
                <div class="stat-card">
                    <h4>Total Followers</h4>
                    <div class="stat-value"><%= creator.getFollowerCount() %></div>
                    <div class="stat-details">
                        <div><span class="label">Per Post Rate</span> <span class="value">$<%= creator.getPricingPerPost() %></span></div>
                        <div><span class="label">Social Platforms</span> <span class="value">
                            <%
                                int platformCount = 0;
                                if (creator.getInstagramLink() != null && !creator.getInstagramLink().isEmpty()) platformCount++;
                                if (creator.getTiktokLink() != null && !creator.getTiktokLink().isEmpty()) platformCount++;
                                if (creator.getYoutubeLink() != null && !creator.getYoutubeLink().isEmpty()) platformCount++;
                                out.print(platformCount);
                            %>
                        </span></div>
                    </div>
                </div>

                <div class="stat-card">
                    <h4>Applications</h4>
                    <div class="stat-value"><%= applications != null ? applications.size() : 0 %></div>
                    <div class="stat-details">
                        <%
                            int pendingCount = 0;
                            int acceptedCount = 0;
                            int rejectedCount = 0;

                            if (applications != null) {
                                for (Application app : applications) {
                                    if ("pending".equals(app.getStatus())) {
                                        pendingCount++;
                                    } else if ("accepted".equals(app.getStatus())) {
                                        acceptedCount++;
                                    } else if ("rejected".equals(app.getStatus())) {
                                        rejectedCount++;
                                    }
                                }
                            }
                        %>
                        <div><span class="label">Pending</span> <span class="value"><%= pendingCount %></span></div>
                        <div><span class="label">Accepted</span> <span class="value"><%= acceptedCount %></span></div>
                        <div><span class="label">Rejected</span> <span class="value"><%= rejectedCount %></span></div>
                    </div>
                </div>

                <div class="stat-card">
                    <h4>Quick Actions</h4>
                    <div class="stat-actions" style="display: flex; flex-direction: column; gap: 1rem; margin-top: 1rem;">
                        <a href="<%= request.getContextPath() %>/creator/campaigns" class="btn btn-primary" style="background-color: var(--primary-color); color: var(--white); text-align: center; margin: 0;">Browse Campaigns</a>
                        <a href="<%= request.getContextPath() %>/creator/profile" class="btn btn-secondary" style="background-color: var(--secondary-color); color: var(--white); text-align: center; margin: 0;">Edit Profile</a>
                    </div>
                </div>
            </div>
        </div>

            <div class="dashboard-grid">
                <section class="dashboard-card">
                <h3>Creator Profile</h3>
                <div class="info-grid">
                    <div class="info-item">
                        <span class="label">Bio</span>
                        <span class="value"><%= creator.getBio() != null ? creator.getBio() : "Not set" %></span>
                    </div>
                    <div class="info-item">
                        <span class="label">Followers</span>
                        <span class="value"><%= creator.getFollowerCount() %></span>
                    </div>
                    <div class="info-item">
                        <span class="label">Per Post Rate</span>
                        <span class="value">$<%= creator.getPricingPerPost() %></span>
                    </div>
                    <div class="info-item full-width">
                        <span class="label">Social Links</span>
                        <div class="social-links" style="display: flex; gap: 1rem; flex-wrap: wrap;">
                            <% if (creator.getInstagramLink() != null && !creator.getInstagramLink().isEmpty()) { %>
                                <a href="<%= creator.getInstagramLink() %>" target="_blank" class="btn btn-outline-primary btn-sm">Instagram</a>
                            <% } %>
                            <% if (creator.getTiktokLink() != null && !creator.getTiktokLink().isEmpty()) { %>
                                <a href="<%= creator.getTiktokLink() %>" target="_blank" class="btn btn-outline-primary btn-sm">TikTok</a>
                            <% } %>
                            <% if (creator.getYoutubeLink() != null && !creator.getYoutubeLink().isEmpty()) { %>
                                <a href="<%= creator.getYoutubeLink() %>" target="_blank" class="btn btn-outline-primary btn-sm">YouTube</a>
                            <% } %>
                        </div>
                    </div>
                </div>
                <div style="margin-top: 1.5rem; text-align: center;">
                    <a href="<%= request.getContextPath() %>/creator/profile" class="btn btn-primary" style="background-color: var(--primary-color); color: var(--white);">Edit Profile</a>
                    </div>
                </section>

                <section class="dashboard-card">
                <h3>Recent Applications</h3>
                    <% if (applications == null || applications.isEmpty()) { %>
                    <div class="empty-state">
                        <p>You haven't applied to any campaigns yet.</p>
                        <a href="<%= request.getContextPath() %>/creator/campaigns" class="btn btn-primary" style="background-color: var(--primary-color); color: var(--white);">Browse Campaigns</a>
                    </div>
                    <% } else { %>
                    <div style="overflow-x: auto;">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Campaign</th>
                                    <th>Status</th>
                                    <th>Applied On</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    applicationCount = 0;
                                    for (Application app : applications) {
                                        if (applicationCount >= 5) break;
                                        Campaign campaign = null;
                                        for (Campaign c : campaigns) {
                                            if (c.getId() == app.getCampaignId()) {
                                                campaign = c;
                                                break;
                                            }
                                        }
                                        if (campaign != null) {
                                %>
                                <tr>
                                    <td><a href="<%= request.getContextPath() %>/campaign?id=<%= campaign.getId() %>" style="color: var(--primary-color); font-weight: 500;"><%= campaign.getTitle() %></a></td>
                                    <td><span class="status-<%= app.getStatus().toLowerCase() %>"><%= app.getStatus() %></span></td>
                                    <td><%= app.getCreatedAt() %></td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="<%= request.getContextPath() %>/campaign?id=<%= campaign.getId() %>" class="btn btn-small">View</a>
                                        </div>
                                    </td>
                                </tr>
                                <%
                                            applicationCount++;
                                        }
                                    }
                                %>
                            </tbody>
                        </table>
                            </div>
                    <% if (applications.size() > 5) { %>
                        <div style="text-align: center; margin-top: 1rem;">
                            <a href="<%= request.getContextPath() %>/creator/applications" style="color: var(--primary-color); text-decoration: none;">View all <%= applications.size() %> applications</a>
                        </div>
                    <% } %>
                    <% } %>
                </section>
        </div>

        <section class="dashboard-action">
            <h3>Find Campaigns to Apply</h3>
            <p>Browse through available campaigns from businesses looking for creators like you. Find opportunities that match your style and audience.</p>
            <div style="display: flex; gap: 1rem; flex-wrap: wrap;">
                <a href="<%= request.getContextPath() %>/creator/campaigns" class="btn btn-primary" style="background-color: var(--primary-color); color: var(--white);">Browse Campaigns</a>
                <a href="<%= request.getContextPath() %>/creator/profile" class="btn btn-secondary" style="background-color: var(--secondary-color); color: var(--white);">Update Profile</a>
            </div>
        </section>
        </main>

        <footer>
        <p>&copy; 2025 Konnect. All rights reserved. <span class="admin-footer-note">Creator Portal</span></p>
        </footer>
</body>
</html>
