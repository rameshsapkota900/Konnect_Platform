package com.konnect.controller.business;

import com.konnect.dao.ProfileDao;
import com.konnect.model.Profile;
import com.konnect.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/business/search")
public class BusinessCreatorSearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProfileDao profileDao = new ProfileDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"business".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get search parameters
        String keyword = request.getParameter("keyword");
        String niche = request.getParameter("niche");
        String minFollowersStr = request.getParameter("minFollowers");

        int minFollowers = 0;
        if (minFollowersStr != null && !minFollowersStr.isEmpty()) {
            try {
                minFollowers = Integer.parseInt(minFollowersStr);
            } catch (NumberFormatException e) { /* ignore, keep 0 */ }
        }

        // Perform search using DAO
        List<Profile> creatorProfiles = profileDao.searchCreators(keyword, niche, minFollowers);

        // Pass results and search parameters back to JSP for display and sticky form fields
        request.setAttribute("creatorProfiles", creatorProfiles);
        request.setAttribute("searchKeyword", keyword);
        request.setAttribute("searchNiche", niche);
        request.setAttribute("searchMinFollowers", minFollowers > 0 ? minFollowers : ""); // Keep empty if 0


        RequestDispatcher dispatcher = request.getRequestDispatcher("/business/search_creators.jsp");
        dispatcher.forward(request, response);
    }

     // doPost could potentially handle actions from the search results, like "View Profile" or "Invite",
     // but often GET is sufficient for search/filter actions.
     // If "Invite" is directly on this page, it might POST here or to a dedicated InviteServlet.
     // Let's keep it simple for now and assume details/invite happens on profile view or via InviteServlet.

}
