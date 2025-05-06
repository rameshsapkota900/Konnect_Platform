<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.Application" %>
<%@ page import="com.konnect.model.Campaign" %>
<%
    // Check if user is logged in and has creator role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("creator")) {
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

<jsp:include page="/includes/nav-creator.jsp">
    <jsp:param name="active" value="applications" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Creator Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile"><i class="fas fa-user-circle"></i> Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
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
                        <div class="application-status">
                            <h3>Application Status</h3>
                            <div class="status-container">
                                <span class="status-badge status-<%= applicationData.getStatus() %> status-large"><%= applicationData.getStatus() %></span>
                                <% if (applicationData.getStatus().equals("approved")) { %>
                                    <p class="status-message">Congratulations! Your application has been approved. Please contact the business to discuss next steps.</p>
                                <% } else if (applicationData.getStatus().equals("rejected")) { %>
                                    <p class="status-message">Unfortunately, your application was not selected for this campaign. Don't be discouraged - keep applying to other opportunities!</p>
                                <% } else { %>
                                    <p class="status-message">Your application is currently under review. You will be notified when the business makes a decision.</p>
                                <% } %>
                            </div>
                        </div>

                        <div class="application-details">
                            <div class="application-section">
                                <h4>Campaign Information</h4>
                                <div class="detail-item">
                                    <span class="detail-label">Campaign:</span>
                                    <span class="detail-value"><a href="<%= request.getContextPath() %>/campaign/view/<%= campaign.getCampaignId() %>"><%= campaign.getTitle() %></a></span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">Business:</span>
                                    <span class="detail-value"><%= applicationData.getBusinessUsername() %></span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">Budget:</span>
                                    <span class="detail-value">$<%= String.format("%.2f", campaign.getBudget()) %></span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">Applied On:</span>
                                    <span class="detail-value"><%= applicationData.getCreatedAt() != null ? applicationData.getCreatedAt().toString().substring(0, 10) : "N/A" %></span>
                                </div>
                            </div>

                            <div class="application-section">
                                <h4>Your Application Message</h4>
                                <div class="message-box">
                                    <p><%= applicationData.getMessage() %></p>
                                </div>
                            </div>
                        </div>

                        <div class="application-actions">
                            <a href="<%= request.getContextPath() %>/message/conversation/<%= campaign.getBusinessId() %>" class="btn"><i class="fas fa-envelope"></i> Message Business</a>
                            <a href="<%= request.getContextPath() %>/campaign" class="btn btn-outline"><i class="fas fa-bullhorn"></i> Browse More Campaigns</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
