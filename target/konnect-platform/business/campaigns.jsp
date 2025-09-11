<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.konnect.model.Campaign" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Campaigns - Konnect</title>
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
                    <li><a href="<%= request.getContextPath() %>/business/dashboard">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/profile">Profile</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/campaigns" class="active">My Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/creators">Browse Creators</a></li>
                    <li><a href="<%= request.getContextPath() %>/messages">Messages</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">My Campaigns</div>
    </header>

    <main>
        <%
            List<Campaign> campaigns = (List<Campaign>)request.getAttribute("campaigns");
            Map<Integer, Integer> applicationCounts = (Map<Integer, Integer>)request.getAttribute("applicationCounts");
            Map<Integer, Integer> pendingCounts = (Map<Integer, Integer>)request.getAttribute("pendingCounts");
            Map<Integer, Integer> approvedCounts = (Map<Integer, Integer>)request.getAttribute("approvedCounts");
            Map<Integer, Integer> completedCounts = (Map<Integer, Integer>)request.getAttribute("completedCounts");
            String statusFilter = (String)request.getAttribute("statusFilter");
        %>

        <section class="about-section">
            <div class="about-card">
                <div class="section-header" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                    <h2>My Campaigns</h2>
                    <a href="<%= request.getContextPath() %>/business/campaign-create" class="btn btn-primary" style="display: inline-flex; align-items: center; gap: 0.5rem;">
                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
                        Create New Campaign
                    </a>
                </div>

                <% if(request.getAttribute("success") != null) { %>
                    <div class="success-message">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                        <%= request.getAttribute("success") %>
                    </div>
                <% } %>

                <% if(request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <div class="filter-tabs" style="display: flex; gap: 0.5rem; margin-bottom: 2rem; flex-wrap: wrap;">
                    <a href="<%= request.getContextPath() %>/business/campaigns" class="filter-tab <%= statusFilter == null ? "active" : "" %>" style="padding: 0.5rem 1rem; border-radius: 20px; text-decoration: none; color: var(--text-color); background-color: <%= statusFilter == null ? "var(--primary-color)" : "var(--light-gray)" %>; color: <%= statusFilter == null ? "white" : "var(--text-color)" %>; font-weight: 500;">All</a>
                    <a href="<%= request.getContextPath() %>/business/campaigns?status=active" class="filter-tab <%= "active".equals(statusFilter) ? "active" : "" %>" style="padding: 0.5rem 1rem; border-radius: 20px; text-decoration: none; color: var(--text-color); background-color: <%= "active".equals(statusFilter) ? "var(--primary-color)" : "var(--light-gray)" %>; color: <%= "active".equals(statusFilter) ? "white" : "var(--text-color)" %>; font-weight: 500;">Active</a>
                    <a href="<%= request.getContextPath() %>/business/campaigns?status=completed" class="filter-tab <%= "completed".equals(statusFilter) ? "active" : "" %>" style="padding: 0.5rem 1rem; border-radius: 20px; text-decoration: none; color: var(--text-color); background-color: <%= "completed".equals(statusFilter) ? "var(--primary-color)" : "var(--light-gray)" %>; color: <%= "completed".equals(statusFilter) ? "white" : "var(--text-color)" %>; font-weight: 500;">Completed</a>
                    <a href="<%= request.getContextPath() %>/business/campaigns?status=cancelled" class="filter-tab <%= "cancelled".equals(statusFilter) ? "active" : "" %>" style="padding: 0.5rem 1rem; border-radius: 20px; text-decoration: none; color: var(--text-color); background-color: <%= "cancelled".equals(statusFilter) ? "var(--primary-color)" : "var(--light-gray)" %>; color: <%= "cancelled".equals(statusFilter) ? "white" : "var(--text-color)" %>; font-weight: 500;">Cancelled</a>
                </div>

                <% if(campaigns == null || campaigns.isEmpty()) { %>
                    <div class="empty-state" style="text-align: center; padding: 3rem 1rem; background-color: var(--light-gray); border-radius: 10px;">
                        <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" stroke-linecap="round" stroke-linejoin="round" style="margin-bottom: 1rem; color: var(--secondary-color);"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path><polyline points="14 2 14 8 20 8"></polyline><line x1="16" y1="13" x2="8" y2="13"></line><line x1="16" y1="17" x2="8" y2="17"></line><polyline points="10 9 9 9 8 9"></polyline></svg>
                        <p style="font-size: 1.2rem; margin-bottom: 1.5rem;">You haven't created any campaigns yet.</p>
                        <a href="<%= request.getContextPath() %>/business/campaign-create" class="btn btn-primary" style="display: inline-flex; align-items: center; gap: 0.5rem;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
                            Create Your First Campaign
                        </a>
                    </div>
                <% } else { %>
                    <div style="overflow-x: auto; background-color: var(--white); border-radius: 10px; box-shadow: var(--shadow);">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Title</th>
                                    <th>Budget</th>
                                    <th>Duration</th>
                                    <th>Status</th>
                                    <th>Applications</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    for(Campaign campaign : campaigns) {
                                        // Apply status filter if set
                                        if (statusFilter != null && !statusFilter.isEmpty() && !statusFilter.equals(campaign.getStatus())) {
                                            continue;
                                        }
                                %>
                                <tr>
                                    <td><a href="<%= request.getContextPath() %>/campaign?id=<%= campaign.getId() %>" style="color: var(--primary-color); font-weight: 500;"><%= campaign.getTitle() %></a></td>
                                    <td>$<%= campaign.getBudget() %></td>
                                    <td><%= campaign.getStartDate() %> to <%= campaign.getEndDate() %></td>
                                    <td><span class="status-<%= campaign.getStatus().toLowerCase() %>"><%= campaign.getStatus() %></span></td>
                                    <td>
                                        <div style="display: flex; flex-direction: column; gap: 0.3rem;">
                                            <div style="display: flex; align-items: center; gap: 0.5rem;">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                                                <span><%= applicationCounts.getOrDefault(campaign.getId(), 0) %> total</span>
                                            </div>
                                            <% if (pendingCounts.getOrDefault(campaign.getId(), 0) > 0) { %>
                                                <div style="display: flex; align-items: center; gap: 0.5rem; color: #856404;">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>
                                                    <span><%= pendingCounts.get(campaign.getId()) %> pending</span>
                                                </div>
                                            <% } %>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="<%= request.getContextPath() %>/campaign?id=<%= campaign.getId() %>" class="btn btn-small" style="background-color: var(--primary-color); color: white; display: inline-flex; align-items: center; gap: 0.3rem;">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>
                                                View
                                            </a>

                                            <% if ("active".equals(campaign.getStatus())) { %>
                                                <form action="<%= request.getContextPath() %>/business/campaigns" method="post" style="display: inline;">
                                                    <input type="hidden" name="campaignId" value="<%= campaign.getId() %>">
                                                    <input type="hidden" name="action" value="cancel">
                                                    <button type="submit" class="btn btn-small" style="background-color: #ffc107; color: #212529; display: inline-flex; align-items: center; gap: 0.3rem;" onclick="return confirm('Are you sure you want to cancel this campaign?')">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="8" y1="12" x2="16" y2="12"></line></svg>
                                                        Cancel
                                                    </button>
                                                </form>

                                                <form action="<%= request.getContextPath() %>/business/campaigns" method="post" style="display: inline;">
                                                    <input type="hidden" name="campaignId" value="<%= campaign.getId() %>">
                                                    <input type="hidden" name="action" value="complete">
                                                    <button type="submit" class="btn btn-small" style="background-color: #28a745; color: white; display: inline-flex; align-items: center; gap: 0.3rem;" onclick="return confirm('Are you sure you want to mark this campaign as completed?')">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                                                        Complete
                                                    </button>
                                                </form>
                                            <% } else if ("cancelled".equals(campaign.getStatus())) { %>
                                                <form action="<%= request.getContextPath() %>/business/campaigns" method="post" style="display: inline;">
                                                    <input type="hidden" name="campaignId" value="<%= campaign.getId() %>">
                                                    <input type="hidden" name="action" value="activate">
                                                    <button type="submit" class="btn btn-small" style="background-color: #28a745; color: white; display: inline-flex; align-items: center; gap: 0.3rem;">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14"></path><path d="M12 5v14"></path></svg>
                                                        Reactivate
                                                    </button>
                                                </form>
                                            <% } %>
                                        </div>
                                    </td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                <% } %>
            </div>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 Konnect. All rights reserved. <span class="admin-footer-note">Business Portal</span></p>
    </footer>
</body>
</html>
