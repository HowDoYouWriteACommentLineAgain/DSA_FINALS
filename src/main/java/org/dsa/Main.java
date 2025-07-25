package org.dsa;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatMonocaiIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import org.dsa.utils.ThemeManager;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;

public class Main {
    public static boolean debugColors = true;
//    public static AppManager app = new AppManager();
    public static void main(String[] args)
    {
        ThemeManager.applyTheme();
        javax.swing.SwingUtilities.invokeLater(()-> AppManager.getInstance().start());
    }

}


