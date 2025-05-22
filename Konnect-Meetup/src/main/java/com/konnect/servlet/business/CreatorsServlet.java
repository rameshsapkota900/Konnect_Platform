package com.konnect.servlet.business;

import com.konnect.dao.CreatorDAO;
import com.konnect.model.Creator;
import com.konnect.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CreatorsServlet
 * Handles creator browsing for businesses
 */
@WebServlet("/business/creators")
public class CreatorsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CreatorDAO creatorDAO;
    
    @Override
    public void init() {
        creatorDAO = new CreatorDAO();
    }
    
    /**
     * Handle GET requests - display creators list
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in and is a business
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"business".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get filter parameters
        String minFollowersStr = request.getParameter("minFollowers");
        String interestFilter = request.getParameter("interest");
        String searchQuery = request.getParameter("search");
        
        // Get all creators
        List<Creator> allCreators = getAllCreators();
        List<Creator> filteredCreators = new ArrayList<>();
        
        // Apply filters
        int minFollowers = 0;
        if (minFollowersStr != null && !minFollowersStr.isEmpty()) {
            try {
                minFollowers = Integer.parseInt(minFollowersStr);
            } catch (NumberFormatException e) {
                // Invalid follower count, ignore filter
            }
        }
        
        for (Creator creator : allCreators) {
            // Apply minimum followers filter
            if (minFollowers > 0 && creator.getFollowerCount() < minFollowers) {
                continue;
            }
            
            // Apply interest filter
            if (interestFilter != null && !interestFilter.isEmpty()) {
                if (creator.getInterests() == null || !creator.getInterests().contains(interestFilter)) {
                    continue;
                }
            }
            
            // Apply search query filter
            if (searchQuery != null && !searchQuery.isEmpty()) {
                if (!creator.getUsername().toLowerCase().contains(searchQuery.toLowerCase()) && 
                    (creator.getBio() == null || !creator.getBio().toLowerCase().contains(searchQuery.toLowerCase()))) {
                    continue;
                }
            }
            
            filteredCreators.add(creator);
        }
        
        // Get all unique interests from creators for filter dropdown
        List<String> allInterests = new ArrayList<>();
        for (Creator creator : allCreators) {
            if (creator.getInterests() != null) {
                for (String interest : creator.getInterests()) {
                    if (!allInterests.contains(interest)) {
                        allInterests.add(interest);
                    }
                }
            }
        }
        
        // Set attributes for the view
        request.setAttribute("creators", filteredCreators);
        request.setAttribute("minFollowers", minFollowersStr);
        request.setAttribute("interestFilter", interestFilter);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("allInterests", allInterests);
        
        // Forward to creators page
        request.getRequestDispatcher("/business/creators.jsp").forward(request, response);
    }
    
    /**
     * Get all creators
     * @return List of all creators
     */
    private List<Creator> getAllCreators() {
        List<Creator> creators = new ArrayList<>();
        
        // Get all users with role 'creator'
        List<User> creatorUsers = getAllCreatorUsers();
        
        // Get creator profiles for each user
        for (User user : creatorUsers) {
            Creator creator = creatorDAO.getByUserId(user.getId());
            if (creator != null) {
                creators.add(creator);
            }
        }
        
        return creators;
    }
    
    /**
     * Get all users with role 'creator'
     * @return List of all creator users
     */
    private List<User> getAllCreatorUsers() {
        String sql = "SELECT * FROM users WHERE role = 'creator' AND status = 'active'";
        
        java.sql.Connection conn = null;
        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;
        List<User> users = new ArrayList<>();
        
        try {
            conn = com.konnect.util.DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                
                users.add(user);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) com.konnect.util.DBConnection.closeConnection(conn);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        
        return users;
    }
}
