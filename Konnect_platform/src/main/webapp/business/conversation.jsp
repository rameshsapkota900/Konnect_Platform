<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Message" %>
<%@ page import="com.konnect.model.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // Check if user is logged in and has business role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("business")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Get messages and other user from request
    List<Message> messages = (List<Message>) request.getAttribute("messages");
    User otherUser = (User) request.getAttribute("otherUser");

    if (messages == null || otherUser == null) {
        // If not set, redirect to messages page
        response.sendRedirect(request.getContextPath() + "/message");
        return;
    }

    int userId = (int) session.getAttribute("userId");
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy h:mm a");
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Conversation with <%= otherUser.getUsername() %>" />
</jsp:include>

<jsp:include page="/includes/nav-business.jsp">
    <jsp:param name="active" value="messages" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Business Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile"><i class="fas fa-building"></i> Company Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile/creators"><i class="fas fa-users"></i> Find Creators</a></li>
                        <li><a href="<%= request.getContextPath() %>/application"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/message" class="active"><i class="fas fa-envelope"></i> Messages</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title">
                            <a href="<%= request.getContextPath() %>/message" class="back-link"><i class="fas fa-arrow-left"></i></a>
                            Conversation with <%= otherUser.getUsername() %>
                        </h2>
                        <div class="dashboard-actions">
                            <a href="<%= request.getContextPath() %>/profile/view/<%= otherUser.getUserId() %>" class="btn btn-outline btn-fixed-height"><i class="fas fa-user"></i> View Profile</a>
                        </div>
                    </div>

                    <% if (request.getAttribute("success") != null) { %>
                    <div class="success-message">
                        <i class="fas fa-check-circle"></i> <%= request.getAttribute("success") %>
                    </div>
                    <% } %>

                    <% if (request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <i class="fas fa-exclamation-circle"></i> <%= request.getAttribute("error") %>
                    </div>
                    <% } %>

                    <div class="card no-hover no-padding">
                        <div class="conversation-container">
                            <div class="conversation-header">
                                <div class="conversation-header-avatar">
                                    <i class="fas fa-user-circle"></i>
                                </div>
                                <div class="conversation-header-info">
                                    <h4 class="conversation-header-name"><%= otherUser.getUsername() %></h4>
                                    <p class="conversation-header-status">Content Creator</p>
                                </div>
                            </div>
                            <div class="message-list" id="messageList">
                                <% if (messages.isEmpty()) { %>
                                    <div class="empty-conversation">
                                        <i class="fas fa-comments empty-icon"></i>
                                        <h3>No messages yet</h3>
                                        <p>Start the conversation by sending a message below.</p>
                                    </div>
                                <% } else { %>
                                    <% for (Message message : messages) { %>
                                        <div class="message <%= message.getSenderId() == userId ? "message-sent" : "message-received" %>">
                                            <% if (message.getSenderId() != userId) { %>
                                            <div class="message-avatar">
                                                <i class="fas fa-user-circle"></i>
                                            </div>
                                            <% } %>
                                            <div class="message-content">
                                                <p><%= message.getContent() %></p>
                                                <span class="message-time"><%= dateFormat.format(message.getCreatedAt()) %></span>
                                            </div>
                                        </div>
                                    <% } %>
                                <% } %>
                            </div>

                            <div class="message-form">
                                <form action="<%= request.getContextPath() %>/message/reply/<%= otherUser.getUserId() %>" method="post" class="message-input-form">
                                    <div class="form-group message-input-container">
                                        <textarea name="content" class="form-control" placeholder="Type your message..." required></textarea>
                                        <button type="submit" class="message-send-btn"><i class="fas fa-paper-plane"></i></button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <script>
        // Scroll to bottom of message list
        document.addEventListener('DOMContentLoaded', function() {
            const messageList = document.getElementById('messageList');
            messageList.scrollTop = messageList.scrollHeight;
        });
    </script>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
