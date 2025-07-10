package org.dsa.UIPanels.TabularPanels;

import org.dsa.abstractions.AbstractTablePanel;
import org.dsa.abstractions.GenericDAO;
import org.dsa.abstractions.GenericService;
import org.dsa.models.objects.Income;
import org.dsa.models.tableModels.IncomeTableModel;
import org.dsa.utils.ColorUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

public class IncomeTablePanel extends AbstractTablePanel<Income> {
    private final GenericService<Income, ? extends GenericDAO<Income>> service;

    public IncomeTablePanel(GenericService<Income, ? extends GenericDAO<Income>> service) {
        super(new IncomeTableModel());
        if (service == null) throw new IllegalArgumentException("Service cannot be null");
        this.service = service;
        loadData();
    }

    @Override
    public void delete() {
        Income obj = getSelectedRowObject();
        if (obj != null && JOptionPane.showConfirmDialog(this, "Delete item permanently?", "Confirm deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            service.delete(obj.id());
            loadData();
        }
    }

    @Override
    public void edit() {
        Income obj = getSelectedRowObject();
        if (obj != null) showDialog(obj, false);
    }

    @Override
    public void add() {
        showDialog(null, true);
    }

    public void showDialog(Income obj, boolean isNew) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isNew ? "Add Income" : "Edit Income", true);
        dialog.setLayout(new GridLayout(0, 2));

        JTextField nameField = new JTextField(isNew || obj.name() == null? "" : obj.name());
        JTextField incomeCatField = new JTextField(isNew ? "" : String.valueOf(obj.income_cat()));
        JTextField amountField = new JTextField(isNew ? "" : String.valueOf(obj.amount()));
        JTextField noteField = new JTextField(isNew || obj.note().isEmpty() ? "" : obj.note());
        JTextField dateField = new JTextField(isNew || obj.date() == null ? "" : obj.date().toString());

        dialog.add(new JLabel("Name:")); dialog.add(nameField);
        dialog.add(new JLabel("Income Category ID:")); dialog.add(incomeCatField);
        dialog.add(new JLabel("Amount:")); dialog.add(amountField);
        dialog.add(new JLabel("Note:")); dialog.add(noteField);
        dialog.add(new JLabel("Date (YYYY-MM-DD):")); dialog.add(dateField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (!validateFields(nameField, incomeCatField, amountField, noteField, dateField)) {
                JOptionPane.showMessageDialog(dialog, "Please correct the highlighted fields.");
                return;
            }
            try {
                Income newIncome = new Income(
                        0, // ID is managed by the DB
                        nameField.getText().trim(),
                        Integer.parseInt(incomeCatField.getText().trim()),
                        Double.parseDouble(amountField.getText().trim()),
                        noteField.getText().trim(),
                        Date.valueOf(dateField.getText().trim())
                );

                if (isNew) service.insert(newIncome);
                else service.edit(obj.id(), newIncome);

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

    @Override
    public void loadData() {
        if (service == null) {
            System.err.println("Service is null during loadData()");
            return;
        }

        ArrayList<Income> data = service.getAll();

        Map<Integer, String> income_map = service.getIncomeCatMap();

        System.out.println("INCOMETABLEPANEL: Service returned as incomeMap: " + income_map);

        tableModel.setCategoryMap(income_map);

        tableModel.setData(data);
        revalidate();
        repaint();
    }

    private boolean validateFields(JTextField name, JTextField cat, JTextField amt, JTextField note, JTextField date) {
        boolean valid = true;
        name.setBackground(ColorUtil.BACKGROUND_COLOR);
        cat.setBackground(ColorUtil.BACKGROUND_COLOR);
        amt.setBackground(ColorUtil.BACKGROUND_COLOR);
        note.setBackground(ColorUtil.BACKGROUND_COLOR);
        date.setBackground(ColorUtil.BACKGROUND_COLOR);

        if (name.getText().trim().isEmpty()) {
            name.setBackground(ColorUtil.WARNING_COLOR); valid = false;
        }

        try { Integer.parseInt(cat.getText().trim()); }
        catch (Exception e) { cat.setBackground(ColorUtil.WARNING_COLOR); valid = false; }

        try {
            double value = Double.parseDouble(amt.getText().trim());
            if (value < 0) throw new NumberFormatException();
        } catch (Exception e) {
            amt.setBackground(ColorUtil.WARNING_COLOR); valid = false;
        }

//        if (note.getText().trim().isEmpty()) {
//            note.setBackground(ColorUtil.WARNING_COLOR); valid = false;
//        }

        try { Date.valueOf(date.getText().trim()); }
        catch (Exception e) { date.setBackground(ColorUtil.WARNING_COLOR); valid = false; }

        return valid;
    }
}
