package org.dsa;

import org.dsa.Constants.Screen;
import org.dsa.UIPanels.DashboardPanel;
import org.dsa.UIPanels.LoginPanel;
import org.dsa.components.MainFrame;
import org.dsa.utils.DatabaseConnectionManager;

import java.sql.Connection;

/*TODOS:
* Add more panels
*
*
* */

public class AppManager {
    private final Connection conn = DatabaseConnectionManager.getConnection();
    MainFrame mf = new MainFrame("Hello World!");


    public void start()
    {
        var loginPanel = new LoginPanel(mf);
        var dashboardPanel = new DashboardPanel();

        mf.addScreen(Screen.LOGIN, loginPanel);
        mf.addScreen(Screen.DASHBOARD, dashboardPanel);

        mf.showScreen(Screen.LOGIN);
        mf.pack();
        mf.setVisible(true);
    }
}
