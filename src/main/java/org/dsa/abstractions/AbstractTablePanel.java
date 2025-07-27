package org.dsa.abstractions;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.dsa.AppManager;
import org.dsa.UIPanels.components.DatePicker;
import org.dsa.utils.ColorUtil;
import org.dsa.utils.CustomTableCellRenderer;
import org.dsa.utils.DateUtil;
import org.dsa.utils.FontsUtil;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTablePanel<O> extends JPanel {
    protected JPanel contentPanel = new JPanel(new BorderLayout());
    JLabel titleLabel;
    JPanel titlePanel = new JPanel(new BorderLayout());
    protected JPanel topPanel;
    protected JTable table;
    protected JPanel filter;
    protected GenericTableModel<O> tableModel;
    protected JTextField searchField = new JTextField(20);
    protected DatePicker startDate = new DatePicker(DateUtil.sixMonthsBefore);
    protected DatePicker endDate = new DatePicker(DateUtil.sixMonthsLater);
    protected Timer debounceTimer;

    protected abstract void add();
    protected abstract void loadData();
    protected abstract void edit();
    public abstract void delete();
    protected abstract void showDialog(O obj, boolean isNew);
    public abstract List<O> filter();

    private static boolean isVisible = false;

    private static final List<AbstractTablePanel<?>> instances = new ArrayList<>();

    public AbstractTablePanel(GenericTableModel<O> tableModel, String title) {
        this.tableModel = tableModel;
        instances.add(this);
        setLayout(new BorderLayout());
        if(title == null || title.isEmpty()) throw new IllegalArgumentException("NO TITLE");
        setContentPanel(title);
        setupTable();
        setupTopPanel();
        setStyles();
    }

    public void setContentPanel(String title)
    {
        titleLabel = new JLabel(title);
        titleLabel.setFont(FontsUtil.TITLE_FONT);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void setStyles()
    {
//        topPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
        contentPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        titlePanel.setOpaque(true);
        titlePanel.setBackground(ColorUtil.BACKGROUND_COLOR_DARKER);
        titleLabel.setForeground(ColorUtil.PRIMARY_TEXT_COLOR);

        JTableHeader header = table.getTableHeader();
        header.setFont(FontsUtil.TITLE_FONT.deriveFont(14f));
        header.setBackground(ColorUtil.HEADER_COLOR);
    }

    public void refresh() {
        AppManager.getInstance().runWithLoading(
                this::loadData,
                this::updateVisibility
        );
    }

    protected void setupTable() {
        if (table != null) return;
        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    protected void setupTopPanel()
    {
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.Y_AXIS));
        topPanel.add(getCrudControls());
        topPanel.add(getFilterControls());
        updateVisibility();
        contentPanel.add(topPanel, BorderLayout.NORTH);
    }

    protected JPanel getCrudControls() {
        JButton refresh = new JButton("Refresh", new FlatSVGIcon("icons/renew.svg"));
        refresh.addActionListener(e -> loadData());

        JButton addButton = new JButton("Add", new FlatSVGIcon("icons/list_alt_add.svg"));
        addButton.addActionListener(e -> add());

        JButton deleteButton = new JButton("Delete", new FlatSVGIcon("icons/delete.svg"));
        deleteButton.addActionListener(e -> delete());

        JButton editButton = new JButton("Edit", new FlatSVGIcon("icons/edit.svg"));
        editButton.addActionListener(e -> edit());

        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        crudPanel.add(refresh);
        crudPanel.add(addButton);
        crudPanel.add(editButton);
        crudPanel.add(deleteButton);
        crudPanel.add(hideBtn);
        return crudPanel;
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
            startDate.setDefault(Date.valueOf(DateUtil.yesterdayDate));
            endDate.setDefault(Date.valueOf(DateUtil.tomorrowDate));
            searchField.setText("");
            refresh();
        });

        applyBtn.addActionListener(e->{
            refresh();
        });

        JPanel dateFilter = new JPanel(new FlowLayout((FlowLayout.LEFT), 0, 0));
        dateFilter.add(startLabel);
        dateFilter.add(startDate);
        dateFilter.add(endLabel);
        dateFilter.add(endDate);

        startDate.setBorder(BorderFactory.createTitledBorder("Start Date"));
        endDate.setBorder(BorderFactory.createTitledBorder("End Date"));

        filterPanel.add(dateFilter);
        filterPanel.add(resetBtn);
        filterPanel.add(applyBtn);

        filter.setVisible(isVisible);
        filter.add(filterPanel);

        filter.setBorder(BorderFactory.createTitledBorder("Filters"));

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
