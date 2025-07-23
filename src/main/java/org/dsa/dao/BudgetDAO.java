package org.dsa.dao;

import org.dsa.abstractions.GenericDAO;
import org.dsa.models.objects.Budget;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BudgetDAO extends GenericDAO<Budget> {
    public BudgetDAO(Connection conn) {
        super(conn);
    }

    @Override
    public ArrayList<Budget> getAll() throws SQLException {
        String sql = "SELECT id, expense_cat, max_amount, goal_amount, date_start, date_end FROM budgets ORDER BY date_end ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            ArrayList<Budget> budget = new ArrayList<>();

            while(rs.next())
                budget.add(
                        new Budget(
                                rs.getInt("id"),
                                rs.getInt("expense_cat"),
                                rs.getDouble("max_amount"),
                                rs.getDouble("goal_amount"),
                                rs.getDate("date_start"),
                                rs.getDate("date_end")
                        )
                );
            return budget;

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Budget getOneById(int id) throws SQLException {
        String sql = "SELECT id, name, expense_cat, amount, note, date FROM expenses WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                return new Budget(
                        rs.getInt("id"),
                        rs.getInt("expense_cat"),
                        rs.getDouble("max_amount"),
                        rs.getDouble("goal_amount"),
                        rs.getDate("date_start"),
                        rs.getDate("date_end")
                );
            }else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean update(int id, Budget obj) throws SQLException {
        String sql = "UPDATE expenses SET expense_cat = ?, max_amount = ? ,goal_amount = ?, date_start = ?, date_end = ? WHERE id = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, obj.expense_cat());
            ps.setDouble(2, obj.max_amount());
            ps.setDouble(3, obj.goal_amount());
            ps.setDate(4, obj.start_date());
            ps.setDate(5, obj.end_date());
            ps.setInt(6, id);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new SQLException("Error Updating Expense: " + e.getMessage(), e);
        }
    }

    @Override
    public void insert(Budget obj) throws SQLException {
        String sql = "INSERT INTO budgets (expense_cat, max_amount, goal_amount, date_start, date_end) values (?,?,?,?,?);";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, obj.expense_cat());
            ps.setDouble(2, obj.max_amount());
            ps.setDouble(3, obj.goal_amount());
            ps.setDate(4, obj.start_date());
            ps.setDate(5, obj.end_date());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error inserting in expenses" + e.getMessage(), e);
        }
    }

    @Override
    public String getTableName() {
        return "Budgets";
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM budgets WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Deleting Budget" + e.getMessage(), e);
        }
    }
}
