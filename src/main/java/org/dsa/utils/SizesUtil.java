package org.dsa.utils;

import java.awt.Dimension;
import java.awt.Font;

public class SizesUtil {
    public static final int PADDING = 12;
    public static final int GAP     = 8;
    public static final int VERTICAL_STRUT     = 8;
    public static final Dimension DEFAULT_BUTTON_SIZE = new Dimension(100, 30);
    public static final Dimension DEFAULT_FIELD_SIZE  = new Dimension(200, 30);
    public static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(800, 400);


    public static final int TITLE_SIZE  =   FontsUtil.TITLE_FONT.getSize();
    public static final int HEADER_SIZE =   FontsUtil.HEADER_FONT.getSize();
    public static final int FIELD_SIZE  =   FontsUtil.FIELD_FONT.getSize();
    public static final int TEXT_SIZE   =   FontsUtil.TEXT_FONT.getSize();



    private SizesUtil(){};
}
