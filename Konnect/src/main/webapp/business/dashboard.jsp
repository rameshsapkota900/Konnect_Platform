<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="Business Dashboard"/>
</jsp:include>

<h2>Business Dashboard</h2>
<p>Manage your campaigns and connect with content creators.</p>

<%
    long myCampaigns = (Long) request.getAttribute("myCampaignsCount");
    long activeCampaigns = (Long) request.getAttribute("activeCampaignsCount");
    long pendingApps = (Long) request.getAttribute("pendingApplicationsCount");
%>

<div class="dashboard-stats">
    <div class="stat-card">
        <h3>My Campaigns</h3>
        <div class="stat-number"><%= myCampaigns %></div>
        <a href="<%= request.getContextPath() %>/business/campaigns" class="btn btn-sm">View Campaigns</a>
    </div>
    <div class="stat-card success">
        <h3>Active Campaigns</h3>
        <div class="stat-number"><%= activeCampaigns %></div>
        <a href="<%= request.getContextPath() %>/business/campaigns" class="btn btn-sm btn-success">View Campaigns</a>
    </div>
    <div class="stat-card warning">
        <h3>Pending Applications</h3>
        <div class="stat-number"><%= pendingApps %></div>
        <a href="<%= request.getContextPath() %>/business/applications" class="btn btn-sm btn-warning">View Applications</a>
    </div>
</div>

<div class="card">
    <div class="card-header">Quick Actions</div>
    <div class="card-body">
        <a href="<%= request.getContextPath() %>/business/campaigns?action=create" class="btn">Create New Campaign</a>
        <a href="<%= request.getContextPath() %>/business/search" class="btn btn-secondary">Search for Creators</a>
        <a href="<%= request.getContextPath() %>/business/chat" class="btn">View Chats</a>
    </div>
</div>

<jsp:include page="/common/footer.jsp" />
