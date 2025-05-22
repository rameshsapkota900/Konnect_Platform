<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Creator" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Browse Creators - Konnect</title>
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
                    <li><a href="<%= request.getContextPath() %>/business/dashboard">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/profile">Profile</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/campaigns">My Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/creators" class="active">Browse Creators</a></li>
                    <li><a href="<%= request.getContextPath() %>/messages">Messages</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Browse Creators</div>
    </header>

    <main>
        <%
            List<Creator> creators = (List<Creator>)request.getAttribute("creators");
            String minFollowers = (String)request.getAttribute("minFollowers");
            String interestFilter = (String)request.getAttribute("interestFilter");
            String searchQuery = (String)request.getAttribute("searchQuery");
            List<String> allInterests = (List<String>)request.getAttribute("allInterests");
        %>

        <section class="about-section">
            <div class="about-card">
                <div class="section-header">
                    <h2>Find Content Creators</h2>
                    <p>Browse our network of content creators to find the perfect match for your brand.</p>
                </div>

                <div class="filter-section light">
                    <h3>Filter Creators</h3>
                    <form action="<%= request.getContextPath() %>/business/creators" method="get" class="form-grid">
                        <div class="form-group">
                            <label for="search">Search</label>
                            <div class="search-input-container">
                                <input type="text" id="search" name="search" value="<%= searchQuery != null ? searchQuery : "" %>" placeholder="Username or Bio">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="minFollowers">Minimum Followers</label>
                            <div class="filter-input-container">
                                <input type="number" id="minFollowers" name="minFollowers" min="0" value="<%= minFollowers != null ? minFollowers : "" %>">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="interest">Interest</label>
                            <div class="filter-input-container">
                                <select id="interest" name="interest">
                                    <option value="">All Interests</option>
                                    <% for(String interest : allInterests) { %>
                                        <option value="<%= interest %>" <%= interest.equals(interestFilter) ? "selected" : "" %>><%= interest %></option>
                                    <% } %>
                                </select>
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 12 15 18 9"></polyline></svg>
                            </div>
                        </div>

                        <div class="form-actions full-width">
                            <button type="submit" class="btn btn-primary">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"></polygon></svg>
                                Apply Filters
                            </button>
                            <a href="<%= request.getContextPath() %>/business/creators" class="btn btn-secondary">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="8" y1="12" x2="16" y2="12"></line></svg>
                                Clear Filters
                            </a>
                        </div>
                    </form>
                </div>

                <% if(creators == null || creators.isEmpty()) { %>
                    <div class="empty-state">
                        <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                        <p>No creators found matching your criteria.</p>
                        <% if(minFollowers != null || interestFilter != null || searchQuery != null) { %>
                            <a href="<%= request.getContextPath() %>/business/creators" class="btn btn-primary">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="8" y1="12" x2="16" y2="12"></line></svg>
                                Clear Filters
                            </a>
                        <% } %>
                    </div>
                <% } else { %>
                    <div class="creators-grid">
                        <% for(Creator creator : creators) { %>
                            <div class="creator-card">
                                <div class="creator-header">
                                    <div class="creator-profile">
                                        <div class="creator-avatar">
                                            <%= creator.getUsername().substring(0, 1).toUpperCase() %>
                                        </div>
                                        <h3 class="creator-name"><%= creator.getUsername() %></h3>
                                    </div>
                                    <div class="creator-followers">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                                        <%= creator.getFollowerCount() %>
                                    </div>
                                </div>

                                <div class="creator-body">
                                    <p class="creator-bio"><%= creator.getBio() != null ? (creator.getBio().length() > 150 ? creator.getBio().substring(0, 150) + "..." : creator.getBio()) : "No bio provided" %></p>

                                    <div>
                                        <p class="creator-pricing">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="1" x2="12" y2="23"></line><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path></svg>
                                            <strong>$<%= creator.getPricingPerPost() %></strong> per post
                                        </p>

                                        <% if(creator.getInterests() != null && !creator.getInterests().isEmpty()) { %>
                                            <div class="creator-interests">
                                                <% for(String interest : creator.getInterests()) { %>
                                                    <span class="interest-tag"><%= interest %></span>
                                                <% } %>
                                            </div>
                                        <% } %>
                                    </div>

                                    <div class="creator-social">
                                        <% if(creator.getInstagramLink() != null && !creator.getInstagramLink().isEmpty()) { %>
                                            <a href="<%= creator.getInstagramLink() %>" target="_blank" class="social-link">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="2" y="2" width="20" height="20" rx="5" ry="5"></rect><path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"></path><line x1="17.5" y1="6.5" x2="17.51" y2="6.5"></line></svg>
                                                Instagram
                                            </a>
                                        <% } %>

                                        <% if(creator.getTiktokLink() != null && !creator.getTiktokLink().isEmpty()) { %>
                                            <a href="<%= creator.getTiktokLink() %>" target="_blank" class="social-link">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 12A4 4 0 1 0 9 4 4 4 0 0 0 9 12z"></path><path d="M7 19a4 4 0 0 1-4-4 4 4 0 0 1 4-4h.5"></path><path d="M9 19h.5a4 4 0 0 0 4-4 4 4 0 0 0-4-4H9"></path></svg>
                                                TikTok
                                            </a>
                                        <% } %>

                                        <% if(creator.getYoutubeLink() != null && !creator.getYoutubeLink().isEmpty()) { %>
                                            <a href="<%= creator.getYoutubeLink() %>" target="_blank" class="social-link">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22.54 6.42a2.78 2.78 0 0 0-1.94-2C18.88 4 12 4 12 4s-6.88 0-8.6.46a2.78 2.78 0 0 0-1.94 2A29 29 0 0 0 1 11.75a29 29 0 0 0 .46 5.33A2.78 2.78 0 0 0 3.4 19c1.72.46 8.6.46 8.6.46s6.88 0 8.6-.46a2.78 2.78 0 0 0 1.94-2 29 29 0 0 0 .46-5.25 29 29 0 0 0-.46-5.33z"></path><polygon points="9.75 15.02 15.5 11.75 9.75 8.48 9.75 15.02"></polygon></svg>
                                                YouTube
                                            </a>
                                        <% } %>
                                    </div>
                                </div>

                                <div class="creator-footer">
                                    <a href="<%= request.getContextPath() %>/business/creator?id=<%= creator.getId() %>" class="btn btn-primary">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>
                                        View Profile
                                    </a>
                                    <a href="<%= request.getContextPath() %>/messages?userId=<%= creator.getId() %>" class="btn btn-secondary">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"></path></svg>
                                        Message
                                    </a>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } %>
            </div>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 Konnect. All rights reserved. <span class="admin-footer-note">Business Portal</span></p>
    </footer>
</body>
</html>
