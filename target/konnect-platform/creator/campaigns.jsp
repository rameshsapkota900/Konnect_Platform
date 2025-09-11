<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.konnect.model.Campaign" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Browse Campaigns - Konnect</title>
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
        <div class="header-subtitle">Browse Campaigns</div>
    </header>

    <main>
        <%
            List<Campaign> campaigns = (List<Campaign>)request.getAttribute("campaigns");
            Map<Integer, Boolean> meetsRequirementMap = (Map<Integer, Boolean>)request.getAttribute("meetsRequirementMap");
            Map<Integer, Boolean> hasAppliedMap = (Map<Integer, Boolean>)request.getAttribute("hasAppliedMap");
            String keyword = (String)request.getAttribute("keyword");
            String minBudget = (String)request.getAttribute("minBudget");
            String interestFilter = (String)request.getAttribute("interestFilter");
            List<String> allInterests = (List<String>)request.getAttribute("allInterests");
        %>

        <section class="about-section">
            <div class="about-card">
                <div class="section-header">
                    <h2>Available Campaigns</h2>
                    <p>Browse and apply to campaigns that match your content style and audience.</p>
                </div>

                <% if(request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <div class="filter-section light">
                    <h3>Filter Campaigns</h3>
                    <form action="<%= request.getContextPath() %>/creator/campaigns" method="get" class="form-grid">
                        <div class="form-group">
                            <label for="keyword" class="form-label">Keyword</label>
                            <div class="search-input-container">
                                <input type="text" id="keyword" name="keyword" value="<%= keyword != null ? keyword : "" %>" class="form-input" placeholder="Search by title or description">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="minBudget" class="form-label">Minimum Budget ($)</label>
                            <div class="filter-input-container">
                                <input type="number" id="minBudget" name="minBudget" min="0" value="<%= minBudget != null ? minBudget : "" %>" class="form-input">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="1" x2="12" y2="23"></line><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path></svg>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="interest" class="form-label">Interest</label>
                            <div class="filter-input-container">
                                <select id="interest" name="interest" class="form-select">
                                    <option value="">All Interests</option>
                                    <% for(String interest : allInterests) { %>
                                        <option value="<%= interest %>" <%= interest.equals(interestFilter) ? "selected" : "" %>><%= interest %></option>
                                    <% } %>
                                </select>
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="select-icon"><polyline points="6 9 12 15 18 9"></polyline></svg>
                            </div>
                        </div>

                        <div class="form-actions full-width">
                            <button type="submit" class="form-button">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"></polygon></svg>
                                Apply Filters
                            </button>
                            <a href="<%= request.getContextPath() %>/creator/campaigns" class="btn btn-secondary">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="8" y1="12" x2="16" y2="12"></line></svg>
                                Clear Filters
                            </a>
                        </div>
                    </form>
                </div>

                <% if(campaigns == null || campaigns.isEmpty()) { %>
                    <div class="empty-state">
                        <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" stroke-linecap="round" stroke-linejoin="round"><path d="M21.21 15.89A10 10 0 1 1 8 2.83"></path><path d="M22 12A10 10 0 0 0 12 2v10z"></path></svg>
                        <p>No campaigns found matching your criteria.</p>
                        <% if(keyword != null || minBudget != null || interestFilter != null) { %>
                            <a href="<%= request.getContextPath() %>/creator/campaigns" class="btn btn-primary">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="8" y1="12" x2="16" y2="12"></line></svg>
                                Clear Filters
                            </a>
                        <% } %>
                    </div>
                <% } else { %>
                    <div class="campaigns-grid">
                        <% for(Campaign campaign : campaigns) {
                            boolean meetsRequirement = meetsRequirementMap.getOrDefault(campaign.getId(), false);
                            boolean hasApplied = hasAppliedMap.getOrDefault(campaign.getId(), false);
                        %>
                            <div class="campaign-card">
                                <div class="campaign-card-header">
                                    <h3><%= campaign.getTitle() %></h3>
                                    <span class="budget-badge">$<%= campaign.getBudget() %></span>
                                </div>

                                <div class="campaign-card-body">
                                    <p class="campaign-description"><%= campaign.getDescription().length() > 150 ? campaign.getDescription().substring(0, 150) + "..." : campaign.getDescription() %></p>

                                    <div class="campaign-meta-info">
                                        <p class="campaign-meta-item">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>
                                            <span><strong>Duration:</strong> <%= campaign.getStartDate() %> to <%= campaign.getEndDate() %></span>
                                        </p>
                                        <p class="campaign-meta-item">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                                            <span><strong>Min. Followers:</strong> <%= campaign.getMinFollowers() %></span>
                                        </p>

                                        <% if(campaign.getTargetInterests() != null && !campaign.getTargetInterests().isEmpty()) { %>
                                            <div class="creator-interests">
                                                <% for(String interest : campaign.getTargetInterests()) { %>
                                                    <span class="interest-tag"><%= interest %></span>
                                                <% } %>
                                            </div>
                                        <% } %>
                                    </div>
                                </div>

                                <div class="campaign-card-footer">
                                    <a href="<%= request.getContextPath() %>/campaign?id=<%= campaign.getId() %>" class="btn btn-primary">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>
                                        View Details
                                    </a>

                                    <% if(hasApplied) { %>
                                        <span class="status-badge status-applied">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                                            Applied
                                        </span>
                                    <% } else if(!meetsRequirement) { %>
                                        <span class="status-badge status-ineligible">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>
                                            Not Eligible
                                        </span>
                                    <% } else { %>
                                        <a href="<%= request.getContextPath() %>/creator/apply?campaignId=<%= campaign.getId() %>" class="btn btn-secondary">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="8.5" cy="7" r="4"></circle><line x1="20" y1="8" x2="20" y2="14"></line><line x1="23" y1="11" x2="17" y2="11"></line></svg>
                                            Apply Now
                                        </a>
                                    <% } %>
                                </div>
                            </div>
                        <% } %>
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
