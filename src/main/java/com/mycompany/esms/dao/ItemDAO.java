package com.mycompany.esms.dao;

import com.mycompany.esms.model.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO extends DAO {
    public ItemDAO() { super(); }

    public List<Item> getKeywordItems(String keyword) {
        String likePattern = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        String sql = "SELECT id, name, description, salePrice, stockQuantity, warranty, active "
                   + "FROM tblItem "
                   + "WHERE active = 1 AND LOWER(name) LIKE LOWER(?)";

        List<Item> items = new ArrayList<>();
        Connection conn = con;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, likePattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getInt("id"));
                    item.setName(rs.getString("name"));
                    item.setDescription(rs.getString("description"));
                    item.setSalePrice(rs.getFloat("salePrice"));
                    item.setStockQuantity(rs.getInt("stockQuantity"));
                    item.setWarranty(rs.getInt("warranty"));
                    item.setActive(rs.getBoolean("active"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch items by keyword", e);
        }
        return items;
    }

    public Item getItemInfo(int itemId) {
        String sql = "SELECT id, name, description, salePrice, stockQuantity, warranty, active "
                   + "FROM tblItem WHERE id = ?";

        Connection conn = con;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getInt("id"));
                    item.setName(rs.getString("name"));
                    item.setDescription(rs.getString("description"));
                    item.setSalePrice(rs.getFloat("salePrice"));
                    item.setStockQuantity(rs.getInt("stockQuantity"));
                    item.setWarranty(rs.getInt("warranty"));
                    item.setActive(rs.getBoolean("active"));
                    return item;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch item info", e);
        }
        return null;
    }
}
