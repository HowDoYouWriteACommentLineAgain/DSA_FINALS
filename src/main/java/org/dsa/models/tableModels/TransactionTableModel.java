package org.dsa.models.tableModels;

import org.dsa.abstractions.GenericTableModel;
import org.dsa.models.objects.Transaction;
import java.util.ArrayList;

public class TransactionTableModel extends GenericTableModel<Transaction> {


    public TransactionTableModel(ArrayList<Transaction> transactions)
    {
        super(transactions);
        String[] columns = {"date", "name", "type", "amount", "note"};
        super.setColumns(columns);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Transaction t = super.objList.get(rowIndex);
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
