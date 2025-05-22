<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.User" %>
<%@ page import="com.konnect.model.Message" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Messages - Konnect</title>
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
                    <li><a href="<%= request.getContextPath() %>/creator/applications">My Applications</a></li>
                    <li><a href="<%= request.getContextPath() %>/messages" class="active">Messages
                        <% if(request.getAttribute("totalUnreadCount") != null && (Integer)request.getAttribute("totalUnreadCount") > 0) { %>
                            <span class="badge"><%= request.getAttribute("totalUnreadCount") %></span>
                        <% } %>
                    </a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Messages</div>
        </header>

        <main>
            <%
                User currentUser = (User)request.getAttribute("currentUser");
                User partner = (User)request.getAttribute("partner");
                List<User> conversationPartners = (List<User>)request.getAttribute("conversationPartners");
                List<Message> messages = (List<Message>)request.getAttribute("messages");
                int totalUnreadCount = (Integer)request.getAttribute("totalUnreadCount");
            %>

        <section class="about-section">
            <div class="about-card">
                <div class="section-header">
                <h2>Messages <% if(totalUnreadCount > 0) { %><span class="badge"><%= totalUnreadCount %></span><% } %></h2>
                </div>

                <% if(request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <div style="display: grid; grid-template-columns: 300px 1fr; gap: 1.5rem; height: 600px;">
                    <!-- Conversation List -->
                    <div style="border-right: 1px solid var(--light-gray); overflow-y: auto;">
                        <% if(conversationPartners != null && !conversationPartners.isEmpty()) { %>
                            <% for(User user : conversationPartners) { %>
                                <a href="<%= request.getContextPath() %>/messages?userId=<%= user.getId() %>" 
                                   style="display: flex; align-items: center; gap: 1rem; padding: 1rem; text-decoration: none; color: var(--text-color); border-bottom: 1px solid var(--light-gray); transition: background-color 0.2s; <%= partner != null && partner.getId() == user.getId() ? "background-color: var(--light-gray);" : "" %>">
                                    <div style="width: 40px; height: 40px; border-radius: 50%; background-color: var(--primary-color); color: white; display: flex; align-items: center; justify-content: center; font-weight: 600; font-size: 1rem;">
                                        <%= user.getUsername().substring(0, 1).toUpperCase() %>
                                    </div>
                                    <div>
                                        <h3 style="margin: 0; font-size: 1rem;"><%= user.getUsername() %></h3>
                                        <div style="font-size: 0.8rem; color: var(--secondary-color); text-transform: capitalize;"><%= user.getRole() %></div>
                        </div>
                                </a>
                            <% } %>
                        <% } else { %>
                            <div style="padding: 2rem; text-align: center; color: var(--secondary-color);">
                                <p>No conversations yet.</p>
                            </div>
                        <% } %>
                    </div>

                    <!-- Chat Area -->
                    <div style="display: flex; flex-direction: column; height: 100%;">
                        <% if(partner != null) { %>
                            <div style="padding: 1rem; border-bottom: 1px solid var(--light-gray); background-color: var(--light-gray); display: flex; align-items: center; gap: 1rem;">
                                <div style="width: 40px; height: 40px; border-radius: 50%; background-color: var(--primary-color); color: white; display: flex; align-items: center; justify-content: center; font-weight: 600; font-size: 1rem;">
                                    <%= partner.getUsername().substring(0, 1).toUpperCase() %>
                                </div>
                                <div>
                                    <h3 style="margin: 0; font-size: 1.1rem;"><%= partner.getUsername() %></h3>
                                    <div style="font-size: 0.8rem; color: var(--secondary-color); text-transform: capitalize;"><%= partner.getRole() %></div>
                                </div>
                            </div>

                            <div style="flex-grow: 1; overflow-y: auto; padding: 1.5rem; display: flex; flex-direction: column; gap: 1rem;">
                                <% if(messages == null || messages.isEmpty()) { %>
                                    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100%; padding: 2rem; text-align: center; color: var(--secondary-color);">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" stroke-linecap="round" stroke-linejoin="round" style="margin-bottom: 1rem;"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path></svg>
                                        <p>No messages yet.</p>
                                        <p style="font-size: 0.9rem;">Start the conversation with <%= partner.getUsername() %>!</p>
                                    </div>
                                <% } else { %>
                                    <% for(Message message : messages) {
                                        boolean isCurrentUserMessage = message.getSenderId() == currentUser.getId();
                                    %>
                                        <div style="display: flex; flex-direction: <%= isCurrentUserMessage ? "row-reverse" : "row" %>; align-items: flex-end; gap: 0.8rem; max-width: 80%;<%= isCurrentUserMessage ? " margin-left: auto;" : "" %>">
                                            <div style="width: 32px; height: 32px; border-radius: 50%; background-color: <%= isCurrentUserMessage ? "var(--primary-color)" : "var(--secondary-color)" %>; color: white; display: flex; align-items: center; justify-content: center; font-weight: 600; font-size: 0.9rem;">
                                                <%= isCurrentUserMessage ? currentUser.getUsername().substring(0, 1).toUpperCase() : partner.getUsername().substring(0, 1).toUpperCase() %>
                                            </div>
                                            <div style="background-color: <%= isCurrentUserMessage ? "var(--primary-color)" : "var(--light-gray)" %>; color: <%= isCurrentUserMessage ? "white" : "var(--text-color)" %>; padding: 0.8rem 1rem; border-radius: 12px; border-bottom-<%= isCurrentUserMessage ? "right" : "left" %>-radius: 0; max-width: calc(100% - 40px);">
                                                <p style="margin: 0; white-space: pre-wrap; word-break: break-word;"><%= message.getContent() %></p>
                                                <span style="display: block; font-size: 0.7rem; margin-top: 0.3rem; text-align: right; opacity: 0.8;"><%= message.getCreatedAt() %></span>
                                            </div>
                                        </div>
                                    <% } %>
                                <% } %>
                            </div>

                            <div style="padding: 1rem; border-top: 1px solid var(--light-gray);">
                                <form action="<%= request.getContextPath() %>/messages" method="post" style="display: flex; gap: 0.8rem;">
                                    <input type="hidden" name="receiverId" value="<%= partner.getId() %>">
                                    <div style="flex-grow: 1; position: relative;">
                                        <textarea name="content" placeholder="Type your message..." required style="width: 100%; padding: 0.8rem; border-radius: 20px; border: 1px solid var(--light-gray); resize: none; min-height: 50px; max-height: 120px; font-family: 'Poppins', sans-serif;"></textarea>
                                    </div>
                                    <button type="submit" class="btn btn-primary" style="align-self: flex-end; border-radius: 50%; width: 50px; height: 50px; padding: 0; display: flex; align-items: center; justify-content: center;">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="22" y1="2" x2="11" y2="13"></line><polygon points="22 2 15 22 11 13 2 9 22 2"></polygon></svg>
                                    </button>
                                </form>
                            </div>
                        <% } else { %>
                            <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100%; padding: 2rem; text-align: center; color: var(--secondary-color);">
                                <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" stroke-linecap="round" stroke-linejoin="round" style="margin-bottom: 1.5rem;"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path></svg>
                                <h3 style="margin-bottom: 1rem;">No Conversation Selected</h3>
                                <p>Select a conversation from the list or start a new one by messaging a business.</p>
                                <a href="<%= request.getContextPath() %>/creator/campaigns" class="btn btn-primary" style="margin-top: 1.5rem; display: inline-flex; align-items: center; gap: 0.5rem;">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                                    Browse Campaigns
                                </a>
                            </div>
                        <% } %>
                    </div>
                    </div>
                </div>
            </section>
        </main>

        <footer>
        <p>&copy; 2025 Konnect. All rights reserved. <span class="admin-footer-note">Creator Portal</span></p>
        </footer>
</body>
</html>
