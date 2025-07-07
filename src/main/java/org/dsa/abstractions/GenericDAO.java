package org.dsa.abstractions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class GenericDAO<T> {
    protected Connection conn;

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

    public abstract ArrayList<T> getAll() throws SQLException;
    public abstract ArrayList<T> getByUser(int userId) throws SQLException;
    public abstract T getOneById(int id) throws SQLException;
    public abstract String[] getColumns();
    public abstract boolean updateById(int id, T obj) throws SQLException;
    public abstract void delete(int id) throws SQLException;
    public abstract void insert(T obj) throws SQLException;
}
