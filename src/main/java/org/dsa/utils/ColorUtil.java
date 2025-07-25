package org.dsa.utils;

import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.Color;

public final class ColorUtil {

    public static final Color PRIMARY_COLOR = new Color(0x007BFF);
    public static final Color SECONDARY_COLOR = new Color(0x6C757D);
    public static final Color TERTIARY_COLOR = new Color(0x6610F2);

    public static final Color BACKGROUND_COLOR    = UIManager.getColor("Panel.background");
    public static final Color PRIMARY_TEXT_COLOR  = UIManager.getColor("Label.Foreground");
    public static final Color SECONDARY_TEXT_COLOR= UIManager.getColor("Label.disabledForeground");
    public static final Color ACCENT_COLOR        = UIManager.getColor("Component.accentColor");
    public static final Color BORDER_COLOR        =  UIManager.getColor("Separator.foreground");

    public static final Color SUCCESS_COLOR       = new Color(0x28A745);
    public static final Color WARNING_COLOR       = new Color(0xFFC107);
    public static final Color OVER_THRESHOLD_COLOR       = new Color(0xB0A600);
    public static final Color OVER_BUDGET_COLOR       = new Color(0xB72020);
    public static final Color ERROR_COLOR         = new Color(0xDC3545);


    public static final Border debugBorder0 = UtilDebugBorder.debugBorder(PRIMARY_COLOR);
    public static final Border debugBorder1 = UtilDebugBorder.debugBorder(SECONDARY_COLOR);
    public static final Border debugBorder2 = UtilDebugBorder.debugBorder(TERTIARY_COLOR);


    private ColorUtil(){};
}
