<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Users - Konnect</title>
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
                    <li><a href="<%= request.getContextPath() %>/admin/users" class="active">Users</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/campaigns">Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/reports">Reports</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Admin Panel - User Management</div>
    </header>

    <main>
        <%
            List<User> users = (List<User>)request.getAttribute("users");
            String roleFilter = (String)request.getAttribute("roleFilter");
            String statusFilter = (String)request.getAttribute("statusFilter");
            String searchQuery = (String)request.getAttribute("searchQuery");
            User currentUser = (User)session.getAttribute("user");
        %>

        <section class="admin-section">
            <div class="section-header">
                <h2>User Management</h2>
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
                <h3>Filter Users</h3>
                <form action="<%= request.getContextPath() %>/admin/users" method="get" class="filter-form">
                    <div class="filter-controls">
                        <div class="filter-group">
                            <label for="search">Search</label>
                            <input type="text" id="search" name="search" value="<%= searchQuery != null ? searchQuery : "" %>" placeholder="Username or Email">
                        </div>

                        <div class="filter-group">
                            <label for="role">Role</label>
                            <select id="role" name="role">
                                <option value="">All Roles</option>
                                <option value="admin" <%= "admin".equals(roleFilter) ? "selected" : "" %>>Admin</option>
                                <option value="creator" <%= "creator".equals(roleFilter) ? "selected" : "" %>>Creator</option>
                                <option value="business" <%= "business".equals(roleFilter) ? "selected" : "" %>>Business</option>
                            </select>
                        </div>

                        <div class="filter-group">
                            <label for="status">Status</label>
                            <select id="status" name="status">
                                <option value="">All Statuses</option>
                                <option value="active" <%= "active".equals(statusFilter) ? "selected" : "" %>>Active</option>
                                <option value="banned" <%= "banned".equals(statusFilter) ? "selected" : "" %>>Banned</option>
                            </select>
                        </div>

                        <div class="filter-group" style="justify-content: flex-end; align-items: flex-end;">
                            <div>
                                <button type="submit" class="btn btn-primary">Apply Filters</button>
                                <a href="<%= request.getContextPath() %>/admin/users" class="btn btn-secondary">Clear Filters</a>
                            </div>
                        </div>
                    </div>
                </form>
            </div>

            <div class="users-table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Joined</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if(users != null && !users.isEmpty()) { %>
                            <% for(User user : users) { %>
                                <tr>
                                    <td><%= user.getId() %></td>
                                    <td><%= user.getUsername() %></td>
                                    <td><%= user.getEmail() %></td>
                                    <td><%= user.getRole() %></td>
                                    <td><span class="status-<%= user.getStatus().toLowerCase() %>"><%= user.getStatus() %></span></td>
                                    <td><%= user.getCreatedAt() %></td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="<%= request.getContextPath() %>/admin/user-details?id=<%= user.getId() %>" class="btn btn-small">View</a>

                                            <% if(user.getId() != currentUser.getId()) { %>
                                                <% if("active".equals(user.getStatus())) { %>
                                                    <form action="<%= request.getContextPath() %>/admin/users" method="post" style="display: inline;">
                                                        <input type="hidden" name="userId" value="<%= user.getId() %>">
                                                        <input type="hidden" name="action" value="ban">
                                                        <button type="submit" class="btn btn-small btn-danger" onclick="return confirm('Are you sure you want to ban this user?')">Ban</button>
                                                    </form>
                                                <% } else if("banned".equals(user.getStatus())) { %>
                                                    <form action="<%= request.getContextPath() %>/admin/users" method="post" style="display: inline;">
                                                        <input type="hidden" name="userId" value="<%= user.getId() %>">
                                                        <input type="hidden" name="action" value="unban">
                                                        <button type="submit" class="btn btn-small btn-primary">Unban</button>
                                                    </form>
                                                <% } %>
                                            <% } %>
                                        </div>
                                    </td>
                                </tr>
                            <% } %>
                        <% } else { %>
                            <tr>
                                <td colspan="7" class="empty-table">No users found matching your criteria.</td>
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
