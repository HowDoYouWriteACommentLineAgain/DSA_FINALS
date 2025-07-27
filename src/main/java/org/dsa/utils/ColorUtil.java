package org.dsa.utils;

import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.Color;
public final class ColorUtil {

    public static final Color PRIMARY_COLOR = new Color(0x007BFF);
    public static final Color SECONDARY_COLOR = new Color(0x6C757D);
    public static final Color TERTIARY_COLOR = new Color(0x6610F2);

    public static Color getBackgroundColor() {
        return UIManager.getColor("Panel.background");
    }

    public static Color getBackgroundColorBrighter() {
        Color base = getBackgroundColor();
        return base != null ? base.brighter() : Color.LIGHT_GRAY;
    }

    public static Color getBackgroundColorDarker() {
        Color base = getBackgroundColor();
        return base != null ? base.darker() : Color.GRAY;
    }

    public static Color getPrimaryTextColor() {
        return UIManager.getColor("Label.foreground");
    }

    public static Color getSecondaryTextColor() {
        return UIManager.getColor("Label.disabledForeground");
    }

    public static Color getAccentColor() {
        return UIManager.getColor("Component.accentColor");
    }

    public static Color getBorderColor() {
        return UIManager.getColor("Separator.foreground");
    }

    public static Color getHeaderColor() {
        return UIManager.getColor("TableHeader.background");
    }

    // Static colors remain unchanged
    public static final Color SUCCESS_COLOR = new Color(0x28A745);
    public static final Color WARNING_COLOR = new Color(0xFFC107);
    public static final Color OVER_THRESHOLD_COLOR = new Color(0xFF6B00);
    public static final Color OVER_BUDGET_COLOR = new Color(0xB72020);
    public static final Color ERROR_COLOR = new Color(0xDC3545);

    private ColorUtil() {}
}
