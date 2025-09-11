<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.konnect.model.User" %>
<%@ page import="com.konnect.model.Campaign" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Konnect</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
    <header>
        <div class="header-row">
            <a href="<%= request.getContextPath() %>/admin/dashboard" class="header-logo">
                Konnect
            </a>
            <nav>
                <ul class="nav-list">
                    <li><a href="<%= request.getContextPath() %>/admin/dashboard" class="active">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/users">Users</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/campaigns">Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/reports">Reports</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Admin Panel</div>
    </header>

    <main>
        <%
            Map<String, Integer> statistics = (Map<String, Integer>)request.getAttribute("statistics");
            List<User> recentUsers = (List<User>)request.getAttribute("recentUsers");
            List<Campaign> recentCampaigns = (List<Campaign>)request.getAttribute("recentCampaigns");
        %>

        <section class="dashboard-welcome">
            <h2>Admin Dashboard</h2>
            <p>Welcome to the admin dashboard. Here you can monitor and manage the platform.</p>
        </section>

        <section class="stats-overview">
            <h3>Platform Statistics</h3>
            <div class="stats-grid">
                <div class="stat-card">
                    <h4>Users</h4>
                    <div class="stat-value"><%= statistics.get("totalUsers") %></div>
                    <div class="stat-details">
                        <div><span class="label">Creators:</span> <span class="value"><%= statistics.get("creatorCount") %></span></div>
                        <div><span class="label">Businesses:</span> <span class="value"><%= statistics.get("businessCount") %></span></div>
                        <div><span class="label">Active:</span> <span class="value"><%= statistics.get("activeUsers") %></span></div>
                        <div><span class="label">Banned:</span> <span class="value"><%= statistics.get("bannedUsers") %></span></div>
                    </div>
                </div>

                <div class="stat-card">
                    <h4>Campaigns</h4>
                    <div class="stat-value"><%= statistics.get("campaignCount") %></div>
                    <div class="stat-details">
                        <div><span class="label">Total Campaigns</span></div>
                        <a href="<%= request.getContextPath() %>/admin/campaigns" class="btn btn-small">View All</a>
                    </div>
                </div>

                <div class="stat-card">
                    <h4>Reports</h4>
                    <div class="stat-value"><%= statistics.get("totalReports") %></div>
                    <div class="stat-details">
                        <div><span class="label">Pending:</span> <span class="value"><%= statistics.get("pendingReports") %></span></div>
                        <div><span class="label">Resolved:</span> <span class="value"><%= statistics.get("resolvedReports") %></span></div>
                        <div><span class="label">Dismissed:</span> <span class="value"><%= statistics.get("dismissedReports") %></span></div>
                    </div>
                </div>
            </div>
        </section>

        <div class="dashboard-grid">
            <section class="dashboard-card">
                <h3>Recent Users</h3>
                <% if (recentUsers == null || recentUsers.isEmpty()) { %>
                    <div class="empty-state">
                        <p>No users found.</p>
                    </div>
                <% } else { %>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Role</th>
                                <th>Status</th>
                                <th>Joined</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (User user : recentUsers) { %>
                            <tr>
                                <td><%= user.getUsername() %></td>
                                <td><%= user.getEmail() %></td>
                                <td><%= user.getRole() %></td>
                                <td><span class="status-<%= user.getStatus().toLowerCase() %>"><%= user.getStatus() %></span></td>
                                <td><%= user.getCreatedAt() %></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                    <a href="<%= request.getContextPath() %>/admin/users" class="btn btn-primary">View All Users</a>
                <% } %>
            </section>

            <section class="dashboard-card">
                <h3>Recent Campaigns</h3>
                <% if (recentCampaigns == null || recentCampaigns.isEmpty()) { %>
                    <div class="empty-state">
                        <p>No campaigns found.</p>
                    </div>
                <% } else { %>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Title</th>
                                <th>Business ID</th>
                                <th>Budget</th>
                                <th>Status</th>
                                <th>Created</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Campaign campaign : recentCampaigns) { %>
                            <tr>
                                <td><%= campaign.getTitle() %></td>
                                <td><%= campaign.getBusinessId() %></td>
                                <td>$<%= campaign.getBudget() %></td>
                                <td><span class="status-<%= campaign.getStatus().toLowerCase() %>"><%= campaign.getStatus() %></span></td>
                                <td><%= campaign.getCreatedAt() %></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                    <a href="<%= request.getContextPath() %>/admin/campaigns" class="btn btn-primary">View All Campaigns</a>
                <% } %>
            </section>
        </div>

        <section class="dashboard-action">
            <h3>Pending Reports</h3>
            <% if (statistics.get("pendingReports") > 0) { %>
                <p>There are <%= statistics.get("pendingReports") %> pending reports that require your attention.</p>
                <a href="<%= request.getContextPath() %>/admin/reports" class="btn btn-primary">View Reports</a>
            <% } else { %>
                <p>There are no pending reports at the moment.</p>
            <% } %>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 Konnect. All rights reserved. <span class="admin-footer-note">Admin Panel</span></p>
    </footer>
</body>
</html>
