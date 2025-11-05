package com.mycompany.esms.dao;

import com.mycompany.esms.model.ImportInvoice;
import com.mycompany.esms.model.ImportDetails;
import com.mycompany.esms.model.Supplier;
import com.mycompany.esms.model.WarehouseEmployee;
import com.mycompany.esms.model.Item;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ImportInvoiceDAO extends DAO {
    public ImportInvoiceDAO() { super(); }

    public List<ImportInvoice> getInvoiceBySupplier(int supplierId, LocalDate startDate, LocalDate endDate) {
        List<ImportInvoice> list = new ArrayList<>();
        Connection conn = con;
        String sql = "SELECT ii.id, ii.importDate, ii.tax, " +
                     "SUM(d.quantity) AS totalQty, " +
                     "SUM(d.quantity * d.unitPrice) AS totalValue, " +
                     "s.id AS supplierId, s.name AS supplierName, s.address AS supplierAddress, " +
                     "s.phoneNumber AS supplierPhone, s.email AS supplierEmail, s.description AS supplierDescription, " +
                     "m.id AS memberId, m.name AS empName, m.username AS empUsername, m.birthdate AS empBirthdate, " +
                     "m.email AS empEmail, m.phoneNumber AS empPhone, m.role AS empRole, e.position AS empPosition, e.active AS empActive " +
                     "FROM tblImportInvoice ii " +
                     "JOIN tblImportDetails d ON d.tblImportInvoiceId = ii.id " +
                     "JOIN tblSupplier s ON s.id = ii.tblSupplierId " +
                     "LEFT JOIN tblWarehouseEmployee we ON we.tblEmployeeId = ii.tblWarehouseEmployeeId " +
                     "LEFT JOIN tblEmployee e ON e.tblMemberId = we.tblEmployeeId " +
                     "LEFT JOIN tblMember m ON m.id = e.tblMemberId " +
                     "WHERE ii.tblSupplierId = ? AND ii.importDate BETWEEN ? AND ? " +
                     "GROUP BY ii.id, ii.importDate, ii.tax, " +
                     "s.id, s.name, s.address, s.phoneNumber, s.email, s.description, " +
                     "m.id, m.name, m.username, m.birthdate, m.email, m.phoneNumber, m.role, e.position, e.active " +
                     "ORDER BY ii.importDate ASC, ii.id ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImportInvoice inv = new ImportInvoice();
                    inv.setId(rs.getInt("id"));
                    inv.setImportDate(rs.getDate("importDate").toLocalDate());
                    inv.setTotalQuantity(rs.getInt("totalQty"));
                    inv.setTotalValue(rs.getFloat("totalValue"));
                    inv.setTax(rs.getFloat("tax"));
                    
                    // Đóng gói thông tin Supplier
                    Supplier supplier = new Supplier();
                    supplier.setId(rs.getInt("supplierId"));
                    supplier.setName(rs.getString("supplierName"));
                    supplier.setAddress(rs.getString("supplierAddress"));
                    supplier.setPhoneNumber(rs.getString("supplierPhone"));
                    supplier.setEmail(rs.getString("supplierEmail"));
                    supplier.setDescription(rs.getString("supplierDescription"));
                    inv.setSupplier(supplier);
                    
                    // Đóng gói thông tin WarehouseEmployee (nếu có)
                    if (rs.getObject("memberId") != null) {
                        WarehouseEmployee warehouseEmp = new WarehouseEmployee();
                        warehouseEmp.setId(rs.getInt("memberId"));
                        warehouseEmp.setName(rs.getString("empName"));
                        warehouseEmp.setUsername(rs.getString("empUsername"));
                        if (rs.getDate("empBirthdate") != null) {
                            warehouseEmp.setBirthdate(rs.getDate("empBirthdate").toLocalDate());
                        }
                        warehouseEmp.setEmail(rs.getString("empEmail"));
                        warehouseEmp.setPhoneNumber(rs.getString("empPhone"));
                        warehouseEmp.setRole(rs.getString("empRole"));
                        warehouseEmp.setPosition(rs.getString("empPosition"));
                        warehouseEmp.setActive(rs.getBoolean("empActive"));
                        inv.setWarehouseEmp(warehouseEmp);
                    }
                    
                    list.add(inv);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get import invoices by supplier", e);
        }
        return list;
    }

    public List<ImportDetails> getImportInvoiceDetails(int importInvoiceId) {
        List<ImportDetails> details = new ArrayList<>();
        Connection conn = con;
        ImportInvoice invoice = null;
        
        // Query 1: Lấy thông tin hóa đơn nhập với Supplier và WarehouseEmployee
        String invoiceSql = "SELECT ii.id, ii.importDate, ii.tax, " +
                           "SUM(d.quantity) AS totalQty, " +
                           "SUM(d.quantity * d.unitPrice) AS totalValue, " +
                           "s.id AS supplierId, s.name AS supplierName, s.address AS supplierAddress, " +
                           "s.phoneNumber AS supplierPhone, s.email AS supplierEmail, s.description AS supplierDescription, " +
                           "m.id AS memberId, m.name AS empName, m.username AS empUsername, m.birthdate AS empBirthdate, " +
                           "m.email AS empEmail, m.phoneNumber AS empPhone, m.role AS empRole, e.position AS empPosition, e.active AS empActive " +
                           "FROM tblImportInvoice ii " +
                           "JOIN tblImportDetails d ON d.tblImportInvoiceId = ii.id " +
                           "JOIN tblSupplier s ON s.id = ii.tblSupplierId " +
                           "LEFT JOIN tblWarehouseEmployee we ON we.tblEmployeeId = ii.tblWarehouseEmployeeId " +
                           "LEFT JOIN tblEmployee e ON e.tblMemberId = we.tblEmployeeId " +
                           "LEFT JOIN tblMember m ON m.id = e.tblMemberId " +
                           "WHERE ii.id = ? " +
                           "GROUP BY ii.id, ii.importDate, ii.tax, " +
                           "s.id, s.name, s.address, s.phoneNumber, s.email, s.description, " +
                           "m.id, m.name, m.username, m.birthdate, m.email, m.phoneNumber, m.role, e.position, e.active";
        
        try (PreparedStatement ps = conn.prepareStatement(invoiceSql)) {
            ps.setInt(1, importInvoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    invoice = new ImportInvoice();
                    invoice.setId(rs.getInt("id"));
                    invoice.setImportDate(rs.getDate("importDate").toLocalDate());
                    invoice.setTotalQuantity(rs.getInt("totalQty"));
                    invoice.setTotalValue(rs.getFloat("totalValue"));
                    invoice.setTax(rs.getFloat("tax"));
                    
                    // Đóng gói thông tin Supplier
                    Supplier supplier = new Supplier();
                    supplier.setId(rs.getInt("supplierId"));
                    supplier.setName(rs.getString("supplierName"));
                    supplier.setAddress(rs.getString("supplierAddress"));
                    supplier.setPhoneNumber(rs.getString("supplierPhone"));
                    supplier.setEmail(rs.getString("supplierEmail"));
                    supplier.setDescription(rs.getString("supplierDescription"));
                    invoice.setSupplier(supplier);
                    
                    // Đóng gói thông tin WarehouseEmployee (nếu có)
                    if (rs.getObject("memberId") != null) {
                        WarehouseEmployee warehouseEmp = new WarehouseEmployee();
                        warehouseEmp.setId(rs.getInt("memberId"));
                        warehouseEmp.setName(rs.getString("empName"));
                        warehouseEmp.setUsername(rs.getString("empUsername"));
                        if (rs.getDate("empBirthdate") != null) {
                            warehouseEmp.setBirthdate(rs.getDate("empBirthdate").toLocalDate());
                        }
                        warehouseEmp.setEmail(rs.getString("empEmail"));
                        warehouseEmp.setPhoneNumber(rs.getString("empPhone"));
                        warehouseEmp.setRole(rs.getString("empRole"));
                        warehouseEmp.setPosition(rs.getString("empPosition"));
                        warehouseEmp.setActive(rs.getBoolean("empActive"));
                        invoice.setWarehouseEmp(warehouseEmp);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get import invoice details", e);
        }
        
        if (invoice == null) {
            return details; // Trả về danh sách rỗng nếu không tìm thấy invoice
        }
        
        // Query 2: Lấy danh sách chi tiết các mặt hàng và gán invoice vào mỗi detail
        String detailsSql = "SELECT d.id, d.quantity, d.unitPrice, " +
                          "i.id AS itemId, i.name AS itemName, i.description AS itemDescription, " +
                          "i.salePrice AS itemSalePrice, i.stockQuantity AS itemStockQty, " +
                          "i.warranty AS itemWarranty, i.active AS itemActive " +
                          "FROM tblImportDetails d " +
                          "JOIN tblItem i ON i.id = d.tblItemId " +
                          "WHERE d.tblImportInvoiceId = ? " +
                          "ORDER BY d.id ASC";
        
        try (PreparedStatement ps = conn.prepareStatement(detailsSql)) {
            ps.setInt(1, importInvoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImportDetails detail = new ImportDetails();
                    detail.setId(rs.getInt("id"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setUnitPrice(rs.getFloat("unitPrice"));
                    detail.setImportInvoice(invoice); // Gán invoice đầy đủ vào mỗi detail
                    
                    // Đóng gói thông tin Item
                    Item item = new Item();
                    item.setId(rs.getInt("itemId"));
                    item.setName(rs.getString("itemName"));
                    item.setDescription(rs.getString("itemDescription"));
                    item.setSalePrice(rs.getFloat("itemSalePrice"));
                    item.setStockQuantity(rs.getInt("itemStockQty"));
                    item.setWarranty(rs.getInt("itemWarranty"));
                    item.setActive(rs.getBoolean("itemActive"));
                    detail.setImportItem(item);
                    
                    details.add(detail);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get import details", e);
        }
        
        return details;
    }
}
