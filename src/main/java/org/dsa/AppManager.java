package org.dsa;

import org.dsa.UIPanels.Settings;
import org.dsa.UIPanels.TabularPanels.BudgetTablePanel;
import org.dsa.UIPanels.TabularPanels.ExpenseTablePanel;
import org.dsa.UIPanels.TabularPanels.IncomeTablePanel;
import org.dsa.UIPanels.TabularPanels.ReportsPanel;
import org.dsa.UIPanels.components.LoadingDialog;
import org.dsa.abstractions.GenericService;
import org.dsa.dao.*;
import org.dsa.models.objects.Budget;
import org.dsa.models.objects.Expense;
import org.dsa.models.objects.Income;
import org.dsa.utils.Constants.Screens;
import org.dsa.UIPanels.DashboardPanel;
import org.dsa.UIPanels.components.MainFrame;
import org.dsa.utils.DatabaseConnectionManager;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.sql.Connection;

public class AppManager {

    private final Connection conn;

    private MainFrame mainFrame;

    private DashboardPanel dashboardPanel;

    private final GenericService<Income, IncomeDAO> inSer;
    private final GenericService<Expense, ExpenseDAO> exSer;
    private final GenericService<Budget, BudgetDAO> buSer;
    private IncomeTablePanel incomeUIPanel;
    private ExpenseTablePanel expenseUIPanel;
    private BudgetTablePanel budgetUIPanel;
    private ReportsPanel reportUIPanel;
    private Settings settings;

    private static final AppManager instance = new AppManager();
    public static AppManager getInstance() {
        return instance;
    }

    private AppManager() {
        conn = DatabaseConnectionManager.getConnection();
        inSer = new GenericService<>(new IncomeDAO(conn));
        exSer = new GenericService<>(new ExpenseDAO(conn));
        buSer = new GenericService<>(new BudgetDAO(conn));
    }

    public void start() {
        build();
        refresh(Screens.DASHBOARD);
    }

    private void build() {
        if (mainFrame != null) mainFrame.dispose(); // avoid duplicates

        mainFrame = new MainFrame("PESO: Financial Assistant");
        incomeUIPanel = new IncomeTablePanel(inSer, Screens.INCOME);
        expenseUIPanel = new ExpenseTablePanel(exSer, Screens.EXPENSE);
        budgetUIPanel = new BudgetTablePanel(buSer, exSer, Screens.BUDGET);
        reportUIPanel = new ReportsPanel(inSer, exSer, buSer, Screens.REPORTS);

        dashboardPanel = new DashboardPanel(inSer, exSer, buSer, Screens.DASHBOARD);

        // NEW: Add IncomeCatDAO and ExpenseCatDAO for Settings panel
        IncomeCatDAO incomeCatDAO = new IncomeCatDAO(conn);
        ExpenseCatDAO expenseCatDAO = new ExpenseCatDAO(conn);

        settings = new Settings(
            inSer.getDao(),
            exSer.getDao(),
            buSer.getDao(),
            incomeCatDAO,
            expenseCatDAO
        );

        mainFrame.addScreen(Screens.DASHBOARD, dashboardPanel);
        mainFrame.addScreen(Screens.INCOME, incomeUIPanel);
        mainFrame.addScreen(Screens.EXPENSE, expenseUIPanel);
        mainFrame.addScreen(Screens.BUDGET, budgetUIPanel);
        mainFrame.addScreen(Screens.REPORTS, reportUIPanel);
        mainFrame.addScreen(Screens.SETTINGS, settings);

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public void handleLogout() {
        shutdown();
    }

    private void shutdown() {
        int i = JOptionPane.showConfirmDialog(mainFrame, "Are you sure?", "Exiting", JOptionPane.YES_NO_OPTION);
        if (i == 0) System.exit(0);
    }

    public void handleNavigation(String screenName) {
        refresh(screenName);
    }

    private void refresh(String screenName) {
        switch (screenName) {
            case Screens.DASHBOARD -> dashboardPanel.refresh();
            case Screens.INCOME -> incomeUIPanel.refresh();
            case Screens.EXPENSE -> expenseUIPanel.refresh();
            case Screens.BUDGET -> budgetUIPanel.refresh();
            case Screens.REPORTS -> reportUIPanel.refresh();
        }
    }

    public IncomeTablePanel getIncomePanel() {
        return incomeUIPanel;
    }

    public ExpenseTablePanel getExpensePanel() {
        return expenseUIPanel;
    }

    public void runWithLoading(Runnable backgroundTask, Runnable onDone) {
        var loading = new LoadingDialog(mainFrame);
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                backgroundTask.run();
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
                if (onDone != null) onDone.run();
            }
        };
        worker.execute();
        loading.setVisible(true);
    }
}
