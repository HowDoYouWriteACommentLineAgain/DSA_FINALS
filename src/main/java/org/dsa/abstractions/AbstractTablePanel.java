package org.dsa.abstractions;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractTablePanel<O> extends JPanel {
    protected JTable table;
    protected GenericTableModel<O> tableModel;

    protected ArrayList<JTextField> textFields;

    public AbstractTablePanel(GenericTableModel<O> tableModel) {
        this.tableModel = tableModel;
        setLayout(new BorderLayout());
        setupTable();
        setupControls();
    }

    protected abstract void edit();

    protected abstract void add();

    protected abstract void loadData();

    public abstract void delete();

    public void refresh() {loadData();}

    protected abstract void showDialog(O obj, boolean isNew);

    protected void setupTable() {
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    protected void setupControls() {
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadData());

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> add());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> delete());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> edit());

        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0)
                edit();
        });

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(refresh);
        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        add(panel, BorderLayout.SOUTH);
    }

    protected O getAt(int row) {
        return tableModel.getAt(row);
    }

    protected O getSelectedRowObject() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            return getAt(row);

        } else {
            JOptionPane.showMessageDialog(this, "Please select a row first");
            return null;
        }
    }

//    protected boolean validateDateField()
//    {
//
//    }
}
