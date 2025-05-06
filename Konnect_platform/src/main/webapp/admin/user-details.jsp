<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.User" %>
<%@ page import="com.konnect.model.CreatorProfile" %>
<%@ page import="com.konnect.model.BusinessProfile" %>
<%
    // Check if user is logged in and has admin role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("admin")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Get user from request
    User user = (User) request.getAttribute("user");
    if (user == null) {
        // If not set, redirect to users page
        response.sendRedirect(request.getContextPath() + "/admin/users");
        return;
    }

    // Get profile if available
    CreatorProfile creatorProfile = (CreatorProfile) request.getAttribute("creatorProfile");
    BusinessProfile businessProfile = (BusinessProfile) request.getAttribute("businessProfile");
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="User Details" />
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
                        <h2 class="dashboard-title">
                            <a href="<%= request.getContextPath() %>/admin/users" class="back-link"><i class="fas fa-arrow-left"></i></a>
                            User Details
                        </h2>
                        <div class="dashboard-actions">
                            <% if (user.getStatus().equals("active")) { %>
                                <a href="#" onclick="confirmBan(<%= user.getUserId() %>)" class="btn btn-danger"><i class="fas fa-ban"></i> Ban User</a>
                            <% } else { %>
                                <a href="#" onclick="confirmUnban(<%= user.getUserId() %>)" class="btn btn-success"><i class="fas fa-undo"></i> Unban User</a>
                            <% } %>
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

                    <div class="card">
                        <div class="user-details">
                            <div class="user-header">
                                <h3><%= user.getUsername() %></h3>
                                <div class="user-badges">
                                    <span class="role-badge role-<%= user.getRole() %>"><%= user.getRole() %></span>
                                    <span class="status-badge status-<%= user.getStatus() %>"><%= user.getStatus() %></span>
                                </div>
                            </div>

                            <div class="detail-section">
                                <h4>Account Information</h4>
                                <div class="detail-grid">
                                    <div class="detail-item">
                                        <span class="detail-label">User ID:</span>
                                        <span class="detail-value"><%= user.getUserId() %></span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-label">Email:</span>
                                        <span class="detail-value"><%= user.getEmail() %></span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-label">Verified:</span>
                                        <span class="detail-value"><%= user.isVerified() ? "Yes" : "No" %></span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-label">Created:</span>
                                        <span class="detail-value"><%= user.getCreatedAt() != null ? user.getCreatedAt().toString() : "N/A" %></span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-label">Last Updated:</span>
                                        <span class="detail-value"><%= user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : "N/A" %></span>
                                    </div>
                                </div>
                            </div>

                            <% if (creatorProfile != null) { %>
                                <div class="detail-section">
                                    <h4>Creator Profile</h4>
                                    <div class="detail-grid">
                                        <div class="detail-item">
                                            <span class="detail-label">Full Name:</span>
                                            <span class="detail-value"><%= creatorProfile.getFullName() %></span>
                                        </div>
                                        <div class="detail-item">
                                            <span class="detail-label">Bio:</span>
                                            <span class="detail-value"><%= creatorProfile.getBio() %></span>
                                        </div>
                                        <div class="detail-item">
                                            <span class="detail-label">Interests:</span>
                                            <span class="detail-value"><%= creatorProfile.getInterests() %></span>
                                        </div>
                                        <div class="detail-item">
                                            <span class="detail-label">Instagram:</span>
                                            <span class="detail-value">
                                                <% if (creatorProfile.getInstagramLink() != null && !creatorProfile.getInstagramLink().isEmpty()) { %>
                                                    <a href="<%= creatorProfile.getInstagramLink() %>" target="_blank"><%= creatorProfile.getInstagramLink() %></a>
                                                    (<%= creatorProfile.getInstagramFollowers() %> followers)
                                                <% } else { %>
                                                    Not provided
                                                <% } %>
                                            </span>
                                        </div>
                                        <div class="detail-item">
                                            <span class="detail-label">YouTube:</span>
                                            <span class="detail-value">
                                                <% if (creatorProfile.getYoutubeLink() != null && !creatorProfile.getYoutubeLink().isEmpty()) { %>
                                                    <a href="<%= creatorProfile.getYoutubeLink() %>" target="_blank"><%= creatorProfile.getYoutubeLink() %></a>
                                                    (<%= creatorProfile.getYoutubeFollowers() %> subscribers)
                                                <% } else { %>
                                                    Not provided
                                                <% } %>
                                            </span>
                                        </div>
                                        <div class="detail-item">
                                            <span class="detail-label">TikTok:</span>
                                            <span class="detail-value">
                                                <% if (creatorProfile.getTiktokLink() != null && !creatorProfile.getTiktokLink().isEmpty()) { %>
                                                    <a href="<%= creatorProfile.getTiktokLink() %>" target="_blank"><%= creatorProfile.getTiktokLink() %></a>
                                                    (<%= creatorProfile.getTiktokFollowers() %> followers)
                                                <% } else { %>
                                                    Not provided
                                                <% } %>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            <% } %>

                            <% if (businessProfile != null) { %>
                                <div class="detail-section">
                                    <h4>Business Profile</h4>
                                    <div class="detail-grid">
                                        <div class="detail-item">
                                            <span class="detail-label">Business Name:</span>
                                            <span class="detail-value"><%= businessProfile.getBusinessName() %></span>
                                        </div>
                                        <div class="detail-item">
                                            <span class="detail-label">Description:</span>
                                            <span class="detail-value"><%= businessProfile.getBusinessDescription() %></span>
                                        </div>
                                        <div class="detail-item">
                                            <span class="detail-label">Industry:</span>
                                            <span class="detail-value"><%= businessProfile.getIndustry() %></span>
                                        </div>
                                        <div class="detail-item">
                                            <span class="detail-label">Website:</span>
                                            <span class="detail-value">
                                                <% if (businessProfile.getWebsite() != null && !businessProfile.getWebsite().isEmpty()) { %>
                                                    <a href="<%= businessProfile.getWebsite() %>" target="_blank"><%= businessProfile.getWebsite() %></a>
                                                <% } else { %>
                                                    Not provided
                                                <% } %>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            <% } %>
                        </div>
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
