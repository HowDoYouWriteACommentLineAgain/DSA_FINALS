package org.dsa;
import org.dsa.components.*;

public class Main {
    public static boolean debugColors = true;
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(Main::initGUI);
    }

    public static void initGUI()
    {
        MainFrame mf = new MainFrame("Hello World!");
        LoginPanel loginPanel = new LoginPanel(mf);
        DashboardPanel dashboardPanel = new DashboardPanel();

        mf.addScreen("login", loginPanel);
        mf.addScreen("dashboard", dashboardPanel);
        mf.showScreen("login");

        mf.pack();
        mf.setVisible(true);
    }
}


