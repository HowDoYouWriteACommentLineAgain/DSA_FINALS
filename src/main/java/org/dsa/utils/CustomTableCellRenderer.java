package org.dsa.utils;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    private final Set<Integer> flaggedColumns;
    private final boolean highlightHeaderCol;

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
        label.setForeground(ColorUtil.PRIMARY_TEXT_COLOR);
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
                label.setForeground(ColorUtil.SECONDARY_TEXT_COLOR);
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
        return label;
    }
}