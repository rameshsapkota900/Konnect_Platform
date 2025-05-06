<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Check if user is logged in and has business role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("business")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Create Campaign" />
</jsp:include>

<jsp:include page="/includes/nav-business.jsp">
    <jsp:param name="active" value="campaigns" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Business Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile"><i class="fas fa-building"></i> Company Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign" class="active"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile/creators"><i class="fas fa-users"></i> Find Creators</a></li>
                        <li><a href="<%= request.getContextPath() %>/application"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/message"><i class="fas fa-envelope"></i> Messages</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-plus-circle"></i> Create Campaign</h2>
                        <div class="dashboard-actions">
                            <a href="<%= request.getContextPath() %>/campaign" class="btn btn-outline btn-fixed-height"><i class="fas fa-arrow-left"></i> Back to Campaigns</a>
                        </div>
                    </div>

                    <% if (request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <i class="fas fa-exclamation-circle"></i> <%= request.getAttribute("error") %>
                    </div>
                    <% } %>

                    <div class="card">
                        <form action="<%= request.getContextPath() %>/campaign/create" method="post" class="form">
                            <div class="form-group">
                                <label for="title">Campaign Title <span class="required">*</span></label>
                                <input type="text" id="title" name="title" class="form-control" required>
                                <div class="form-text">Enter a clear, descriptive title for your campaign.</div>
                            </div>

                            <div class="form-group">
                                <label for="description">Campaign Description <span class="required">*</span></label>
                                <textarea id="description" name="description" class="form-control" rows="5" required></textarea>
                                <div class="form-text">Describe your campaign in detail, including what you're looking for from creators.</div>
                            </div>

                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label for="budget">Budget (USD) <span class="required">*</span></label>
                                    <input type="number" id="budget" name="budget" class="form-control" min="1" step="0.01" required>
                                    <div class="form-text">Enter your campaign budget in USD.</div>
                                </div>

                                <div class="form-group col-md-6">
                                    <label for="goals">Campaign Goals <span class="required">*</span></label>
                                    <input type="text" id="goals" name="goals" class="form-control" required>
                                    <div class="form-text">What are your goals for this campaign? (e.g., Increase brand awareness)</div>
                                </div>
                            </div>

                            <div class="form-actions">
                                <button type="submit" class="btn btn-primary btn-fixed-height"><i class="fas fa-save"></i> Create Campaign</button>
                                <a href="<%= request.getContextPath() %>/campaign" class="btn btn-outline btn-fixed-height">Cancel</a>
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
