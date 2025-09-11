<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.Creator" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Creator Profile - Konnect</title>
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
                    <li><a href="<%= request.getContextPath() %>/creator/profile" class="active">Profile</a></li>
                    <li><a href="<%= request.getContextPath() %>/creator/campaigns">Browse Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/creator/applications">My Applications</a></li>
                    <li><a href="<%= request.getContextPath() %>/messages">Messages</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Creator Profile</div>
    </header>

    <main>
        <%
            Creator creator = (Creator)request.getAttribute("creator");
            List<String> interests = creator.getInterests();
        %>

        <section class="about-section">
            <div class="about-card" >
                <div class="section-header">
                    <h2>Edit Your Creator Profile</h2>
                    <p>Complete your profile to help businesses find you for collaborations.</p>
                </div>

                <% if(request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <% if(request.getAttribute("success") != null) { %>
                    <div class="success-message">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                        <%= request.getAttribute("success") %>
                    </div>
                <% } %>

                <form action="<%= request.getContextPath() %>/creator/profile" method="post" class="form-card" style="min-width='300px'; max-width='800px';">
                    <h3 class="form-title">Profile Information</h3>

                    <div class="form-grid">
                        <div class="form-group full-width">
                            <label for="bio" class="form-label">Bio</label>
                            <div class="input-container">
                                <textarea id="bio" name="bio" rows="4" required class="form-textarea"><%= creator.getBio() != null ? creator.getBio() : "" %></textarea>
                            </div>
                            <p class="form-hint">Tell businesses about yourself and your content style.</p>
                        </div>

                        <div class="form-group">
                            <label for="followerCount" class="form-label">Total Follower Count</label>
                            <div class="input-container">
                                <input type="number" id="followerCount" name="followerCount" value="<%= creator.getFollowerCount() %>" min="0" required class="form-input">
                            </div>
                            <p class="form-hint">Combined followers across all your social media platforms.</p>
                        </div>

                        <div class="form-group">
                            <label for="pricingPerPost" class="form-label">Pricing Per Post ($)</label>
                            <div class="input-container">
                                <input type="number" id="pricingPerPost" name="pricingPerPost" value="<%= creator.getPricingPerPost() %>" min="0" step="0.01" required class="form-input">
                    
                            </div>
                            <p class="form-hint">Your standard rate for a sponsored post.</p>
                        </div>

                        <div class="form-group full-width">
                            <label class="form-label">Interests</label>
                            <div class="creator-interests" style="margin-top: 0.5rem;">
                                <label class="interest-tag" style="cursor: pointer; margin-bottom: 0.5rem; display: inline-block;">
                                    <input type="checkbox" name="interests" value="Technology" <%= interests != null && interests.contains("Technology") ? "checked" : "" %> style="margin-right: 0.5rem;">
                                    Technology
                                </label>
                                <label class="interest-tag" style="cursor: pointer; margin-bottom: 0.5rem; display: inline-block;">
                                    <input type="checkbox" name="interests" value="Beauty" <%= interests != null && interests.contains("Beauty") ? "checked" : "" %> style="margin-right: 0.5rem;">
                                    Beauty
                                </label>
                                <label class="interest-tag" style="cursor: pointer; margin-bottom: 0.5rem; display: inline-block;">
                                    <input type="checkbox" name="interests" value="Fashion" <%= interests != null && interests.contains("Fashion") ? "checked" : "" %> style="margin-right: 0.5rem;">
                                    Fashion
                                </label>
                                <label class="interest-tag" style="cursor: pointer; margin-bottom: 0.5rem; display: inline-block;">
                                    <input type="checkbox" name="interests" value="Fitness" <%= interests != null && interests.contains("Fitness") ? "checked" : "" %> style="margin-right: 0.5rem;">
                                    Fitness
                                </label>
                                <label class="interest-tag" style="cursor: pointer; margin-bottom: 0.5rem; display: inline-block;">
                                    <input type="checkbox" name="interests" value="Food" <%= interests != null && interests.contains("Food") ? "checked" : "" %> style="margin-right: 0.5rem;">
                                    Food
                                </label>
                                <label class="interest-tag" style="cursor: pointer; margin-bottom: 0.5rem; display: inline-block;">
                                    <input type="checkbox" name="interests" value="Travel" <%= interests != null && interests.contains("Travel") ? "checked" : "" %> style="margin-right: 0.5rem;">
                                    Travel
                                </label>
                                <label class="interest-tag" style="cursor: pointer; margin-bottom: 0.5rem; display: inline-block;">
                                    <input type="checkbox" name="interests" value="Gaming" <%= interests != null && interests.contains("Gaming") ? "checked" : "" %> style="margin-right: 0.5rem;">
                                    Gaming
                                </label>
                                <label class="interest-tag" style="cursor: pointer; margin-bottom: 0.5rem; display: inline-block;">
                                    <input type="checkbox" name="interests" value="Lifestyle" <%= interests != null && interests.contains("Lifestyle") ? "checked" : "" %> style="margin-right: 0.5rem;">
                                    Lifestyle
                                </label>
                            </div>
                            <p class="form-hint">Select categories that match your content.</p>
                        </div>

                        <h3 class="form-section-title">Social Media Links</h3>

                        <div class="form-group full-width">
                            <label for="instagramLink" class="form-label">Instagram Link</label>
                            <div class="input-container">
                                <input type="url" id="instagramLink" name="instagramLink" value="<%= creator.getInstagramLink() != null ? creator.getInstagramLink() : "" %>" class="form-input">
                        
                            </div>
                            <p class="form-hint">Full URL to your Instagram profile.</p>
                        </div>

                        <div class="form-group full-width">
                            <label for="tiktokLink" class="form-label">TikTok Link</label>
                            <div class="input-container">
                                <input type="url" id="tiktokLink" name="tiktokLink" value="<%= creator.getTiktokLink() != null ? creator.getTiktokLink() : "" %>" class="form-input">
                               
                            </div>
                            <p class="form-hint">Full URL to your TikTok profile.</p>
                        </div>

                        <div class="form-group full-width">
                            <label for="youtubeLink" class="form-label">YouTube Link</label>
                            <div class="input-container">
                                <input type="url" id="youtubeLink" name="youtubeLink" value="<%= creator.getYoutubeLink() != null ? creator.getYoutubeLink() : "" %>" class="form-input">
                              
                            </div>
                            <p class="form-hint">Full URL to your YouTube channel.</p>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="form-button">
                        
                            Save Profile
                        </button>
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
