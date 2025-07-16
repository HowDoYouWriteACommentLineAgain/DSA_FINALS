package org.dsa.UIPanels.TabularPanels;

import org.dsa.abstractions.AbstractTablePanel;
import org.dsa.abstractions.GenericDAO;
import org.dsa.abstractions.GenericService;
import org.dsa.models.objects.Expense;
import org.dsa.models.tableModels.ExpenseTableModel;
import org.dsa.utils.ColorUtil;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Frame;
import java.awt.GridLayout;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

public class ExpenseTablePanel extends AbstractTablePanel<Expense> {
    private final GenericService<Expense, ? extends GenericDAO<Expense>> service;

    public ExpenseTablePanel(GenericService<Expense, ? extends GenericDAO<Expense>> service) {
        super(new ExpenseTableModel());
        if (service == null) throw new IllegalArgumentException("Service cannot be null");
        this.service = service;
        loadData();
    }

    @Override
    protected void edit() {
        Expense obj = getSelectedRowObject();
        if (obj == null) return; // prevent double-triggered calls
        showDialog(obj, false);
    }

    @Override
    protected void add() {
        showDialog(null, true);
    }

    @Override
    protected void loadData() {
        if (service == null) {
            System.err.println("Service is null during loadData()");
            return;
        }

        ArrayList<Expense> data = service.getAll();

        Map<Integer, String> expenseMap = service.getIdNameExpenseCatMap();

        System.out.println("INCOMETABLEPANEL: Service returned as expenseMap: " + expenseMap);

        tableModel.setCategoryMap(expenseMap);

        tableModel.setData(data);

        table.clearSelection();
        revalidate();
        repaint();
    }

    @Override
    public void delete() {
        Expense obj = getSelectedRowObject();
        if (obj == null) return;
        if (JOptionPane.showConfirmDialog(this, "Delete item permanently?", "Confirm deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            service.delete(obj.id());
            loadData();
        }
    }

    @Override
    protected void showDialog(Expense obj, boolean isNew) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isNew ? "Add Budget" : "Edit Budget", true);
        dialog.setLayout(new GridLayout(0, 2));

        JTextField nameField = new JTextField(isNew || obj.name() == null? "" : obj.name());
        JComboBox<String> expenseCatSelect = new JComboBox<>(service.getNameIdExpenseCatMap().keySet().toArray(new String[0]));
        JTextField amountField = new JTextField(isNew ? "" : String.valueOf(obj.amount()));
        JTextField noteField = new JTextField(isNew || obj.note().isEmpty() ? "" : obj.note());
        JTextField dateField = new JTextField(isNew || obj.date() == null ? "" : obj.date().toString());


        dialog.add(new JLabel("Name:")); dialog.add(nameField);
        dialog.add(new JLabel("Category:")); dialog.add(expenseCatSelect);
        dialog.add(new JLabel("Amount:")); dialog.add(amountField);
        dialog.add(new JLabel("Note:")); dialog.add(noteField);
        dialog.add(new JLabel("Date (YYYY-MM-DD):")); dialog.add(dateField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (!validateFields(nameField, expenseCatSelect, amountField, noteField, dateField)) {
                JOptionPane.showMessageDialog(dialog, "Please correct the highlighted fields.");
                return;
            }
            try {
                System.out.println("ExpenseCatBox:" + expenseCatSelect.getSelectedItem());
                System.out.println("Equivalent to db:" + service.getNameIdExpenseCatMap().get(expenseCatSelect.getSelectedItem()));
                Expense newTransaction = new Expense(
                        0, // ID is managed by the DB
                        nameField.getText().trim(),
                        service.getNameIdExpenseCatMap().get(expenseCatSelect.getSelectedItem()),
                        Double.parseDouble(amountField.getText().trim()),
                        noteField.getText().trim(),
                        Date.valueOf(dateField.getText().trim())
                );

                if (isNew) service.insert(newTransaction);
                else service.edit(obj.id(), newTransaction);

                loadData();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Unexpected error: " + ex.getMessage());
            }
        });

        dialog.add(new JLabel());
        dialog.add(saveButton);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    public boolean validateFields(JTextField name, JComboBox cat, JTextField amt, JTextField note, JTextField date) {
        boolean valid = true;
        name.setBackground(ColorUtil.BACKGROUND_COLOR);
        cat.setBackground(ColorUtil.BACKGROUND_COLOR);
        amt.setBackground(ColorUtil.BACKGROUND_COLOR);
        note.setBackground(ColorUtil.BACKGROUND_COLOR);
        date.setBackground(ColorUtil.BACKGROUND_COLOR);

        if (name.getText().trim().isEmpty()) {
            name.setBackground(ColorUtil.WARNING_COLOR); valid = false;
        }

        try {
            if (service.getNameIdExpenseCatMap().get((String) cat.getSelectedItem()) == null) throw new IllegalArgumentException();
        } catch (Exception e) {
            cat.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        try {
            double value = Double.parseDouble(amt.getText().trim());
            if (value < 0) throw new NumberFormatException();
        } catch (Exception e) {
            amt.setBackground(ColorUtil.WARNING_COLOR); valid = false;
        }

        try { Date.valueOf(date.getText().trim()); }
        catch (Exception e) { date.setBackground(ColorUtil.WARNING_COLOR); valid = false; }

        return valid;
    }
}
