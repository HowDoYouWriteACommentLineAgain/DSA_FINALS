package org.dsa.UIPanels.TabularPanels;

import org.dsa.models.objects.Transaction;
import org.dsa.models.tableModels.TransactionTableModel;
import org.dsa.services.TransactionService;
import org.dsa.utils.DatabaseConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TransactionUI extends JPanel {
    private JTable table;
    private final TransactionTableModel model;
    private final TransactionService service;

    public TransactionUI(TransactionService service) {
        this.service = service;
        this.model = new TransactionTableModel(service.getAll());

        setLayout(new BorderLayout());
        setupTable();
        setupControls();
    }

    private void setupTable() {
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupControls() {
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadData());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        ArrayList<Transaction> data = service.getAll();
        model.setData(data);
    }
}
