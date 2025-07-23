package org.dsa.additionalServices;

import org.dsa.dao.ExpenseDAO;
import org.dsa.models.objects.Expense;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzeAdherence {
    public static int callTimes = 0;
    public static Double ofBudgetWithExpense(int id, List<Expense> List)
    {
        List<Expense> expenseList = new ArrayList<>(List);
        Map<Integer, Double> idToTotalExpenseMap = new HashMap<>();

        for(Expense expense : expenseList)
        {
            int curr_id = expense.expense_cat();
            double curr_amount = expense.amount();
            idToTotalExpenseMap.merge(curr_id, curr_amount, Double::sum);
        }

        return idToTotalExpenseMap.get(id)!= null ? idToTotalExpenseMap.get(id) : 0.00d;
    }
}
