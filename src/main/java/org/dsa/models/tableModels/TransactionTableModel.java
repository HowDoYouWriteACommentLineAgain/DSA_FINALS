package org.dsa.models.tableModels;

import org.dsa.models.objects.Transaction;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TransactionTableModel extends AbstractTableModel {
    private final String[] columns = {"date", "name", "type", "amount","note"};
    private final ArrayList<Transaction> transactions;


    public TransactionTableModel(ArrayList<Transaction> transactions)
    {
        this.transactions = transactions;
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
