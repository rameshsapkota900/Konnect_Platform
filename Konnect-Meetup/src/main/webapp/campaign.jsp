<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.Campaign" %>
<%@ page import="com.konnect.model.Business" %>
<%@ page import="com.konnect.model.Creator" %>
<%@ page import="com.konnect.model.Application" %>
<%@ page import="com.konnect.model.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Campaign Details - Konnect</title>
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
                    <%
                        User user = (User)session.getAttribute("user");
                        String role = user.getRole();

                        if ("creator".equals(role)) {
                    %>
                        <li><a href="<%= request.getContextPath() %>/creator/dashboard">Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/creator/profile">Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/creator/campaigns" class="active">Browse Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/creator/applications">My Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/messages">Messages</a></li>
                    <% } else if ("business".equals(role)) { %>
                        <li><a href="<%= request.getContextPath() %>/business/dashboard">Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/business/profile">Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/business/campaigns" class="active">My Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/business/creators">Browse Creators</a></li>
                        <li><a href="<%= request.getContextPath() %>/messages">Messages</a></li>
                    <% } else if ("admin".equals(role)) { %>
                        <li><a href="<%= request.getContextPath() %>/admin/dashboard">Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/users">Users</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/campaigns" class="active">Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/reports">Reports</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                    <% } %>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Campaign Details</div>
    </header>

    <main>
        <%
            Campaign campaign = (Campaign)request.getAttribute("campaign");
            Business business = (Business)request.getAttribute("business");
            Boolean hasApplied = (Boolean)request.getAttribute("hasApplied");
            Boolean meetsRequirement = (Boolean)request.getAttribute("meetsRequirement");
            List<Application> applications = (List<Application>)request.getAttribute("applications");
        %>

        <section class="about-section">
            <div class="about-card">
                <div class="section-header">
                    <h2><%= campaign.getTitle() %></h2>
                    <span class="status-<%= campaign.getStatus().toLowerCase() %>"><%= campaign.getStatus() %></span>
                </div>

                <div class="campaign-details-grid" style="display: grid; grid-template-columns: 1fr 2fr; gap: 2rem; margin-top: 2rem;">
                    <div class="campaign-meta" style="background-color: var(--light-gray); padding: 1.5rem; border-radius: 10px;">
                        <h3 style="color: var(--primary-color); margin-bottom: 1.5rem; padding-bottom: 0.5rem; border-bottom: 1px solid #e0e0e0;">Campaign Details</h3>
                        <div style="display: flex; flex-direction: column; gap: 1rem;">
                            <div>
                                <p style="font-weight: 600; color: var(--secondary-color); margin-bottom: 0.3rem;">Budget</p>
                                <p style="font-size: 1.2rem;">$<%= campaign.getBudget() %></p>
                            </div>
                            <div>
                                <p style="font-weight: 600; color: var(--secondary-color); margin-bottom: 0.3rem;">Duration</p>
                                <p><%= campaign.getStartDate() %> to <%= campaign.getEndDate() %></p>
                            </div>
                            <div>
                                <p style="font-weight: 600; color: var(--secondary-color); margin-bottom: 0.3rem;">Minimum Followers</p>
                                <p><%= campaign.getMinFollowers() %></p>
                            </div>
                            <% if (campaign.getTargetInterests() != null && !campaign.getTargetInterests().isEmpty()) { %>
                                <div>
                                    <p style="font-weight: 600; color: var(--secondary-color); margin-bottom: 0.3rem;">Target Interests</p>
                                    <div style="display: flex; flex-wrap: wrap; gap: 0.5rem;">
                                        <% for (String interest : campaign.getTargetInterests()) { %>
                                            <span style="background-color: var(--white); padding: 0.3rem 0.8rem; border-radius: 20px; font-size: 0.9rem;"><%= interest %></span>
                                        <% } %>
                                    </div>
                                </div>
                            <% } %>
                        </div>

                        <% if (business != null) { %>
                            <div style="margin-top: 2rem; padding-top: 1.5rem; border-top: 1px solid #e0e0e0;">
                                <h3 style="color: var(--primary-color); margin-bottom: 1.5rem;">About the Business</h3>
                                <div style="display: flex; flex-direction: column; gap: 1rem;">
                                    <div>
                                        <p style="font-weight: 600; color: var(--secondary-color); margin-bottom: 0.3rem;">Company</p>
                                        <p><%= business.getCompanyName() != null ? business.getCompanyName() : business.getUsername() %></p>
                                    </div>
                                    <div>
                                        <p style="font-weight: 600; color: var(--secondary-color); margin-bottom: 0.3rem;">Industry</p>
                                        <p><%= business.getIndustry() != null ? business.getIndustry() : "Not specified" %></p>
                                    </div>
                                    <% if (business.getWebsite() != null && !business.getWebsite().isEmpty()) { %>
                                        <div>
                                            <p style="font-weight: 600; color: var(--secondary-color); margin-bottom: 0.3rem;">Website</p>
                                            <p><a href="<%= business.getWebsite() %>" target="_blank" style="color: var(--primary-color);"><%= business.getWebsite() %></a></p>
                                        </div>
                                    <% } %>
                                </div>
                            </div>
                        <% } else { %>
                            <div style="margin-top: 2rem; padding-top: 1.5rem; border-top: 1px solid #e0e0e0;">
                                <h3 style="color: var(--primary-color); margin-bottom: 1rem;">About the Business</h3>
                                <p>Business information not available.</p>
                            </div>
                        <% } %>
                    </div>

                    <div class="campaign-content">
                        <div style="background-color: var(--white); padding: 2rem; border-radius: 10px; box-shadow: var(--shadow); margin-bottom: 2rem;">
                            <h3 style="color: var(--primary-color); margin-bottom: 1.5rem; padding-bottom: 0.5rem; border-bottom: 1px solid #e0e0e0;">Description</h3>
                            <p style="line-height: 1.8; white-space: pre-line;"><%= campaign.getDescription() %></p>
                        </div>

                        <% if (campaign.getRequirements() != null && !campaign.getRequirements().isEmpty()) { %>
                            <div style="background-color: var(--white); padding: 2rem; border-radius: 10px; box-shadow: var(--shadow);">
                                <h3 style="color: var(--primary-color); margin-bottom: 1.5rem; padding-bottom: 0.5rem; border-bottom: 1px solid #e0e0e0;">Requirements</h3>
                                <p style="line-height: 1.8; white-space: pre-line;"><%= campaign.getRequirements() %></p>
                            </div>
                        <% } %>
                    </div>
                </div>

                <% if ("creator".equals(role) && "active".equals(campaign.getStatus())) { %>
                    <div style="margin-top: 2rem; padding-top: 1.5rem; border-top: 1px solid #e0e0e0;">
                        <% if (Boolean.TRUE.equals(hasApplied)) { %>
                            <div class="info-message">
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="margin-right: 8px;"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="16" x2="12" y2="12"></line><line x1="12" y1="8" x2="12.01" y2="8"></line></svg>
                                You have already applied to this campaign.
                            </div>
                        <% } else if (Boolean.FALSE.equals(meetsRequirement)) { %>
                            <div class="warning-message">
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="margin-right: 8px;"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>
                                You don't meet the minimum follower requirement for this campaign.
                            </div>
                        <% } else { %>
                            <a href="<%= request.getContextPath() %>/creator/apply?campaignId=<%= campaign.getId() %>" class="btn btn-primary" style="background-color: var(--primary-color); color: var(--white); display: inline-flex; align-items: center; gap: 0.5rem;">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                                Apply Now
                            </a>
                        <% } %>
                    </div>
                <% } %>

                <% if ("business".equals(role) && applications != null && !applications.isEmpty()) { %>
                    <div style="margin-top: 2rem; padding-top: 1.5rem; border-top: 1px solid #e0e0e0;">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                            <h3 style="color: var(--primary-color); margin-bottom: 0;">Applications (<%= applications.size() %>)</h3>
                            <div class="filter-controls" style="background: none; padding: 0; margin: 0;">
                                <select style="padding: 0.5rem 1rem; border-radius: 6px; border: 1px solid #e0e0e0; font-family: 'Poppins', sans-serif;">
                                    <option value="all">All Applications</option>
                                    <option value="pending">Pending</option>
                                    <option value="approved">Approved</option>
                                    <option value="rejected">Rejected</option>
                                    <option value="completed">Completed</option>
                                </select>
                            </div>
                        </div>
                        <div style="overflow-x: auto; background-color: var(--white); border-radius: 10px; box-shadow: var(--shadow);">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Creator</th>
                                        <th>Followers</th>
                                        <th>Message</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (Application app : applications) {
                                        Creator creator = (Creator)request.getAttribute("creator_" + app.getId());
                                        if (creator != null) {
                                    %>
                                    <tr>
                                        <td>
                                            <a href="<%= request.getContextPath() %>/business/creator?id=<%= creator.getId() %>" style="color: var(--primary-color); font-weight: 500; display: flex; align-items: center; gap: 0.5rem;">
                                                <div style="width: 32px; height: 32px; border-radius: 50%; background-color: var(--light-gray); display: flex; align-items: center; justify-content: center; font-weight: 600; color: var(--secondary-color);">
                                                    <%= creator.getUsername().substring(0, 1).toUpperCase() %>
                                                </div>
                                                <%= creator.getUsername() %>
                                            </a>
                                        </td>
                                        <td>
                                            <div style="display: flex; align-items: center; gap: 0.3rem;">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                                                <%= creator.getFollowerCount() %>
                                            </div>
                                        </td>
                                        <td>
                                            <div style="max-width: 250px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" title="<%= app.getMessage() %>">
                                                <%= app.getMessage().length() > 50 ? app.getMessage().substring(0, 50) + "..." : app.getMessage() %>
                                            </div>
                                        </td>
                                        <td><span class="status-<%= app.getStatus().toLowerCase() %>"><%= app.getStatus() %></span></td>
                                        <td>
                                            <div class="action-buttons">
                                                <% if ("pending".equals(app.getStatus())) { %>
                                                    <a href="<%= request.getContextPath() %>/business/application?action=approve&id=<%= app.getId() %>" class="btn btn-small" style="background-color: #28a745; color: white;">Approve</a>
                                                    <a href="<%= request.getContextPath() %>/business/application?action=reject&id=<%= app.getId() %>" class="btn btn-small" style="background-color: #dc3545; color: white;">Reject</a>
                                                <% } else if ("approved".equals(app.getStatus())) { %>
                                                    <a href="<%= request.getContextPath() %>/business/application?action=complete&id=<%= app.getId() %>" class="btn btn-small" style="background-color: #17a2b8; color: white;">Complete</a>
                                                <% } %>
                                                <a href="<%= request.getContextPath() %>/messages?userId=<%= creator.getId() %>" class="btn btn-small" style="background-color: var(--primary-color); color: white;">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="margin-right: 4px;"><path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"></path></svg>
                                                    Message
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    }
                                    %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                <% } %>

                <% if ("admin".equals(role)) { %>
                    <div style="margin-top: 2rem; padding-top: 1.5rem; border-top: 1px solid #e0e0e0;">
                        <h3 style="color: var(--primary-color); margin-bottom: 1.5rem;">Admin Actions</h3>
                        <div class="action-buttons" style="display: flex; flex-wrap: wrap; gap: 1rem;">
                            <% if ("active".equals(campaign.getStatus())) { %>
                                <form action="<%= request.getContextPath() %>/admin/campaigns" method="post" style="display: inline;">
                                    <input type="hidden" name="campaignId" value="<%= campaign.getId() %>">
                                    <input type="hidden" name="action" value="cancel">
                                    <button type="submit" class="btn btn-small" style="background-color: #ffc107; color: #212529; display: inline-flex; align-items: center; gap: 0.5rem;" onclick="return confirm('Are you sure you want to cancel this campaign?')">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="8" y1="12" x2="16" y2="12"></line></svg>
                                        Cancel Campaign
                                    </button>
                                </form>
                            <% } else if ("cancelled".equals(campaign.getStatus())) { %>
                                <form action="<%= request.getContextPath() %>/admin/campaigns" method="post" style="display: inline;">
                                    <input type="hidden" name="campaignId" value="<%= campaign.getId() %>">
                                    <input type="hidden" name="action" value="activate">
                                    <button type="submit" class="btn btn-small" style="background-color: #28a745; color: white; display: inline-flex; align-items: center; gap: 0.5rem;">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                                        Activate Campaign
                                    </button>
                                </form>
                            <% } %>

                            <form action="<%= request.getContextPath() %>/admin/campaigns" method="post" style="display: inline;">
                                <input type="hidden" name="campaignId" value="<%= campaign.getId() %>">
                                <input type="hidden" name="action" value="delete">
                                <button type="submit" class="btn btn-small" style="background-color: #dc3545; color: white; display: inline-flex; align-items: center; gap: 0.5rem;" onclick="return confirm('Are you sure you want to delete this campaign? This action cannot be undone.')">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>
                                    Delete Campaign
                                </button>
                            </form>
                        </div>
                    </div>
                <% } %>

                <div style="margin-top: 2rem; text-align: center;">
                    <% if ("creator".equals(role)) { %>
                        <a href="<%= request.getContextPath() %>/creator/campaigns" style="color: var(--primary-color); text-decoration: none; display: inline-flex; align-items: center; gap: 0.5rem;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"></line><polyline points="12 19 5 12 12 5"></polyline></svg>
                            Back to Campaigns
                        </a>
                    <% } else if ("business".equals(role)) { %>
                        <a href="<%= request.getContextPath() %>/business/campaigns" style="color: var(--primary-color); text-decoration: none; display: inline-flex; align-items: center; gap: 0.5rem;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"></line><polyline points="12 19 5 12 12 5"></polyline></svg>
                            Back to My Campaigns
                        </a>
                    <% } else if ("admin".equals(role)) { %>
                        <a href="<%= request.getContextPath() %>/admin/campaigns" style="color: var(--primary-color); text-decoration: none; display: inline-flex; align-items: center; gap: 0.5rem;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"></line><polyline points="12 19 5 12 12 5"></polyline></svg>
                            Back to Campaigns
                        </a>
                    <% } %>
                </div>
            </div>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 Konnect. All rights reserved.
            <% if ("admin".equals(role)) { %>
                <span class="admin-footer-note">Admin Panel</span>
            <% } else if ("business".equals(role)) { %>
                <span class="admin-footer-note">Business Portal</span>
            <% } else if ("creator".equals(role)) { %>
                <span class="admin-footer-note">Creator Portal</span>
            <% } %>
        </p>
    </footer>
</body>
</html>
