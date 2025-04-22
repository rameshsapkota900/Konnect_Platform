<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Report" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="User Reports"/>
</jsp:include>

<h2>User Reports</h2>
<p>Review reports submitted by users.</p>

<% List<Report> reportList = (List<Report>) request.getAttribute("reportList"); %>

<% if (reportList == null || reportList.isEmpty()) { %>
    <div class="alert alert-info">No reports found.</div>
<% } else { %>
    <div class="table-responsive">
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Reported By</th>
                    <th>Reported User</th>
                    <th>Reason</th>
                    <th>Details</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                 <% DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); %>
                <% for (Report report : reportList) { %>
                    <tr class="<%= "new".equals(report.getStatus()) ? "table-warning" : "" %>">
                        <td><%= report.getReportId() %></td>
                        <td><%= report.getReporterUsername() != null ? report.getReporterUsername() : "N/A" %></td>
                        <td><%= report.getReportedUsername() != null ? report.getReportedUsername() : "N/A" %></td>
                        <td><%= report.getReason() != null ? report.getReason() : "N/A" %></td>
                        <td><%= report.getDetails() != null && !report.getDetails().isEmpty() ? report.getDetails() : "-" %></td>
                        <td><%= report.getReportedAt() != null ? report.getReportedAt().toLocalDateTime().format(formatter) : "N/A" %></td>
                        <td>
                            <% String statusClass = "new".equals(report.getStatus()) ? "text-danger" : "text-success"; %>
                            <span class="<%= statusClass %>"><%= report.getStatus() %></span>
                        </td>
                        <td class="actions">
                            <% if ("new".equals(report.getStatus())) { %>
                                <form action="<%= request.getContextPath() %>/admin/reports" method="POST" style="display: inline;">
                                    <input type="hidden" name="action" value="resolve">
                                    <input type="hidden" name="reportId" value="<%= report.getReportId() %>">
                                    <button type="submit" class="btn btn-sm btn-success">Mark Resolved</button>
                                </form>
                            <% } else { %>
                                <span class="text-muted">Resolved</span>
                            <% } %>
                            <a href="<%= request.getContextPath() %>/admin/users" class="btn btn-sm">View Users</a>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>
<% } %>

<jsp:include page="/common/footer.jsp" />
