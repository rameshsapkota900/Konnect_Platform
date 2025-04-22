<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.User" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="Manage Users"/>
</jsp:include>

<h2>Manage Users</h2>
<p>View, ban, or unban users on the platform.</p>

<% List<User> userList = (List<User>) request.getAttribute("userList"); %>

<% if (userList == null || userList.isEmpty()) { %>
    <div class="alert alert-info">No users found.</div>
<% } else { %>
    <div class="table-responsive">
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); %>
                <% User adminUser = (User) session.getAttribute("user"); %>
                <% for (User user : userList) { %>
                 <% if (adminUser != null && user.getUserId() == adminUser.getUserId()) continue; // Skip showing the current admin %>
                    <tr>
                        <td><%= user.getUserId() %></td>
                        <td><%= user.getUsername() %></td>
                        <td><%= user.getEmail() %></td>
                        <td><%= user.getRole() %></td>
                        <td>
                            <% if (user.isActive()) { %>
                                <span class="text-success">Active</span>
                            <% } else { %>
                                <span class="text-danger">Banned</span>
                            <% } %>
                        </td>
                         <td><%= user.getCreatedAt() != null ? user.getCreatedAt().toLocalDateTime().format(formatter) : "N/A" %></td>
                        <td class="actions">
                            <form action="<%= request.getContextPath() %>/admin/users" method="POST" style="display: inline;">
                                <input type="hidden" name="action" value="toggleStatus">
                                <input type="hidden" name="userId" value="<%= user.getUserId() %>">
                                <input type="hidden" name="currentStatus" value="<%= user.isActive() %>">
                                <% if (user.isActive()) { %>
                                    <button type="submit" class="btn btn-sm btn-danger confirm-delete" data-confirm-message="Are you sure you want to BAN this user?">Ban</button>
                                <% } else { %>
                                    <button type="submit" class="btn btn-sm btn-success">Unban</button>
                                <% } %>
                            </form>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>
<% } %>

<jsp:include page="/common/footer.jsp" />
