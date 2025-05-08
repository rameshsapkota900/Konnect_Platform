package com.konnect.dao;

import com.konnect.model.Business;
import com.konnect.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusinessDAO {
    public BusinessDAO() {
    }

    public boolean insert(Business business) {
        int userId = business.getId();
        if (userId <= 0) {
            return false;
        } else {
            String sql = "INSERT INTO business_profiles (user_id, company_name, industry, description, website, contact_phone) VALUES (?, ?, ?, ?, ?, ?)";
            Connection conn = null;
            PreparedStatement pstmt = null;
            boolean success = false;

            try {
                conn = DBConnection.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, userId);
                pstmt.setString(2, business.getCompanyName());
                pstmt.setString(3, business.getIndustry());
                pstmt.setString(4, business.getDescription());
                pstmt.setString(5, business.getWebsite());
                pstmt.setString(6, business.getContactPhone());
                int affectedRows = pstmt.executeUpdate();
                success = affectedRows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }

                    if (conn != null) {
                        DBConnection.closeConnection(conn);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            return success;
        }
    }

    public Business getByUserId(int userId) {
        String sql = "SELECT b.*, u.* FROM business_profiles b JOIN users u ON b.user_id = u.id WHERE u.id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Business business = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                business = this.mapResultSetToBusiness(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                }

                if (conn != null) {
                    DBConnection.closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return business;
    }

    public int getBusinessProfileId(int userId) {
        String sql = "SELECT id FROM business_profiles WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int businessProfileId = -1;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                businessProfileId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                }

                if (conn != null) {
                    DBConnection.closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return businessProfileId;
    }

    public boolean update(Business business) {
        String sql = "UPDATE business_profiles SET company_name = ?, industry = ?, description = ?, website = ?, contact_phone = ?, updated_at = NOW() WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, business.getCompanyName());
            pstmt.setString(2, business.getIndustry());
            pstmt.setString(3, business.getDescription());
            pstmt.setString(4, business.getWebsite());
            pstmt.setString(5, business.getContactPhone());
            pstmt.setInt(6, business.getId());
            int affectedRows = pstmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                if (conn != null) {
                    DBConnection.closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return success;
    }

    public List<Business> getAllByRole(String role) {
        if (!"business".equals(role)) {
            return new ArrayList();
        } else {
            String sql = "SELECT b.*, u.* FROM business_profiles b JOIN users u ON b.user_id = u.id WHERE u.role = ? AND u.status = 'active'";
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<Business> businesses = new ArrayList();

            try {
                conn = DBConnection.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, role);
                rs = pstmt.executeQuery();

                while(rs.next()) {
                    Business business = this.mapResultSetToBusiness(rs);
                    businesses.add(business);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }

                    if (pstmt != null) {
                        pstmt.close();
                    }

                    if (conn != null) {
                        DBConnection.closeConnection(conn);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            return businesses;
        }
    }

    private Business mapResultSetToBusiness(ResultSet rs) throws SQLException {
        Business business = new Business();
        business.setId(rs.getInt("u.id"));
        business.setUsername(rs.getString("u.username"));
        business.setEmail(rs.getString("u.email"));
        business.setPassword(rs.getString("u.password"));
        business.setRole(rs.getString("u.role"));
        business.setStatus(rs.getString("u.status"));
        business.setCreatedAt(rs.getTimestamp("u.created_at"));
        business.setUpdatedAt(rs.getTimestamp("u.updated_at"));
        business.setCompanyName(rs.getString("b.company_name"));
        business.setIndustry(rs.getString("b.industry"));
        business.setDescription(rs.getString("b.description"));
        business.setWebsite(rs.getString("b.website"));
        business.setContactPhone(rs.getString("b.contact_phone"));
        return business;
    }
}
