package org.dsa.models.tableModels;

import org.dsa.abstractions.GenericTableModel;
import org.dsa.models.objects.Budget;

import java.util.ArrayList;
import java.util.Arrays;

public class BudgetTableModel extends GenericTableModel<Budget> {
    public BudgetTableModel() {
        super(new ArrayList<Budget>());
    }

    @Override
    public ArrayList<String> returnColumnNames() {
        return new ArrayList<>(Arrays.asList("Expense_cat", "Max amount", "Goal amount", "Date start", "Date end"));
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Budget i = super.objList.get(rowIndex);
        return switch (columnIndex)
        {
            case 0 -> categoryMap.getOrDefault(i.expense_cat(), "Unknown");
            case 1 -> String.format("%.2f", i.max_amount());
            case 2 -> String.format("%.2f", i.goal_amount());
            case 3 -> i.start_date();
            case 4 -> i.end_date();
            default -> null;
        };
    }
}
