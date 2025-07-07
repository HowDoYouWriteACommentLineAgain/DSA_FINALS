package org.dsa.models.tableModels;

import org.dsa.models.objects.Transaction;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class TransactionTableModel extends AbstractTableModel {
    private final String[] columns = {"date", "name", "type", "amount","note"};
    private final ArrayList<Transaction> transactions;


    public TransactionTableModel(ArrayList<Transaction> transactions)
    {
        this.transactions = transactions;
        new DefaultTableModel();
    }

    public void setData(ArrayList<Transaction> data)
    {
        this.transactions.clear();
        this.transactions.addAll(data);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column)
    {
        return this.columns[column];
    }

    public Transaction getAt(int rowIndex)
    {
        return transactions.get(rowIndex);
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Transaction t = transactions.get(rowIndex);
        return switch (columnIndex)
        {
            case 0 -> t.getT_date();
            case 1 -> t.getName();
            case 2 -> t.getT_type();
            case 3 -> t.getAmount();
            case 4 -> t.getNote();
            default -> null;
        };
    }
}
