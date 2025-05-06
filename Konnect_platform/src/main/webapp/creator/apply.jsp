<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.Campaign" %>
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
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Apply to Campaign" />
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
                        <h2 class="dashboard-title"><i class="fas fa-paper-plane"></i> Apply to Campaign</h2>
                        <div class="dashboard-actions">
                            <a href="<%= request.getContextPath() %>/campaign/view/<%= campaign.getCampaignId() %>" class="btn btn-outline"><i class="fas fa-arrow-left"></i> Back to Campaign</a>
                        </div>
                    </div>

                    <% if (request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <i class="fas fa-exclamation-circle"></i> <%= request.getAttribute("error") %>
                    </div>
                    <% } %>

                    <div class="card">
                        <div class="campaign-summary">
                            <h3>Campaign: <%= campaign.getTitle() %></h3>
                            <div class="campaign-meta">
                                <span class="budget-badge">Budget: $<%= String.format("%.2f", campaign.getBudget()) %></span>
                            </div>
                        </div>

                        <form action="<%= request.getContextPath() %>/application/apply/<%= campaign.getCampaignId() %>" method="post" class="form">
                            <div class="form-group">
                                <label for="message">Application Message <span class="required">*</span></label>
                                <textarea id="message" name="message" class="form-control" rows="8" required></textarea>
                                <div class="form-text">
                                    Explain why you're a good fit for this campaign. Include relevant experience, ideas for content, and how you can help achieve the campaign goals.
                                </div>
                            </div>

                            <div class="form-actions">
                                <button type="submit" class="btn btn-primary"><i class="fas fa-paper-plane"></i> Submit Application</button>
                                <a href="<%= request.getContextPath() %>/campaign/view/<%= campaign.getCampaignId() %>" class="btn btn-outline">Cancel</a>
                            </div>
                        </form>
                    </div>

                    <div class="card mt-4">
                        <h3 class="card-title"><i class="fas fa-lightbulb"></i> Tips for a Successful Application</h3>
                        <ul class="tips-list">
                            <li><i class="fas fa-check"></i> Be specific about how you can help achieve the campaign goals</li>
                            <li><i class="fas fa-check"></i> Highlight your relevant experience and past collaborations</li>
                            <li><i class="fas fa-check"></i> Mention your audience demographics if they align with the campaign</li>
                            <li><i class="fas fa-check"></i> Share creative ideas for content that would work well for this campaign</li>
                            <li><i class="fas fa-check"></i> Keep your message professional and free of spelling/grammar errors</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
