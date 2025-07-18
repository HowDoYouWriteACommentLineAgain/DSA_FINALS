package org.dsa.dao;

import org.dsa.abstractions.GenericDAO;
import org.dsa.models.objects.Expense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExpenseDAO extends GenericDAO<Expense> {
    public ExpenseDAO(Connection conn) {
        super(conn);
    }

    @Override
    public ArrayList<Expense> getAll() throws SQLException {
        String sql = "SELECT id, name, expense_cat, amount, note, date FROM expenses ORDER BY date ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            ArrayList<Expense> expenses = new ArrayList<>();

            while(rs.next())
                expenses.add(
                        new Expense(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getInt("expense_cat"),
                                rs.getDouble("amount"),
                                rs.getString("note"),
                                rs.getDate("date")
                        )
                );
            return expenses;

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Expense getOneById(int id) throws SQLException {
        String sql = "SELECT id, name, expense_cat, amount, note, date FROM expenses WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                return new Expense(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("expense_cat"),
                        rs.getDouble("amount"),
                        rs.getString("note"),
                        rs.getDate("date")
                );
            }else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }

    }

    @Override
    public boolean update(int id, Expense obj) throws SQLException {
        String sql = "UPDATE expenses SET expense_cat = ?, name = ? ,amount = ?, note = ?, date = ? WHERE id = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, obj.expense_cat());
            ps.setString(2, obj.name());
            ps.setDouble(3, obj.amount());
            ps.setString(4, obj.note());
            ps.setDate(5, obj.date());
            ps.setInt(6, id);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new SQLException("Error Updating Expense: " + e.getMessage(), e);
        }
    }

    @Override
    public void insert(Expense obj) throws SQLException {
        String sql = "INSERT INTO expenses (expense_cat, name, amount, note, date) values (?,?,?,?,?);";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, obj.expense_cat());
            ps.setString(2, obj.name());
            ps.setDouble(3, obj.amount());
            ps.setString(4, obj.note());
            ps.setDate(5, obj.date());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error inserting in expenses" + e.getMessage(), e);
        }
    }

    @Override
    public String getTableName() {
        return "Expenses";
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM expenses WHERE id = ?";
        System.out.println("DELETING ID:" + id);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Deleting Transaction" + e.getMessage(), e);
        }
    }
}
