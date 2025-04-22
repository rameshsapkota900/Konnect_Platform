<%@ page import="com.konnect.model.User" %>
<%
    User loggedInUser = (User) session.getAttribute("user");
    String targetUrl = request.getContextPath();

    if (loggedInUser != null) {
        // User is logged in, redirect to their dashboard
        switch (loggedInUser.getRole()) {
            case "admin":
                targetUrl += "/admin/dashboard";
                break;
            case "creator":
                targetUrl += "/creator/dashboard";
                break;
            case "business":
                targetUrl += "/business/dashboard";
                break;
            default:
                targetUrl += "/login.jsp"; // Fallback if role is weird
                break;
        }
    } else {
        // User not logged in, redirect to login page
        targetUrl += "/login.jsp";
    }
    response.sendRedirect(targetUrl);
%>
<%-- This page should have no visible content as it always redirects --%>
