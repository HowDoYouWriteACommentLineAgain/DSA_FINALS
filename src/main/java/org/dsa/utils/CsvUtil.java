package org.dsa.utils;

import org.dsa.dao.BudgetDAO;
import org.dsa.dao.ExpenseDAO;
import org.dsa.dao.IncomeDAO;
import org.dsa.models.objects.Budget;
import org.dsa.models.objects.Expense;
import org.dsa.models.objects.Income;

import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    public static void exportDataToCSV(
            List<Income> incomes,
            List<Expense> expenses,
            List<Budget> budgets,
            String basePath) throws IOException {

        exportToCSV(incomes, new File(basePath + "_incomes.csv"));
        exportToCSV(expenses, new File(basePath + "_expenses.csv"));
        exportToCSV(budgets, new File(basePath + "_budgets.csv"));
    }

    public static void importDataFromCSV(
            String basePath,
            IncomeDAO incomeDAO,
            ExpenseDAO expenseDAO,
            BudgetDAO budgetDAO) {

        try {
            // Import Incomes
            File incomeFile = new File(basePath + "_incomes.csv");
            if (incomeFile.exists()) {
                for (String[] row : importFromCSV(incomeFile)) {
                    Income income = new Income(
                            0, // let DB assign id
                            row[1],
                            Integer.parseInt(row[2]),
                            Double.parseDouble(row[3]),
                            row[4],
                            Date.valueOf(row[5])
                    );
                    incomeDAO.insert(income);
                }
            }

            // Import Expenses
            File expenseFile = new File(basePath + "_expenses.csv");
            if (expenseFile.exists()) {
                for (String[] row : importFromCSV(expenseFile)) {
                    Expense expense = new Expense(
                            0,
                            row[1],
                            Integer.parseInt(row[2]),
                            Double.parseDouble(row[3]),
                            row[4],
                            Date.valueOf(row[5])
                    );
                    expenseDAO.insert(expense);
                }
            }

            // Import Budgets
            File budgetFile = new File(basePath + "_budgets.csv");
            if (budgetFile.exists()) {
                for (String[] row : importFromCSV(budgetFile)) {
                    Budget budget = new Budget(
                            0,
                            Integer.parseInt(row[1]),
                            Double.parseDouble(row[2]),
                            Double.parseDouble(row[3]),
                            Date.valueOf(row[4]),
                            Date.valueOf(row[5])
                    );
                    budgetDAO.insert(budget);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to import CSV data: " + e.getMessage(), e);
        }
    }

    public static void exportToCSV(List<?> dataList, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (!dataList.isEmpty()) {
                Object first = dataList.get(0);
                if (first instanceof Income) {
                    writer.write("id,name,income_cat,amount,note,date\n");
                    for (Object obj : dataList) {
                        Income i = (Income) obj;
                        writer.write(String.format("%d,%s,%d,%.2f,%s,%s\n",
                                i.id(), escape(i.name()), i.income_cat(), i.amount(),
                                escape(i.note()), i.date()));
                    }
                } else if (first instanceof Expense) {
                    writer.write("id,name,expense_cat,amount,note,date\n");
                    for (Object obj : dataList) {
                        Expense e = (Expense) obj;
                        writer.write(String.format("%d,%s,%d,%.2f,%s,%s\n",
                                e.id(), escape(e.name()), e.expense_cat(), e.amount(),
                                escape(e.note()), e.date()));
                    }
                } else if (first instanceof Budget) {
                    writer.write("id,expense_cat,max_amount,goal_amount,start_date,end_date\n");
                    for (Object obj : dataList) {
                        Budget b = (Budget) obj;
                        writer.write(String.format("%d,%d,%.2f,%.2f,%s,%s\n",
                                b.id(), b.expense_cat(), b.max_amount(), b.goal_amount(),
                                b.start_date(), b.end_date()));
                    }
                } else {
                    throw new IllegalArgumentException("Unsupported object type for export.");
                }
            }
        }
    }

    public static List<String[]> importFromCSV(File file) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                rows.add(line.split(",", -1));
            }
        }
        return rows;
    }

    private static String escape(String value) {
        if (value == null) return "";
        return value.replace(",", "\\,");
    }
}
