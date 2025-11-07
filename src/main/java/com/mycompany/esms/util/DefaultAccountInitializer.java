package com.mycompany.esms.util;

import com.mycompany.esms.dao.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Lớp để chèn các tài khoản mặc định vào database
 */
public class DefaultAccountInitializer {
    
    private static final String DEFAULT_PASSWORD = "123456"; // Mật khẩu mặc định chung cho tất cả tài khoản
    
    /**
     * Khởi tạo các tài khoản mặc định
     */
    public static void initializeDefaultAccounts() {
        try {
            // Chèn tài khoản khách hàng
            insertCustomer("Nguyễn Văn An", "customer1", "nguyenvanan@example.com", 
                    "0123456789", LocalDate.of(1990, 1, 15));
            insertCustomer("Trần Thị Bình", "customer2", "tranthibinh@example.com", 
                    "0123456788", LocalDate.of(1992, 5, 20));
            insertCustomer("Lê Văn Cường", "customer3", "levancuong@example.com", 
                    "0123456787", LocalDate.of(1988, 8, 10));
            
            // Chèn tài khoản nhân viên bán hàng
            insertSalesEmployee("Phạm Văn Đức", "sales1", "phamvanduc@example.com", 
                    "0987654321", LocalDate.of(1995, 3, 25), "sales");
            insertSalesEmployee("Hoàng Thị Em", "sales2", "hoangthiem@example.com", 
                    "0987654322", LocalDate.of(1993, 7, 12), "sales");
            
            // Chèn tài khoản nhân viên kho
            insertWarehouseEmployee("Vũ Văn Phong", "warehouse1", "vuvanphong@example.com", 
                    "0912345678", LocalDate.of(1991, 2, 18), "warehouse");
            insertWarehouseEmployee("Đặng Thị Giang", "warehouse2", "dangthigiang@example.com", 
                    "0912345679", LocalDate.of(1994, 9, 5), "warehouse");
            insertWarehouseEmployee("Nguyễn Hữu Khang", "warehouse3", "nguyenhuukhang@example.com", 
                    "0912345680", LocalDate.of(1992, 1, 9), "warehouse");
            insertWarehouseEmployee("Trần Quốc Lợi", "warehouse4", "tranquocloi@example.com", 
                    "0912345681", LocalDate.of(1990, 12, 2), "warehouse");
            insertWarehouseEmployee("Phạm Thị Mai", "warehouse5", "phamthimai@example.com", 
                    "0912345682", LocalDate.of(1993, 6, 14), "warehouse");
            
            // Chèn tài khoản nhân viên giao hàng
            insertDeliveryEmployee("Bùi Văn Hùng", "delivery1", "buivanhung@example.com", 
                    "0934567890", LocalDate.of(1989, 11, 30), "delivery");
            insertDeliveryEmployee("Ngô Thị Lan", "delivery2", "ngothilan@example.com", 
                    "0934567891", LocalDate.of(1996, 4, 22), "delivery");
            
            // Chèn tài khoản quản lý
            insertManager("Trần Văn Quản", "manager1", "tranvanquan@example.com", 
                    "0901234567", LocalDate.of(1985, 6, 15), "manager");
            insertManager("Lê Thị Lý", "manager2", "lethily@example.com", 
                    "0901234568", LocalDate.of(1987, 9, 28), "manager");
            
            System.out.println("Da khoi tao thanh cong cac tai khoan mac dinh!");
            
        } catch (SQLException e) {
            System.err.println("Loi khi khoi tao tai khoan mac dinh: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Chèn tài khoản khách hàng
     */
    private static void insertCustomer(String name, String username, String email, 
            String phoneNumber, LocalDate birthdate) throws SQLException {
        Connection conn = null;
        try {
            new DAO();
            conn = DAO.con;
            conn.setAutoCommit(false);
            
            // Kiểm tra username đã tồn tại chưa
            if (isUsernameExists(conn, username)) {
                System.out.println("Tai khoan " + username + " da ton tai, bo qua.");
                return;
            }
            
            // Insert vào tblMember
            String memberSql = "INSERT INTO tblMember (name, username, password, birthdate, email, phoneNumber, role) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement memberPs = conn.prepareStatement(memberSql, PreparedStatement.RETURN_GENERATED_KEYS);
            memberPs.setString(1, name);
            memberPs.setString(2, username);
            memberPs.setString(3, PasswordUtils.hashPassword(DEFAULT_PASSWORD));
            memberPs.setDate(4, java.sql.Date.valueOf(birthdate));
            memberPs.setString(5, email);
            memberPs.setString(6, phoneNumber);
            memberPs.setString(7, "customer");
            memberPs.executeUpdate();
            
            // Lấy ID vừa insert
            ResultSet rs = memberPs.getGeneratedKeys();
            int memberId = 0;
            if (rs.next()) {
                memberId = rs.getInt(1);
            }
            rs.close();
            memberPs.close();
            
            // Insert vào tblCustomer
            String customerSql = "INSERT INTO tblCustomer (tblMemberId) VALUES (?)";
            PreparedStatement customerPs = conn.prepareStatement(customerSql);
            customerPs.setInt(1, memberId);
            customerPs.executeUpdate();
            customerPs.close();
            
            conn.commit();
            System.out.println("Da tao tai khoan khach hang: " + username);
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }
    
    /**
     * Chèn tài khoản nhân viên bán hàng
     */
    private static void insertSalesEmployee(String name, String username, String email, 
            String phoneNumber, LocalDate birthdate, String position) throws SQLException {
        Connection conn = null;
        try {
            new DAO();
            conn = DAO.con;
            conn.setAutoCommit(false);
            
            // Kiểm tra username đã tồn tại chưa
            if (isUsernameExists(conn, username)) {
                System.out.println("Tai khoan " + username + " da ton tai, bo qua.");
                return;
            }
            
            // Insert vào tblMember
            String memberSql = "INSERT INTO tblMember (name, username, password, birthdate, email, phoneNumber, role) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement memberPs = conn.prepareStatement(memberSql, PreparedStatement.RETURN_GENERATED_KEYS);
            memberPs.setString(1, name);
            memberPs.setString(2, username);
            memberPs.setString(3, PasswordUtils.hashPassword(DEFAULT_PASSWORD));
            memberPs.setDate(4, java.sql.Date.valueOf(birthdate));
            memberPs.setString(5, email);
            memberPs.setString(6, phoneNumber);
            memberPs.setString(7, "employee");
            memberPs.executeUpdate();
            
            // Lấy ID vừa insert
            ResultSet rs = memberPs.getGeneratedKeys();
            int memberId = 0;
            if (rs.next()) {
                memberId = rs.getInt(1);
            }
            rs.close();
            memberPs.close();
            
            // Insert vào tblEmployee
            String employeeSql = "INSERT INTO tblEmployee (tblMemberId, position, active) VALUES (?, ?, ?)";
            PreparedStatement employeePs = conn.prepareStatement(employeeSql);
            employeePs.setInt(1, memberId);
            employeePs.setString(2, position);
            employeePs.setInt(3, 1); // 1 = active (true), 0 = inactive (false)
            employeePs.executeUpdate();
            employeePs.close();
            
            // Insert vào tblSalesEmployee
            String salesSql = "INSERT INTO tblSalesEmployee (tblEmployeeId) VALUES (?)";
            PreparedStatement salesPs = conn.prepareStatement(salesSql);
            salesPs.setInt(1, memberId);
            salesPs.executeUpdate();
            salesPs.close();
            
            conn.commit();
            System.out.println("Da tao tai khoan nhan vien ban hang: " + username);
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }
    
    /**
     * Chèn tài khoản nhân viên kho
     */
    private static void insertWarehouseEmployee(String name, String username, String email, 
            String phoneNumber, LocalDate birthdate, String position) throws SQLException {
        Connection conn = null;
        try {
            new DAO();
            conn = DAO.con;
            conn.setAutoCommit(false);
            
            // Kiểm tra username đã tồn tại chưa
            if (isUsernameExists(conn, username)) {
                System.out.println("Tai khoan " + username + " da ton tai, bo qua.");
                return;
            }
            
            // Insert vào tblMember
            String memberSql = "INSERT INTO tblMember (name, username, password, birthdate, email, phoneNumber, role) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement memberPs = conn.prepareStatement(memberSql, PreparedStatement.RETURN_GENERATED_KEYS);
            memberPs.setString(1, name);
            memberPs.setString(2, username);
            memberPs.setString(3, PasswordUtils.hashPassword(DEFAULT_PASSWORD));
            memberPs.setDate(4, java.sql.Date.valueOf(birthdate));
            memberPs.setString(5, email);
            memberPs.setString(6, phoneNumber);
            memberPs.setString(7, "employee");
            memberPs.executeUpdate();
            
            // Lấy ID vừa insert
            ResultSet rs = memberPs.getGeneratedKeys();
            int memberId = 0;
            if (rs.next()) {
                memberId = rs.getInt(1);
            }
            rs.close();
            memberPs.close();
            
            // Insert vào tblEmployee
            String employeeSql = "INSERT INTO tblEmployee (tblMemberId, position, active) VALUES (?, ?, ?)";
            PreparedStatement employeePs = conn.prepareStatement(employeeSql);
            employeePs.setInt(1, memberId);
            employeePs.setString(2, position);
            employeePs.setInt(3, 1); // 1 = active (true), 0 = inactive (false)
            employeePs.executeUpdate();
            employeePs.close();
            
            // Insert vào tblWarehouseEmployee
            String warehouseSql = "INSERT INTO tblWarehouseEmployee (tblEmployeeId) VALUES (?)";
            PreparedStatement warehousePs = conn.prepareStatement(warehouseSql);
            warehousePs.setInt(1, memberId);
            warehousePs.executeUpdate();
            warehousePs.close();
            
            conn.commit();
            System.out.println("Da tao tai khoan nhan vien kho: " + username);
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }
    
    /**
     * Chèn tài khoản nhân viên giao hàng
     */
    private static void insertDeliveryEmployee(String name, String username, String email, 
            String phoneNumber, LocalDate birthdate, String position) throws SQLException {
        Connection conn = null;
        try {
            new DAO();
            conn = DAO.con;
            conn.setAutoCommit(false);
            
            // Kiểm tra username đã tồn tại chưa
            if (isUsernameExists(conn, username)) {
                System.out.println("Tai khoan " + username + " da ton tai, bo qua.");
                return;
            }
            
            // Insert vào tblMember
            String memberSql = "INSERT INTO tblMember (name, username, password, birthdate, email, phoneNumber, role) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement memberPs = conn.prepareStatement(memberSql, PreparedStatement.RETURN_GENERATED_KEYS);
            memberPs.setString(1, name);
            memberPs.setString(2, username);
            memberPs.setString(3, PasswordUtils.hashPassword(DEFAULT_PASSWORD));
            memberPs.setDate(4, java.sql.Date.valueOf(birthdate));
            memberPs.setString(5, email);
            memberPs.setString(6, phoneNumber);
            memberPs.setString(7, "employee");
            memberPs.executeUpdate();
            
            // Lấy ID vừa insert
            ResultSet rs = memberPs.getGeneratedKeys();
            int memberId = 0;
            if (rs.next()) {
                memberId = rs.getInt(1);
            }
            rs.close();
            memberPs.close();
            
            // Insert vào tblEmployee
            String employeeSql = "INSERT INTO tblEmployee (tblMemberId, position, active) VALUES (?, ?, ?)";
            PreparedStatement employeePs = conn.prepareStatement(employeeSql);
            employeePs.setInt(1, memberId);
            employeePs.setString(2, position);
            employeePs.setInt(3, 1); // 1 = active (true), 0 = inactive (false)
            employeePs.executeUpdate();
            employeePs.close();
            
            // Insert vào tblDeliveryEmployee
            String deliverySql = "INSERT INTO tblDeliveryEmployee (tblEmployeeId) VALUES (?)";
            PreparedStatement deliveryPs = conn.prepareStatement(deliverySql);
            deliveryPs.setInt(1, memberId);
            deliveryPs.executeUpdate();
            deliveryPs.close();
            
            conn.commit();
            System.out.println("Da tao tai khoan nhan vien giao hang: " + username);
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }
    
    /**
     * Chèn tài khoản quản lý
     */
    private static void insertManager(String name, String username, String email, 
            String phoneNumber, LocalDate birthdate, String position) throws SQLException {
        Connection conn = null;
        try {
            new DAO();
            conn = DAO.con;
            conn.setAutoCommit(false);
            
            // Kiểm tra username đã tồn tại chưa
            if (isUsernameExists(conn, username)) {
                System.out.println("Tai khoan " + username + " da ton tai, bo qua.");
                return;
            }
            
            // Insert vào tblMember
            String memberSql = "INSERT INTO tblMember (name, username, password, birthdate, email, phoneNumber, role) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement memberPs = conn.prepareStatement(memberSql, PreparedStatement.RETURN_GENERATED_KEYS);
            memberPs.setString(1, name);
            memberPs.setString(2, username);
            memberPs.setString(3, PasswordUtils.hashPassword(DEFAULT_PASSWORD));
            memberPs.setDate(4, java.sql.Date.valueOf(birthdate));
            memberPs.setString(5, email);
            memberPs.setString(6, phoneNumber);
            memberPs.setString(7, "employee");
            memberPs.executeUpdate();
            
            // Lấy ID vừa insert
            ResultSet rs = memberPs.getGeneratedKeys();
            int memberId = 0;
            if (rs.next()) {
                memberId = rs.getInt(1);
            }
            rs.close();
            memberPs.close();
            
            // Insert vào tblEmployee
            String employeeSql = "INSERT INTO tblEmployee (tblMemberId, position, active) VALUES (?, ?, ?)";
            PreparedStatement employeePs = conn.prepareStatement(employeeSql);
            employeePs.setInt(1, memberId);
            employeePs.setString(2, position);
            employeePs.setInt(3, 1); // 1 = active (true), 0 = inactive (false)
            employeePs.executeUpdate();
            employeePs.close();
            
            // Manager không cần insert vào các bảng con (SalesEmployee, WarehouseEmployee, DeliveryEmployee)
            // vì manager có thể quản lý tất cả các phòng ban
            
            conn.commit();
            System.out.println("Da tao tai khoan quan ly: " + username);
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    if (!conn.isClosed()) {
                        conn.setAutoCommit(true);
                    }
                } catch (SQLException ignore) {}
            }
        }
    }
    
    /**
     * Kiểm tra username đã tồn tại chưa
     */
    private static boolean isUsernameExists(Connection conn, String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tblMember WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    /**
     * Main method để chạy thủ công
     */
    public static void main(String[] args) {
        System.out.println("Bat dau khoi tao tai khoan mac dinh...");
        initializeDefaultAccounts();
        System.out.println("Hoan thanh!");
    }
}

