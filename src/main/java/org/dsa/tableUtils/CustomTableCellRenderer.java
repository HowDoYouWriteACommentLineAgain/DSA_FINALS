package org.dsa.tableUtils;

import org.dsa.utils.ColorUtil;
import org.dsa.utils.FontsUtil;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    private final Set<Integer> flaggedColumns;
    protected final boolean highlightHeaderCol;

    public CustomTableCellRenderer(Set<Integer> flaggedColumns, boolean highlightHeaderCol) {
        this.flaggedColumns = flaggedColumns;
        this.highlightHeaderCol = highlightHeaderCol;
    }

    public CustomTableCellRenderer()
    {
        super();
        this.flaggedColumns = Set.of();
        this.highlightHeaderCol = false;
    }

    public CustomTableCellRenderer(boolean bool)
    {
        super();
        this.flaggedColumns = Set.of();
        this.highlightHeaderCol = bool;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        label.setFont(FontsUtil.REGULAR);
        label.setForeground(ColorUtil.getPrimaryTextColor());
        label.setHorizontalAlignment(SwingConstants.LEFT);

        switch (value) {
            case null -> {
                label.setText("--N/A--");
                label.setFont(FontsUtil.REGULAR_BOLD);
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }
            case Object _ when highlightHeaderCol && column == 0 -> {
                label.setText(String.valueOf(value));
                label.setFont(FontsUtil.REGULAR_BOLD);
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }
            case Date d -> {
                LocalDate ld = d.toLocalDate();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                label.setText(fmt.format(ld));
                label.setFont(FontsUtil.REGULAR);
            }
            case String s when s.isBlank() ->{
                label.setText("--LEFT AS EMPTY--");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(FontsUtil.REGULAR_ITALIC);
                label.setForeground(ColorUtil.getSecondaryTextColor());
            }
            case String s -> {
                label.setText(s);
            }
            case Number n -> {
                label.setText(String.format("%.2f", n));
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                label.setFont(FontsUtil.REGULAR_ITALIC);
            }
            default -> {
                label.setFont(FontsUtil.REGULAR_BOLD);
                label.setText(String.valueOf(value));
            }
        }

        if (isSelected) {
            label.setBackground(table.getSelectionBackground());
            label.setForeground(table.getSelectionForeground());
        } else {
            if (row % 2 == 0) label.setBackground(ColorUtil.getBackgroundColorDarker());
            else label.setBackground(ColorUtil.getBackgroundColor());
        }


        // Outer: subtle border line
        Border lineBorder = BorderFactory.createMatteBorder(0, 0, 1, 1, ColorUtil.getBackgroundColorBrighter());

        // Inner: padding
        Border padding = BorderFactory.createEmptyBorder(5, 10, 10, 5);

        label.setBorder(BorderFactory.createCompoundBorder(lineBorder, padding));
        return label;
    }
}