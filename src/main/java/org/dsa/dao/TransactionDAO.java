package org.dsa.dao;

import org.dsa.abstractions.GenericDAO;
import org.dsa.models.objects.Transaction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionDAO extends GenericDAO<Transaction> {
    public TransactionDAO(Connection conn)
    {
        super(conn);
    }

    @Override
    public ArrayList<Transaction> getAll() throws SQLException
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
                t.setT_date(rs.getDate("t_date"));
                transactions.add(t);
            }
            return transactions;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public ArrayList<Transaction> getByUser(int userId) throws SQLException{
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
                t.setT_date(rs.getDate("t_date"));
                transactions.add(t);
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction getOneById(int id) throws SQLException
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
                transaction.setT_date(rs.getDate("t_date"));

                return transaction;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching by id" + e.getMessage(), e);
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
    public boolean updateById(int id, Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET t_type = ?, refId = ?, amount = ?, note = ?, t_date = ?, name = ? WHERE id = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, transaction.getT_type());
            ps.setInt(2, transaction.getRefId());
            ps.setDouble(3, transaction.getAmount());
            ps.setString(4, transaction.getNote());
            ps.setDate(5, transaction.getT_date());
            ps.setString(6, transaction.getName());
            ps.setInt(7, id);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new SQLException("Error Updating Transaction" + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) throws SQLException
    {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Deleting Transaction", e);
        }
    }

    @Override
    public void insert(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (userId, t_type, refId, amount, note, t_date, name) values (?,?,?,?,?,?,?);";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, transaction.getUserId());
            ps.setString(2, transaction.getT_type());
            ps.setInt(3, transaction.getRefId());
            ps.setDouble(4, transaction.getAmount());
            ps.setString(5, transaction.getNote());
            ps.setDate(6, transaction.getT_date());
            ps.setString(7, transaction.getName());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error inserting transaction", e);
        }
    }

}
