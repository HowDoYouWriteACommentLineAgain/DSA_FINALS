package org.dsa;

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
    private final Connection conn = DatabaseConnectionManager.getConnection();
    MainFrame mf = new MainFrame("Aiuto: Financial Tracker");

    public void start()
    {
        var loginPanel = new LoginPanel(mf);
        var dashboardPanel = new DashboardPanel();
        var transactionPanel = new SimpleDataPanel();

        mf.addScreen(Screens.LOGIN, loginPanel);
        mf.addScreen(Screens.DASHBOARD, dashboardPanel);
        mf.addScreen(Screens.TRANSACTIONS, transactionPanel);

        mf.showScreen(Screens.LOGIN);
        mf.pack();
        mf.setVisible(true);
    }
}
