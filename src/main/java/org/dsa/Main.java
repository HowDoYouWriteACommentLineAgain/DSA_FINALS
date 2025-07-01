package org.dsa;
import org.dsa.UIPanels.DashboardPanel;
import org.dsa.UIPanels.LoginPanel;
import org.dsa.components.*;

public class Main {
    public static boolean debugColors = true;
    public static AppManager app = new AppManager();
    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(()-> new AppManager().start());
    }

}


