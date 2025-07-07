package org.dsa;

public class Main {
    public static boolean debugColors = true;
//    public static AppManager app = new AppManager();
    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(()-> AppManager.getInstance().start());
    }

}


