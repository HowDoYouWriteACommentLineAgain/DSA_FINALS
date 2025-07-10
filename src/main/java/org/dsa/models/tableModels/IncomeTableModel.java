package org.dsa.models.tableModels;

import org.dsa.abstractions.GenericTableModel;
import org.dsa.models.objects.Income;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class IncomeTableModel extends GenericTableModel<Income> {

    public IncomeTableModel() {
        super(new ArrayList<Income>());
    }

    @Override
    public ArrayList<String> returnColumnNames() {
        return new ArrayList<>(Arrays.asList("Name","Category", "Amount", "Note", "Date"));
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        System.out.println("getValueAt CatMap: "+ categoryMap);

        Income i = super.objList.get(rowIndex);
        return switch (columnIndex)
        {
            case 0 -> i.name();
            case 1 -> categoryMap.getOrDefault(i.income_cat(), "Unknown");
            case 2 -> i.amount();
            case 3 -> i.note();
            case 4 -> i.date();
            default -> null;
        };
    }
}
