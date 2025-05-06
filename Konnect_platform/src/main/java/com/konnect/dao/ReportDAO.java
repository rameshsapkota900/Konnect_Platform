package com.konnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.konnect.model.Report;
import com.konnect.util.DBConnection;

/**
 * Data Access Object for Report-related database operations
 */
public class ReportDAO {

    /**
     * Create a new report
     * @param report Report object with report details
     * @return Report ID if successful, -1 if failed
     */
    public int createReport(Report report) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int reportId = -1;

        try {
            conn = DBConnection.getConnection();

            String sql = "INSERT INTO reports (reporter_id, reported_user_id, reason, description) VALUES (?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, report.getReporterId());
            pstmt.setInt(2, report.getReportedId());
            pstmt.setString(3, report.getReason());
            pstmt.setString(4, report.getDescription());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    reportId = rs.getInt(1);
                    report.setReportId(reportId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return reportId;
    }

    /**
     * Get report by ID
     * @param reportId Report ID
     * @return Report object if found, null otherwise
     */
    public Report getReportById(int reportId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Report report = null;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT r.*, u1.username as reporter_username, u1.role as reporter_role, " +
                         "u2.username as reported_username, u2.role as reported_role " +
                         "FROM reports r " +
                         "JOIN users u1 ON r.reporter_id = u1.user_id " +
                         "JOIN users u2 ON r.reported_id = u2.user_id " +
                         "WHERE r.report_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reportId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                report = new Report();
                report.setReportId(rs.getInt("report_id"));
                report.setReporterId(rs.getInt("reporter_id"));
                report.setReportedId(rs.getInt("reported_user_id"));
                report.setReason(rs.getString("reason"));
                report.setDescription(rs.getString("description"));
                report.setStatus(rs.getString("status"));
                report.setCreatedAt(rs.getTimestamp("created_at"));
                report.setUpdatedAt(rs.getTimestamp("updated_at"));
                report.setReporterUsername(rs.getString("reporter_username"));
                report.setReportedUsername(rs.getString("reported_username"));
                report.setReporterRole(rs.getString("reporter_role"));
                report.setReportedRole(rs.getString("reported_role"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
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
    public List<Report> getAllReports() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Report> reports = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT r.*, u1.username as reporter_username, u1.role as reporter_role, " +
                         "u2.username as reported_username, u2.role as reported_role " +
                         "FROM reports r " +
                         "JOIN users u1 ON r.reporter_id = u1.user_id " +
                         "JOIN users u2 ON r.reported_id = u2.user_id " +
                         "ORDER BY r.created_at DESC";

            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Report report = new Report();
                report.setReportId(rs.getInt("report_id"));
                report.setReporterId(rs.getInt("reporter_id"));
                report.setReportedId(rs.getInt("reported_user_id"));
                report.setReason(rs.getString("reason"));
                report.setDescription(rs.getString("description"));
                report.setStatus(rs.getString("status"));
                report.setCreatedAt(rs.getTimestamp("created_at"));
                report.setUpdatedAt(rs.getTimestamp("updated_at"));
                report.setReporterUsername(rs.getString("reporter_username"));
                report.setReportedUsername(rs.getString("reported_username"));
                report.setReporterRole(rs.getString("reporter_role"));
                report.setReportedRole(rs.getString("reported_role"));

                reports.add(report);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return reports;
    }

    /**
     * Get reports by status
     * @param status Report status
     * @return List of reports with the given status
     */
    public List<Report> getReportsByStatus(String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Report> reports = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT r.*, u1.username as reporter_username, u1.role as reporter_role, " +
                         "u2.username as reported_username, u2.role as reported_role " +
                         "FROM reports r " +
                         "JOIN users u1 ON r.reporter_id = u1.user_id " +
                         "JOIN users u2 ON r.reported_id = u2.user_id " +
                         "WHERE r.status = ? " +
                         "ORDER BY r.created_at DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Report report = new Report();
                report.setReportId(rs.getInt("report_id"));
                report.setReporterId(rs.getInt("reporter_id"));
                report.setReportedId(rs.getInt("reported_user_id"));
                report.setReason(rs.getString("reason"));
                report.setDescription(rs.getString("description"));
                report.setStatus(rs.getString("status"));
                report.setCreatedAt(rs.getTimestamp("created_at"));
                report.setUpdatedAt(rs.getTimestamp("updated_at"));
                report.setReporterUsername(rs.getString("reporter_username"));
                report.setReportedUsername(rs.getString("reported_username"));
                report.setReporterRole(rs.getString("reporter_role"));
                report.setReportedRole(rs.getString("reported_role"));

                reports.add(report);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return reports;
    }

    /**
     * Update report status
     * @param reportId Report ID
     * @param status New status (resolved/dismissed)
     * @return true if successful, false otherwise
     */
    public boolean updateReportStatus(int reportId, String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsUpdated = 0;

        try {
            conn = DBConnection.getConnection();

            String sql = "UPDATE reports SET status = ? WHERE report_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, reportId);

            rowsUpdated = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rowsUpdated > 0;
    }

    /**
     * Count total reports
     * @return Total number of reports
     */
    public int countTotalReports() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT COUNT(*) FROM reports";

            pstmt = conn.prepareStatement(sql);

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
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return count;
    }

    /**
     * Count reports by status
     * @param status Report status
     * @return Number of reports with the given status
     */
    public int countReportsByStatus(String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT COUNT(*) FROM reports WHERE status = ?";

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
                DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return count;
    }
}
