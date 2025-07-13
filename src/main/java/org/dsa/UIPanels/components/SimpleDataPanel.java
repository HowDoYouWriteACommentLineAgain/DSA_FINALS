package org.dsa.UIPanels.TabularPanels;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public abstract class SimpleDataPanel extends JPanel {
    JTable table;
    JPanel filterPanel;
    JDialog editPanel;
    JTextField[] fields;
    protected AbstractTableModel model;

    public SimpleDataPanel(AbstractTableModel model) {
        this.model = model;

        setLayout(new BorderLayout());
        setUpFilterPanel();
        setupTable();
        setupEditPanel();
        generateEditFields(model.getColumnCount()); // Initial generation
        setupSelectionListener();
    }

    private void setUpFilterPanel()
    {
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(new JTextField(30));
        add(filterPanel, BorderLayout.NORTH);
    }

    private void setupTable()
    {
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void setupEditPanel()
    {
        JFrame frame = (JFrame)this.getTopLevelAncestor();
        editPanel = new JDialog(frame, "Please Insert into Fields", true);
        editPanel.setLocationRelativeTo(frame);
        editPanel.setLayout(new GridLayout(0, 2)); // 2 cols: label + input
        editPanel.setVisible(false);
//        add(editPanel);
    }

    private void generateEditFields(int cols) {
        editPanel.removeAll();
        fields = new JTextField[cols];

        for (int i = 0; i < cols; i++) {
            editPanel.add(new JLabel(model.getColumnName(i)));
            JTextField field = new JTextField();
            fields[i] = field;
            editPanel.add(field);
        }

        revalidate();
        repaint();
    }

    private void setupSelectionListener() {
        table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selected = table.getSelectedRow();
                if (selected >= 0) {
                    for (int i = 0; i < fields.length; i++) {
                        Object value = table.getValueAt(selected, i);
                        fields[i].setText(value != null ? value.toString() : "");
                    }
                }
            }
        });
    }

}
