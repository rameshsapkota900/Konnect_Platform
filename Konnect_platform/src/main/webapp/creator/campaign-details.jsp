<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.Campaign" %>
<%@ page import="com.konnect.dao.ApplicationDAO" %>
<%
    // Check if user is logged in and has creator role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("creator")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Get campaign from request
    Campaign campaign = (Campaign) request.getAttribute("campaign");
    if (campaign == null) {
        // If not set, redirect to campaigns page
        response.sendRedirect(request.getContextPath() + "/campaign");
        return;
    }
    
    // Check if creator has already applied
    int userId = (int) session.getAttribute("userId");
    ApplicationDAO applicationDAO = new ApplicationDAO();
    boolean hasApplied = applicationDAO.hasCreatorApplied(campaign.getCampaignId(), userId);
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Campaign Details" />
</jsp:include>

<jsp:include page="/includes/nav-creator.jsp">
    <jsp:param name="active" value="campaigns" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Creator Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile"><i class="fas fa-user-circle"></i> Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign" class="active"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/application"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/message"><i class="fas fa-envelope"></i> Messages</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-bullhorn"></i> Campaign Details</h2>
                        <div class="dashboard-actions">
                            <a href="<%= request.getContextPath() %>/campaign" class="btn btn-outline"><i class="fas fa-arrow-left"></i> Back to Campaigns</a>
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
                                <span class="budget-badge">$<%= String.format("%.2f", campaign.getBudget()) %></span>
                            </div>
                            
                            <div class="campaign-meta">
                                <div class="meta-item">
                                    <i class="fas fa-calendar-alt"></i>
                                    <span>Posted: <%= campaign.getCreatedAt() != null ? campaign.getCreatedAt().toString().substring(0, 10) : "N/A" %></span>
                                </div>
                                <div class="meta-item">
                                    <i class="fas fa-user"></i>
                                    <span>Business: <a href="<%= request.getContextPath() %>/profile/view/<%= campaign.getBusinessId() %>">View Profile</a></span>
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
                            
                            <div class="campaign-actions">
                                <% if (hasApplied) { %>
                                    <div class="alert alert-info">
                                        <i class="fas fa-info-circle"></i> You have already applied to this campaign.
                                        <a href="<%= request.getContextPath() %>/application" class="btn btn-sm">View Your Application</a>
                                    </div>
                                <% } else { %>
                                    <a href="<%= request.getContextPath() %>/application/apply/<%= campaign.getCampaignId() %>" class="btn btn-primary"><i class="fas fa-paper-plane"></i> Apply Now</a>
                                <% } %>
                                <a href="<%= request.getContextPath() %>/message/conversation/<%= campaign.getBusinessId() %>" class="btn"><i class="fas fa-envelope"></i> Message Business</a>
                                <a href="<%= request.getContextPath() %>/report/user/<%= campaign.getBusinessId() %>" class="btn btn-outline btn-sm"><i class="fas fa-flag"></i> Report</a>
                            </div>
                        </div>
                    </div>

                    <div class="card mt-4">
                        <h3 class="card-title"><i class="fas fa-lightbulb"></i> Tips for Applying</h3>
                        <ul class="tips-list">
                            <li><i class="fas fa-check"></i> Carefully read the campaign description and goals</li>
                            <li><i class="fas fa-check"></i> Highlight your relevant experience and skills</li>
                            <li><i class="fas fa-check"></i> Explain how you can help achieve the campaign goals</li>
                            <li><i class="fas fa-check"></i> Be professional and clear in your application</li>
                            <li><i class="fas fa-check"></i> Provide examples of similar work you've done</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
