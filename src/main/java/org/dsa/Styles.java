package org.dsa;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public final class Styles {
    public static final String FontFace = "Arial";
    public static final int FontBaseSize = 8;

    public static final Font TITLE_FONT = new Font(FontFace, Font.BOLD, FontBaseSize * 3);
    public static final Font HEADER_FONT = new Font(FontFace, Font.BOLD, FontBaseSize * 2);
    public static final Font FIELD_FONT = new Font(FontFace, Font.BOLD, FontBaseSize * 2);
    public static final Font TEXT_FONT = new Font(FontFace, Font.PLAIN, FontBaseSize * 2);

    public static final Dimension LARGE_TEXT_DIM = new Dimension(225, 60);

    public static final Dimension LARGE_CONTAINER = new Dimension(700, 500);
    public static final Dimension MEDIUM_CONTAINER = new Dimension(450, 250);
    public static final Dimension SMALL_CONTAINER = new Dimension(300, 75);
    public static final Dimension LONG_CONTAINER = new Dimension(300, 25);
    public static final Dimension SLIM_CONTAINER = new Dimension(200, 50);


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

    public static final Border debugBorder0 = Styles.utilDebugBorder(PRIMARY_COLOR);
    public static final Border debugBorder1 = Styles.utilDebugBorder(SECONDARY_COLOR);
    public static final Border debugBorder2 = Styles.utilDebugBorder(TERTIARY_COLOR);
    public static Border utilDebugBorder(Color color) {
        return new LineBorder(color, 2);
    }

    public static void setStaticSize(JComponent comp, Dimension dim)
    {
        int width = (int) dim.getWidth();
        int height =(int) dim.getHeight();
        comp.setPreferredSize(dim);
        comp.setMinimumSize(dim);
        comp.setMaximumSize(dim);
    }


    private Styles(){};
}
