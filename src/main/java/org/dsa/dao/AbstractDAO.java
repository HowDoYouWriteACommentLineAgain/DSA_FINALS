package org.dsa.dao;

import java.sql.Connection;

public abstract class AbstractDAO {
    protected Connection conn;

    public AbstractDAO(Connection conn)
    {
        this.conn = conn;
    }

    protected void closeQuietly(AutoCloseable ac)
    {
        try{
            if(ac != null) ac.close();
        } catch (Exception ignored) {}
    }

}
