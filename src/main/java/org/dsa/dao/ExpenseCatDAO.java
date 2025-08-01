package org.dsa.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseCatDAO {
    private final Connection conn;

    public ExpenseCatDAO(Connection conn) {
        this.conn = conn;
    }

    public List<CategoryItem> getAllCategories() {
        List<CategoryItem> list = new ArrayList<>();
        String sql = "SELECT id, name FROM expense_cat ORDER BY id ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new CategoryItem(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addCategory(String name) {
        String sql = "INSERT INTO expense_cat (name) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory(int id) {
        String sql = "DELETE FROM expense_cat WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public record CategoryItem(int id, String name) {}
}
