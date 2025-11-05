package com.mycompany.esms.dao;

import com.mycompany.esms.model.SupplierStatistic;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SupplierStatisticsDAO extends DAO {
    public SupplierStatisticsDAO() { super(); }

    public List<SupplierStatistic> getSupplierStatistics(LocalDate startDate, LocalDate endDate) {
        List<SupplierStatistic> results = new ArrayList<>();
        Connection conn = con;
        String sql = "SELECT s.id, s.name, SUM(d.quantity) AS totalQty, " +
                     "SUM(d.quantity * d.unitPrice) AS totalValue, COUNT(DISTINCT ii.id) AS importCount " +
                     "FROM tblImportInvoice ii " +
                     "JOIN tblSupplier s ON s.id = ii.tblSupplierId " +
                     "JOIN tblImportDetails d ON d.tblImportInvoiceId = ii.id " +
                     "WHERE ii.importDate BETWEEN ? AND ? " +
                     "GROUP BY s.id, s.name " +
                     "ORDER BY totalValue DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int totalQty = rs.getInt("totalQty");
                    float totalValue = rs.getFloat("totalValue");
                    int importCount = rs.getInt("importCount");
                    results.add(new SupplierStatistic(id, name, totalQty, totalValue, importCount));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get supplier statistics", e);
        }
        return results;
    }
}

