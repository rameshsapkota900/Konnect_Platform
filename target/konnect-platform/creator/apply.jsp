<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.Campaign" %>
<%@ page import="com.konnect.model.Creator" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apply to Campaign - Konnect</title>
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
                    <li><a href="<%= request.getContextPath() %>/creator/campaigns" class="active">Browse Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/creator/applications">My Applications</a></li>
                    <li><a href="<%= request.getContextPath() %>/messages">Messages</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Apply to Campaign</div>
        </header>

        <main>
            <%
                Campaign campaign = (Campaign)request.getAttribute("campaign");
                Creator creator = (Creator)request.getAttribute("creator");
            %>

        <section class="about-section">
            <div class="about-card">
                <div class="section-header">
                    <h2>Apply to Campaign</h2>
                    <p>Submit your application for <%= campaign.getTitle() %></p>
                </div>

                <% if(request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <div class="campaign-summary">
                    <h3>Campaign Summary</h3>
                    <div class="summary-grid">
                        <div class="summary-item">
                            <span class="summary-label">Budget</span>
                            <span class="summary-value">$<%= campaign.getBudget() %></span>
                        </div>
                        <div class="summary-item">
                            <span class="summary-label">Duration</span>
                            <span class="summary-value"><%= campaign.getStartDate() %> to <%= campaign.getEndDate() %></span>
                        </div>
                        <div class="summary-item full-width">
                            <span class="summary-label">Description</span>
                            <p class="summary-value"><%= campaign.getDescription() %></p>
                        </div>
                    <% if (campaign.getRequirements() != null && !campaign.getRequirements().isEmpty()) { %>
                            <div class="summary-item full-width">
                                <span class="summary-label">Requirements</span>
                                <p class="summary-value"><%= campaign.getRequirements() %></p>
                            </div>
                    <% } %>
                    </div>
                </div>

                <form action="<%= request.getContextPath() %>/creator/apply" method="post" class="form-card">
                    <input type="hidden" name="campaignId" value="<%= campaign.getId() %>">

                    <div class="form-group">
                        <label for="message" class="form-label">Application Message</label>
                        <div class="input-container">
                            <textarea id="message" name="message" rows="6" required class="form-textarea" placeholder="Explain why you're a good fit for this campaign and how you can help promote their product or service."></textarea>
                        </div>
                        <p class="form-hint">Make your application stand out by highlighting your relevant experience and audience engagement.</p>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                            Submit Application
                        </button>
                        <a href="<%= request.getContextPath() %>/campaign?id=<%= campaign.getId() %>" class="btn btn-secondary">
                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"></line><polyline points="12 19 5 12 12 5"></polyline></svg>
                            Cancel
                        </a>
                    </div>
                </form>
            </div>
            </section>
        </main>

        <footer>
        <p>&copy; 2025 Konnect. All rights reserved. <span class="admin-footer-note">Creator Portal</span></p>
        </footer>
</body>
</html>
