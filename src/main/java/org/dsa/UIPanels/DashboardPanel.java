package org.dsa.UIPanels;

import org.dsa.abstractions.GenericDAO;
import org.dsa.abstractions.GenericService;
import org.dsa.models.objects.Income;
import org.dsa.models.tableModels.IncomeTableModel;
import org.dsa.utils.Constants.Screens;
import org.dsa.utils.FontsUtil;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.util.Map;

public class DashboardPanel extends JPanel{

    private final GenericService<Income, ? extends GenericDAO<Income>> service;
    private final IncomeTableModel model;

    public DashboardPanel(GenericService<Income, ? extends GenericDAO<Income>> service)
    {
        super();

        if (service == null)
            throw new IllegalArgumentException("TransactionService cannot be null");

        this.service = service;
        this.model = new IncomeTableModel();
        setStyles();
        setupTables();
    }

    private void setStyles()
    {
        setLayout(new BorderLayout());
        JLabel title = new JLabel(Screens.DASHBOARD);
        title.setFont(FontsUtil.TITLE_FONT);
        add(title, BorderLayout.NORTH);
    }

    private void setupTables() {
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void load() {
        var data = service.getAll();
        System.out.println("Dashboard says: Data = " + data);

        Map<Integer, String> income_map = service.getIdNameIncomeCatMap();
        model.setCategoryMap(income_map);
        model.setData(data);
    }

    public void refresh(){load();}
}
