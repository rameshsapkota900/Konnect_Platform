package com.konnect.dao;

import com.konnect.model.Report;
import com.konnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ReportDAO Class
 * Handles database operations for Report objects
 */
public class ReportDAO {
    
    /**
     * Insert a new report into the database
     * @param report Report object to insert
     * @return generated report ID if successful, -1 otherwise
     */
    public int insert(Report report) {
        String sql = "INSERT INTO reports (reporter_id, reported_user_id, reason, description, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int reportId = -1;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, report.getReporterId());
            pstmt.setInt(2, report.getReportedUserId());
            pstmt.setString(3, report.getReason());
            pstmt.setString(4, report.getDescription());
            pstmt.setString(5, report.getStatus());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    reportId = rs.getInt(1);
                    report.setId(reportId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return reportId;
    }
    
    /**
     * Get a report by ID
     * @param id Report ID
     * @return Report object if found, null otherwise
     */
    public Report getById(int id) {
        String sql = "SELECT * FROM reports WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Report report = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                report = mapResultSetToReport(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return report;
    }
    
    /**
     * Get all reports
     * @return List of all reports
     */
    public List<Report> getAll() {
        String sql = "SELECT * FROM reports ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Report> reports = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Report report = mapResultSetToReport(rs);
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return reports;
    }
    
    /**
     * Get reports by status
     * @param status Report status
     * @return List of reports with the specified status
     */
    public List<Report> getByStatus(String status) {
        String sql = "SELECT * FROM reports WHERE status = ? ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Report> reports = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Report report = mapResultSetToReport(rs);
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return reports;
    }
    
    /**
     * Get reports by reported user ID
     * @param reportedUserId Reported user ID
     * @return List of reports against the user
     */
    public List<Report> getByReportedUserId(int reportedUserId) {
        String sql = "SELECT * FROM reports WHERE reported_user_id = ? ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Report> reports = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reportedUserId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Report report = mapResultSetToReport(rs);
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return reports;
    }
    
    /**
     * Update report status
     * @param reportId Report ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateStatus(int reportId, String status) {
        String sql = "UPDATE reports SET status = ?, updated_at = NOW() WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, status);
            pstmt.setInt(2, reportId);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return success;
    }
    
    /**
     * Count reports by status
     * @param status Report status
     * @return Number of reports with the specified status
     */
    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM reports WHERE status = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return count;
    }
    
    /**
     * Map a ResultSet to a Report object
     * @param rs ResultSet containing report data
     * @return Report object
     * @throws SQLException if a database access error occurs
     */
    private Report mapResultSetToReport(ResultSet rs) throws SQLException {
        Report report = new Report();
        
        report.setId(rs.getInt("id"));
        report.setReporterId(rs.getInt("reporter_id"));
        report.setReportedUserId(rs.getInt("reported_user_id"));
        report.setReason(rs.getString("reason"));
        report.setDescription(rs.getString("description"));
        report.setStatus(rs.getString("status"));
        report.setCreatedAt(rs.getTimestamp("created_at"));
        report.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return report;
    }
}
