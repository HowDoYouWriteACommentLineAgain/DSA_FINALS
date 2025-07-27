package org.dsa;
import org.dsa.utils.ThemeManager;

public class Main {
    public static boolean debugColors = true;
//    public static AppManager app = new AppManager();
    public static void main(String[] args)
    {
        ThemeManager.applyTheme();
        javax.swing.SwingUtilities.invokeLater(()-> AppManager.getInstance().start());
    }

}


