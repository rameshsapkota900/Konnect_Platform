<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.konnect.model.Business" %>
<%@ page import="com.konnect.model.Campaign" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Business Dashboard - Konnect</title>
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
                    <li><a href="<%= request.getContextPath() %>/business/dashboard" class="active">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/profile">Profile</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/campaigns">My Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/creators">Browse Creators</a></li>
                    <li><a href="<%= request.getContextPath() %>/messages">Messages
                        <% if ((Integer)request.getAttribute("unreadMessages") > 0) { %>
                            <span class="badge"><%= request.getAttribute("unreadMessages") %></span>
                        <% } %>
                    </a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Business Dashboard</div>
    </header>

    <main>
        <%
            Business business = (Business)request.getAttribute("business");
            List<Campaign> campaigns = (List<Campaign>)request.getAttribute("campaigns");
            Map<Integer, Integer> applicationCounts = (Map<Integer, Integer>)request.getAttribute("applicationCounts");
        %>

        <section class="dashboard-welcome">
            <h2>Welcome, <%= business.getUsername() %>!</h2>
            <p>This is your business dashboard where you can manage your campaigns and find creators for collaborations.</p>
        </section>

        <div class="stats-overview">
            <h3>Business Overview</h3>
            <div class="stats-grid">
                <div class="stat-card">
                    <h4>Total Campaigns</h4>
                    <div class="stat-value"><%= campaigns != null ? campaigns.size() : 0 %></div>
                    <div class="stat-details">
                        <%
                            int activeCount = 0;
                            int completedCount = 0;
                            int cancelledCount = 0;

                            if (campaigns != null) {
                                for (Campaign c : campaigns) {
                                    if ("active".equals(c.getStatus())) {
                                        activeCount++;
                                    } else if ("completed".equals(c.getStatus())) {
                                        completedCount++;
                                    } else if ("cancelled".equals(c.getStatus())) {
                                        cancelledCount++;
                                    }
                                }
                            }
                        %>
                        <div><span class="label">Active</span> <span class="value"><%= activeCount %></span></div>
                        <div><span class="label">Completed</span> <span class="value"><%= completedCount %></span></div>
                        <div><span class="label">Cancelled</span> <span class="value"><%= cancelledCount %></span></div>
                    </div>
                </div>

                <div class="stat-card">
                    <h4>Applications</h4>
                    <div class="stat-value">
                        <%
                            int totalApplications = 0;
                            if (applicationCounts != null) {
                                for (Integer count : applicationCounts.values()) {
                                    totalApplications += count;
                                }
                            }
                            out.print(totalApplications);
                        %>
                    </div>
                    <div class="stat-details">
                        <div><span class="label">Avg. per Campaign</span> <span class="value">
                            <%= campaigns != null && !campaigns.isEmpty() ? String.format("%.1f", (double)totalApplications / campaigns.size()) : "0" %>
                        </span></div>
                        <div><span class="label">Latest Campaign</span> <span class="value">
                            <%
                                int latestCampaignApps = 0;
                                if (campaigns != null && !campaigns.isEmpty()) {
                                    Campaign latest = campaigns.get(0);
                                    latestCampaignApps = applicationCounts.getOrDefault(latest.getId(), 0);
                                }
                                out.print(latestCampaignApps);
                            %>
                        </span></div>
                    </div>
                </div>

                <div class="stat-card">
                    <h4>Quick Actions</h4>
                    <div class="stat-actions" style="display: flex; flex-direction: column; gap: 1rem; margin-top: 1rem;">
                        <a href="<%= request.getContextPath() %>/business/campaign-create" class="btn btn-primary" style="background-color: var(--primary-color); color: var(--white); text-align: center; margin: 0;">Create Campaign</a>
                        <a href="<%= request.getContextPath() %>/business/creators" class="btn btn-secondary" style="background-color: var(--secondary-color); color: var(--white); text-align: center; margin: 0;">Browse Creators</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="dashboard-grid">
            <section class="dashboard-card">
                <h3>Business Profile</h3>
                <div class="info-grid">
                    <div class="info-item">
                        <span class="label">Company</span>
                        <span class="value"><%= business.getCompanyName() != null ? business.getCompanyName() : "Not set" %></span>
                    </div>
                    <div class="info-item">
                        <span class="label">Industry</span>
                        <span class="value"><%= business.getIndustry() != null ? business.getIndustry() : "Not set" %></span>
                    </div>
                    <div class="info-item">
                        <span class="label">Website</span>
                        <span class="value">
                            <% if (business.getWebsite() != null && !business.getWebsite().isEmpty()) { %>
                                <a href="<%= business.getWebsite() %>" target="_blank" style="color: var(--primary-color);"><%= business.getWebsite() %></a>
                            <% } else { %>
                                Not set
                            <% } %>
                        </span>
                    </div>
                    <div class="info-item">
                        <span class="label">Contact</span>
                        <span class="value"><%= business.getContactPhone() != null ? business.getContactPhone() : "Not set" %></span>
                    </div>
                    <div class="info-item full-width">
                        <span class="label">Description</span>
                        <span class="value"><%= business.getDescription() != null ? business.getDescription() : "Not set" %></span>
                    </div>
                </div>
                <div style="margin-top: 1.5rem; text-align: center;">
                    <a href="<%= request.getContextPath() %>/business/profile" class="btn btn-primary" style="background-color: var(--primary-color); color: var(--white);">Edit Profile</a>
                </div>
            </section>

            <section class="dashboard-card">
                <h3>Recent Campaigns</h3>
                <% if (campaigns == null || campaigns.isEmpty()) { %>
                    <div class="empty-state">
                        <p>You haven't created any campaigns yet.</p>
                        <a href="<%= request.getContextPath() %>/business/campaign-create" class="btn btn-primary" style="background-color: var(--primary-color); color: var(--white);">Create Your First Campaign</a>
                    </div>
                <% } else { %>
                    <div style="overflow-x: auto;">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Title</th>
                                    <th>Status</th>
                                    <th>Applications</th>
                                    <th>Budget</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    // Display only the 5 most recent campaigns
                                    int count = 0;
                                    for (Campaign campaign : campaigns) {
                                        if (count >= 5) break;
                                %>
                                <tr>
                                    <td><a href="<%= request.getContextPath() %>/campaign?id=<%= campaign.getId() %>" style="color: var(--primary-color); font-weight: 500;"><%= campaign.getTitle() %></a></td>
                                    <td><span class="status-<%= campaign.getStatus().toLowerCase() %>"><%= campaign.getStatus() %></span></td>
                                    <td><%= applicationCounts.getOrDefault(campaign.getId(), 0) %></td>
                                    <td>$<%= campaign.getBudget() %></td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="<%= request.getContextPath() %>/campaign?id=<%= campaign.getId() %>" class="btn btn-small">View</a>
                                            <% if ("active".equals(campaign.getStatus())) { %>
                                                <a href="<%= request.getContextPath() %>/business/campaign-edit?id=<%= campaign.getId() %>" class="btn btn-small">Edit</a>
                                            <% } %>
                                        </div>
                                    </td>
                                </tr>
                                <%
                                        count++;
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                    <% if (campaigns.size() > 5) { %>
                        <div style="text-align: center; margin-top: 1rem;">
                            <a href="<%= request.getContextPath() %>/business/campaigns" style="color: var(--primary-color); text-decoration: none;">View all <%= campaigns.size() %> campaigns</a>
                        </div>
                    <% } %>
                <% } %>
            </section>
        </div>

        <section class="dashboard-action">
            <h3>Find Creators for Your Campaigns</h3>
            <p>Browse our network of content creators to find the perfect match for your brand. Connect with influencers who align with your values and can help you reach your target audience effectively.</p>
            <div style="display: flex; gap: 1rem; flex-wrap: wrap;">
                <a href="<%= request.getContextPath() %>/business/creators" class="btn btn-primary" style="background-color: var(--primary-color); color: var(--white);">Browse Creators</a>
                <a href="<%= request.getContextPath() %>/business/campaign-create" class="btn btn-secondary" style="background-color: var(--secondary-color); color: var(--white);">Create New Campaign</a>
            </div>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 Konnect. All rights reserved. <span class="admin-footer-note">Business Portal</span></p>
    </footer>
</body>
</html>
