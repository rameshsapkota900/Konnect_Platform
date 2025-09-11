<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.konnect.model.Application" %>
<%@ page import="com.konnect.model.Campaign" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Applications - Konnect</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
    <header>
        <div class="header-row">
            <a href="<%= request.getContextPath() %>/" class="header-logo">Konnect</a>
            <nav>
                <ul class="nav-list">
                    <li><a href="<%= request.getContextPath() %>/creator/dashboard">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/creator/profile">Profile</a></li>
                    <li><a href="<%= request.getContextPath() %>/creator/campaigns">Browse Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/creator/applications" class="active">My Applications</a></li>
                    <li><a href="<%= request.getContextPath() %>/messages">Messages</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">My Applications</div>
    </header>

    <main>
        <%
            List<Application> applications = (List<Application>)request.getAttribute("applications");
            Map<Integer, Campaign> campaignMap = (Map<Integer, Campaign>)request.getAttribute("campaignMap");
        %> 

        <section class="about-section">
            <div class="about-card">
                <div class="section-header">
                    <h2>My Applications</h2>
                    <p>Track and manage your campaign applications.</p>
                </div>

                <% if(request.getAttribute("success") != null) { %>
                    <div class="success-message">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                        <%= request.getAttribute("success") %>
                    </div>
                <% } %>

                <% if(request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <% if(applications == null || applications.isEmpty()) { %>
                    <div class="empty-state">
                        <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" stroke-linecap="round" stroke-linejoin="round" style="margin-bottom: 1.5rem;"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path><polyline points="14 2 14 8 20 8"></polyline><line x1="16" y1="13" x2="8" y2="13"></line><line x1="16" y1="17" x2="8" y2="17"></line><polyline points="10 9 9 9 8 9"></polyline></svg>
                        <h3>No Applications Yet</h3>
                        <p>You haven't applied to any campaigns yet. Start exploring available opportunities!</p>
                        <a href="<%= request.getContextPath() %>/creator/campaigns" class="btn btn-primary" style="margin-top: 1.5rem; display: inline-flex; align-items: center; gap: 0.5rem;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                            Browse Campaigns
                        </a>
                    </div>
                <% } else { %>
                    <div class="applications-grid">
                        <% for(Application app : applications) {
                            Campaign campaign = campaignMap.get(app.getCampaignId());
                            if(campaign != null) {
                        %>
                            <div class="application-card">
                                <div class="application-header">
                                    <h3><%= campaign.getTitle() %></h3>
                                    <span class="status-badge status-<%= app.getStatus().toLowerCase() %>"><%= app.getStatus() %></span>
                                </div>

                                <div class="application-details">
                                    <div class="detail-item">
                                        <span class="detail-label">Applied on</span>
                                        <span class="detail-value"><%= app.getCreatedAt() %></span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-label">Budget</span>
                                        <span class="detail-value">$<%= campaign.getBudget() %></span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-label">Campaign Status</span>
                                        <span class="status-badge status-<%= campaign.getStatus().toLowerCase() %>"><%= campaign.getStatus() %></span>
                                    </div>
                                </div>

                                <div class="application-message">
                                    <h4>Your Message</h4>
                                    <p><%= app.getMessage() %></p>
                                </div>

                                <div class="application-actions">
                                    <a href="<%= request.getContextPath() %>/campaign?id=<%= campaign.getId() %>" class="btn btn-primary">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>
                                        View Campaign
                                    </a>

                                    <% if("pending".equals(app.getStatus())) { %>
                                        <a href="<%= request.getContextPath() %>/creator/withdraw?id=<%= app.getId() %>" class="btn btn-secondary" onclick="return confirm('Are you sure you want to withdraw this application?')">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
                                            Withdraw
                                        </a>
                                    <% } %>
                                </div>
                            </div>
                        <%
                            }
                        }
                        %>
                    </div>
                <% } %>
            </div>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 Konnect. All rights reserved. <span class="admin-footer-note">Creator Portal</span></p>
    </footer>
</body>
</html>
