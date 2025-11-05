package com.mycompany.esms.util;

import com.mycompany.esms.dao.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

/**
 * Utility to seed demo data for tblSupplier (10 rows) and tblItem (100 rows).
 */
public class DataSeeder {

    private static final Random RANDOM = new Random();

    public static void seedSuppliers(int numSuppliers) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            new DAO();
            connection = DAO.con;
            connection.setAutoCommit(false);

            String insertSql = "INSERT INTO tblSupplier (name, address, phoneNumber, email, description) VALUES (?,?,?,?,?)";
            ps = connection.prepareStatement(insertSql);

            for (int i = 1; i <= numSuppliers; i++) {
                String name = "Nhà cung cấp " + i;
                String address = "Địa chỉ nhà cung cấp " + i;
                String phone = "1800" + String.format("%04d", RANDOM.nextInt(10000));
                String email = "nhacungcap" + i + "@gmail.com";
                String description = "Mô tả nhà cung cấp " + i;

                ps.setString(1, name);
                ps.setString(2, address);
                ps.setString(3, phone);
                ps.setString(4, email);
                ps.setString(5, description);
                ps.addBatch();
            }

            ps.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (ps != null) ps.close();
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    public static void seedItems(int numItems) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            new DAO();
            connection = DAO.con;
            connection.setAutoCommit(false);

            String insertSql = "INSERT INTO tblItem (name, description, salePrice, stockQuantity, warranty, active) VALUES (?,?,?,?,?,?)";
            ps = connection.prepareStatement(insertSql);

            int[] warrantyYears = new int[] { 12, 18, 24, 36 }; // months: 1, 1.5, 2, 3 years

            for (int i = 1; i <= numItems; i++) {
                String name = "Tên mặt hàng " + i;
                String description = "Mô tả mặt hàng " + i;

                int priceFactor = 500 + RANDOM.nextInt(3000 - 500 + 1); // 500..3000
                float salePrice = priceFactor * 10000.0f; // 5,000,000 .. 30,000,000

                int stockQuantity = RANDOM.nextInt(21); // 0..20
                int warranty = warrantyYears[RANDOM.nextInt(warrantyYears.length)];
                int active = 1;

                ps.setString(1, name);
                ps.setString(2, description);
                ps.setFloat(3, salePrice);
                ps.setInt(4, stockQuantity);
                ps.setInt(5, warranty);
                ps.setInt(6, active);
                ps.addBatch();
            }

            ps.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (ps != null) ps.close();
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Bat dau sinh du lieu mau cho Supplier va Item...");
        try {
            seedSuppliers(10);
            seedItems(100);
            ImportInvoiceSeeder.seedImportInvoices(2015, 2025, 0.10f);
            System.out.println("Hoan thanh sinh du lieu!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Loi khi sinh du lieu: " + e.getMessage());
        }
    }

    // ImportInvoice logic has been moved to ImportInvoiceSeeder
}
