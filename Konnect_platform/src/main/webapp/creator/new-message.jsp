<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.User" %>
<%
    // Check if user is logged in and has creator role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("creator")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Get potential recipients from request
    List<User> recipients = (List<User>) request.getAttribute("recipients");
    if (recipients == null) {
        // If not set, redirect to message servlet to get the data
        response.sendRedirect(request.getContextPath() + "/message/new");
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="New Message" />
</jsp:include>

<jsp:include page="/includes/nav-creator.jsp">
    <jsp:param name="active" value="messages" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Creator Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile"><i class="fas fa-user-circle"></i> Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/application"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/message" class="active"><i class="fas fa-envelope"></i> Messages</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title">
                            <a href="<%= request.getContextPath() %>/message" class="back-link"><i class="fas fa-arrow-left"></i></a>
                            New Message
                        </h2>
                    </div>

                    <% if (request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <i class="fas fa-exclamation-circle"></i> <%= request.getAttribute("error") %>
                    </div>
                    <% } %>

                    <div class="card">
                        <form action="<%= request.getContextPath() %>/message/send" method="post" class="form">
                            <div class="form-group">
                                <label for="receiverId">Recipient <span class="required">*</span></label>
                                <select id="receiverId" name="receiverId" class="form-control" required>
                                    <option value="" disabled selected>Select a business</option>
                                    <% for (User recipient : recipients) { %>
                                        <option value="<%= recipient.getUserId() %>"><%= recipient.getUsername() %></option>
                                    <% } %>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label for="content">Message <span class="required">*</span></label>
                                <textarea id="content" name="content" class="form-control" rows="6" required></textarea>
                            </div>
                            
                            <div class="form-actions">
                                <button type="submit" class="btn btn-primary"><i class="fas fa-paper-plane"></i> Send Message</button>
                                <a href="<%= request.getContextPath() %>/message" class="btn btn-outline">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
