package org.dsa.models.tableModels;

import org.dsa.abstractions.GenericDAO;
import org.dsa.abstractions.GenericService;
import org.dsa.abstractions.GenericTableModel;
import org.dsa.additionalServices.AnalyzeAdherence;
import org.dsa.models.objects.Budget;
import org.dsa.models.objects.Expense;

import java.util.ArrayList;
import java.util.Arrays;

public class BudgetTableModel extends GenericTableModel<Budget> {
    private ArrayList<Expense> records;
    public BudgetTableModel(ArrayList<Expense> records) {
        super(new ArrayList<Budget>());
        this.records = records;
    }

    private AnalyzeAdherence totalExpenditureService;

    @Override
    public ArrayList<String> returnColumnNames() {
        return new ArrayList<>(Arrays.asList("Expense_cat", "Max amount", "Goal amount", "Date start", "Date end", "Expenditure"));
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
            case 5 -> AnalyzeAdherence.ofBudgetWithExpense(i.expense_cat(),records); //map goes into here
            default -> null;
        };
    }
}
