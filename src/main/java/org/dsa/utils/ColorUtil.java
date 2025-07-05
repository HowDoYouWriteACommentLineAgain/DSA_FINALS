package org.dsa.utils;

import javax.swing.border.Border;
import java.awt.Color;

public final class ColorUtil {

    public static final Color PRIMARY_COLOR = new Color(0xE81A1A);
    public static final Color SECONDARY_COLOR = new Color(0x1A73E8);
    public static final Color TERTIARY_COLOR = new Color(0xE81ADE);

    public static final Color BACKGROUND_COLOR    = new Color(0xF4F4F4);
    public static final Color PRIMARY_TEXT_COLOR  = new Color(0x333333);
    public static final Color SECONDARY_TEXT_COLOR= new Color(0x666666);
    public static final Color ACCENT_COLOR        = new Color(0x4A90E2);
    public static final Color SUCCESS_COLOR       = new Color(0x7ED321);
    public static final Color WARNING_COLOR       = new Color(0xF5A623);
    public static final Color ERROR_COLOR         = new Color(0xD0021B);
    public static final Color BORDER_COLOR        = new Color(0xCCCCCC);

    public static final Border debugBorder0 = UtilDebugBorder.debugBorder(PRIMARY_COLOR);
    public static final Border debugBorder1 = UtilDebugBorder.debugBorder(SECONDARY_COLOR);
    public static final Border debugBorder2 = UtilDebugBorder.debugBorder(TERTIARY_COLOR);


    private ColorUtil(){};
}
