package org.dsa.UIPanels;

import org.dsa.Session;
import org.dsa.models.tableModels.TransactionTableModel;
import org.dsa.services.TransactionService;
import org.dsa.utils.DatabaseConnectionManager;
import org.dsa.utils.FontsUtil;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class DashboardPanel extends JPanel{

    private final TransactionService service = new TransactionService(DatabaseConnectionManager.getConnection());
    private final TransactionTableModel model = new TransactionTableModel(service.getAllByUser(Session.getCurrentUserId()));
    public DashboardPanel()
    {
        super();
        setStyles();
        setupTables();
        setupTables();
        load();
    }

    private void setStyles()
    {
        setLayout(new GridLayout(2,4,10,10));
        JLabel title = new JLabel("Dashboard");
        title.setFont(FontsUtil.TITLE_FONT);
        add(title);
    }

    private void setupTables() {
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void load() {service.getAllByUser(Session.getCurrentUserId());}

    public void refresh(){load();}
}
