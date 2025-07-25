package org.dsa;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;

public class Main {
    public static boolean debugColors = true;
//    public static AppManager app = new AppManager();
    public static void main(String[] args)
    {
        try {
            UIManager.setLookAndFeel(new FlatDarkPurpleIJTheme());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();

        for (Object key : defaults.keySet()) {
            Object value = defaults.get(key);
            System.out.println(key + " = ");
//            if (value instanceof Color) {
//                System.out.println(key + " = " + value);
//            }
        }

        javax.swing.SwingUtilities.invokeLater(()-> AppManager.getInstance().start());
    }

}


