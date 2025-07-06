package org.dsa.services;

import org.dsa.dao.TransactionDAO;
import org.dsa.interfaces.Service;
import org.dsa.models.objects.Transaction;
import org.dsa.utils.DatabaseConnectionManager;

public class TransactionService
{
    TransactionDAO dao = new TransactionDAO(DatabaseConnectionManager.getConnection());

    public Transaction[] getAll()
    {
        return dao.getAll();
    }
}
