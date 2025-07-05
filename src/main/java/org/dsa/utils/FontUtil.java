package org.dsa.utils;

import javax.swing.UIManager;
import java.awt.Font;

public class FontUtil
{
    public static Font base = UIManager.getFont("Label.font");

    public static Font getTitleFont()
    {
        return base.deriveFont(Font.BOLD, base.getSize() + 24f);
    }

    public static Font getHeadingFont()
    {
        return base.deriveFont(Font.BOLD, base.getSize() + 4f);
    }

    public static Font getBodyFont()
    {
        return base.deriveFont(Font.PLAIN, base.getSize());
    }

    public static Font getFieldFont()
    {
        return base.deriveFont(Font.BOLD, base.getSize());
    }

    private FontUtil(){}
}
