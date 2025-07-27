package org.dsa.UIPanels.TabularPanels;

import org.dsa.UIPanels.components.DatePicker;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncomeTablePanel extends AbstractTablePanel<Income> {

    public IncomeTablePanel(GenericService<Income, ? extends GenericDAO<Income>> service, String title) {
        super(new IncomeTableModel(), title, service);
        if (service == null) throw new IllegalArgumentException("Service cannot be null");
    }

    @Override
    public void delete() {
        List<Income> list = getSelectedRowObjects();
        Income first = list.getFirst();
        if (list == null) return;
        if (JOptionPane.showConfirmDialog(this,
                "Delete "+first.name()+", and " + list.size()+ "more ... item permanently?", "Confirm deletion",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            for(Income item : list) mainService.delete(item.id());
            refresh();
        }
    }

    @Override
    public void add() {
        showDialog(null, true);
    }

    public void showDialog(Income obj, boolean isNew) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isNew ? "Add Income" : "Edit Income", true);
        dialog.setLayout(new GridLayout(0, 2));

        JTextField nameField = new JTextField(isNew || obj.name() == null? "" : obj.name());
        JComboBox<String> incomeCatSelect = new JComboBox<>(mainService.getNameIdIncomeCatMap().keySet().toArray(new String[0]));
        JTextField amountField = new JTextField(isNew ? "" : String.valueOf(obj.amount()));
        JTextField noteField = new JTextField(isNew || obj.note().isEmpty() ? "" : obj.note());
        DatePicker dateField =isNew ? new DatePicker() : new DatePicker( obj.date());

        dialog.add(new JLabel("Name:")); dialog.add(nameField);
        dialog.add(new JLabel("Category:")); dialog.add(incomeCatSelect);
        dialog.add(new JLabel("Amount:")); dialog.add(amountField);
        dialog.add(new JLabel("Note:")); dialog.add(noteField);
        dialog.add(new JLabel("Date:")); dialog.add(dateField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (!validateFields(nameField, incomeCatSelect, amountField, noteField, dateField)) {
                JOptionPane.showMessageDialog(dialog, "Please correct the highlighted fields.");
                return;
            }
            try {
                Income newIncome = new Income(
                        0, // ID is managed by the DB
                        nameField.getText().trim(),
                        mainService.getNameIdIncomeCatMap().get(incomeCatSelect.getSelectedItem()),
                        Double.parseDouble(amountField.getText().trim()),
                        noteField.getText().trim(),
                        dateField.getFullDate()
                );

                if (isNew) mainService.insert(newIncome);
                else mainService.edit(obj.id(), newIncome);

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
    public List<Income> filter() {
        List<Income> data = mainService.getAll();
        Map<Integer, String> map = mainService.getIdNameIncomeCatMap();
        Date startDate = this.startDate.getFullDate();
        Date endDate = this.endDate.getFullDate();
        String searchQuery = this.searchField.getText();
        return data.stream()
                .filter(d -> d.date() != null)
                .filter(d -> !d.date().after(endDate) && !d.date().before(startDate))
                .filter(d ->{
                    String name = d.name() != null ? d.name().toLowerCase() : "";
                    String note = d.note() != null ? d.note().toLowerCase() : "";
                    String cat = map.get(d.income_cat()) != null ? map.get(d.income_cat()) : "";
                    String query = searchQuery.toLowerCase();
                    return name.contains(query) || note.contains(query) || cat.contains(query);
                })
                .collect(Collectors.toList());

    }

    @Override
    public void loadData() {

        if (mainService == null) {
            System.err.println("Service is null during loadData()");
            return;
        }

        ArrayList<Income> data = new ArrayList<>(filter());;

        Map<Integer, String> income_map = mainService.getIdNameIncomeCatMap();

        tableModel.setCategoryMap(income_map);

        tableModel.setData(data);
        table.clearSelection();
        revalidate();
        repaint();
    }

    @Override
    protected void appendStatusInfo(StringBuilder builder) {
        int[] rows = table.getSelectedRows();
        if (rows.length > 0) {
            double sum = 0;
            for (int row : rows) {
                Income e = tableModel.getAt(row);
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
            if (mainService.getNameIdIncomeCatMap().get((String) cat.getSelectedItem()) == null) throw new IllegalArgumentException();
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
