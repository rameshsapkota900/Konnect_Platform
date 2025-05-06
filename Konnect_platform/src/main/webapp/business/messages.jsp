<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.User" %>
<%
    // Check if user is logged in and has business role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("business")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Get conversation users from request
    List<User> conversationUsers = (List<User>) request.getAttribute("conversationUsers");
    if (conversationUsers == null) {
        // If not set, redirect to message servlet to get the data
        response.sendRedirect(request.getContextPath() + "/message");
        return;
    }
    
    // Get unread count
    Integer unreadCount = (Integer) request.getAttribute("unreadCount");
    if (unreadCount == null) {
        unreadCount = 0;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Messages" />
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
                        <li><a href="<%= request.getContextPath() %>/message" class="active"><i class="fas fa-envelope"></i> Messages <% if (unreadCount > 0) { %><span class="badge"><%= unreadCount %></span><% } %></a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-envelope"></i> Messages <% if (unreadCount > 0) { %><span class="badge"><%= unreadCount %></span><% } %></h2>
                        <div class="dashboard-actions">
                            <a href="<%= request.getContextPath() %>/message/new" class="btn"><i class="fas fa-plus"></i> New Message</a>
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

                    <div class="card">
                        <% if (conversationUsers.isEmpty()) { %>
                            <div class="empty-state">
                                <i class="fas fa-envelope empty-icon"></i>
                                <h3>No Messages Yet</h3>
                                <p>You haven't exchanged any messages yet. Start a conversation with a creator to discuss collaborations.</p>
                                <a href="<%= request.getContextPath() %>/message/new" class="btn mt-3"><i class="fas fa-plus"></i> New Message</a>
                            </div>
                        <% } else { %>
                            <div class="conversation-list">
                                <% for (User user : conversationUsers) { %>
                                    <a href="<%= request.getContextPath() %>/message/conversation/<%= user.getUserId() %>" class="conversation-item">
                                        <div class="conversation-avatar">
                                            <i class="fas fa-user-circle"></i>
                                        </div>
                                        <div class="conversation-info">
                                            <h4 class="conversation-name"><%= user.getUsername() %></h4>
                                            <p class="conversation-preview">Click to view conversation</p>
                                        </div>
                                        <!-- <span class="conversation-time">2h ago</span> -->
                                    </a>
                                <% } %>
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
