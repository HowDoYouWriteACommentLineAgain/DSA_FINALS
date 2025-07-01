package org.dsa.dao;

import org.dsa.models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionDAO extends AbstractDAO{
    public TransactionDAO(Connection conn)
    {
        super(conn);
    }

    public void add(Transaction t) throws SQLException
    {
        String preparedInsert =
            """
                INSERT INTO transactions (type, category, amount, date, note)
                VALUES (?, ?, ?, ?, ?);
            """;

        try(PreparedStatement ps = conn.prepareStatement(preparedInsert, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, t.getType());
            ps.setString(2, t.getCat());
            ps.setFloat(3, t.getAmount());
            ps.setDate(4, t.getDate());
            ps.setString(5, t.getNote());
            ps.executeQuery();

            try(ResultSet rs = ps.getGeneratedKeys())
            {
                if(rs.next())
                {
                    int id = rs.getInt(1);
                    t.setId(id);
                }
            }
        }
    }

}
