<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.Creator" %>
<!DOCTYPE html>
<html lang="en">
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
                    <li><a href="<%= request.getContextPath() %>/business/dashboard">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/profile">Profile</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/campaigns">My Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/creators" class="active">Browse Creators</a></li>
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
        %>

        <section class="about-section">
            <div class="about-card">
                <div class="section-header" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                    <div style="display: flex; align-items: center; gap: 1rem;">
                        <div style="width: 64px; height: 64px; border-radius: 50%; background-color: var(--primary-color); color: white; display: flex; align-items: center; justify-content: center; font-weight: 600; font-size: 1.5rem;">
                            <%= creator.getUsername().substring(0, 1).toUpperCase() %>
                        </div>
                        <h2><%= creator.getUsername() %></h2>
                    </div>
                    <a href="<%= request.getContextPath() %>/business/creators" class="btn btn-secondary" style="display: inline-flex; align-items: center; gap: 0.5rem;">
                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"></line><polyline points="12 19 5 12 12 5"></polyline></svg>
                        Back to Creators
                    </a>
                </div>

                <div class="creator-profile-grid" style="display: grid; grid-template-columns: 1fr 2fr; gap: 2rem;">
                    <div style="background-color: var(--light-gray); padding: 1.5rem; border-radius: 10px;">
                        <h3 style="color: var(--primary-color); margin-bottom: 1.5rem; padding-bottom: 0.5rem; border-bottom: 1px solid #e0e0e0;">Creator Details</h3>

                        <div class="info-grid">
                            <div class="info-item">
                                <span class="label">Email</span>
                                <span class="value" style="display: flex; align-items: center; gap: 0.5rem;">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path><polyline points="22,6 12,13 2,6"></polyline></svg>
                                    <%= creator.getEmail() %>
                                </span>
                            </div>
                            <div class="info-item">
                                <span class="label">Followers</span>
                                <span class="value" style="display: flex; align-items: center; gap: 0.5rem;">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                                    <%= creator.getFollowerCount() %>
                                </span>
                            </div>
                            <div class="info-item">
                                <span class="label">Pricing</span>
                                <span class="value" style="display: flex; align-items: center; gap: 0.5rem;">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="1" x2="12" y2="23"></line><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path></svg>
                                    $<%= creator.getPricingPerPost() %> per post
                                </span>
                            </div>
                        </div>

                        <div style="margin-top: 2rem;">
                            <h4 style="color: var(--secondary-color); margin-bottom: 1rem;">Social Media</h4>
                            <div style="display: flex; flex-direction: column; gap: 1rem;">
                                <% if (creator.getInstagramLink() != null && !creator.getInstagramLink().isEmpty()) { %>
                                    <a href="<%= creator.getInstagramLink() %>" target="_blank" style="display: flex; align-items: center; gap: 0.8rem; color: var(--text-color); text-decoration: none; padding: 0.5rem; border-radius: 8px; background-color: var(--white); transition: all 0.2s;">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="2" y="2" width="20" height="20" rx="5" ry="5"></rect><path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"></path><line x1="17.5" y1="6.5" x2="17.51" y2="6.5"></line></svg>
                                        Instagram
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="margin-left: auto;"><path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"></path><polyline points="15 3 21 3 21 9"></polyline><line x1="10" y1="14" x2="21" y2="3"></line></svg>
                                    </a>
                                <% } %>
                                <% if (creator.getTiktokLink() != null && !creator.getTiktokLink().isEmpty()) { %>
                                    <a href="<%= creator.getTiktokLink() %>" target="_blank" style="display: flex; align-items: center; gap: 0.8rem; color: var(--text-color); text-decoration: none; padding: 0.5rem; border-radius: 8px; background-color: var(--white); transition: all 0.2s;">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 12A4 4 0 1 0 9 4 4 4 0 0 0 9 12z"></path><path d="M7 19a4 4 0 0 1-4-4 4 4 0 0 1 4-4h.5"></path><path d="M9 19h.5a4 4 0 0 0 4-4 4 4 0 0 0-4-4H9"></path></svg>
                                        TikTok
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="margin-left: auto;"><path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"></path><polyline points="15 3 21 3 21 9"></polyline><line x1="10" y1="14" x2="21" y2="3"></line></svg>
                                    </a>
                                <% } %>
                                <% if (creator.getYoutubeLink() != null && !creator.getYoutubeLink().isEmpty()) { %>
                                    <a href="<%= creator.getYoutubeLink() %>" target="_blank" style="display: flex; align-items: center; gap: 0.8rem; color: var(--text-color); text-decoration: none; padding: 0.5rem; border-radius: 8px; background-color: var(--white); transition: all 0.2s;">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22.54 6.42a2.78 2.78 0 0 0-1.94-2C18.88 4 12 4 12 4s-6.88 0-8.6.46a2.78 2.78 0 0 0-1.94 2A29 29 0 0 0 1 11.75a29 29 0 0 0 .46 5.33A2.78 2.78 0 0 0 3.4 19c1.72.46 8.6.46 8.6.46s6.88 0 8.6-.46a2.78 2.78 0 0 0 1.94-2 29 29 0 0 0 .46-5.25 29 29 0 0 0-.46-5.33z"></path><polygon points="9.75 15.02 15.5 11.75 9.75 8.48 9.75 15.02"></polygon></svg>
                                        YouTube
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="margin-left: auto;"><path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"></path><polyline points="15 3 21 3 21 9"></polyline><line x1="10" y1="14" x2="21" y2="3"></line></svg>
                                    </a>
                                <% } %>
                            </div>
                        </div>

                        <div style="margin-top: 2rem;">
                            <h4 style="color: var(--secondary-color); margin-bottom: 1rem;">Interests</h4>
                            <% if (creator.getInterests() != null && !creator.getInterests().isEmpty()) { %>
                                <div style="display: flex; flex-wrap: wrap; gap: 0.5rem;">
                                    <% for (String interest : creator.getInterests()) { %>
                                        <span style="background-color: var(--white); padding: 0.3rem 0.8rem; border-radius: 20px; font-size: 0.9rem;"><%= interest %></span>
                                    <% } %>
                                </div>
                            <% } else { %>
                                <p>No interests specified</p>
                            <% } %>
                        </div>

                        <div style="margin-top: 2rem; text-align: center;">
                            <a href="<%= request.getContextPath() %>/messages?userId=<%= creator.getId() %>" class="btn btn-primary" style="display: inline-flex; align-items: center; gap: 0.5rem; width: 100%;">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"></path></svg>
                                Message Creator
                            </a>
                            <% if (request.getAttribute("error") != null) { %>
                                <div class="error-message" style="margin-top: 1rem;">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>
                                    <%= request.getAttribute("error") %>
                                </div>
                            <% } %>
                        </div>
                    </div>

                    <div>
                        <div style="background-color: var(--white); padding: 2rem; border-radius: 10px; box-shadow: var(--shadow); margin-bottom: 2rem;">
                            <h3 style="color: var(--primary-color); margin-bottom: 1.5rem; padding-bottom: 0.5rem; border-bottom: 1px solid #e0e0e0;">About <%= creator.getUsername() %></h3>
                            <p style="line-height: 1.8; white-space: pre-line;"><%= creator.getBio() != null ? creator.getBio() : "No bio provided" %></p>
                        </div>

                        <div style="background-color: var(--white); padding: 2rem; border-radius: 10px; box-shadow: var(--shadow);">
                            <h3 style="color: var(--primary-color); margin-bottom: 1.5rem; padding-bottom: 0.5rem; border-bottom: 1px solid #e0e0e0;">Collaboration Opportunities</h3>
                            <p style="line-height: 1.8;">
                                <%= creator.getUsername() %> is available for collaborations and sponsored content.
                                Their pricing starts at $<%= creator.getPricingPerPost() %> per post.
                            </p>
                            <div style="margin-top: 1.5rem;">
                                <h4 style="color: var(--secondary-color); margin-bottom: 1rem;">What You Can Expect:</h4>
                                <ul style="list-style-type: none; padding: 0; margin: 0;">
                                    <li style="display: flex; align-items: center; gap: 0.8rem; margin-bottom: 0.8rem;">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="var(--primary-color)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                                        Professional content creation
                                    </li>
                                    <li style="display: flex; align-items: center; gap: 0.8rem; margin-bottom: 0.8rem;">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="var(--primary-color)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                                        Authentic brand integration
                                    </li>
                                    <li style="display: flex; align-items: center; gap: 0.8rem; margin-bottom: 0.8rem;">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="var(--primary-color)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                                        Engagement with <%= creator.getFollowerCount() %> followers
                                    </li>
                                    <li style="display: flex; align-items: center; gap: 0.8rem;">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="var(--primary-color)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                                        Detailed performance metrics
                                    </li>
                                </ul>
                            </div>
                            <div style="margin-top: 2rem; text-align: center;">
                                <a href="<%= request.getContextPath() %>/messages?userId=<%= creator.getId() %>" class="btn btn-primary" style="display: inline-flex; align-items: center; gap: 0.5rem;">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"></path></svg>
                                    Start a Conversation
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 Konnect. All rights reserved. <span class="admin-footer-note">Business Portal</span></p>
    </footer>
</body>
</html>
