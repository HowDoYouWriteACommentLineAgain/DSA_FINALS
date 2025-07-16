package org.dsa.abstractions;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

    protected abstract void add();

    protected abstract void loadData();

    protected abstract void edit();

    public abstract void delete();

    protected abstract void showDialog(O obj, boolean isNew);

    public void refresh() {loadData();}

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
        if (row >= 0) return getAt(row);
        else {
            JOptionPane.showMessageDialog(this, "Please select a row first");
            return null;
        }
    }

//    protected abstract boolean validateFields(JTextField name, JComboBox cat, JTextField amt, JTextField note, JTextField date);
//    protected abstract boolean validateFields(JComboBox cat, JTextField goalAmt, JTextField maxAmt, JTextField startDate, JTextField endDate);
//    protected boolean validateDateField()
//    {
//
//    }
}
