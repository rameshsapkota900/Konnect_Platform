<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="Admin Dashboard"/>
</jsp:include>

<h2>Admin Dashboard</h2>
<p>Overview of the Konnect platform.</p>

<div class="dashboard-stats">
    <div class="stat-card">
        <h3>Total Users</h3>
        <div class="stat-number"><%= request.getAttribute("totalUsers") != null ? request.getAttribute("totalUsers") : "N/A" %></div>
        <a href="<%= request.getContextPath() %>/admin/users" class="btn btn-sm">View Users</a>
    </div>
    <div class="stat-card success">
        <h3>Total Campaigns</h3>
        <div class="stat-number"><%= request.getAttribute("totalCampaigns") != null ? request.getAttribute("totalCampaigns") : "N/A" %></div>
        <a href="<%= request.getContextPath() %>/admin/campaigns" class="btn btn-sm btn-success">View Campaigns</a>
    </div>
    <div class="stat-card warning">
        <h3>New Reports</h3>
        <div class="stat-number"><%= request.getAttribute("newReports") != null ? request.getAttribute("newReports") : "N/A" %></div>
        <a href="<%= request.getContextPath() %>/admin/reports" class="btn btn-sm btn-warning">View Reports</a>
    </div>
</div>

<div class="card">
    <div class="card-header">Quick Actions</div>
    <div class="card-body">
        <a href="<%= request.getContextPath() %>/admin/users" class="btn">Manage Users</a>
        <a href="<%= request.getContextPath() %>/admin/campaigns" class="btn btn-secondary">View All Campaigns</a>
        <a href="<%= request.getContextPath() %>/admin/reports" class="btn btn-warning">Review Reports</a>
    </div>
</div>

<jsp:include page="/common/footer.jsp" />
