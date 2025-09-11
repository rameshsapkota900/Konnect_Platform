<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.konnect.model.Campaign" %>
<%@ page import="com.konnect.model.Business" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Campaigns - Konnect</title>
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
                    <li><a href="<%= request.getContextPath() %>/admin/dashboard">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/users">Users</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/campaigns" class="active">Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/reports">Reports</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Admin Panel - Campaign Management</div>
    </header>

    <main>
        <%
            List<Campaign> campaigns = (List<Campaign>)request.getAttribute("campaigns");
            Map<Integer, Business> businessMap = (Map<Integer, Business>)request.getAttribute("businessMap");
            String statusFilter = (String)request.getAttribute("statusFilter");
            String searchQuery = (String)request.getAttribute("searchQuery");
        %>

        <section class="admin-section">
            <div class="section-header">
                <h2>Campaign Management</h2>
            </div>

            <% if(request.getAttribute("success") != null) { %>
                <div class="success-message">
                    <%= request.getAttribute("success") %>
                </div>
            <% } %>

            <% if(request.getAttribute("error") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>

            <div class="filter-section">
                <h3>Filter Campaigns</h3>
                <form action="<%= request.getContextPath() %>/admin/campaigns" method="get" class="filter-form">
                    <div class="filter-controls">
                        <div class="filter-group">
                            <label for="search">Search</label>
                            <input type="text" id="search" name="search" value="<%= searchQuery != null ? searchQuery : "" %>" placeholder="Title or Description">
                        </div>

                        <div class="filter-group">
                            <label for="status">Status</label>
                            <select id="status" name="status">
                                <option value="">All Statuses</option>
                                <option value="active" <%= "active".equals(statusFilter) ? "selected" : "" %>>Active</option>
                                <option value="completed" <%= "completed".equals(statusFilter) ? "selected" : "" %>>Completed</option>
                                <option value="cancelled" <%= "cancelled".equals(statusFilter) ? "selected" : "" %>>Cancelled</option>
                            </select>
                        </div>

                        <div class="filter-group" style="justify-content: flex-end; align-items: flex-end;">
                            <div>
                                <button type="submit" class="btn btn-primary">Apply Filters</button>
                                <a href="<%= request.getContextPath() %>/admin/campaigns" class="btn btn-secondary">Clear Filters</a>
                            </div>
                        </div>
                    </div>
                </form>
            </div>

            <div class="campaigns-table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Business</th>
                            <th>Budget</th>
                            <th>Duration</th>
                            <th>Status</th>
                            <th>Created</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if(campaigns != null && !campaigns.isEmpty()) { %>
                            <% for(Campaign campaign : campaigns) {
                                Business business = businessMap.get(campaign.getId());
                            %>
                                <tr>
                                    <td><%= campaign.getId() %></td>
                                    <td><%= campaign.getTitle() %></td>
                                    <td>
                                        <% if(business != null) { %>
                                            <%= business.getCompanyName() != null ? business.getCompanyName() : business.getUsername() %>
                                        <% } else { %>
                                            Unknown
                                        <% } %>
                                    </td>
                                    <td>$<%= campaign.getBudget() %></td>
                                    <td><%= campaign.getStartDate() %> to <%= campaign.getEndDate() %></td>
                                    <td><span class="status-<%= campaign.getStatus().toLowerCase() %>"><%= campaign.getStatus() %></span></td>
                                    <td><%= campaign.getCreatedAt() %></td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="<%= request.getContextPath() %>/campaign?id=<%= campaign.getId() %>" class="btn btn-small">View</a>

                                            <% if("active".equals(campaign.getStatus())) { %>
                                                <form action="<%= request.getContextPath() %>/admin/campaigns" method="post" style="display: inline;">
                                                    <input type="hidden" name="campaignId" value="<%= campaign.getId() %>">
                                                    <input type="hidden" name="action" value="cancel">
                                                    <button type="submit" class="btn btn-small btn-secondary" onclick="return confirm('Are you sure you want to cancel this campaign?')">Cancel</button>
                                                </form>
                                            <% } else if("cancelled".equals(campaign.getStatus())) { %>
                                                <form action="<%= request.getContextPath() %>/admin/campaigns" method="post" style="display: inline;">
                                                    <input type="hidden" name="campaignId" value="<%= campaign.getId() %>">
                                                    <input type="hidden" name="action" value="activate">
                                                    <button type="submit" class="btn btn-small btn-primary">Activate</button>
                                                </form>
                                            <% } %>

                                            <form action="<%= request.getContextPath() %>/admin/campaigns" method="post" style="display: inline;">
                                                <input type="hidden" name="campaignId" value="<%= campaign.getId() %>">
                                                <input type="hidden" name="action" value="delete">
                                                <button type="submit" class="btn btn-small btn-danger" onclick="return confirm('Are you sure you want to delete this campaign? This action cannot be undone.')">Delete</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            <% } %>
                        <% } else { %>
                            <tr>
                                <td colspan="8" class="empty-table">
                                    <div class="empty-state">
                                        <p>No campaigns found matching your criteria.</p>
                                    </div>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 Konnect. All rights reserved. <span class="admin-footer-note">Admin Panel</span></p>
    </footer>
</body>
</html>
