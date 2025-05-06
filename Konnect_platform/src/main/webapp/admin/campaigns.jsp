<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Campaign" %>
<%
    // Check if user is logged in and has admin role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("admin")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Get campaigns from request
    List<Campaign> campaigns = (List<Campaign>) request.getAttribute("campaigns");
    if (campaigns == null) {
        // If not set, redirect to admin servlet to get the data
        response.sendRedirect(request.getContextPath() + "/admin/campaigns");
        return;
    }

    // Get filter value
    String statusFilter = (String) request.getAttribute("statusFilter");
    if (statusFilter == null) statusFilter = "all";
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Manage Campaigns" />
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
                        <h2 class="dashboard-title"><i class="fas fa-bullhorn"></i> Manage Campaigns</h2>
                        <div class="dashboard-actions">
                            <form action="<%= request.getContextPath() %>/admin/search-campaigns" method="get" class="search-form">
                                <input type="text" name="keyword" placeholder="Search campaigns..." class="search-input">
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
                        <form action="<%= request.getContextPath() %>/admin/campaigns" method="get" id="filterForm">
                            <div class="filter-group">
                                <label>Status:</label>
                                <select name="status" id="statusFilter" onchange="document.getElementById('filterForm').submit()">
                                    <option value="all" <%= statusFilter.equals("all") ? "selected" : "" %>>All Status</option>
                                    <option value="active" <%= statusFilter.equals("active") ? "selected" : "" %>>Active</option>
                                    <option value="completed" <%= statusFilter.equals("completed") ? "selected" : "" %>>Completed</option>
                                    <option value="cancelled" <%= statusFilter.equals("cancelled") ? "selected" : "" %>>Cancelled</option>
                                </select>
                            </div>
                        </form>
                    </div>

                    <div class="card">
                        <% if (campaigns.isEmpty()) { %>
                            <div class="empty-state">
                                <i class="fas fa-bullhorn empty-icon"></i>
                                <h3>No Campaigns Found</h3>
                                <p>There are no campaigns matching your filter criteria.</p>
                            </div>
                        <% } else { %>
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Title</th>
                                            <th>Business</th>
                                            <th>Budget</th>
                                            <th>Status</th>
                                            <th>Created</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (Campaign campaign : campaigns) { %>
                                            <tr>
                                                <td><%= campaign.getCampaignId() %></td>
                                                <td><%= campaign.getTitle() %></td>
                                                <td><a href="<%= request.getContextPath() %>/admin/user/<%= campaign.getBusinessId() %>"><%= campaign.getBusinessUsername() %></a></td>
                                                <td>$<%= String.format("%.2f", campaign.getBudget()) %></td>
                                                <td><span class="status-badge status-<%= campaign.getStatus() %>"><%= campaign.getStatus() %></span></td>
                                                <td><%= campaign.getCreatedAt() != null ? campaign.getCreatedAt().toString().substring(0, 10) : "N/A" %></td>
                                                <td>
                                                    <div class="action-buttons">
                                                        <a href="<%= request.getContextPath() %>/admin/campaign/<%= campaign.getCampaignId() %>" class="btn-icon" title="View"><i class="fas fa-eye"></i></a>
                                                        <a href="#" onclick="confirmDelete(<%= campaign.getCampaignId() %>)" class="btn-icon" title="Delete"><i class="fas fa-trash-alt"></i></a>
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
