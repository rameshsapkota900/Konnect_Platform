package com.konnect.dao;

import com.konnect.config.DatabaseConnection;
import com.konnect.model.Report;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDao {

    public boolean createReport(Report report) {
        String sql = "INSERT INTO reports (reporter_user_id, reported_user_id, reason, details, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, report.getReporterUserId());
            stmt.setInt(2, report.getReportedUserId());
            stmt.setString(3, report.getReason());
            stmt.setString(4, report.getDetails());
            stmt.setString(5, "new"); // Default status

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error creating report: " + e.getMessage());
            return false;
        }
    }

    // Get all reports (for Admin view)
    public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT r.*, u_reporter.username as reporterUsername, u_reported.username as reportedUsername " +
                     "FROM reports r " +
                     "JOIN users u_reporter ON r.reporter_user_id = u_reporter.user_id " +
                     "JOIN users u_reported ON r.reported_user_id = u_reported.user_id " +
                     "ORDER BY r.status ASC, r.reported_at DESC"; // Show 'new' reports first

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reports.add(mapResultSetToReport(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all reports: " + e.getMessage());
        }
        return reports;
    }

     public Report findById(int reportId) {
        String sql = "SELECT r.*, u_reporter.username as reporterUsername, u_reported.username as reportedUsername " +
                     "FROM reports r " +
                     "JOIN users u_reporter ON r.reporter_user_id = u_reporter.user_id " +
                     "JOIN users u_reported ON r.reported_user_id = u_reported.user_id " +
                     "WHERE r.report_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToReport(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding report by ID: " + e.getMessage());
        }
        return null;
    }


    // Update report status (for Admin action)
    public boolean updateReportStatus(int reportId, String status) {
        String sql = "UPDATE reports SET status = ? WHERE report_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

             if (!"new".equalsIgnoreCase(status) && !"resolved".equalsIgnoreCase(status)) {
                 System.err.println("Invalid report status: " + status);
                 return false; // Invalid status
             }

            stmt.setString(1, status);
            stmt.setInt(2, reportId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating report status: " + e.getMessage());
            return false;
        }
    }

    private Report mapResultSetToReport(ResultSet rs) throws SQLException {
        Report report = new Report();
        report.setReportId(rs.getInt("report_id"));
        report.setReporterUserId(rs.getInt("reporter_user_id"));
        report.setReportedUserId(rs.getInt("reported_user_id"));
        report.setReason(rs.getString("reason"));
        report.setDetails(rs.getString("details"));
        report.setReportedAt(rs.getTimestamp("reported_at"));
        report.setStatus(rs.getString("status"));

        // Map joined fields if present
         if (hasColumn(rs, "reporterUsername")) {
            report.setReporterUsername(rs.getString("reporterUsername"));
        }
         if (hasColumn(rs, "reportedUsername")) {
            report.setReportedUsername(rs.getString("reportedUsername"));
        }

        return report;
    }

    // Helper to check if a column exists
    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equalsIgnoreCase(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }
}
