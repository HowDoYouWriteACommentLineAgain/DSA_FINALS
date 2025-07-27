package org.dsa.UIPanels.TabularPanels;

import org.dsa.UIPanels.components.DatePicker;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseTablePanel extends AbstractTablePanel<Expense> {
//    private final GenericService<Expense, ? extends GenericDAO<Expense>> mainService;

    public ExpenseTablePanel(GenericService<Expense, ? extends GenericDAO<Expense>> service, String title) {
        super(new ExpenseTableModel(), title, service);
        if (service == null) throw new IllegalArgumentException("Service cannot be null");
//        this.mainService = service;
    }

//    @Override
//    protected void edit() {
//        Expense obj = getSelectedRowObject();
//        if (obj == null) return; // prevent double-triggered calls
//        showDialog(obj, false);
//    }

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

        ArrayList<Expense> data = new ArrayList<>(filter());

        Map<Integer, String> expenseMap = mainService.getIdNameExpenseCatMap();

        tableModel.setCategoryMap(expenseMap);

        tableModel.setData(data);
        table.clearSelection();
        revalidate();
        repaint();
    }

    @Override
    public void delete() {
        List<Expense> list = getSelectedRowObjects();
        Expense first = list.getFirst();
        if (list == null) return;
        if (JOptionPane.showConfirmDialog(this,
                "Delete "+first.name()+", and " + list.size()+ "more ... item permanently?", "Confirm deletion",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            for(Expense item : list) mainService.delete(item.id());
            refresh();
        }
    }

    @Override
    protected void showDialog(Expense obj, boolean isNew) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isNew ? "Add Budget" : "Edit Budget", true);
        dialog.setLayout(new GridLayout(0, 2));

        JTextField nameField = new JTextField(isNew || obj.name() == null? "" : obj.name());
        JComboBox<String> expenseCatSelect = new JComboBox<>(mainService.getNameIdExpenseCatMap().keySet().toArray(new String[0]));
        JTextField amountField = new JTextField(isNew ? "" : String.valueOf(obj.amount()));
        JTextField noteField = new JTextField(isNew || obj.note().isEmpty() ? "" : obj.note());
        DatePicker dateField = isNew ? new DatePicker() : new DatePicker(obj.date());


        dialog.add(new JLabel("Name:")); dialog.add(nameField);
        dialog.add(new JLabel("Category:")); dialog.add(expenseCatSelect);
        dialog.add(new JLabel("Amount:")); dialog.add(amountField);
        dialog.add(new JLabel("Note:")); dialog.add(noteField);
        dialog.add(new JLabel("Date:")); dialog.add(dateField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (!validateFields(nameField, expenseCatSelect, amountField, noteField, dateField)) {
                JOptionPane.showMessageDialog(dialog, "Please correct the highlighted fields.");
                return;
            }
            try {
                Expense newTransaction = new Expense(
                        0, // ID is managed by the DB
                        nameField.getText().trim(),
                        mainService.getNameIdExpenseCatMap().get(expenseCatSelect.getSelectedItem()),
                        Double.parseDouble(amountField.getText().trim()),
                        noteField.getText().trim(),
                        dateField.getFullDate()
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
    public List<Expense> filter() {
        List<Expense> data = mainService.getAll();
        Map<Integer, String> map = mainService.getIdNameExpenseCatMap();
        Date startDate = this.startDate.getFullDate();
        Date endDate = this.endDate.getFullDate();
        String searchQuery = this.searchField.getText().toLowerCase();
        return data.stream()
                .filter(d -> d.date() != null)
                .filter(d -> !d.date().after(endDate) && !d.date().before(startDate))
                .filter(d ->{
                        String name = d.name() != null ? d.name().toLowerCase() : "";
                        String note = d.note() != null ? d.note().toLowerCase() : "";
                        String cat = map.get(d.expense_cat()) != null ? map.get(d.expense_cat()) : "";
                        String query = searchQuery.toLowerCase();
                        return name.contains(query) || note.contains(query);
                })
                .collect(Collectors.toList());
    }

    @Override
    protected void appendStatusInfo(StringBuilder builder) {
        int[] rows = table.getSelectedRows();
        if (rows.length > 0) {
            double sum = 0;
            for (int row : rows) {
                Expense e = tableModel.getAt(row);
                sum += e.amount();
            }
            builder.append(" | Total Selected Amount: ₱").append(String.format("%.2f", sum));
        }
    }


    public boolean validateFields(JTextField name, JComboBox cat, JTextField amt, JTextField note, DatePicker date) {
        boolean valid = true;
        name.setBackground(ColorUtil.getBackgroundColor());
        cat.setBackground(ColorUtil.getBackgroundColor());
        amt.setBackground(ColorUtil.getBackgroundColor());
        note.setBackground(ColorUtil.getBackgroundColor());
        date.setBackground(ColorUtil.getBackgroundColor());

        if (name.getText().trim().isEmpty()) {
            name.setBackground(ColorUtil.WARNING_COLOR); valid = false;
        }

        try {
            if (mainService.getNameIdExpenseCatMap().get((String) cat.getSelectedItem()) == null) throw new IllegalArgumentException();
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

        try { Date.valueOf(date.getLocalDate()); }
        catch (Exception e) { date.setBackground(ColorUtil.WARNING_COLOR); valid = false; }

        return valid;
    }
}
