<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.User" %>
<%
    // Check if user is logged in and has admin role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("admin")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Get users from request
    List<User> users = (List<User>) request.getAttribute("users");
    if (users == null) {
        // If not set, redirect to admin servlet to get the data
        response.sendRedirect(request.getContextPath() + "/admin/users");
        return;
    }

    // Get filter values
    String roleFilter = (String) request.getAttribute("roleFilter");
    String statusFilter = (String) request.getAttribute("statusFilter");

    if (roleFilter == null) roleFilter = "all";
    if (statusFilter == null) statusFilter = "all";
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Manage Users" />
</jsp:include>

<jsp:include page="/includes/nav-admin.jsp">
    <jsp:param name="active" value="users" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Admin Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/admin/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/users" class="active"><i class="fas fa-users"></i> Manage Users</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/campaigns"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/reports"><i class="fas fa-flag"></i> Reports</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-users"></i> Manage Users</h2>
                        <div class="dashboard-actions">
                            <form action="<%= request.getContextPath() %>/admin/search-users" method="get" class="search-form">
                                <input type="text" name="keyword" placeholder="Search users..." class="search-input">
                                <button type="submit" class="search-button"><i class="fas fa-search"></i></button>
                            </form>
                        </div>
                    </div>

                    <% if (request.getAttribute("success") != null) { %>
                    <div class="success-message">
                        <i class="fas fa-check-circle"></i> <%= request.getAttribute("success") %>
                    </div>
                    <% } %>

                    <% if (request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <i class="fas fa-exclamation-circle"></i> <%= request.getAttribute("error") %>
                    </div>
                    <% } %>

                    <div class="filter-bar">
                        <form action="<%= request.getContextPath() %>/admin/users" method="get" id="filterForm">
                            <div class="filter-group">
                                <label>Role:</label>
                                <select name="role" id="roleFilter" onchange="document.getElementById('filterForm').submit()">
                                    <option value="all" <%= roleFilter.equals("all") ? "selected" : "" %>>All Roles</option>
                                    <option value="admin" <%= roleFilter.equals("admin") ? "selected" : "" %>>Admin</option>
                                    <option value="business" <%= roleFilter.equals("business") ? "selected" : "" %>>Business</option>
                                    <option value="creator" <%= roleFilter.equals("creator") ? "selected" : "" %>>Creator</option>
                                </select>
                            </div>
                            <div class="filter-group">
                                <label>Status:</label>
                                <select name="status" id="statusFilter" onchange="document.getElementById('filterForm').submit()">
                                    <option value="all" <%= statusFilter.equals("all") ? "selected" : "" %>>All Status</option>
                                    <option value="active" <%= statusFilter.equals("active") ? "selected" : "" %>>Active</option>
                                    <option value="banned" <%= statusFilter.equals("banned") ? "selected" : "" %>>Banned</option>
                                </select>
                            </div>
                        </form>
                    </div>

                    <div class="card">
                        <% if (users.isEmpty()) { %>
                            <div class="empty-state">
                                <i class="fas fa-users empty-icon"></i>
                                <h3>No Users Found</h3>
                                <p>No users match your search or filter criteria.</p>
                            </div>
                        <% } else { %>
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Username</th>
                                            <th>Email</th>
                                            <th>Role</th>
                                            <th>Status</th>
                                            <th>Verified</th>
                                            <th>Created</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (User user : users) { %>
                                            <tr>
                                                <td><%= user.getUserId() %></td>
                                                <td><%= user.getUsername() %></td>
                                                <td><%= user.getEmail() %></td>
                                                <td><span class="role-badge role-<%= user.getRole() %>"><%= user.getRole() %></span></td>
                                                <td><span class="status-badge status-<%= user.getStatus() %>"><%= user.getStatus() %></span></td>
                                                <td><%= user.isVerified() ? "<i class='fas fa-check-circle text-success'></i>" : "<i class='fas fa-times-circle text-danger'></i>" %></td>
                                                <td><%= user.getCreatedAt() != null ? user.getCreatedAt().toString().substring(0, 10) : "N/A" %></td>
                                                <td>
                                                    <div class="action-buttons">
                                                        <a href="<%= request.getContextPath() %>/admin/user/<%= user.getUserId() %>" class="btn-icon" title="View"><i class="fas fa-eye"></i></a>
                                                        <% if (user.getStatus().equals("active")) { %>
                                                            <a href="#" onclick="confirmBan(<%= user.getUserId() %>)" class="btn-icon" title="Ban"><i class="fas fa-ban"></i></a>
                                                        <% } else { %>
                                                            <a href="#" onclick="confirmUnban(<%= user.getUserId() %>)" class="btn-icon" title="Unban"><i class="fas fa-undo"></i></a>
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
                </div>
            </div>
        </div>
    </main>

    <script>
        function confirmBan(userId) {
            if (confirm("Are you sure you want to ban this user? They will no longer be able to access the platform.")) {
                window.location.href = "<%= request.getContextPath() %>/admin/ban-user/" + userId;
            }
        }

        function confirmUnban(userId) {
            if (confirm("Are you sure you want to unban this user? They will regain access to the platform.")) {
                window.location.href = "<%= request.getContextPath() %>/admin/unban-user/" + userId;
            }
        }
    </script>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
