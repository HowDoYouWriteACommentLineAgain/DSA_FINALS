package org.dsa.utils;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class UtilDebugBorder
{
    private UtilDebugBorder(){};
    public static Border debugBorder(Color color) {
        return new LineBorder(color, 2);
    }
}
