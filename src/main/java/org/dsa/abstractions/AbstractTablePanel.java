package org.dsa.abstractions;

import org.dsa.AppManager;
import org.dsa.UIPanels.components.DatePicker;
import org.dsa.utils.CustomTableCellRenderer;
import org.dsa.utils.SizesUtil;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTablePanel<O> extends JPanel {
    protected JPanel topPanel;
    protected JTable table;
    protected JPanel filter;
//    protected JPanel stackedPanels = new JPanel();
    protected GenericTableModel<O> tableModel;
    protected JTextField searchField = new JTextField(20);
    protected DatePicker startDate = new DatePicker();
    protected DatePicker endDate = new DatePicker();
    protected Timer debounceTimer;

    private static boolean isVisible = false;

    private static final List<AbstractTablePanel<?>> instances = new ArrayList<>();

    public AbstractTablePanel(GenericTableModel<O> tableModel) {
        this.tableModel = tableModel;
        instances.add(this);
        setLayout(new BorderLayout());
        setupTopPanel();
        setupTable();
    }

    protected abstract void add();

    protected abstract void loadData();

    protected abstract void edit();

    public abstract void delete();

    protected abstract void showDialog(O obj, boolean isNew);

    public abstract List<O> filter();

    public void refresh() {
        AppManager.getInstance().runWithLoading(
                this::loadData,
                this::updateVisibility
        );
    }

    protected void setupTable() {
        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    protected void setupTopPanel()
    {
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.Y_AXIS));
        topPanel.add(getCrudControls());
        topPanel.add(getFilterControls());
        updateVisibility();
        add(topPanel, BorderLayout.NORTH);
    }

    protected JPanel getCrudControls() {
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadData());

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> add());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> delete());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> edit());

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(refresh);
        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(hideBtn);
        return panel;
    }

    protected JButton hideBtn = new JButton(isVisible ? "Hide Filters" : "Show Filters");
    protected JButton resetBtn = new JButton("Reset");
    protected JButton applyBtn = new JButton("Apply");
    protected JLabel startLabel = new JLabel("Range: ");
    protected JLabel endLabel = new JLabel(" - ");
    protected JPanel filterPanel = new JPanel(new FlowLayout((FlowLayout.LEFT), 2, 0));

    protected JPanel getFilterControls()
    {
        filter = new JPanel(new FlowLayout((FlowLayout.LEFT), 2, 0));
        filterPanel.add(searchField);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) refresh();
            }

            @Override
            public void keyReleased(KeyEvent e){
                if (debounceTimer != null && debounceTimer.isRunning()) {
                    debounceTimer.restart();
                } else {
                    debounceTimer = new Timer(300, evt -> {
                        loadData();
                    });
                    debounceTimer.setRepeats(false);
                    debounceTimer.start();
                }
            };

        });

        hideBtn.addActionListener(e->{
            isVisible = !isVisible;
            updateVisibility();
        });

        resetBtn.addActionListener(e->{
            startDate.setDefault(Date.valueOf(LocalDate.of(2000, 1, 1)));
            endDate.setDefault(Date.valueOf(LocalDate.of(2100, 12, 31)));
            searchField.setText("");
            refresh();
        });

        applyBtn.addActionListener(e->{
            refresh();
        });

        JPanel dateFilter = new JPanel(new FlowLayout((FlowLayout.LEFT), 0, 0));
        dateFilter.add(startLabel);
        startDate.setDefault(1,1,2000);
        dateFilter.add(startDate);
        dateFilter.add(endLabel);
        endDate.setDefault(31,12,2100);
        dateFilter.add(endDate);

        filterPanel.add(dateFilter);
        filterPanel.add(resetBtn);
        filterPanel.add(applyBtn);

        filter.setVisible(isVisible);
        filter.add(filterPanel);

        return filter;
    }

    private void updateVisibility()
    {
        System.out.print("UPD");
        for (AbstractTablePanel<?> panel : instances) {
            panel.hideBtn.setText(isVisible ? "Minimize Filters" : "Maximize Filters");
            panel.filter.setVisible(isVisible);
        }
    }
//
//    public static void updateAllVisibilities() {
//        for (AbstractTablePanel<?> panel : instances) {
//            panel.updateVisibility();
//        }
//    }

    protected O getAt(int row) {
        return tableModel.getAt(row);
    }

    protected O getSelectedRowObject() {
        int row = table.getSelectedRow();
        if (row >= 0) return getAt(row);
        else {
            JOptionPane.showMessageDialog(this, "Please select a row first");
            return null;
        }
    }
}
