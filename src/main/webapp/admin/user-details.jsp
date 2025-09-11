<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.User" %>
<%@ page import="com.konnect.model.Creator" %>
<%@ page import="com.konnect.model.Business" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin User Details - Konnect</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
    <header>
        <div class="header-row">
            <a href="<%= request.getContextPath() %>/admin/dashboard" class="header-logo">
                Konnect
            </a>
            <nav>
                <ul class="nav-list">
                    <li><a href="<%= request.getContextPath() %>/admin/dashboard">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/users" class="active">Users</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/campaigns">Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/reports">Reports</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Admin Panel - User Details</div>
    </header>

    <main>
        <%
            User userDetails = (User)request.getAttribute("userDetails");
            Creator creator = (Creator)request.getAttribute("creator");
            Business business = (Business)request.getAttribute("business");
        %>

        <section class="admin-section">
            <div class="section-header">
                <h2>User Details</h2>
                <a href="<%= request.getContextPath() %>/admin/users" class="btn btn-secondary">Back to Users</a>
            </div>

            <% if(request.getAttribute("error") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>

            <div class="user-details-container">
                <div class="user-details-card">
                    <h3>Basic Information</h3>
                    <div class="info-grid">
                        <div class="info-item">
                            <span class="label">ID:</span>
                            <span class="value"><%= userDetails.getId() %></span>
                        </div>
                        <div class="info-item">
                            <span class="label">Username:</span>
                            <span class="value"><%= userDetails.getUsername() %></span>
                        </div>
                        <div class="info-item">
                            <span class="label">Email:</span>
                            <span class="value"><%= userDetails.getEmail() %></span>
                        </div>
                        <div class="info-item">
                            <span class="label">Role:</span>
                            <span class="value"><%= userDetails.getRole() %></span>
                        </div>
                        <div class="info-item">
                            <span class="label">Status:</span>
                            <span class="value status-<%= userDetails.getStatus().toLowerCase() %>"><%= userDetails.getStatus() %></span>
                        </div>
                        <div class="info-item">
                            <span class="label">Joined:</span>
                            <span class="value"><%= userDetails.getCreatedAt() %></span>
                        </div>
                        <% if(userDetails.getUpdatedAt() != null) { %>
                            <div class="info-item">
                                <span class="label">Last Updated:</span>
                                <span class="value"><%= userDetails.getUpdatedAt() %></span>
                            </div>
                        <% } %>
                    </div>

                    <% if(userDetails.getId() != ((User)session.getAttribute("user")).getId()) { %>
                        <div class="user-actions">
                            <% if("active".equals(userDetails.getStatus())) { %>
                                <form action="<%= request.getContextPath() %>/admin/users" method="post" style="display: inline;">
                                    <input type="hidden" name="userId" value="<%= userDetails.getId() %>">
                                    <input type="hidden" name="action" value="ban">
                                    <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to ban this user?')">
                                        Ban User
                                    </button>
                                </form>
                            <% } else if("banned".equals(userDetails.getStatus())) { %>
                                <form action="<%= request.getContextPath() %>/admin/users" method="post" style="display: inline;">
                                    <input type="hidden" name="userId" value="<%= userDetails.getId() %>">
                                    <input type="hidden" name="action" value="unban">
                                    <button type="submit" class="btn btn-primary">
                                        Unban User
                                    </button>
                                </form>
                            <% } %>
                        </div>
                    <% } %>
                </div>

                <% if("creator".equals(userDetails.getRole()) && creator != null) { %>
                    <div class="user-details-card">
                        <h3>Creator Profile</h3>
                        <div class="info-grid">
                            <div class="info-item">
                                <span class="label">Bio:</span>
                                <span class="value"><%= creator.getBio() != null ? creator.getBio() : "Not provided" %></span>
                            </div>
                            <div class="info-item">
                                <span class="label">Followers:</span>
                                <span class="value"><%= creator.getFollowerCount() %></span>
                            </div>
                            <div class="info-item">
                                <span class="label">Pricing:</span>
                                <span class="value">$<%= creator.getPricingPerPost() %> per post</span>
                            </div>

                            <div class="info-item full-width">
                                <span class="label">Social Media:</span>
                                <div class="social-links">
                                    <% if(creator.getInstagramLink() != null && !creator.getInstagramLink().isEmpty()) { %>
                                        <a href="<%= creator.getInstagramLink() %>" target="_blank" class="social-link">Instagram</a>
                                    <% } %>
                                    <% if(creator.getTiktokLink() != null && !creator.getTiktokLink().isEmpty()) { %>
                                        <a href="<%= creator.getTiktokLink() %>" target="_blank" class="social-link">TikTok</a>
                                    <% } %>
                                    <% if(creator.getYoutubeLink() != null && !creator.getYoutubeLink().isEmpty()) { %>
                                        <a href="<%= creator.getYoutubeLink() %>" target="_blank" class="social-link">YouTube</a>
                                    <% } %>
                                </div>
                            </div>

                            <% if(creator.getInterests() != null && !creator.getInterests().isEmpty()) { %>
                                <div class="info-item full-width">
                                    <span class="label">Interests:</span>
                                    <div class="interests-tags">
                                        <% for(String interest : creator.getInterests()) { %>
                                            <span class="interest-tag"><%= interest %></span>
                                        <% } %>
                                    </div>
                                </div>
                            <% } %>
                        </div>
                    </div>
                <% } else if("business".equals(userDetails.getRole()) && business != null) { %>
                    <div class="user-details-card">
                        <h3>Business Profile</h3>
                        <div class="info-grid">
                            <div class="info-item">
                                <span class="label">Company Name:</span>
                                <span class="value"><%= business.getCompanyName() != null ? business.getCompanyName() : "Not provided" %></span>
                            </div>
                            <div class="info-item">
                                <span class="label">Industry:</span>
                                <span class="value"><%= business.getIndustry() != null ? business.getIndustry() : "Not provided" %></span>
                            </div>
                            <div class="info-item">
                                <span class="label">Website:</span>
                                <span class="value">
                                    <% if(business.getWebsite() != null && !business.getWebsite().isEmpty()) { %>
                                        <a href="<%= business.getWebsite() %>" target="_blank"><%= business.getWebsite() %></a>
                                    <% } else { %>
                                        Not provided
                                    <% } %>
                                </span>
                            </div>
                            <div class="info-item">
                                <span class="label">Contact Phone:</span>
                                <span class="value"><%= business.getContactPhone() != null ? business.getContactPhone() : "Not provided" %></span>
                            </div>
                            <div class="info-item full-width">
                                <span class="label">Description:</span>
                                <span class="value"><%= business.getDescription() != null ? business.getDescription() : "Not provided" %></span>
                            </div>
                        </div>
                    </div>
                <% } %>
            </div>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 Konnect. All rights reserved. <span class="admin-footer-note">Admin Panel</span></p>
    </footer>
</body>
</html>
