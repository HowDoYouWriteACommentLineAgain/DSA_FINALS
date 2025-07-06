package org.dsa.dao;

import org.dsa.models.objects.Budget;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BudgetDAO extends DAOAbstractClass {
    public BudgetDAO(Connection conn)
    {
        super(conn);
    }


    public boolean insert(Budget budget) throws SQLException
    {
        String sql =
                """
                INSERT INTO budgets (userId, cat, limit, spent, timeLimit) values (?,?,?,?,?);
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, budget.getUserId());
            ps.setString(2, budget.getCat().name());
            ps.setFloat(3, budget.getLimit());
            ps.setFloat(4, budget.getSpent());
            ps.setDate(5, Date.valueOf(budget.getTimeLimit()));

            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public Budget[] getAll()
    {
        return new Budget[0];
    }


    @Override
    public Budget[] getByUser(int id) {
        return new Budget[0];
    }

    @Override
    public Object getKeys() {
        return null;
    }
}
