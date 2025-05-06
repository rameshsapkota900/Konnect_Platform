<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.BusinessProfile" %>
<%@ page import="com.konnect.model.User" %>
<%@ page import="com.konnect.dao.CampaignDAO" %>
<%
    // Get profile from request
    BusinessProfile profile = (BusinessProfile) request.getAttribute("profile");
    User profileUser = (User) request.getAttribute("profileUser");

    if (profile == null || profileUser == null) {
        // If not set, redirect to home page
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }

    // Check if user is logged in
    boolean isLoggedIn = session.getAttribute("user") != null;
    String role = (String) session.getAttribute("role");
    int userId = isLoggedIn ? (int) session.getAttribute("userId") : 0;

    // Check if viewing own profile
    boolean isOwnProfile = isLoggedIn && role.equals("business") && userId == profile.getUserId();

    // Get campaign count
    CampaignDAO campaignDAO = new CampaignDAO();
    int campaignCount = campaignDAO.countCampaignsByBusiness(profile.getUserId());
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="<%= profile.getBusinessName() %> - Business Profile" />
</jsp:include>

<jsp:include page="/includes/nav.jsp" />

    <main class="main-content">
        <div class="container">
            <div class="profile-container">
                <div class="profile-header">
                    <div class="profile-info">
                        <h1 class="profile-name"><%= profile.getBusinessName() %></h1>
                        <div class="profile-meta">
                            <span class="industry-badge"><%= profile.getIndustry() %></span>
                            <span class="campaign-count"><i class="fas fa-bullhorn"></i> <%= campaignCount %> Campaigns</span>
                        </div>
                    </div>
                    <div class="profile-actions">
                        <% if (isOwnProfile) { %>
                            <a href="<%= request.getContextPath() %>/profile" class="btn"><i class="fas fa-edit"></i> Edit Profile</a>
                        <% } else if (isLoggedIn && role.equals("creator")) { %>
                            <a href="<%= request.getContextPath() %>/message/conversation/<%= profile.getUserId() %>" class="btn"><i class="fas fa-envelope"></i> Message</a>
                            <a href="<%= request.getContextPath() %>/report/user/<%= profile.getUserId() %>" class="btn btn-outline"><i class="fas fa-flag"></i> Report</a>
                        <% } else if (isLoggedIn) { %>
                            <a href="<%= request.getContextPath() %>/report/user/<%= profile.getUserId() %>" class="btn btn-outline"><i class="fas fa-flag"></i> Report</a>
                        <% } else { %>
                            <a href="<%= request.getContextPath() %>/login.jsp" class="btn"><i class="fas fa-sign-in-alt"></i> Login to Connect</a>
                        <% } %>
                    </div>
                </div>

                <div class="profile-content">
                    <div class="profile-main">
                        <div class="profile-section">
                            <h2 class="section-title">About</h2>
                            <p class="profile-bio"><%= profile.getBusinessDescription() %></p>
                        </div>

                        <% if (profile.getWebsite() != null && !profile.getWebsite().isEmpty()) { %>
                            <div class="profile-section">
                                <h2 class="section-title">Website</h2>
                                <a href="<%= profile.getWebsite() %>" target="_blank" class="website-link">
                                    <i class="fas fa-globe"></i> <%= profile.getWebsite() %>
                                </a>
                            </div>
                        <% } %>

                        <% if (isLoggedIn && role.equals("creator")) { %>
                            <div class="profile-section">
                                <h2 class="section-title">Collaboration</h2>
                                <p>Interested in working with <%= profile.getBusinessName() %>? Send them a message to discuss potential collaborations.</p>
                                <a href="<%= request.getContextPath() %>/message/conversation/<%= profile.getUserId() %>" class="btn mt-3"><i class="fas fa-envelope"></i> Contact Business</a>
                            </div>
                        <% } %>
                    </div>

                    <div class="profile-sidebar">
                        <div class="profile-section">
                            <h2 class="section-title">Active Campaigns</h2>
                            <% if (campaignCount > 0) { %>
                                <p>This business has <%= campaignCount %> active campaign<%= campaignCount > 1 ? "s" : "" %>.</p>
                                <% if (isLoggedIn && role.equals("creator")) { %>
                                    <a href="<%= request.getContextPath() %>/campaign" class="btn btn-block mt-3"><i class="fas fa-bullhorn"></i> View Campaigns</a>
                                <% } %>
                            <% } else { %>
                                <p>This business doesn't have any active campaigns at the moment.</p>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
