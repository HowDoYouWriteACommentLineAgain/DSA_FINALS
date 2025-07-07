package org.dsa.UIPanels.TabularPanels;

import org.dsa.models.objects.Transaction;
import org.dsa.models.tableModels.TransactionTableModel;
import org.dsa.Session;
import org.dsa.services.TransactionService;
import org.dsa.utils.ColorUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TransactionUI extends JPanel {
    private JTable table;
    private final TransactionTableModel model;
    private final TransactionService service;

    public TransactionUI(TransactionService service) {
        this.service = service;
        this.model = new TransactionTableModel(new ArrayList<>());

        setLayout(new BorderLayout());
        setupTable();
        setupControls();
        loadData();
    }

    private void setupTable() {
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupControls() {
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadData());

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> openEditDialog(null));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> promptDelete(getSelectedRowObject()));

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> openEditDialog(getSelectedRowObject()));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void promptDelete(Transaction tx) {
        if(tx != null)
            if (JOptionPane.showConfirmDialog(this, "Delete \""+tx.getName()+"\" permanently?", "Confirm deletion",JOptionPane.YES_NO_OPTION) == 0)
                service.delete(tx.getId());
        loadData();
    }

    public void refresh() {loadData();}



    private void openEditDialog(Transaction tx) {
        boolean isNew = (tx == null);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isNew ? "Add Transaction" : "Edit Transaction", true);
        dialog.setLayout(new GridLayout(0, 2));

        JTextField dateField = new JTextField(isNew ? "" : tx.getT_date().toString());
        JTextField nameField = new JTextField(isNew ? "" : tx.getName());
        JTextField typeField = new JTextField(isNew ? "" : tx.getT_type());
        JTextField amountField = new JTextField(isNew ? "" : String.valueOf(tx.getAmount()));
        JTextField noteField = new JTextField(isNew ? "" : tx.getNote());


        dialog.add(new JLabel("Date:(YYYY-MM-DD)"));
        dialog.add(dateField);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Amount:"));
        dialog.add(amountField);
        dialog.add(new JLabel("Type:"));
        dialog.add(typeField);
        dialog.add(new JLabel("Note:"));
        dialog.add(noteField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (!validateFields(dateField, nameField, amountField, typeField)) {
                JOptionPane.showMessageDialog(dialog, "Please correct the highlighted fields.");
                return;
            }

            try {
                java.sql.Date date = java.sql.Date.valueOf(dateField.getText());
                String name = nameField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String type = typeField.getText();
                String note = noteField.getText();

                if (isNew) {
                    Transaction newTx = new Transaction();
                    newTx.setUserId(Session.getCurrentUserId());
                    newTx.setT_date(date);
                    newTx.setName(name);
                    newTx.setAmount(amount);
                    newTx.setRefId(1);
                    newTx.setT_type(type);
                    newTx.setNote(note);
                    service.insert(newTx);
                } else {
                    tx.setT_date(date);
                    tx.setName(name);
                    tx.setAmount(amount);
                    tx.setRefId(1);
                    tx.setT_type(type);
                    tx.setNote(note);
                    service.edit(tx.getId(), tx);
                }

                loadData();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Unexpected error: " + ex.getClass().getSimpleName());
            }
        });

        dialog.add(new JLabel());
        dialog.add(saveButton);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void loadData() {
        ArrayList<Transaction> data = service.getAllByUser(Session.getCurrentUserId());
        model.setData(data);
        revalidate();
        repaint();
    }

    private boolean validateFields(JTextField dateField, JTextField nameField, JTextField amountField, JTextField typeField)
    {
        boolean valid = true;

        // Reset colors
        dateField.setBackground(ColorUtil.BACKGROUND_COLOR);
        nameField.setBackground(ColorUtil.BACKGROUND_COLOR);
        amountField.setBackground(ColorUtil.BACKGROUND_COLOR);
        typeField.setBackground(ColorUtil.BACKGROUND_COLOR);

        // Date
        try {
            java.sql.Date.valueOf(dateField.getText());
        } catch (Exception e) {
            dateField.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        // Name
        if (nameField.getText().trim().isEmpty()) {
            nameField.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        // Amount
        try {
            double amt = Double.parseDouble(amountField.getText());
            if (amt < 0) throw new NumberFormatException();
        } catch (Exception e) {
            amountField.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        // Type
        if (typeField.getText().trim().isEmpty()) {
            typeField.setBackground(ColorUtil.WARNING_COLOR);
            valid = false;
        }

        return valid;
    }

    private Transaction getSelectedRowObject()
    {
        int row = table.getSelectedRow();
        if (row>= 0)
        {
            return model.getAt(row);

        }else{
            JOptionPane.showMessageDialog(this, "Please select a row first");
            return null;
        }
    }
}
