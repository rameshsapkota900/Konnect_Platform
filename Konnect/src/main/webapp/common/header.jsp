<%@ page import="com.konnect.model.User" %>
<%@ page import="com.konnect.dao.MessageDao" %>
<%
    // Get user from session
    User loggedInUser = (User) session.getAttribute("user");
    String userRole = (loggedInUser != null) ? loggedInUser.getRole() : "";
    String currentPath = request.getRequestURI().substring(request.getContextPath().length());

    int unreadMessages = 0;
    if (loggedInUser != null && !"admin".equals(userRole)) {
        MessageDao msgDao = new MessageDao();
        unreadMessages = msgDao.countUnreadMessages(loggedInUser.getUserId());
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getParameter("title") != null ? request.getParameter("title") + " | Konnect" : "Konnect Platform" %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<header class="main-header">
    <div class="container">
        <h1><a href="<%= request.getContextPath() %>/">Konnect</a></h1>
        <% if (loggedInUser != null) { %>
            <div class="user-info">
                <span>Welcome, <%= loggedInUser.getUsername() %></span>
                <% if (!"admin".equals(userRole) && unreadMessages > 0) { %>
                    <a href="<%= request.getContextPath() %>/<%= userRole %>/chat" class="btn btn-sm" style="margin-left: 10px;">
                        Messages <span class="badge"><%= unreadMessages %></span>
                    </a>
                <% } %>
                <a href="<%= request.getContextPath() %>/LogoutServlet" class="btn btn-secondary btn-sm" style="margin-left: 15px;">Logout</a>
            </div>
        <% } %>
    </div>
</header>

<div class="page-wrapper">
    <% if (loggedInUser != null) { %>
        <aside class="sidebar">
            <h3>Navigation</h3>
            <ul>
                <% if ("admin".equals(userRole)) { %>
                    <li><a href="<%= request.getContextPath() %>/admin/dashboard" class="<%= currentPath.startsWith("/admin/dashboard") ? "active" : "" %>">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/users" class="<%= currentPath.startsWith("/admin/users") ? "active" : "" %>">Manage Users</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/campaigns" class="<%= currentPath.startsWith("/admin/campaigns") ? "active" : "" %>">View Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/admin/reports" class="<%= currentPath.startsWith("/admin/reports") ? "active" : "" %>">View Reports</a></li>
                <% } else if ("creator".equals(userRole)) { %>
                    <li><a href="<%= request.getContextPath() %>/creator/dashboard" class="<%= currentPath.startsWith("/creator/dashboard") ? "active" : "" %>">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/creator/profile" class="<%= currentPath.startsWith("/creator/profile") ? "active" : "" %>">My Profile</a></li>
                    <li><a href="<%= request.getContextPath() %>/creator/campaigns" class="<%= currentPath.startsWith("/creator/campaigns") ? "active" : "" %>">Browse Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/creator/applications" class="<%= currentPath.startsWith("/creator/applications") ? "active" : "" %>">My Applications/Invites</a></li>
                    <li><a href="<%= request.getContextPath() %>/creator/chat" class="<%= currentPath.startsWith("/creator/chat") ? "active" : "" %>">
                        Chat <% if(unreadMessages > 0) { %><span class="badge"><%= unreadMessages %></span><% } %>
                    </a></li>
                <% } else if ("business".equals(userRole)) { %>
                    <li><a href="<%= request.getContextPath() %>/business/dashboard" class="<%= currentPath.startsWith("/business/dashboard") ? "active" : "" %>">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/campaigns" class="<%= currentPath.startsWith("/business/campaigns") && (request.getQueryString() == null || !request.getQueryString().contains("action=create")) ? "active" : "" %>">My Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/campaigns?action=create" class="<%= currentPath.startsWith("/business/campaigns") && request.getQueryString() != null && request.getQueryString().contains("action=create") ? "active" : "" %>">Create Campaign</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/applications" class="<%= currentPath.startsWith("/business/applications") ? "active" : "" %>">Campaign Applications</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/search" class="<%= currentPath.startsWith("/business/search") ? "active" : "" %>">Search Creators</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/chat" class="<%= currentPath.startsWith("/business/chat") ? "active" : "" %>">
                        Chat <% if(unreadMessages > 0) { %><span class="badge"><%= unreadMessages %></span><% } %>
                    </a></li>
                <% } %>
            </ul>
        </aside>
        <main class="main-content">
    <% } else { %>
        <main class="container">
    <% } %>

    <!-- Flash messages -->
    <% String successMessage = (String) session.getAttribute("message"); %>
    <% String errorMessage = (String) session.getAttribute("error"); %>

    <% if (successMessage != null) { %>
        <div class="alert alert-success">
            <%= successMessage %>
            <button type="button" class="alert-close" style="float: right; background: none; border: none; font-size: 1.2em; line-height: 1; cursor: pointer;">×</button>
        </div>
        <% session.removeAttribute("message"); %>
    <% } %>

    <% if (errorMessage != null) { %>
        <div class="alert alert-danger">
            <%= errorMessage %>
            <button type="button" class="alert-close" style="float: right; background: none; border: none; font-size: 1.2em; line-height: 1; cursor: pointer;">×</button>
        </div>
        <% session.removeAttribute("error"); %>
    <% } %>

    <% String requestError = (String) request.getAttribute("error"); %>
    <% if (requestError != null) { %>
        <div class="alert alert-danger">
            <%= requestError %>
            <button type="button" class="alert-close" style="float: right; background: none; border: none; font-size: 1.2em; line-height: 1; cursor: pointer;">×</button>
        </div>
    <% } %>

    <% String requestMessage = (String) request.getAttribute("message"); %>
    <% if (requestMessage != null) { %>
        <div class="alert alert-success">
            <%= requestMessage %>
            <button type="button" class="alert-close" style="float: right; background: none; border: none; font-size: 1.2em; line-height: 1; cursor: pointer;">×</button>
        </div>
    <% } %>

    <!-- Page content goes here -->
