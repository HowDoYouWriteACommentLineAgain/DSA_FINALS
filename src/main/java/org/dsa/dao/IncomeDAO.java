package org.dsa.dao;

import org.dsa.abstractions.GenericDAO;
import org.dsa.models.objects.Income;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class IncomeDAO extends GenericDAO<Income> {

    public IncomeDAO(Connection conn)
    {
        super(conn);
    }

    @Override
    public ArrayList<Income> getAll() throws SQLException {
        String sql = "SELECT id, name, income_cat, amount, note, date FROM incomes ORDER BY date ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            ArrayList<Income> incomes = new ArrayList<>();

            while(rs.next())
                incomes.add(
                    new Income(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("income_cat"),
                            rs.getDouble("amount"),
                            rs.getString("note"),
                            rs.getDate("date")
                            )
                    );
            return incomes;

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Income getOneById(int id) throws SQLException {
        String sql = "SELECT id, name, income_cat, amount, note, date FROM incomes WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                return new Income(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("income_cat"),
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
    public boolean update(int id, Income obj) throws SQLException {
        String sql = "UPDATE incomes SET income_cat = ?, name = ? ,amount = ?, note = ?, date = ? WHERE id = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, obj.income_cat());
            ps.setString(2, obj.name());
            ps.setDouble(3, obj.amount());
            ps.setString(4, obj.note());
            ps.setDate(5, obj.date());
            ps.setInt(6, id);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new SQLException("Error Updating Incomes" + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM incomes WHERE id = ?";
        System.out.println("DELETING ID:" + id);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Deleting Transaction" + e.getMessage(), e);
        }
    }


    @Override
    public void insert(Income obj) throws SQLException {
        String sql = "INSERT INTO incomes (income_cat, name, amount, note, date) values (?,?,?,?,?);";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, obj.income_cat());
            ps.setString(2, obj.name());
            ps.setDouble(3, obj.amount());
            ps.setString(4, obj.note());
            ps.setDate(5, obj.date());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error inserting in incomes" + e.getMessage(), e);
        }
    }

    @Override
    public String getTableName() {
        return "Incomes";
    }



}
