package org.dsa.UIPanels.TabularPanels;

import org.dsa.abstractions.AbstractTablePanel;
import org.dsa.abstractions.GenericDAO;
import org.dsa.abstractions.GenericService;
import org.dsa.models.objects.Budget;
import org.dsa.models.tableModels.BudgetTableModel;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class BudgetTablePanel extends AbstractTablePanel<Budget> {
    private final GenericService<Budget, ? extends GenericDAO<Budget>> service;

    private ArrayList<Integer> taken = new ArrayList<>();

    private String exceedMessage = "";

    public BudgetTablePanel(GenericService<Budget, ? extends GenericDAO<Budget>> service) {
        super(new BudgetTableModel());
        if (service == null) throw new IllegalArgumentException("Service cannot be null");
        this.service = service;
        loadData();
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

        ArrayList<Budget> data = service.getAll();

        for(Budget datum : data)
            addToTaken(datum);

        System.out.println("Available: " + getAvailable());
        System.out.println("Taken: " + taken);

        Map<Integer, String> expenseMap = service.getIdNameExpenseCatMap();

        System.out.println("BUDGETTABLEPANEL: Service returned as expenseMap: " + expenseMap);

        tableModel.setCategoryMap(expenseMap);

        tableModel.setData(data);

        table.clearSelection();
        revalidate();
        repaint();
    }

    @Override
    protected void edit() {
        Budget obj = getSelectedRowObject();
        if (obj == null) return; // prevent double-triggered calls
        showDialog(obj, false);
    }

    @Override
    public void delete() {
        Budget obj = getSelectedRowObject();
        if (obj == null) return;
        if (JOptionPane.showConfirmDialog(this, "Delete item permanently?", "Confirm deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            service.delete(obj.id());
            loadData();
        }
    }

    private ArrayList<String> getAvailable()
    {
        Map<Integer, String> allMaps = new HashMap<>(service.getIdNameExpenseCatMap());
        ArrayList<String> available = new ArrayList<>();

        for (int key : allMaps.keySet()) if(!taken.contains(key)) available.add(allMaps.get(key));
        return available;
    }

    private void addToTaken(Budget obj)
    {
        taken.add(obj.expense_cat());
    }


    @Override
    protected void showDialog(Budget obj, boolean isNew) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isNew ? "Add Budget" : "Edit Budget", true);
        dialog.setLayout(new GridLayout(0, 2));

        JComboBox<String> expenseCatSelect = new JComboBox<>(new Vector<>(getAvailable()));
        JTextField maxAmountField = new JTextField(isNew ? "" : String.valueOf(obj.max_amount()));
        JTextField goalAmountField = new JTextField(isNew ? "" : String.valueOf(obj.goal_amount()));
        JTextField startDateField = new JTextField(isNew || obj.start_date() == null ? "" : obj.start_date().toString());
        JTextField endDateField = new JTextField(isNew || obj.end_date() == null ? "" : obj.end_date().toString());

        dialog.add(new JLabel("Category:")); dialog.add(expenseCatSelect);
        dialog.add(new JLabel("Maximum Amount:")); dialog.add(maxAmountField);
        dialog.add(new JLabel("Goal Amount:")); dialog.add(goalAmountField);
        dialog.add(new JLabel("Start: (YYYY-MM-DD):")); dialog.add(startDateField);
        dialog.add(new JLabel("End: (YYYY-MM-DD):")); dialog.add(endDateField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (!validateFields(expenseCatSelect, maxAmountField, goalAmountField, startDateField, endDateField)) {
                JOptionPane.showMessageDialog(dialog, "Please correct the highlighted field. \n" + exceedMessage);
                return;
            }
            try {
                System.out.println("ExpenseCatBox:" + expenseCatSelect.getSelectedItem());
                System.out.println("Equivalent to db:" + service.getNameIdExpenseCatMap().get(expenseCatSelect.getSelectedItem()));
                Budget newTransaction = new Budget(
                        0, // ID is managed by the DB
                        service.getNameIdExpenseCatMap().get(expenseCatSelect.getSelectedItem()),
                        Double.parseDouble(maxAmountField.getText().trim()),
                        Double.parseDouble(goalAmountField.getText().trim()),
                        Date.valueOf(startDateField.getText().trim()),
                        Date.valueOf(endDateField.getText().trim())
                );

                if (isNew) service.insert(newTransaction);
                else service.edit(obj.id(), newTransaction);

                addToTaken(newTransaction);

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

    protected boolean validateFields(JComboBox cat, JTextField maxAmt, JTextField goalAmt, JTextField startDate, JTextField endDate) {
        boolean valid = true;
        exceedMessage = "";
        cat.setBackground(ColorUtil.BACKGROUND_COLOR);
        goalAmt.setBackground(ColorUtil.BACKGROUND_COLOR);
        maxAmt.setBackground(ColorUtil.BACKGROUND_COLOR);
        startDate.setBackground(ColorUtil.BACKGROUND_COLOR);
        endDate.setBackground(ColorUtil.BACKGROUND_COLOR);

        try {
            if (service.getNameIdExpenseCatMap().get((String) cat.getSelectedItem()) == null) throw new IllegalArgumentException();
        } catch (Exception e) {
            cat.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        try {
            double valueMax = Double.parseDouble(maxAmt.getText().trim());
            if (valueMax < 0) throw new NumberFormatException();
        } catch (Exception e) {
            maxAmt.setBackground(ColorUtil.WARNING_COLOR); valid = false;
        }

        try {
            double valueMax = Double.parseDouble(maxAmt.getText().trim());
            double valueGoal = Double.parseDouble(goalAmt.getText().trim());
            if (valueGoal < 0) throw new NumberFormatException();
            if (valueMax < valueGoal)
            {
                exceedMessage = "Goal cannot exceed Total.";
                throw new IllegalArgumentException();
            };
        } catch (Exception e) {
            goalAmt.setBackground(ColorUtil.WARNING_COLOR); valid = false;
        }

        try {
            Date.valueOf(startDate.getText().trim());
        }
        catch (Exception e) {
            startDate.setBackground(ColorUtil.WARNING_COLOR); valid = false;
        }

        try {
            Date start = Date.valueOf(startDate.getText().trim());
            Date end = Date.valueOf(endDate.getText().trim());
            if(start.compareTo(end) >= 0)
            {
                exceedMessage = "End date cannot be before start date";
                throw new IllegalArgumentException();
            }
        }
        catch (Exception e) {
            endDate.setBackground(ColorUtil.WARNING_COLOR); valid = false;
        }

        return valid;
    }
}
