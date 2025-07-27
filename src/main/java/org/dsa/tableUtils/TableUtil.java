package org.dsa.tableUtils;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.Component;

public class TableUtil {
    public static void adjustColumnWidths(JTable table) {
        TableModel model = table.getModel();
        TableColumnModel columnModel = table.getColumnModel();
        for (int col = 0; col < columnModel.getColumnCount(); col++) {
            int maxWidth = 75; // min width
            TableColumn column = columnModel.getColumn(col);
            TableCellRenderer renderer = column.getHeaderRenderer();
            if (renderer == null) renderer = table.getTableHeader().getDefaultRenderer();

            Component comp = renderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, 0, col);
            maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);

            for (int row = 0; row < model.getRowCount(); row++) {
                renderer = table.getCellRenderer(row, col);
                comp = renderer.getTableCellRendererComponent(table, model.getValueAt(row, col), false, false, row, col);
                maxWidth = Math.max(comp.getPreferredSize().width + 10, maxWidth); // +10 padding
            }
            column.setPreferredWidth(maxWidth);
        }
    }

}
