package org.dsa.UIPanels.TabularPanels;

import org.dsa.abstractions.AbstractTablePanel;
import org.dsa.models.objects.Transaction;
import org.dsa.models.tableModels.TransactionTableModel;
import org.dsa.Session;
import org.dsa.services.TransactionService;
import org.dsa.utils.ColorUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TransactionUI extends AbstractTablePanel<Transaction> {
    private final TransactionService service;

    public TransactionUI(TransactionService service) {
        super(new TransactionTableModel(service.getAllByUser(Session.getCurrentUserId())));

        System.out.println("User ID in TransactionUI constructor: " + Session.getCurrentUserId());
        if (service == null)
            throw new IllegalArgumentException("TransactionService cannot be null in TransactionUI");


        this.service = service;
    }

    @Override
    public void delete() {
        Transaction tx = getSelectedRowObject();
        if(tx != null)
            if (JOptionPane.showConfirmDialog(this, "Delete \""+tx.getName()+"\" permanently?", "Confirm deletion",JOptionPane.YES_NO_OPTION) == 0)
                service.delete(tx.getId());
        loadData();
    }
    public void refresh() {loadData();}

    @Override
    public void edit() {
        Transaction tx = getSelectedRowObject();
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

    @Override
    public void loadData() {
        System.out.println("loadData called, service = " + service);

        if (service == null) {
            System.err.println("TransactionService is null during loadData()");
            return;
        }

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
}
