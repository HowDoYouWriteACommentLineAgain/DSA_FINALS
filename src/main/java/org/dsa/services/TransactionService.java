package org.dsa.services;

import org.dsa.abstractions.GenericService;
import org.dsa.dao.TransactionDAO;
import org.dsa.models.objects.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionService extends GenericService<Transaction, TransactionDAO>
{
    public TransactionService(Connection conn) {
        this.dao = new TransactionDAO(conn);
    }

    @Override
    public ArrayList<Transaction> getAllByUser(int id) {
        try {
            return dao.getByUser(id);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch records", e);
        }
    }
}
