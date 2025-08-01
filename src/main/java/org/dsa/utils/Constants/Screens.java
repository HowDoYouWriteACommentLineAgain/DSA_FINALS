package org.dsa.utils.Constants;

public final class Screens {
//    public static final String LOGIN = "Exit";
    public static final String REGISTER = "Register";
    public static final String DASHBOARD = "Dashboard";
//    public static final String TRANSACTION = "Ledger";
    public static final String INCOME = "Income Manager";
    public static final String EXPENSE = "Expense Manager";
    public static final String BUDGET = "Budget and Goals Manager";
    public static final String REPORTS = "Periodical Reports";
    public static final String SETTINGS = "Settings";

    private Screens(){}

    public static String[] getScreens() {
        return new String[]{
            DASHBOARD, INCOME, EXPENSE, BUDGET, REPORTS, SETTINGS
        };
    }
}
