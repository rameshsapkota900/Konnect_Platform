<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.CreatorProfile" %>
<%@ page import="com.konnect.model.User" %>
<%
    // Get profile from request
    CreatorProfile profile = (CreatorProfile) request.getAttribute("profile");
    User profileUser = (User) request.getAttribute("profileUser");

    if (profile == null || profileUser == null) {
        // If not set, redirect to home page
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }

    // Ensure profile has a userId set
    if (profile.getUserId() == 0) {
        profile.setUserId(profileUser.getUserId());
    }

    // Check if user is logged in
    boolean isLoggedIn = session.getAttribute("user") != null;
    String role = (String) session.getAttribute("role");
    int userId = isLoggedIn ? (int) session.getAttribute("userId") : 0;

    // Check if viewing own profile
    boolean isOwnProfile = isLoggedIn && role.equals("creator") && userId == profile.getUserId();

    // Helper method to format follower counts is defined below using JSP declaration
%>

<%!
    // Helper method to format follower counts
    private String formatFollowers(int followers) {
        if (followers < 1000) {
            return String.valueOf(followers);
        } else if (followers < 1000000) {
            return String.format("%.1fK", followers / 1000.0);
        } else {
            return String.format("%.1fM", followers / 1000000.0);
        }
    }
%>


<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="<%= profile.getFullName() != null ? profile.getFullName() : profileUser.getUsername() %> - Creator Profile" />
</jsp:include>

<jsp:include page="/includes/nav.jsp" />

    <main class="main-content">
        <div class="container">
            <div class="profile-container">
                <div class="profile-header">
                    <div class="profile-info">
                        <h1 class="profile-name"><%= profile.getFullName() != null ? profile.getFullName() : profileUser.getUsername() %></h1>
                        <div class="profile-stats">
                            <% if (profile.getInstagramFollowers() > 0) { %>
                                <div class="stat-item">
                                    <i class="fab fa-instagram"></i>
                                    <span class="stat-value"><%= formatFollowers(profile.getInstagramFollowers()) %></span>
                                    <span class="stat-label">Instagram</span>
                                </div>
                            <% } %>
                            <% if (profile.getYoutubeFollowers() > 0) { %>
                                <div class="stat-item">
                                    <i class="fab fa-youtube"></i>
                                    <span class="stat-value"><%= formatFollowers(profile.getYoutubeFollowers()) %></span>
                                    <span class="stat-label">YouTube</span>
                                </div>
                            <% } %>
                            <% if (profile.getTiktokFollowers() > 0) { %>
                                <div class="stat-item">
                                    <i class="fab fa-tiktok"></i>
                                    <span class="stat-value"><%= formatFollowers(profile.getTiktokFollowers()) %></span>
                                    <span class="stat-label">TikTok</span>
                                </div>
                            <% } %>
                            <div class="stat-item">
                                <i class="fas fa-users"></i>
                                <span class="stat-value"><%= formatFollowers(profile.getTotalFollowers()) %></span>
                                <span class="stat-label">Total</span>
                            </div>
                        </div>
                    </div>
                    <div class="profile-actions">
                        <% if (isOwnProfile) { %>
                            <a href="<%= request.getContextPath() %>/profile" class="btn"><i class="fas fa-edit"></i> Edit Profile</a>
                        <% } else if (isLoggedIn && role.equals("business")) { %>
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
                            <p class="profile-bio"><%= profile.getBio() != null ? profile.getBio() : "No bio available yet." %></p>
                        </div>

                        <div class="profile-section">
                            <h2 class="section-title">Content Categories</h2>
                            <div class="tag-list">
                                <%
                                    if (profile.getInterests() != null) {
                                        String[] interests = profile.getInterests().split(",");
                                        for (String interest : interests) {
                                            if (interest.trim().length() > 0) {
                                %>
                                                <span class="tag"><%= interest.trim() %></span>
                                <%
                                            }
                                        }
                                    }
                                %>
                            </div>
                        </div>

                        <div class="profile-section">
                            <h2 class="section-title">Social Media</h2>
                            <div class="social-links">
                                <% if (profile.getInstagramLink() != null && !profile.getInstagramLink().isEmpty()) { %>
                                    <a href="<%= profile.getInstagramLink() %>" target="_blank" class="social-link">
                                        <i class="fab fa-instagram"></i>
                                        <span>Instagram</span>
                                    </a>
                                <% } %>
                                <% if (profile.getYoutubeLink() != null && !profile.getYoutubeLink().isEmpty()) { %>
                                    <a href="<%= profile.getYoutubeLink() %>" target="_blank" class="social-link">
                                        <i class="fab fa-youtube"></i>
                                        <span>YouTube</span>
                                    </a>
                                <% } %>
                                <% if (profile.getTiktokLink() != null && !profile.getTiktokLink().isEmpty()) { %>
                                    <a href="<%= profile.getTiktokLink() %>" target="_blank" class="social-link">
                                        <i class="fab fa-tiktok"></i>
                                        <span>TikTok</span>
                                    </a>
                                <% } %>
                            </div>
                        </div>
                    </div>

                    <div class="profile-sidebar">
                        <div class="profile-section pricing-section">
                            <h2 class="section-title">Collaboration Pricing</h2>
                            <div class="pricing-table">
                                <% if (profile.getPostPrice() > 0) { %>
                                    <div class="price-item">
                                        <div class="price-icon"><i class="fas fa-image"></i></div>
                                        <div class="price-details">
                                            <span class="price-label">Post</span>
                                            <span class="price-value">$<%= String.format("%.2f", profile.getPostPrice()) %></span>
                                        </div>
                                    </div>
                                <% } %>
                                <% if (profile.getStoryPrice() > 0) { %>
                                    <div class="price-item">
                                        <div class="price-icon"><i class="fas fa-clock"></i></div>
                                        <div class="price-details">
                                            <span class="price-label">Story</span>
                                            <span class="price-value">$<%= String.format("%.2f", profile.getStoryPrice()) %></span>
                                        </div>
                                    </div>
                                <% } %>
                                <% if (profile.getVideoPrice() > 0) { %>
                                    <div class="price-item">
                                        <div class="price-icon"><i class="fas fa-video"></i></div>
                                        <div class="price-details">
                                            <span class="price-label">Video</span>
                                            <span class="price-value">$<%= String.format("%.2f", profile.getVideoPrice()) %></span>
                                        </div>
                                    </div>
                                <% } %>
                            </div>
                            <% if (isLoggedIn && role.equals("business")) { %>
                                <div class="pricing-cta">
                                    <a href="<%= request.getContextPath() %>/message/conversation/<%= profile.getUserId() %>" class="btn btn-block"><i class="fas fa-envelope"></i> Contact for Collaboration</a>
                                </div>
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
