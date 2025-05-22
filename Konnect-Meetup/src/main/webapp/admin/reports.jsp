<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.konnect.model.Report" %>
<%@ page import="com.konnect.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Reports - Konnect</title>
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
                    <li><a href="<%= request.getContextPath() %>/admin/campaigns">Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/reports" class="active">Reports</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Admin Panel - Report Management</div>
    </header>

    <main>
        <%
            List<Report> reports = (List<Report>)request.getAttribute("reports");
            Map<Integer, User> reporterMap = (Map<Integer, User>)request.getAttribute("reporterMap");
            Map<Integer, User> reportedUserMap = (Map<Integer, User>)request.getAttribute("reportedUserMap");
            String statusFilter = (String)request.getAttribute("statusFilter");
        %>

        <section class="admin-section">
            <div class="section-header">
                <h2>Report Management</h2>
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

            <div class="filter-tabs">
                <a href="<%= request.getContextPath() %>/admin/reports" class="filter-tab <%= statusFilter == null ? "active" : "" %>">
                    All
                </a>
                <a href="<%= request.getContextPath() %>/admin/reports?status=pending" class="filter-tab <%= "pending".equals(statusFilter) ? "active" : "" %>">
                    Pending
                </a>
                <a href="<%= request.getContextPath() %>/admin/reports?status=resolved" class="filter-tab <%= "resolved".equals(statusFilter) ? "active" : "" %>">
                    Resolved
                </a>
                <a href="<%= request.getContextPath() %>/admin/reports?status=dismissed" class="filter-tab <%= "dismissed".equals(statusFilter) ? "active" : "" %>">
                    Dismissed
                </a>
            </div>

            <div class="reports-container">
                <% if(reports != null && !reports.isEmpty()) { %>
                    <% for(Report report : reports) {
                        User reporter = reporterMap.get(report.getId());
                        User reportedUser = reportedUserMap.get(report.getId());
                    %>
                        <div class="report-card">
                            <div class="report-header">
                                <h3>Report #<%= report.getId() %></h3>
                                <span class="status-<%= report.getStatus().toLowerCase() %>"><%= report.getStatus() %></span>
                            </div>

                            <div class="report-details">
                                <div class="report-info">
                                    <p><strong>Reason:</strong> <%= report.getReason() %></p>
                                    <p><strong>Reported On:</strong> <%= report.getCreatedAt() %></p>
                                    <% if(report.getUpdatedAt() != null) { %>
                                        <p><strong>Last Updated:</strong> <%= report.getUpdatedAt() %></p>
                                    <% } %>
                                </div>

                                <div class="report-users">
                                    <div class="report-user">
                                        <h4>Reporter</h4>
                                        <% if(reporter != null) { %>
                                            <p><strong>Username:</strong> <%= reporter.getUsername() %></p>
                                            <p><strong>Email:</strong> <%= reporter.getEmail() %></p>
                                            <p><strong>Role:</strong> <%= reporter.getRole() %></p>
                                            <a href="<%= request.getContextPath() %>/admin/user-details?id=<%= reporter.getId() %>" class="btn btn-small">View Profile</a>
                                        <% } else { %>
                                            <p>User information not available</p>
                                        <% } %>
                                    </div>

                                    <div class="report-user">
                                        <h4>Reported User</h4>
                                        <% if(reportedUser != null) { %>
                                            <p><strong>Username:</strong> <%= reportedUser.getUsername() %></p>
                                            <p><strong>Email:</strong> <%= reportedUser.getEmail() %></p>
                                            <p><strong>Role:</strong> <%= reportedUser.getRole() %></p>
                                            <p><strong>Status:</strong> <span class="status-<%= reportedUser.getStatus().toLowerCase() %>"><%= reportedUser.getStatus() %></span></p>
                                            <a href="<%= request.getContextPath() %>/admin/user-details?id=<%= reportedUser.getId() %>" class="btn btn-small">View Profile</a>
                                        <% } else { %>
                                            <p>User information not available</p>
                                        <% } %>
                                    </div>
                                </div>

                                <div class="report-description">
                                    <h4>Description</h4>
                                    <p><%= report.getDescription() != null ? report.getDescription() : "No description provided" %></p>
                                </div>

                                <% if("pending".equals(report.getStatus())) { %>
                                    <div class="report-actions">
                                        <form action="<%= request.getContextPath() %>/admin/reports" method="post">
                                            <input type="hidden" name="reportId" value="<%= report.getId() %>">
                                            <input type="hidden" name="action" value="resolve">

                                            <div class="form-group checkbox-inline">
                                                <label>
                                                    <input type="checkbox" name="banUser"> Ban reported user
                                                </label>
                                            </div>

                                            <div class="action-buttons">
                                                <button type="submit" class="btn btn-primary">Resolve Report</button>

                                                <button type="submit" class="btn btn-secondary" formaction="<%= request.getContextPath() %>/admin/reports"
                                                        formmethod="post" name="action" value="dismiss"
                                                        onclick="return confirm('Are you sure you want to dismiss this report?')">
                                                    Dismiss Report
                                                </button>
                                            </div>
                                        </form>
                                    </div>
                                <% } %>
                            </div>
                        </div>
                    <% } %>
                <% } else { %>
                    <div class="empty-state">
                        <p>No reports found matching your criteria.</p>
                    </div>
                <% } %>
            </div>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 Konnect. All rights reserved. <span class="admin-footer-note">Admin Panel</span></p>
    </footer>
</body>
</html>
