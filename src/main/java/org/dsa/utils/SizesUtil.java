package org.dsa.utils;

import java.awt.Dimension;

public class SizesUtil {
    public static final int PADDING = 12;
    public static final int GAP     = 8;
    public static final int VERTICAL_STRUT     = 8;
    public static final Dimension DEFAULT_BUTTON_SIZE = new Dimension(100, 30);
    public static final Dimension DEFAULT_FIELD_SIZE  = new Dimension(200, 30);
    public static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(1000, 600);
    public static final Dimension HALF_WINDOW_WITDH = new Dimension(500, 80);
    public static final Dimension SMALL_DAY_BOX = new Dimension(new Dimension(50, 25));
    public static final Dimension SMALL_YEAR_BOX = new Dimension(new Dimension(70, 25));

    public static final int TITLE_SIZE  =   FontsUtil.TITLE_FONT.getSize();
    public static final int HEADER_SIZE =   FontsUtil.HEADER_FONT.getSize();
    public static final int FIELD_SIZE  =   FontsUtil.FIELD_FONT.getSize();
    public static final int TEXT_SIZE   =   FontsUtil.REGULAR.getSize();



    private SizesUtil(){};
}
