package org.dsa.utils.Constants;

public final class Screens {
    public static final String LOGIN = "Logout";
    public static final String REGISTER = "Register";
    public static final String DASHBOARD = "Dashboard";
    public static final String TRANSACTION = "Ledger";
    public static final String INCOME = "Income";
    public static final String EXPENSE = "Expense";
    public static final String BUDGET = "Budget";
    public static final String GOALS = "Goals";

    private Screens(){}

    public static String[] getScreens() {
        return new String[]{
            DASHBOARD, TRANSACTION, INCOME,EXPENSE, BUDGET, GOALS,LOGIN,
        };
    }
}
