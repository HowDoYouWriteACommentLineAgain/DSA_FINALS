package org.dsa.UIPanels.components;

import org.dsa.utils.ColorUtil;
import org.dsa.utils.FontsUtil;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class ProgressBarRenderer extends JProgressBar implements TableCellRenderer {
    public ProgressBarRenderer() {
        setStringPainted(true);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        int progress = (int) Math.round((double)value * 100);
        setMaximum(100);
        setValue(progress);

        setForeground(ColorUtil.getPrimaryTextColor());
        setFont(FontsUtil.HEADER_FONT_BOLD);

        if(progress > 100) {
          setForeground(ColorUtil.OVER_BUDGET_COLOR);
        } else if (progress >= 80) {
            setForeground(ColorUtil.OVER_THRESHOLD_COLOR);
        } else if (progress >= 70) {
            setForeground(ColorUtil.WARNING_COLOR);
        } else {
            setForeground(ColorUtil.SUCCESS_COLOR); // dark green
        }

        return this;
    }
}
