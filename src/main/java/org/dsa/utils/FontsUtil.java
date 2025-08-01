package org.dsa.utils;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class FontsUtil {
    public static final Font BASE = resolveBase();
    public static final Font TITLE_FONT = BASE.deriveFont(Font.BOLD, 38f);
    public static final Font HEADER_FONT = BASE.deriveFont(Font.BOLD, 26f);
    public static final Font HEADER_FONT_BOLD = BASE.deriveFont(Font.BOLD, HEADER_FONT.getSize());
    public static final Font REGULAR = BASE.deriveFont(Font.PLAIN, 18f);
    public static final Font REGULAR_BOLD = BASE.deriveFont(Font.BOLD, REGULAR.getSize());
    public static final Font REGULAR_ITALIC = BASE.deriveFont(Font.ITALIC, REGULAR.getSize());
    public static final Font FIELD_FONT = BASE.deriveFont(Font.PLAIN, REGULAR.getSize());

    public static final Font MANROPE_REGULAR = loadFont("/fonts/Manrope-Regular.ttf", 18f, Font.PLAIN);
    public static final Font MANROPE_BOLD = loadFont("/fonts/Manrope-Bold.ttf", 18f, Font.BOLD);
    public static final Font MEDIUM_FONT = MANROPE_REGULAR.deriveFont(15f);

    private static Font resolveBase() {
        Font lafFont = UIManager.getFont("Label.font");
        return (lafFont != null) ? lafFont : new JLabel().getFont();
    }

    private static Font loadFont(String path, float size, int style) {
        try (InputStream is = FontsUtil.class.getResourceAsStream(path)) {
            if (is == null) throw new RuntimeException("Font not found: " + path);
            Font base = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(base);
            return base.deriveFont(style, size);
        } catch (Exception e) {
            System.err.println("Failed to load font: " + path + " → " + e.getMessage());
            return BASE.deriveFont(style, size); // Fallback
        }
    }

    private FontsUtil() {}
}
