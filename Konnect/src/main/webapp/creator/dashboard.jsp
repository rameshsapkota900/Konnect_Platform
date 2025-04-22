<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.konnect.model.Profile" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="Creator Dashboard"/>
</jsp:include>

<h2>Creator Dashboard</h2>
<p>Welcome back! Manage your profile, campaigns, and connections.</p>

<%
    Profile profile = (Profile) request.getAttribute("profile");
    long pendingApps = (Long) request.getAttribute("pendingApplicationsCount");
    long acceptedApps = (Long) request.getAttribute("acceptedApplicationsCount");
    long invitesCount = (Long) request.getAttribute("invitationsCount");
%>

<div class="dashboard-stats">
    <div class="stat-card">
        <h3>Pending Applications</h3>
        <div class="stat-number"><%= pendingApps %></div>
        <a href="<%= request.getContextPath() %>/creator/applications" class="btn btn-sm">View Applications</a>
    </div>
    <div class="stat-card success">
        <h3>Accepted Campaigns</h3>
        <div class="stat-number"><%= acceptedApps %></div>
        <a href="<%= request.getContextPath() %>/creator/applications" class="btn btn-sm btn-success">View Applications</a>
    </div>
    <div class="stat-card warning">
        <h3>Pending Invitations</h3>
        <div class="stat-number"><%= invitesCount %></div>
        <a href="<%= request.getContextPath() %>/creator/applications#invitations" class="btn btn-sm btn-warning">View Invitations</a>
    </div>
</div>

<div class="card">
    <div class="card-header">Your Profile Summary</div>
    <div class="card-body">
        <% if (profile != null) { %>
            <p><strong>Full Name:</strong> <%= profile.getFullName() != null ? profile.getFullName() : "Not Set" %></p>
            <p><strong>Niche:</strong> <%= profile.getNiche() != null ? profile.getNiche() : "Not Set" %></p>
            <p><strong>Followers:</strong> <%= profile.getFollowerCount() %></p>
            <p><strong>Media Kit:</strong>
                <% if (profile.getMediaKitPath() != null && !profile.getMediaKitPath().isEmpty()) { %>
                    <a href="<%= request.getContextPath() %>/<%= profile.getMediaKitPath().replace(java.io.File.separatorChar, '/') %>" target="_blank">View Media Kit</a>
                <% } else { %>
                    Not Uploaded
                <% } %>
            </p>
            <a href="<%= request.getContextPath() %>/creator/profile" class="btn mt-2">Edit Profile</a>
        <% } else { %>
            <p class="text-danger">Profile not found. Please complete your profile.</p>
            <a href="<%= request.getContextPath() %>/creator/profile" class="btn">Create Profile</a>
        <% } %>
    </div>
</div>

<jsp:include page="/common/footer.jsp" />
