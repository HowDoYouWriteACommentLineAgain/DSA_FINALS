package org.dsa.utils;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.border.Border;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

public class LayoutUtil {
    public static final Insets DEFAULT_INSETS = new Insets(5, 5, 5, 5);
    public static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);

    public static Border defaultPadding() {
        return BorderFactory.createEmptyBorder(10, 10, 10, 10);
    }

    public static Border padded(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }
    public static Component rigidSpacer(int width, int height) {
        return Box.createRigidArea(new Dimension(width, height));
    }

    public static Component verticalGap(int height) {
        return Box.createVerticalStrut(height);
    }

    public static Component horizontalGap(int width) {
        return Box.createHorizontalStrut(width);
    }

}
