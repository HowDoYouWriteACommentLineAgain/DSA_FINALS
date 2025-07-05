package org.dsa.UIPanels.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class SimpleDataPanel extends JPanel {
    JTable table;
    DefaultTableModel model;
    JPanel editPanel;
    JTextField[] fields;

    public SimpleDataPanel() {
        setLayout(new BorderLayout());

        // Filter panel (top)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(new JTextField(15));
        add(filterPanel, BorderLayout.NORTH);

        // Table panel (center)
        String[] columns = {"ID", "Name", "Email"};
        Object[][] data = {
                {1, "Alice", "alice@example.com"},
                {2, "Bob", "bob@example.com"}
        };

        model = new DefaultTableModel(data, columns);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Edit panel (bottom)
        editPanel = new JPanel();
        editPanel.setLayout(new GridLayout(0, 2)); // 2 cols: label + input
        add(editPanel, BorderLayout.SOUTH);

        generateEditFields(); // Initial generation
        setupSelectionListener();
    }

    private void generateEditFields() {
        editPanel.removeAll();
        int cols = model.getColumnCount();
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
