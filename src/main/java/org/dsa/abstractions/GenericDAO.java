package org.dsa.abstractions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericDAO<T> {
    protected Connection conn;

    public abstract ArrayList<T> getAll() throws SQLException;
    public abstract T getOneById(int id) throws SQLException;
    public abstract boolean update(int id, T obj) throws SQLException;
    public abstract void insert(T obj) throws SQLException;
    public abstract String getTableName();
    //    public abstract String[] getColumns();

    public GenericDAO(Connection conn)
    {
        this.conn = conn;
    }

    protected void closeQuietly(AutoCloseable ac)
    {
        try{
            if(ac != null) ac.close();
        } catch (Exception ignored) {}
    }

    public abstract void delete(int id) throws SQLException;

    public ArrayList<String> getIncomeCats() throws SQLException{
        String sql = "Select name from income_categories";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            ArrayList<String> incomeCats = new ArrayList<>();

            while(rs.next())
                incomeCats.add(rs.getString("name"));
            return incomeCats;

        } catch (SQLException e) {
            throw new SQLException("Error Deleting from "+ getTableName(), e);
        }
    }

    public Map<Integer, String> getExpenseCatsMap() throws SQLException{
        String sql = "Select name from income_cat";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            Map<Integer, String> incomeCats = new HashMap<>();

            while(rs.next())
                incomeCats.put(rs.getInt("id"), rs.getString("name"));
            return incomeCats;

        } catch (SQLException e) {
            throw new SQLException("Error Deleting from "+ getTableName(), e);
        }
    }

    public Map<Integer, String> getIncomeCatMap() throws SQLException
    {
        String sql = "SELECT id, name FROM income_cat";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            Map<Integer, String> incomeCatMap = new HashMap<>();

            while(rs.next())
                incomeCatMap.put(rs.getInt("id"), rs.getString("name"));

            return incomeCatMap;

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

}
