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
import org.dsa.tableUtils.CustomTableCellRenderer;
import org.dsa.utils.ColorUtil;
import org.dsa.utils.FontsUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class BudgetTablePanel extends AbstractTablePanel<Budget> {
    private final ProgressBarScrollPanel progressTable = new ProgressBarScrollPanel();
    private String exceedMessage = "";
    private final ArrayList<Expense> secondaryData;

    public BudgetTablePanel(GenericService<Budget, ? extends GenericDAO<Budget>> mainService,
                            GenericService<Expense, ? extends GenericDAO<Expense>> helperService,
                            String title) {
        super(new BudgetTableModel(helperService.getAll()), title, mainService);
        this.secondaryData = helperService.getAll();
    }

    @Override
    protected void setupTable() {
        super.setupTable();
        table.getColumnModel().getColumn(5).setCellRenderer(new ProgressBarRenderer());
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
    }

    @Override
    protected void add() {
        showDialog(null, true);
    }

    @Override
    protected void loadData() {
        if (mainService == null) return;

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
    public void delete() {
        List<Budget> list = getSelectedRowObjects();
        if (list == null || list.isEmpty()) return;
        if (JOptionPane.showConfirmDialog(this,
                "Delete selected budget(s)?", "Confirm deletion",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            for (Budget item : list) mainService.delete(item.id());
            refresh();
        }
    }

    @Override
    protected void showDialog(Budget obj, boolean isNew) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isNew ? "Add Budget" : "Edit Budget", true);
        dialog.setUndecorated(false);
        dialog.setBackground(ColorUtil.getBackgroundColor());

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(ColorUtil.getBackgroundColorBrighter());
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ColorUtil.getBorderColor(), 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> expenseCatSelect = new JComboBox<>(new Vector<>(mainService.getNameIdExpenseCatMap().keySet()));
        JTextField maxAmountField = new JTextField(isNew ? "" : String.valueOf(obj.max_amount()));
        JTextField goalAmountField = new JTextField(isNew ? "" : String.valueOf(obj.goal_amount()));
        DatePicker startDateField = isNew ? new DatePicker() : new DatePicker(obj.start_date());
        DatePicker endDateField = isNew ? new DatePicker() : new DatePicker(obj.end_date());

        Font labelFont = FontsUtil.MANROPE_BOLD.deriveFont(15f);
        Font inputFont = FontsUtil.MANROPE_REGULAR.deriveFont(14f);

        expenseCatSelect.setFont(inputFont);
        maxAmountField.setFont(inputFont);
        goalAmountField.setFont(inputFont);
        startDateField.setFont(inputFont);
        endDateField.setFont(inputFont);

        int row = 0;
        contentPanel.add(makeLabel("Category:", labelFont), setGbc(gbc, 0, row));
        contentPanel.add(expenseCatSelect, setGbc(gbc, 1, row++));
        contentPanel.add(makeLabel("Maximum Amount:", labelFont), setGbc(gbc, 0, row));
        contentPanel.add(maxAmountField, setGbc(gbc, 1, row++));
        contentPanel.add(makeLabel("Goal Amount:", labelFont), setGbc(gbc, 0, row));
        contentPanel.add(goalAmountField, setGbc(gbc, 1, row++));
        contentPanel.add(makeLabel("Start Date:", labelFont), setGbc(gbc, 0, row));
        contentPanel.add(startDateField, setGbc(gbc, 1, row++));
        contentPanel.add(makeLabel("End Date:", labelFont), setGbc(gbc, 0, row));
        contentPanel.add(endDateField, setGbc(gbc, 1, row++));

        JButton saveButton = new JButton("Save");
        saveButton.setFont(inputFont);
        saveButton.addActionListener(e -> {
            if (!validateFields(expenseCatSelect, maxAmountField, goalAmountField, startDateField, endDateField)) {
                JOptionPane.showMessageDialog(dialog, "Please correct the highlighted fields.\n" + exceedMessage);
                return;
            }
            try {
                Budget newBudget = new Budget(
                        0,
                        mainService.getNameIdExpenseCatMap().get(expenseCatSelect.getSelectedItem()),
                        Double.parseDouble(maxAmountField.getText().trim()),
                        Double.parseDouble(goalAmountField.getText().trim()),
                        startDateField.getFullDate(),
                        endDateField.getFullDate()
                );
                if (isNew) mainService.insert(newBudget);
                else mainService.edit(obj.id(), newBudget);
                refresh();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Unexpected error: " + ex.getMessage());
            }
        });

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = row++;
        contentPanel.add(saveButton, gbc);

        dialog.setContentPane(contentPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private GridBagConstraints setGbc(GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        return gbc;
    }

    private JLabel makeLabel(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(ColorUtil.getPrimaryTextColor());
        return lbl;
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
                .filter(d -> mainService.getIdNameExpenseCatMap().get(d.expense_cat()).toLowerCase().contains(searchQuery))
                .collect(Collectors.toList());
    }

    @Override
    protected void appendStatusInfo(StringBuilder builder) {
        int[] rows = table.getSelectedRows();
        if (rows.length > 0) {
            double mSum = 0, gSum = 0;
            for (int row : rows) {
                Budget e = tableModel.getAt(row);
                mSum += e.max_amount();
                gSum += e.goal_amount();
            }
            builder.append(" | Total Max: ₱").append(String.format("%.2f", mSum));
            builder.append(" | Total Goal: ₱").append(String.format("%.2f", gSum));
        }
    }

    protected boolean validateFields(JComboBox<?> cat, JTextField maxAmt, JTextField goalAmt, DatePicker startDate, DatePicker endDate) {
        boolean valid = true;
        exceedMessage = "";
        cat.setBackground(ColorUtil.getBackgroundColor());
        goalAmt.setBackground(ColorUtil.getBackgroundColor());
        maxAmt.setBackground(ColorUtil.getBackgroundColor());
        startDate.setBackground(ColorUtil.getBackgroundColor());
        endDate.setBackground(ColorUtil.getBackgroundColor());

        try {
            if (mainService.getNameIdExpenseCatMap().get((String) cat.getSelectedItem()) == null)
                throw new IllegalArgumentException();
        } catch (Exception e) {
            cat.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        try {
            double max = Double.parseDouble(maxAmt.getText().trim());
            if (max < 0) throw new NumberFormatException();
        } catch (Exception e) {
            maxAmt.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        try {
            double max = Double.parseDouble(maxAmt.getText().trim());
            double goal = Double.parseDouble(goalAmt.getText().trim());
            if (goal < 0 || goal > max) {
                exceedMessage = "Goal cannot exceed Max.";
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            goalAmt.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        try {
            Date.valueOf(startDate.getFullDate().toLocalDate());
        } catch (Exception e) {
            startDate.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        try {
            if (startDate.getFullDate().compareTo(endDate.getFullDate()) >= 0) {
                exceedMessage = "End date must be after Start date.";
                endDate.setBackground(ColorUtil.WARNING_COLOR);
                valid = false;
            }
        } catch (Exception e) {
            endDate.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        return valid;
    }
}
