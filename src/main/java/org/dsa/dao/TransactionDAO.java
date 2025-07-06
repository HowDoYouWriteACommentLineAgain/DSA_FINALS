package org.dsa.dao;

import org.dsa.models.objects.Transaction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class TransactionDAO extends DAOAbstractClass{
    public TransactionDAO(Connection conn)
    {
        super(conn);
    }

    public boolean insert(Transaction transaction) throws SQLException
    {
        String sql = "INSERT INTO transactions (userId, type, refId, amount, note, date) values (?,?,?,?,?,?);";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, transaction.getUserId());
            ps.setString(2, transaction.getType());
            ps.setInt(3, transaction.getRefId());
            ps.setDouble(4, transaction.getAmount());
            ps.setDate(5, Date.valueOf(transaction.getDate()));

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Transaction[] getAll()
    {
        String sql = "SELECT * FROM transactions";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            Transaction[] transactions = new Transaction[rs.getFetchSize()];
            for(Transaction t : transactions)
            {
                t.setId(rs.getInt("id"));
                t.setType(rs.getString("type"));
                t.setAmount(rs.getDouble("amount"));
                t.setNote(rs.getString("note"));
                t.setDate(rs.getDate("date").toLocalDate());
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction[] getByUser(int id) {
        return new Transaction[0];
    }

    @Override
    public String[] getKeys()
    {
        String sql = "SELECT * FROM transactions";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();

            String[] columns = new String[columnCount];
            for(int i = 0; i < columnCount; i++ )
            {
                columns[i] = md.getColumnName(i);
            }
            return columns;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
