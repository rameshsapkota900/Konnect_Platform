<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.Application" %>
<%@ page import="com.konnect.model.Campaign" %>
<%
    // Check if user is logged in and has business role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("business")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Get application and campaign from request
    Application applicationData = (Application) request.getAttribute("application");
    Campaign campaign = (Campaign) request.getAttribute("campaign");

    if (applicationData == null || campaign == null) {
        // If not set, redirect to applications page
        response.sendRedirect(request.getContextPath() + "/application");
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Application Details" />
</jsp:include>

<jsp:include page="/includes/nav-business.jsp">
    <jsp:param name="active" value="applications" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Business Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile"><i class="fas fa-building"></i> Company Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile/creators"><i class="fas fa-users"></i> Find Creators</a></li>
                        <li><a href="<%= request.getContextPath() %>/application" class="active"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/message"><i class="fas fa-envelope"></i> Messages</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-file-alt"></i> Application Details</h2>
                        <div class="dashboard-actions">
                            <a href="<%= request.getContextPath() %>/application" class="btn btn-outline"><i class="fas fa-arrow-left"></i> Back to Applications</a>
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
                        <div class="application-header">
                            <div class="creator-info">
                                <h3>Application from <a href="<%= request.getContextPath() %>/profile/view/<%= applicationData.getCreatorId() %>"><%= applicationData.getCreatorUsername() %></a></h3>
                                <p>For campaign: <a href="<%= request.getContextPath() %>/campaign/view/<%= campaign.getCampaignId() %>"><%= campaign.getTitle() %></a></p>
                            </div>
                            <div class="application-status">
                                <span class="status-badge status-<%= applicationData.getStatus() %> status-large"><%= applicationData.getStatus() %></span>
                            </div>
                        </div>

                        <div class="application-details">
                            <div class="application-section">
                                <h4>Application Message</h4>
                                <div class="message-box">
                                    <p><%= applicationData.getMessage() %></p>
                                </div>
                            </div>

                            <div class="application-section">
                                <h4>Application Information</h4>
                                <div class="detail-item">
                                    <span class="detail-label">Applied On:</span>
                                    <span class="detail-value"><%= applicationData.getCreatedAt() != null ? applicationData.getCreatedAt().toString().substring(0, 10) : "N/A" %></span>
                                </div>
                                <% if (applicationData.getStatus().equals("approved") || applicationData.getStatus().equals("rejected")) { %>
                                <div class="detail-item">
                                    <span class="detail-label">Decision Date:</span>
                                    <span class="detail-value"><%= applicationData.getUpdatedAt() != null ? applicationData.getUpdatedAt().toString().substring(0, 10) : "N/A" %></span>
                                </div>
                                <% } %>
                            </div>
                        </div>

                        <% if (applicationData.getStatus().equals("pending")) { %>
                        <div class="application-actions btn-group">
                            <form action="<%= request.getContextPath() %>/application/update/<%= applicationData.getApplicationId() %>" method="post" class="inline-form">
                                <input type="hidden" name="status" value="approved">
                                <button type="submit" class="btn btn-success"><i class="fas fa-check"></i> Approve Application</button>
                            </form>
                            <form action="<%= request.getContextPath() %>/application/update/<%= applicationData.getApplicationId() %>" method="post" class="inline-form">
                                <input type="hidden" name="status" value="rejected">
                                <button type="submit" class="btn btn-danger"><i class="fas fa-times"></i> Reject Application</button>
                            </form>
                        </div>
                        <% } %>

                        <div class="application-actions mt-3 btn-group">
                            <a href="<%= request.getContextPath() %>/message/conversation/<%= applicationData.getCreatorId() %>" class="btn"><i class="fas fa-envelope"></i> Message Creator</a>
                            <a href="<%= request.getContextPath() %>/profile/view/<%= applicationData.getCreatorId() %>" class="btn btn-outline"><i class="fas fa-user-circle"></i> View Creator Profile</a>
                            <a href="<%= request.getContextPath() %>/report/user/<%= applicationData.getCreatorId() %>" class="btn btn-outline btn-sm"><i class="fas fa-flag"></i> Report Creator</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
