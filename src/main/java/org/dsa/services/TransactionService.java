package org.dsa.services;

import org.dsa.dao.TransactionDAO;
import org.dsa.interfaces.TransactionServiceInterface;
import org.dsa.models.objects.Transaction;
import org.dsa.utils.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionService implements TransactionServiceInterface
{
    private final TransactionDAO dao;

    public TransactionService(Connection conn) {
        this.dao = new TransactionDAO(conn);
    }
    public ArrayList<Transaction> getAll()
    {
        return dao.getAll();
    }

    public void save(Transaction transaction) throws SQLException {
        if(!transaction.validate())
            throw new IllegalArgumentException("Invalid transaction arguments");
        dao.insert(transaction);
    }
}
