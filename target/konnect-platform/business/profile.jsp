<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.Business" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Business Profile - Konnect</title>
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
                    <li><a href="<%= request.getContextPath() %>/business/profile" class="active">Profile</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/campaigns">My Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/creators">Browse Creators</a></li>
                    <li><a href="<%= request.getContextPath() %>/messages">Messages</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Business Profile</div>
    </header>

    <main>
        <%
            Business business = (Business)request.getAttribute("business");
        %>

        <section class="about-section">
            <div class="about-card">
                <div class="section-header">
                    <h2>Edit Your Business Profile</h2>
                    <p>Complete your business profile to help creators understand your brand.</p>
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

                <div class="profile-grid">
                    <!-- Profile Preview Section -->
                    <div class="profile-preview">
                        <div class="profile-header">
                            <div class="profile-avatar">
                                <%= business.getUsername().substring(0, 1).toUpperCase() %>
                            </div>
                            <h3 class="profile-username"><%= business.getUsername() %></h3>
                            <span class="profile-badge">Business Account</span>
                        </div>

                        <div class="profile-section">
                            <h4 class="profile-section-title">Profile Completion</h4>

                            <%
                                int completionPercentage = 0;
                                int totalFields = 5;
                                int completedFields = 0;

                                if (business.getCompanyName() != null && !business.getCompanyName().isEmpty()) completedFields++;
                                if (business.getIndustry() != null && !business.getIndustry().isEmpty()) completedFields++;
                                if (business.getDescription() != null && !business.getDescription().isEmpty()) completedFields++;
                                if (business.getWebsite() != null && !business.getWebsite().isEmpty()) completedFields++;
                                if (business.getContactPhone() != null && !business.getContactPhone().isEmpty()) completedFields++;

                                completionPercentage = (completedFields * 100) / totalFields;
                            %>

                            <div class="completion-stats">
                                <div class="completion-header">
                                    <span class="completion-percentage"><%= completionPercentage %>% Complete</span>
                                    <span><%= completedFields %>/<%= totalFields %> fields</span>
                                </div>
                                <div class="completion-progress">
                                    <div class="completion-bar" style="width: <%= completionPercentage %>%;"></div>
                                </div>
                            </div>

                            <div class="completion-message">
                                <p>Complete your profile to attract more creators and improve your visibility.</p>
                            </div>
                        </div>

                        <div class="profile-section">
                            <h4 class="profile-section-title">Profile Preview</h4>

                            <div class="preview-card">
                                <div class="preview-header">
                                    <h5 class="preview-company">
                                        <%= business.getCompanyName() != null && !business.getCompanyName().isEmpty() ? business.getCompanyName() : "Your Company Name" %>
                                    </h5>
                                    <span class="preview-industry">
                                        <%= business.getIndustry() != null && !business.getIndustry().isEmpty() ? business.getIndustry() : "Industry" %>
                                    </span>
                                </div>

                                <p class="preview-description">
                                    <%= business.getDescription() != null && !business.getDescription().isEmpty() ?
                                        (business.getDescription().length() > 100 ? business.getDescription().substring(0, 100) + "..." : business.getDescription()) :
                                        "Your company description will appear here..." %>
                                </p>

                                <div class="preview-contact">
                                    <% if (business.getWebsite() != null && !business.getWebsite().isEmpty()) { %>
                                        <div class="contact-item">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="2" y1="12" x2="22" y2="12"></line><path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"></path></svg>
                                            <a href="<%= business.getWebsite() %>" target="_blank" class="contact-link"><%= business.getWebsite() %></a>
                                        </div>
                                    <% } %>

                                    <% if (business.getContactPhone() != null && !business.getContactPhone().isEmpty()) { %>
                                        <div class="contact-item">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path></svg>
                                            <span><%= business.getContactPhone() %></span>
                                        </div>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Form Section -->
                    <div>
                        <form action="<%= request.getContextPath() %>/business/profile" method="post" class="form-card">
                            <h3 class="form-title">Business Information</h3>

                            <div class="form-grid">
                                <div class="form-group full-width">
                                    <label for="companyName" class="form-label">Company Name</label>
                                    <div class="input-container">
                                        <input type="text" id="companyName" name="companyName" value="<%= business.getCompanyName() != null ? business.getCompanyName() : "" %>" required class="form-input">
                                    </div>
                                </div>

                                <div class="form-group full-width">
                                    <label for="industry" class="form-label">Industry</label>
                                    <div class="input-container">
                                        <select id="industry" name="industry" required class="form-select">
                                            <option value="" disabled <%= business.getIndustry() == null ? "selected" : "" %>>Select an industry</option>
                                            <option value="Technology" <%= "Technology".equals(business.getIndustry()) ? "selected" : "" %>>Technology</option>
                                            <option value="Fashion" <%= "Fashion".equals(business.getIndustry()) ? "selected" : "" %>>Fashion</option>
                                            <option value="Beauty" <%= "Beauty".equals(business.getIndustry()) ? "selected" : "" %>>Beauty</option>
                                            <option value="Food & Beverage" <%= "Food & Beverage".equals(business.getIndustry()) ? "selected" : "" %>>Food & Beverage</option>
                                            <option value="Health & Fitness" <%= "Health & Fitness".equals(business.getIndustry()) ? "selected" : "" %>>Health & Fitness</option>
                                            <option value="Travel" <%= "Travel".equals(business.getIndustry()) ? "selected" : "" %>>Travel</option>
                                            <option value="Entertainment" <%= "Entertainment".equals(business.getIndustry()) ? "selected" : "" %>>Entertainment</option>
                                            <option value="Education" <%= "Education".equals(business.getIndustry()) ? "selected" : "" %>>Education</option>
                                            <option value="Finance" <%= "Finance".equals(business.getIndustry()) ? "selected" : "" %>>Finance</option>
                                            <option value="Other" <%= "Other".equals(business.getIndustry()) ? "selected" : "" %>>Other</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group full-width">
                                    <label for="description" class="form-label">Company Description</label>
                                    <div class="input-container">
                                        <textarea id="description" name="description" rows="5" required class="form-textarea"><%= business.getDescription() != null ? business.getDescription() : "" %></textarea>
                                    </div>
                                    <p class="form-hint">Tell creators about your company, products, and brand values.</p>
                                </div>

                                <h3 class="form-section-title">Contact Information</h3>

                                <div class="form-group full-width">
                                    <label for="website" class="form-label">Website</label>
                                    <div class="input-container">
                                        <input type="url" id="website" name="website" value="<%= business.getWebsite() != null ? business.getWebsite() : "" %>" class="form-input">
                                    </div>
                                    <p class="form-hint">Your company website URL.</p>
                                </div>

                                <div class="form-group full-width">
                                    <label for="contactPhone" class="form-label">Contact Phone</label>
                                    <div class="input-container">
                                        <input type="tel" id="contactPhone" name="contactPhone" value="<%= business.getContactPhone() != null ? business.getContactPhone() : "" %>" class="form-input">
                                    </div>
                                    <p class="form-hint">Business contact number.</p>
                                </div>
                            </div>

                            <div class="form-actions">
                                <button type="submit" class="btn btn-primary form-button">
                                    Save Profile
                                </button>
                            </div>
                        </form>
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
