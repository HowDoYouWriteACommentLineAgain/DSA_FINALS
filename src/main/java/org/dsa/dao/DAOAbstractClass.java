package org.dsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DAOAbstractClass {
    protected Connection conn;

    public DAOAbstractClass(Connection conn)
    {
        this.conn = conn;
    }

    protected void closeQuietly(AutoCloseable ac)
    {
        try{
            if(ac != null) ac.close();
        } catch (Exception ignored) {}
    }

    public abstract Object getAll();
    public abstract Object getByUser(int id);
    public abstract Object getKeys();
//    public abstract boolean insert(Object object) throws SQLException;

}
