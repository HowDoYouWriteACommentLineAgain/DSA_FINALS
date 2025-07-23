package org.dsa.models.tableModels;

import org.dsa.abstractions.GenericTableModel;
import org.dsa.models.objects.Expense;

import java.util.ArrayList;
import java.util.Arrays;

public class ExpenseTableModel extends GenericTableModel<Expense> {

    public ExpenseTableModel() {
        super(new ArrayList<>());
    }

    @Override
    public ArrayList<String> returnColumnNames() {
        return new ArrayList<>(Arrays.asList("Name","Category", "Amount", "Note", "Date"));
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Expense i = super.objList.get(rowIndex);
        return switch (columnIndex)
        {
            case 0 -> i.name();
            case 1 -> categoryMap.getOrDefault(i.expense_cat(), "Unknown");
            case 2 -> i.amount();
            case 3 -> i.note();
            case 4 -> i.date();
            default -> null;
        };
    }
}
