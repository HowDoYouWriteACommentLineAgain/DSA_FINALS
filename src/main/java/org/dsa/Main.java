package org.dsa;
import org.dsa.components.*;

public class Main {
    public static boolean debugColors = true;
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initGUI();
            }
        });
    }

    public static void initGUI()
    {


        MainFrame mf = new MainFrame("Hello World!");

        LoginPanel loginPanel = new LoginPanel();
        LoginPanel DashboardPanel = new LoginPanel();

        mf.addScreen("login", loginPanel);
        mf.showScreen("login");

        mf.pack();
        mf.setVisible(true);
    }
}


