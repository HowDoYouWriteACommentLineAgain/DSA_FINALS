package org.dsa;

import org.dsa.UIPanels.TabularPanels.BudgetTablePanel;
import org.dsa.UIPanels.TabularPanels.ExpenseTablePanel;
import org.dsa.UIPanels.TabularPanels.IncomeTablePanel;
import org.dsa.UIPanels.TabularPanels.ReportsPanel;
import org.dsa.UIPanels.components.LoadingDialog;
import org.dsa.UIPanels.components.NavigationBar;
import org.dsa.abstractions.GenericService;
import org.dsa.dao.BudgetDAO;
import org.dsa.dao.ExpenseDAO;
import org.dsa.dao.IncomeDAO;
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

/*TODOS:
* Add more panels
*
*
* */

public class AppManager {

    private final Connection conn;

    private final MainFrame mainFrame;
    private final NavigationBar navbar;

    private DashboardPanel dashboardPanel;

    private final GenericService<Income, IncomeDAO> inSer;
    private final GenericService<Expense, ExpenseDAO> exSer;
    private final GenericService<Budget, BudgetDAO> buSer;
    private IncomeTablePanel incomeUIPanel;
    private ExpenseTablePanel expenseUIPanel;
    private BudgetTablePanel budgetUIPanel;
    private ReportsPanel reportUIPanel;


    private static final AppManager instance = new AppManager();
    public static AppManager getInstance()
    {
        return instance;
    }

    /*
     * static constants of the singleton App Manager
     */
    private AppManager(){
        conn = DatabaseConnectionManager.getConnection();
        inSer = new GenericService<>(new IncomeDAO(conn));
        exSer = new GenericService<>(new ExpenseDAO(conn));
        buSer = new GenericService<>(new BudgetDAO(conn));

//                loginPanel = new LoginPanel();
        mainFrame = new MainFrame("PESO: Financial Assistant");
        navbar = new NavigationBar();
    }

    public void start(){

        build();

        //starting screens after building
//        mainFrame.showScreen(Screens.DASHBOARD);
        mainFrame.pack();
        mainFrame.setVisible(true);


        refresh(Screens.DASHBOARD);
    }

    private void build()
    {
        incomeUIPanel = new IncomeTablePanel(inSer);
        expenseUIPanel = new ExpenseTablePanel(exSer);
        budgetUIPanel = new BudgetTablePanel(buSer, exSer);
        reportUIPanel = new ReportsPanel(inSer,exSer,buSer);

        dashboardPanel = new DashboardPanel(inSer);

//        mainFrame.addNavbar(navbar);
        mainFrame.addScreen(Screens.DASHBOARD, dashboardPanel);
        mainFrame.addScreen(Screens.INCOME, incomeUIPanel);
        mainFrame.addScreen(Screens.EXPENSE, expenseUIPanel);
        mainFrame.addScreen(Screens.BUDGET, budgetUIPanel);
        mainFrame.addScreen(Screens.REPORTS, reportUIPanel);
    }

    public void handleLogout()
    {
        shutdown();
    }

    private void shutdown()
    {
        int i = JOptionPane.showConfirmDialog(mainFrame,"Are you sure?", "Exiting", JOptionPane.YES_NO_OPTION);
        if(i == 0) System.exit(0);
    }

    public void handleNavigation(String screenName)
    {
        refresh(screenName);
//        mainFrame.showScreen(screenName);
    }

    private void refresh(String screenName)
    {
        switch (screenName)
        {
            case Screens.DASHBOARD ->dashboardPanel.refresh();
            case Screens.INCOME ->incomeUIPanel.refresh();
            case Screens.EXPENSE -> expenseUIPanel.refresh();
            case Screens.BUDGET -> budgetUIPanel.refresh();
            case Screens.REPORTS -> reportUIPanel.refresh();
        }
    }

    public void runWithLoading(Runnable backgroundTask, Runnable onDone) {
        var loading = new LoadingDialog(mainFrame); // your modal "loading..." panel
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