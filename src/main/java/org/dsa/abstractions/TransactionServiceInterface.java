package org.dsa.abstractions;

import org.dsa.models.objects.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;

public interface TransactionServiceInterface {
    ArrayList<Transaction> getAll();
    void save(Transaction transaction) throws SQLException;

}
