package org.dsa.dao;

import org.dsa.models.objects.Transaction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionDAO extends DAOAbstractClass{
    public TransactionDAO(Connection conn)
    {
        super(conn);
    }

    public boolean insert(Transaction transaction) throws SQLException
    {
        String sql = "INSERT INTO transactions (userId, t_type, refId, amount, note, t_date, name) values (?,?,?,?,?,?,?);";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, transaction.getUserId());
            ps.setString(2, transaction.getT_type());
            ps.setInt(3, transaction.getRefId());
            ps.setDouble(4, transaction.getAmount());
            ps.setString(5, transaction.getNote());
            ps.setDate(6, Date.valueOf(transaction.getT_date()));
            ps.setString(7, transaction.getName());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public ArrayList<Transaction> getAll()
    {
        String sql = "SELECT * FROM transactions";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            ArrayList<Transaction> transactions = new ArrayList<>();
            while (rs.next())
            {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
                t.setUserId(rs.getInt("userId"));
                t.setRefId(rs.getInt("refId"));
                t.setT_type(rs.getString("t_type"));
                t.setAmount(rs.getDouble("amount"));
                t.setNote(rs.getString("note"));
                t.setT_date(rs.getDate("t_date").toLocalDate());
                transactions.add(t);
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Transaction> getByUser(int userId) {
        String sql = "SELECT * FROM transactions WHERE userId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1,userId);
            ResultSet rs = ps.executeQuery();
            ArrayList<Transaction> transactions = new ArrayList<>();

            while(rs.next())
            {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
                t.setUserId(rs.getInt("userId"));
                t.setRefId(rs.getInt("refId"));
                t.setT_type(rs.getString("t_type"));
                t.setAmount(rs.getDouble("amount"));
                t.setNote(rs.getString("note"));
                t.setT_date(rs.getDate("t_date").toLocalDate());
                transactions.add(t);
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction getOneById(int id)
    {
        String sql = "SELECT * FROM transactions WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            Transaction transaction = new Transaction();

            if(rs.next())
            {
                transaction.setId(rs.getInt("id"));
                transaction.setName(rs.getString("name"));
                transaction.setUserId(rs.getInt("userId"));
                transaction.setRefId(rs.getInt("refId"));
                transaction.setT_type(rs.getString("t_type"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setNote(rs.getString("note"));
                transaction.setT_date(rs.getDate("t_date").toLocalDate());

                return transaction;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String[] getColumns()
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
                columns[i] = md.getColumnName(i+1);
            }
            return columns;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id)
    {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
