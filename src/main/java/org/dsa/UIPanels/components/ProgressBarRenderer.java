package org.dsa.UIPanels.components;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
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

        if (progress >= 90) {
            setForeground(Color.RED);
        } else if (progress >= 60) {
            setForeground(Color.ORANGE);
        } else {
            setForeground(new Color(0, 128, 0)); // dark green
        }

        return this;
    }
}
