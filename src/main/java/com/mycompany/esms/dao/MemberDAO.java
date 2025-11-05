package com.mycompany.esms.dao;

import com.mycompany.esms.model.Member;
import com.mycompany.esms.util.PasswordUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO extends DAO {
    public MemberDAO() { super(); }

    public Member authenticate(String username, String password) {
        String sql = "SELECT id, name, username, password, birthdate, email, phoneNumber, role "
                   + "FROM tblMember WHERE username = ?";
        Connection conn = con;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashed = rs.getString("password");
                    if (!PasswordUtils.matches(password, hashed)) {
                        return null;
                    }
                    Member m = new Member();
                    m.setId(rs.getInt("id"));
                    m.setName(rs.getString("name"));
                    m.setUsername(rs.getString("username"));
                    m.setPassword(hashed);
                    java.sql.Date bd = rs.getDate("birthdate");
                    if (bd != null) m.setBirthdate(bd.toLocalDate());
                    m.setEmail(rs.getString("email"));
                    m.setPhoneNumber(rs.getString("phoneNumber"));
                    m.setRole(rs.getString("role"));
                    return m;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to authenticate member", e);
        }
        return null;
    }

    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM tblMember WHERE username = ?";
        Connection conn = con;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check username existence", e);
        }
        return false;
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM tblMember WHERE email = ?";
        Connection conn = con;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check email existence", e);
        }
        return false;
    }

    public void insertMember(Member member) {
        Connection conn = null;
        try {
            conn = con;
            conn.setAutoCommit(false);
            
            String sql = "INSERT INTO tblMember (name, username, password, birthdate, email, phoneNumber, role) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getName());
            ps.setString(2, member.getUsername());
            ps.setString(3, PasswordUtils.hashPassword(member.getPassword()));
            if (member.getBirthdate() != null) {
                ps.setDate(4, java.sql.Date.valueOf(member.getBirthdate()));
            } else {
                ps.setDate(4, null);
            }
            ps.setString(5, member.getEmail());
            ps.setString(6, member.getPhoneNumber());
            String role = member.getRole() != null ? member.getRole() : "customer";
            ps.setString(7, role);
            ps.executeUpdate();
            
            // Lấy ID vừa insert
            ResultSet rs = ps.getGeneratedKeys();
            int memberId = 0;
            if (rs.next()) {
                memberId = rs.getInt(1);
            }
            rs.close();
            ps.close();
            
            // Nếu role là "customer", insert vào tblCustomer
            if ("customer".equalsIgnoreCase(role)) {
                String customerSql = "INSERT INTO tblCustomer (tblMemberId) VALUES (?)";
                PreparedStatement customerPs = conn.prepareStatement(customerSql);
                customerPs.setInt(1, memberId);
                customerPs.executeUpdate();
                customerPs.close();
            }
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException("Failed to rollback transaction", ex);
                }
            }
            throw new RuntimeException("Failed to insert member", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to reset autocommit", e);
                }
            }
        }
    }
}
