package org.dsa.utils.Constants;

public final class Screens {
    public static final String LOGIN = "Logout";
    public static final String DASHBOARD = "Dashboard";
    public static final String TRANSACTIONS = "Transactions";
    public static final String BUDGET = "Budget";
    public static final String GOALS = "Goals";

    private Screens(){}

    public static String[] getScreens() {
        return new String[]{
            DASHBOARD, TRANSACTIONS, BUDGET, GOALS,LOGIN,
        };
    }
}
