package org.dsa.UIPanels.TabularPanels;

import org.dsa.UIPanels.components.DatePicker;
import org.dsa.UIPanels.components.ProgressBarRenderer;
import org.dsa.UIPanels.components.ProgressBarScrollPanel;
import org.dsa.abstractions.AbstractTablePanel;
import org.dsa.abstractions.GenericDAO;
import org.dsa.abstractions.GenericService;
import org.dsa.models.objects.Budget;
import org.dsa.models.objects.Expense;
import org.dsa.models.tableModels.BudgetTableModel;
import org.dsa.utils.ColorUtil;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class BudgetTablePanel extends AbstractTablePanel<Budget> {
    private final GenericService<Budget, ? extends GenericDAO<Budget>> mainService;
    private ProgressBarScrollPanel progressTable = new ProgressBarScrollPanel();
    private String exceedMessage = "";
    private ArrayList<Expense> secondaryData;

//    private JButton toggleProgressBar = new JButton("Edit");


    public BudgetTablePanel(GenericService<Budget, ? extends GenericDAO<Budget>> mainService, GenericService<Expense, ? extends GenericDAO<Expense>> helperService) {
        super(new BudgetTableModel(helperService.getAll()));
        secondaryData = helperService.getAll();
        if (mainService == null) throw new IllegalArgumentException("Service cannot be null");
        this.mainService = mainService;
//        centerPane.add(progressTable, BorderLayout.CENTER);

//        editButton.addActionListener(e -> edit());
    }

    @Override
    protected void setupTable()
    {
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(5).setCellRenderer(new ProgressBarRenderer());
        add(new JScrollPane(table));
    }

    @Override
    protected void add() {
        showDialog(null, true);
    }

    @Override
    protected void loadData() {
        if (mainService == null) {
            System.err.println("Service is null during loadData()");
            return;
        }


//        for(Budget datum : data) addToTaken(datum);
        Map<Integer, String> expenseMap = mainService.getIdNameExpenseCatMap();

        ArrayList<Budget> data = new ArrayList<>(filter());
        tableModel.setCategoryMap(expenseMap);
        tableModel.setData(data);
        progressTable.setBudgetList(data);
        progressTable.setExpenseList(secondaryData);
        progressTable.setIdNameMap(expenseMap);
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
            mainService.delete(obj.id());
            refresh();
        }
    }

    @Override
    protected void showDialog(Budget obj, boolean isNew) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isNew ? "Add Budget" : "Edit Budget", true);
        dialog.setLayout(new GridLayout(0, 2));

//        JComboBox<String> expenseCatSelect = new JComboBox<>(new Vector<>(getAvailable(isNew ? -1 : obj.expense_cat())));
        JComboBox<String> expenseCatSelect = new JComboBox<>(new Vector<>(mainService.getNameIdExpenseCatMap().keySet()));
        JTextField maxAmountField = new JTextField(isNew ? "" : String.valueOf(obj.max_amount()));
        JTextField goalAmountField = new JTextField(isNew ? "" : String.valueOf(obj.goal_amount()));
        DatePicker startDateField = isNew ? new DatePicker() : new DatePicker(obj.start_date());
        DatePicker endDateField = isNew ? new DatePicker() : new DatePicker(obj.end_date());

        dialog.add(new JLabel("Category:")); dialog.add(expenseCatSelect);
        dialog.add(new JLabel("Maximum Amount:")); dialog.add(maxAmountField);
        dialog.add(new JLabel("Goal Amount:")); dialog.add(goalAmountField);
        dialog.add(new JLabel("Start:")); dialog.add(startDateField);
        dialog.add(new JLabel("End:")); dialog.add(endDateField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (!validateFields(expenseCatSelect, maxAmountField, goalAmountField, startDateField, endDateField)) {
                JOptionPane.showMessageDialog(dialog, "Please correct the highlighted field. \n" + exceedMessage);
                return;
            }
            try {
                Budget newTransaction = new Budget(
                        0, // ID is managed by the DB
                        mainService.getNameIdExpenseCatMap().get(expenseCatSelect.getSelectedItem()),
                        Double.parseDouble(maxAmountField.getText().trim()),
                        Double.parseDouble(goalAmountField.getText().trim()),
                        startDateField.getFullDate(),
                        endDateField.getFullDate()
                );

                if (isNew) mainService.insert(newTransaction);
                else mainService.edit(obj.id(), newTransaction);

                refresh();
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
    public List<Budget> filter() {
        List<Budget> data = mainService.getAll();

        Date startDate = this.startDate.getFullDate();
        Date endDate = this.endDate.getFullDate();
        String searchQuery = this.searchField.getText().toLowerCase();
        return data.stream()
                .filter(d -> d.start_date() != null && d.end_date() != null)
                .filter(d -> !d.start_date().after(endDate) && !d.end_date().before(startDate))
                .filter(d -> mainService.getIdNameExpenseCatMap().get(d.expense_cat()).contains(searchQuery.toLowerCase()))
                .collect(Collectors.toList());
    }

    protected boolean validateFields(JComboBox cat, JTextField maxAmt, JTextField goalAmt, DatePicker startDate, DatePicker endDate) {
        boolean valid = true;
        exceedMessage = "";
        cat.setBackground(ColorUtil.BACKGROUND_COLOR);
        goalAmt.setBackground(ColorUtil.BACKGROUND_COLOR);
        maxAmt.setBackground(ColorUtil.BACKGROUND_COLOR);
        startDate.setBackground(ColorUtil.BACKGROUND_COLOR);
        endDate.setBackground(ColorUtil.BACKGROUND_COLOR);

        try {
            if (mainService.getNameIdExpenseCatMap().get((String) cat.getSelectedItem()) == null) throw new IllegalArgumentException();
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
            Date.valueOf(startDate.getFullDate().toLocalDate());
        }
        catch (Exception e) {
            startDate.setBackground(ColorUtil.WARNING_COLOR);
            endDate.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        try {
            Date start = Date.valueOf(startDate.getFullDate().toLocalDate());
            Date end = Date.valueOf(endDate.getFullDate().toLocalDate());
            if(start.compareTo(end) >= 0)
            {
                exceedMessage = "End date cannot be before start date";
                throw new IllegalArgumentException();
            }
        }
        catch (Exception e) {
            endDate.setBackground(ColorUtil.WARNING_COLOR);
            endDate.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        return valid;
    }
}
