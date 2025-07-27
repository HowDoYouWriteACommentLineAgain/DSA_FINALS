package org.dsa.models.tableModels;

import org.dsa.abstractions.GenericTableModel;
import org.dsa.models.objects.Report;

import java.util.ArrayList;
import java.util.Arrays;

public class ReportTableModel extends GenericTableModel<Report> {
    public ReportTableModel(ArrayList<Report> objList) {
        super(objList);
    }

    @Override
    public ArrayList<String> returnColumnNames() {
        return new ArrayList<>(Arrays.asList("Period", "Total Income", "Total Expense", "Net", "Single Largest Expenditure"));
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (objList.isEmpty()) {
            if (columnIndex == 0) return "No records found";
            else return "";
        }

        Report r = objList.get(rowIndex);return switch (columnIndex)
        {
            case 0 -> rowIndex;
            case 1 -> r.totalIncome() == 0.00d ? "" : r.totalIncome();
            case 2 -> r.totalExpense() == 0.00d ? "" : r.totalExpense() ;
            case 3 -> r.netSavings() == 0.00d ? "" : r.netSavings();
            case 4 -> r.largestExpense() == 0.00d ? "" : r.largestExpense();
//            case 5 -> r.largestCategoryExpenditure();
            default -> null;
        };
    }
}