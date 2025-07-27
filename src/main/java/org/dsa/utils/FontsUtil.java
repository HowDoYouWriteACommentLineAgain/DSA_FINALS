package org.dsa.utils;

import javax.swing.JLabel;
import javax.swing.UIManager;
import java.awt.Font;

public class FontsUtil
{
    public static Font BASE = resolveBase();

    private static Font resolveBase(){
        Font lafFont = UIManager.getFont("Label.font");
        return (lafFont != null) ? lafFont : new JLabel().getFont();
    }

    public static final Font TITLE_FONT = BASE.deriveFont(Font.BOLD, 18f);
    public static final Font HEADER_FONT = BASE.deriveFont(Font.BOLD, 16f);
    public static final Font FIELD_FONT = BASE.deriveFont(Font.PLAIN, 13f);
    public static final Font REGULAR = BASE.deriveFont(Font.PLAIN, 13f);
    public static final Font REGULAR_BOLD = BASE.deriveFont(Font.BOLD, 13f);
    public static final Font REGULAR_ITALIC = BASE.deriveFont(Font.ITALIC, 13f);



    private FontsUtil(){}
}
