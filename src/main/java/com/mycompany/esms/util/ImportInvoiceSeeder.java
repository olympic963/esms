package com.mycompany.esms.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Seeder for ImportInvoice (tblImportInvoice) and ImportDetails (tblImportDetails).
 */
public class ImportInvoiceSeeder {

    private static final Random RANDOM = new Random();

    /**
     * Seed import invoices with constraints:
     * - Years in [startYear, endYear] inclusive
     * - For each supplier, each month: at most 4 invoices
     * - Each invoice: up to 10 distinct random items, quantity 1..10
     * - Tax is a percentage (e.g., 0.10f => 10%) stored in tblImportInvoice.tax
     * Note: tblImportInvoice no longer stores totalPrice (derived).
     */
    public static void seedImportInvoices(int startYear, int endYear, float taxRate) throws SQLException {
        Connection connection = null;
        PreparedStatement psInvoice = null;
        PreparedStatement psDetail = null;
        try {
            new com.mycompany.esms.dao.DAO();
            connection = com.mycompany.esms.dao.DAO.con;
            connection.setAutoCommit(false);

            // Load suppliers
            List<Integer> supplierIds = new ArrayList<>();
            try (var st = connection.createStatement(); var rs = st.executeQuery("SELECT id FROM tblSupplier")) {
                while (rs.next()) supplierIds.add(rs.getInt(1));
            }

            if (supplierIds.isEmpty()) {
                System.out.println("Khong co supplier de sinh hoa don nhap.");
                return;
            }

            // Load warehouse employees (use member id key from tblWarehouseEmployee)
            List<Integer> warehouseIds = new ArrayList<>();
            try (var st = connection.createStatement(); var rs = st.executeQuery("SELECT tblEmployeeId FROM tblWarehouseEmployee")) {
                while (rs.next()) warehouseIds.add(rs.getInt(1));
            }
            if (warehouseIds.isEmpty()) {
                System.out.println("Khong co nhan vien kho de gan cho hoa don nhap.");
                return;
            }

            // Load items with price
            List<Integer> itemIds = new ArrayList<>();
            HashMap<Integer, Float> itemIdToPrice = new HashMap<>();
            try (var st = connection.createStatement(); var rs = st.executeQuery("SELECT id, salePrice FROM tblItem WHERE active = 1")) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    float price = rs.getFloat(2);
                    itemIds.add(id);
                    itemIdToPrice.put(id, price);
                }
            }
            if (itemIds.isEmpty()) {
                System.out.println("Khong co item de sinh chi tiet nhap.");
                return;
            }

            String insertInvoiceSql = "INSERT INTO tblImportInvoice (importDate, tax, tblSupplierId, tblWarehouseEmployeeId) VALUES (?,?,?,?)";
            String insertDetailSql = "INSERT INTO tblImportDetails (quantity, unitPrice, tblImportInvoiceId, tblItemId) VALUES (?,?,?,?)";
            psInvoice = connection.prepareStatement(insertInvoiceSql, PreparedStatement.RETURN_GENERATED_KEYS);
            psDetail = connection.prepareStatement(insertDetailSql);

            // Ensure chronological insertion order (older dates get lower IDs)
            for (int year = startYear; year <= endYear; year++) {
                for (int month = 1; month <= 12; month++) {
                    for (int supplierId : supplierIds) {
                        int invoicesThisMonth = RANDOM.nextInt(5); // 0..4
                        for (int k = 0; k < invoicesThisMonth; k++) {
                            java.time.LocalDate date = randomDateInMonth(year, month);
                            int warehouseEmpId = warehouseIds.get(RANDOM.nextInt(warehouseIds.size()));

                            // Insert invoice
                            psInvoice.clearParameters();
                            psInvoice.setDate(1, java.sql.Date.valueOf(date));
                            psInvoice.setFloat(2, taxRate);
                            psInvoice.setInt(3, supplierId);
                            psInvoice.setInt(4, warehouseEmpId);
                            psInvoice.executeUpdate();
                            int invoiceId;
                            try (var rsKeys = psInvoice.getGeneratedKeys()) {
                                rsKeys.next();
                                invoiceId = rsKeys.getInt(1);
                            }

                            // Choose up to 10 distinct items
                            int numItems = 1 + RANDOM.nextInt(10); // 1..10
                            Collections.shuffle(itemIds, RANDOM);
                            List<Integer> chosen = itemIds.subList(0, Math.min(numItems, itemIds.size()));

                            for (int itemId : chosen) {
                                int qty = 1 + RANDOM.nextInt(10); // 1..10
                                float salePrice = itemIdToPrice.getOrDefault(itemId, 0.0f);
                                float factor = 0.8f + RANDOM.nextFloat() * (1.5f - 0.8f);
                                float adjusted = salePrice * factor;
                                float unitPrice = roundTo10k(adjusted);

                                psDetail.clearParameters();
                                psDetail.setInt(1, qty);
                                psDetail.setFloat(2, unitPrice);
                                psDetail.setInt(3, invoiceId);
                                psDetail.setInt(4, itemId);
                                psDetail.addBatch();
                            }
                            psDetail.executeBatch();
                        }
                    }
                }
            }

            connection.commit();
            System.out.println("Da sinh du lieu hoa don nhap (ImportInvoice) va chi tiet (ImportDetails).");
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            throw e;
        } finally {
            if (psDetail != null) psDetail.close();
            if (psInvoice != null) psInvoice.close();
            if (connection != null) {
                try { connection.setAutoCommit(true); } catch (SQLException ignore) {}
            }
        }
    }

    private static java.time.LocalDate randomDateInMonth(int year, int month) {
        java.time.YearMonth ym = java.time.YearMonth.of(year, month);
        int day = 1 + RANDOM.nextInt(ym.lengthOfMonth());
        return java.time.LocalDate.of(year, month, day);
    }

    private static float roundTo10k(float value) {
        if (value <= 0) return 10000f;
        return Math.round(value / 10000f) * 10000f;
    }

    public static void main(String[] args) {
        System.out.println("Bat dau sinh du lieu hoa don nhap...");
        try {
            seedImportInvoices(2015, 2025, 0.10f);
            System.out.println("Hoan thanh sinh du lieu hoa don nhap!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Loi khi sinh du lieu hoa don nhap: " + e.getMessage());
        }
    }
}


