package org.dsa;

import org.dsa.UIPanels.components.NavigationBar;
import org.dsa.utils.Constants.Screens;
import org.dsa.UIPanels.DashboardPanel;
import org.dsa.UIPanels.LoginPanel;
import org.dsa.UIPanels.components.MainFrame;
import org.dsa.UIPanels.components.SimpleDataPanel;
import org.dsa.utils.DatabaseConnectionManager;

import java.sql.Connection;

/*TODOS:
* Add more panels
*
*
* */

public class AppManager {
//    private final Connection conn = DatabaseConnectionManager.getConnection();
    private int userId;
    public void start()
    {
        final MainFrame mf = new MainFrame("Aiuto: Financial Tracker");
        new NavigationBar(mf);

        var loginPanel = new LoginPanel(mf);
        var dashboardPanel = new DashboardPanel();
        var transactionPanel = new SimpleDataPanel();
        var incomePanel = new SimpleDataPanel();
        var expensePanel = new SimpleDataPanel();
        var budgetPanel = new SimpleDataPanel();
        var goalsPanel = new SimpleDataPanel();

        mf.addScreen(Screens.LOGIN, loginPanel);
        mf.addScreen(Screens.DASHBOARD, dashboardPanel);
        mf.addScreen(Screens.TRANSACTION, transactionPanel);
        mf.addScreen(Screens.INCOME, incomePanel);
        mf.addScreen(Screens.EXPENSE,expensePanel);
        mf.addScreen(Screens.BUDGET, budgetPanel);
        mf.addScreen(Screens.GOALS, goalsPanel);

        mf.showScreen(Screens.LOGIN);
        mf.pack();
        mf.setVisible(true);
    }
}
