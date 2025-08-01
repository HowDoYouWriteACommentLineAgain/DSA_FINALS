package org.dsa.UIPanels.TabularPanels;

import org.dsa.UIPanels.components.DatePicker;
import org.dsa.abstractions.AbstractTablePanel;
import org.dsa.abstractions.GenericDAO;
import org.dsa.abstractions.GenericService;
import org.dsa.models.objects.Expense;
import org.dsa.models.tableModels.ExpenseTableModel;
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
import java.util.stream.Collectors;

public class ExpenseTablePanel extends AbstractTablePanel<Expense> {

    public ExpenseTablePanel(GenericService<Expense, ? extends GenericDAO<Expense>> service, String title) {
        super(new ExpenseTableModel(), title, service);
        if (service == null) throw new IllegalArgumentException("Service cannot be null");
    }

    @Override
    public void delete() {
        List<Expense> list = getSelectedRowObjects();
        if (list == null || list.isEmpty()) return;
        Expense first = list.getFirst();
        if (JOptionPane.showConfirmDialog(this,
                "Delete " + first.name() + ", and " + list.size() + " more ... item permanently?", "Confirm deletion",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            for (Expense item : list) mainService.delete(item.id());
            refresh();
        }
    }

    @Override
    protected void add() {
        showDialog(null, true);
    }

    @Override
    protected void showDialog(Expense obj, boolean isNew) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isNew ? "Add Expense" : "Edit Expense", true);
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

        JTextField nameField = new JTextField(isNew || obj.name() == null ? "" : obj.name());
        JComboBox<String> expenseCatSelect = new JComboBox<>(mainService.getNameIdExpenseCatMap().keySet().toArray(new String[0]));
        JTextField amountField = new JTextField(isNew ? "" : String.valueOf(obj.amount()));
        JTextField noteField = new JTextField(isNew || obj.note() == null ? "" : obj.note());
        DatePicker dateField = isNew ? new DatePicker() : new DatePicker(obj.date());

        Font labelFont = FontsUtil.MANROPE_BOLD.deriveFont(15f);
        Font inputFont = FontsUtil.MANROPE_REGULAR.deriveFont(14f);

        nameField.setFont(inputFont);
        expenseCatSelect.setFont(inputFont);
        amountField.setFont(inputFont);
        noteField.setFont(inputFont);
        dateField.setFont(inputFont);

        int row = 0;
        contentPanel.add(makeLabel("Name:", labelFont), setGbc(gbc, 0, row));
        contentPanel.add(nameField, setGbc(gbc, 1, row++));
        contentPanel.add(makeLabel("Category:", labelFont), setGbc(gbc, 0, row));
        contentPanel.add(expenseCatSelect, setGbc(gbc, 1, row++));
        contentPanel.add(makeLabel("Amount:", labelFont), setGbc(gbc, 0, row));
        contentPanel.add(amountField, setGbc(gbc, 1, row++));
        contentPanel.add(makeLabel("Note:", labelFont), setGbc(gbc, 0, row));
        contentPanel.add(noteField, setGbc(gbc, 1, row++));
        contentPanel.add(makeLabel("Date:", labelFont), setGbc(gbc, 0, row));
        contentPanel.add(dateField, setGbc(gbc, 1, row++));

        JButton saveButton = new JButton("Save");
        saveButton.setFont(inputFont);
        saveButton.addActionListener(e -> {
            if (!validateFields(nameField, expenseCatSelect, amountField, noteField, dateField)) {
                JOptionPane.showMessageDialog(dialog, "Please correct the highlighted fields.");
                return;
            }
            try {
                Expense newExpense = new Expense(
                        0,
                        nameField.getText().trim(),
                        mainService.getNameIdExpenseCatMap().get(expenseCatSelect.getSelectedItem()),
                        Double.parseDouble(amountField.getText().trim()),
                        noteField.getText().trim(),
                        dateField.getFullDate()
                );

                if (isNew) mainService.insert(newExpense);
                else mainService.edit(obj.id(), newExpense);

                refresh();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Unexpected error: " + ex.getMessage());
            }
        });

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = row;
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
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(ColorUtil.getPrimaryTextColor());
        return label;
    }

    @Override
    public void loadData() {
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
    public List<Expense> filter() {
        List<Expense> data = mainService.getAll();
        Map<Integer, String> map = mainService.getIdNameExpenseCatMap();
        Date startDate = this.startDate.getFullDate();
        Date endDate = this.endDate.getFullDate();
        String searchQuery = this.searchField.getText().toLowerCase();
        return data.stream()
                .filter(d -> d.date() != null)
                .filter(d -> !d.date().after(endDate) && !d.date().before(startDate))
                .filter(d -> {
                    String name = d.name() != null ? d.name().toLowerCase() : "";
                    String note = d.note() != null ? d.note().toLowerCase() : "";
                    String cat = map.get(d.expense_cat()) != null ? map.get(d.expense_cat()) : "";
                    return name.contains(searchQuery) || note.contains(searchQuery) || cat.contains(searchQuery);
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

    public boolean validateFields(JTextField name, JComboBox<?> cat, JTextField amt, JTextField note, DatePicker date) {
        boolean valid = true;

        name.setBackground(ColorUtil.getBackgroundColor());
        cat.setBackground(ColorUtil.getBackgroundColor());
        amt.setBackground(ColorUtil.getBackgroundColor());
        note.setBackground(ColorUtil.getBackgroundColor());
        date.setBackground(ColorUtil.getBackgroundColor());

        if (name.getText().trim().isEmpty()) {
            name.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        try {
            if (mainService.getNameIdExpenseCatMap().get((String) cat.getSelectedItem()) == null)
                throw new IllegalArgumentException();
        } catch (Exception e) {
            cat.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        try {
            double value = Double.parseDouble(amt.getText().trim());
            if (value < 0) throw new NumberFormatException();
        } catch (Exception e) {
            amt.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        try {
            Date.valueOf(date.getLocalDate());
        } catch (Exception e) {
            date.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        return valid;
    }

    public void reloadCategories() {
        if (tableModel != null && mainService != null) {
            tableModel.setCategoryMap(mainService.getIdNameExpenseCatMap());
            revalidate();
            repaint();
        }
    }
}
