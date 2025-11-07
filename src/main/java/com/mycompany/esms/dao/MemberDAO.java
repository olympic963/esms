package com.mycompany.esms.dao;

import com.mycompany.esms.model.Member;
import com.mycompany.esms.util.PasswordUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO extends DAO {
    private static final int MAX_FAILED_ATTEMPTS = 5;

    public MemberDAO() { super(); }

    public static class AuthenticationResult {
        public enum Status { SUCCESS, INVALID_CREDENTIALS, ACCOUNT_LOCKED }

        private final Status status;
        private final Member member;
        private final int remainingAttempts;

        public AuthenticationResult(Status status, Member member, int remainingAttempts) {
            this.status = status;
            this.member = member;
            this.remainingAttempts = remainingAttempts;
        }

        public Status getStatus() { return status; }
        public Member getMember() { return member; }
        public int getRemainingAttempts() { return remainingAttempts; }
    }

    public AuthenticationResult authenticateWithLock(String username, String password) {
        String sql = "SELECT id, name, username, password, birthdate, email, phoneNumber, role, failedAttempts, locked "
                   + "FROM tblMember WHERE username = ?";
        Connection conn = con;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return new AuthenticationResult(AuthenticationResult.Status.INVALID_CREDENTIALS, null, MAX_FAILED_ATTEMPTS);
                }

                boolean locked = rs.getBoolean("locked");
                int memberId = rs.getInt("id");
                int failedAttempts = rs.getInt("failedAttempts");

                if (locked) {
                    return new AuthenticationResult(AuthenticationResult.Status.ACCOUNT_LOCKED, null, 0);
                }

                String hashed = rs.getString("password");

                if (PasswordUtils.matches(password, hashed)) {
                    resetFailedAttempts(conn, memberId);
                    Member member = mapMember(rs, hashed);
                    member.setFailedAttempts(0);
                    member.setLocked(false);
                    return new AuthenticationResult(AuthenticationResult.Status.SUCCESS, member, MAX_FAILED_ATTEMPTS);
                }

                int updatedAttempts = failedAttempts + 1;
                boolean nowLocked = updatedAttempts >= MAX_FAILED_ATTEMPTS;
                updateFailedAttempts(conn, memberId, updatedAttempts, nowLocked);
                int remaining = Math.max(0, MAX_FAILED_ATTEMPTS - updatedAttempts);
                AuthenticationResult.Status status = nowLocked
                        ? AuthenticationResult.Status.ACCOUNT_LOCKED
                        : AuthenticationResult.Status.INVALID_CREDENTIALS;
                return new AuthenticationResult(status, null, remaining);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to authenticate member", e);
        }
    }

    private Member mapMember(ResultSet rs, String hashedPassword) throws SQLException {
        Member m = new Member();
        m.setId(rs.getInt("id"));
        m.setName(rs.getString("name"));
        m.setUsername(rs.getString("username"));
        m.setPassword(hashedPassword);
        java.sql.Date bd = rs.getDate("birthdate");
        if (bd != null) m.setBirthdate(bd.toLocalDate());
        m.setEmail(rs.getString("email"));
        m.setPhoneNumber(rs.getString("phoneNumber"));
        m.setRole(rs.getString("role"));
        m.setFailedAttempts(rs.getInt("failedAttempts"));
        m.setLocked(rs.getBoolean("locked"));
        return m;
    }

    private void resetFailedAttempts(Connection conn, int memberId) throws SQLException {
        String sql = "UPDATE tblMember SET failedAttempts = 0, locked = 0 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ps.executeUpdate();
        }
    }

    private void updateFailedAttempts(Connection conn, int memberId, int attempts, boolean locked) throws SQLException {
        String sql = "UPDATE tblMember SET failedAttempts = ?, locked = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, attempts);
            ps.setBoolean(2, locked);
            ps.setInt(3, memberId);
            ps.executeUpdate();
        }
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

    /**
     * Lấy position của employee từ tblEmployee dựa trên memberId
     * @param memberId ID của member
     * @return Position của employee hoặc null nếu không phải employee
     */
    public String getEmployeePosition(int memberId) {
        String sql = "SELECT position FROM tblEmployee WHERE tblMemberId = ?";
        Connection conn = con;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("position");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get employee position", e);
        }
        return null;
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
