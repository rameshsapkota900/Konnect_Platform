<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.Campaign" %>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Application" %>
<%
    // Check if user is logged in and has admin role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("admin")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Get campaign from request
    Campaign campaign = (Campaign) request.getAttribute("campaign");
    if (campaign == null) {
        // If not set, redirect to campaigns page
        response.sendRedirect(request.getContextPath() + "/admin/campaigns");
        return;
    }

    // Get applications for this campaign
    List<Application> applications = (List<Application>) request.getAttribute("applications");
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Campaign Details" />
</jsp:include>

<jsp:include page="/includes/nav-admin.jsp">
    <jsp:param name="active" value="campaigns" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Admin Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/admin/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/users"><i class="fas fa-users"></i> Manage Users</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/campaigns" class="active"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/reports"><i class="fas fa-flag"></i> Reports</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title">
                            <a href="<%= request.getContextPath() %>/admin/campaigns" class="back-link"><i class="fas fa-arrow-left"></i></a>
                            Campaign Details
                        </h2>
                        <div class="dashboard-actions">
                            <a href="#" onclick="confirmDelete(<%= campaign.getCampaignId() %>)" class="btn btn-danger"><i class="fas fa-trash-alt"></i> Delete Campaign</a>
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
                        <div class="campaign-details">
                            <div class="campaign-header">
                                <h3><%= campaign.getTitle() %></h3>
                                <span class="status-badge status-<%= campaign.getStatus() %>"><%= campaign.getStatus() %></span>
                            </div>

                            <div class="campaign-meta">
                                <div class="meta-item">
                                    <i class="fas fa-user"></i>
                                    <span>Business: <a href="<%= request.getContextPath() %>/admin/user/<%= campaign.getBusinessId() %>"><%= campaign.getBusinessUsername() %></a></span>
                                </div>
                                <div class="meta-item">
                                    <i class="fas fa-money-bill-wave"></i>
                                    <span>Budget: $<%= String.format("%.2f", campaign.getBudget()) %></span>
                                </div>
                                <div class="meta-item">
                                    <i class="fas fa-calendar-alt"></i>
                                    <span>Created: <%= campaign.getCreatedAt() != null ? campaign.getCreatedAt().toString() : "N/A" %></span>
                                </div>
                                <div class="meta-item">
                                    <i class="fas fa-calendar-check"></i>
                                    <span>Updated: <%= campaign.getUpdatedAt() != null ? campaign.getUpdatedAt().toString() : "N/A" %></span>
                                </div>
                            </div>

                            <div class="campaign-section">
                                <h4>Description</h4>
                                <p><%= campaign.getDescription() %></p>
                            </div>

                            <div class="campaign-section">
                                <h4>Goals</h4>
                                <p><%= campaign.getGoals() %></p>
                            </div>
                        </div>
                    </div>

                    <div class="card mt-4">
                        <h3 class="card-title"><i class="fas fa-file-alt"></i> Applications</h3>

                        <% if (applications == null || applications.isEmpty()) { %>
                            <div class="empty-state">
                                <i class="fas fa-file-alt empty-icon"></i>
                                <h3>No Applications</h3>
                                <p>This campaign hasn't received any applications yet.</p>
                            </div>
                        <% } else { %>
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Creator</th>
                                            <th>Status</th>
                                            <th>Applied On</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (Application app : applications) { %>
                                            <tr>
                                                <td><%= app.getApplicationId() %></td>
                                                <td><a href="<%= request.getContextPath() %>/admin/user/<%= app.getCreatorId() %>"><%= app.getCreatorUsername() %></a></td>
                                                <td><span class="status-badge status-<%= app.getStatus() %>"><%= app.getStatus() %></span></td>
                                                <td><%= app.getCreatedAt() != null ? app.getCreatedAt().toString().substring(0, 10) : "N/A" %></td>
                                                <td>
                                                    <div class="action-buttons">
                                                        <a href="<%= request.getContextPath() %>/admin/application/<%= app.getApplicationId() %>" class="btn-icon" title="View"><i class="fas fa-eye"></i></a>
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
        function confirmDelete(campaignId) {
            if (confirm("Are you sure you want to delete this campaign? This action cannot be undone.")) {
                window.location.href = "<%= request.getContextPath() %>/admin/campaign/delete/" + campaignId;
            }
        }
    </script>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
