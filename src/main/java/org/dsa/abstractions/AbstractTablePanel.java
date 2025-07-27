package org.dsa.abstractions;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.dsa.AppManager;
import org.dsa.UIPanels.components.DatePicker;
import org.dsa.tableUtils.TableUtil;
import org.dsa.utils.ColorUtil;
import org.dsa.tableUtils.CustomTableCellRenderer;
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

public abstract class AbstractTablePanel<O extends ObjectModel> extends JPanel {
    protected JPanel contentPanel = new JPanel(new BorderLayout());
    protected JLabel titleLabel;
    protected JPanel titlePanel = new JPanel(new BorderLayout());
    protected JPanel topPanel;
    protected JTable table;
    protected JPanel filter;
    protected GenericTableModel<O> tableModel;
    protected JTextField searchField = new JTextField(20);
    protected DatePicker startDate = new DatePicker(DateUtil.sixMonthsBefore);
    protected DatePicker endDate = new DatePicker(DateUtil.sixMonthsLater);
    protected JLabel statusLabel = new JLabel("No selection");
    protected JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    protected Timer debounceTimer;
    private static boolean isVisible = false;
    private static final List<AbstractTablePanel<?>> instances = new ArrayList<>();
    protected final GenericService<O, ? extends GenericDAO<O>> mainService;

    protected abstract void add();
    protected abstract void loadData();
//    protected abstract void edit();
    public abstract void delete();
    protected abstract void showDialog(O obj, boolean isNew);
    public abstract List<O> filter();
    protected abstract void appendStatusInfo(StringBuilder builder);// Override in subclass for additional information

    public AbstractTablePanel(GenericTableModel<O> tableModel, String title, GenericService<O, ? extends GenericDAO<O>> mainService) {
        this.tableModel = tableModel;
        this.mainService = mainService;
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

    protected void setupStatusPanel() {
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        statusLabel.setForeground(ColorUtil.getSecondaryTextColor());
        statusPanel.setBackground(ColorUtil.getBackgroundColor());
        statusPanel.add(statusLabel);
        contentPanel.add(statusPanel, BorderLayout.SOUTH);
    }

    protected void updateStatusBar() {
        int[] selected = table.getSelectedRows();
        StringBuilder builder = new StringBuilder();
        if (selected.length == 0) {
            builder.append("No selection.");
        } else if (selected.length == 1) {
            builder.append("1 row selected.");
        } else {
            builder.append(selected.length).append(" rows selected.");
        }

        appendStatusInfo(builder); // extension point
        statusLabel.setText(builder.toString());
    }


    protected void setStyles()
    {
//        topPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
        contentPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        titlePanel.setOpaque(true);
        titlePanel.setBackground(ColorUtil.getBackgroundColorDarker());
        titleLabel.setForeground(ColorUtil.getPrimaryTextColor());

        if (table == null) return;
        JTableHeader header = table.getTableHeader();
        header.setFont(FontsUtil.TITLE_FONT.deriveFont(14f));
        header.setBackground(ColorUtil.getHeaderColor());
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
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) updateStatusBar();
        });
        table.setRowHeight(28);
        TableUtil.adjustColumnWidths(table);
        setupStatusPanel();
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
        refresh.addActionListener(_ -> loadData());

        JButton addButton = new JButton("Add", new FlatSVGIcon("icons/list_alt_add.svg"));
        addButton.addActionListener(_ -> add());

        JButton deleteButton = new JButton("Delete", new FlatSVGIcon("icons/delete.svg"));
        deleteButton.addActionListener(_ -> delete());

        JButton editButton = new JButton("Edit", new FlatSVGIcon("icons/edit.svg"));
        editButton.addActionListener(_ -> edit());

        JButton selectAllButton = new JButton("Select All", new FlatSVGIcon("icons/select_all.svg"));
        selectAllButton.addActionListener(_ -> table.selectAll());

        JButton deselectAllButton = new JButton("Deselect All", new FlatSVGIcon("icons/deselect.svg"));
        deselectAllButton.addActionListener(_ -> table.clearSelection());

        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        crudPanel.add(refresh);
        crudPanel.add(addButton);
        crudPanel.add(editButton);
        crudPanel.add(deleteButton);
        crudPanel.add(selectAllButton);
        crudPanel.add(deselectAllButton);
        crudPanel.add(hideBtn);
        return crudPanel;
    }

    protected JButton hideBtn = new JButton(isVisible ? "Hide Filters" : "Show Filters", new FlatSVGIcon("icons/filter_alt.svg"));
    protected JButton resetBtn = new JButton("Reset", new FlatSVGIcon("icons/reset_alt.svg"));
    protected JButton applyBtn = new JButton("Apply", new FlatSVGIcon("icons/filter_apply.svg"));
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
                    debounceTimer = new Timer(300, _ -> loadData());
                    debounceTimer.setRepeats(false);
                    debounceTimer.start();
                }
            }

        });

        hideBtn.addActionListener(_->{
            isVisible = !isVisible;
            updateVisibility();
        });

        resetBtn.addActionListener(_->{
            startDate.setDefault(Date.valueOf(DateUtil.yesterdayDate));
            endDate.setDefault(Date.valueOf(DateUtil.tomorrowDate));
            searchField.setText("");
            refresh();
        });

        applyBtn.addActionListener(_->refresh());

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
        for (AbstractTablePanel<?> panel : instances) {
            panel.hideBtn.setText(isVisible ? "Minimize Filters" : "Maximize Filters");
            panel.filter.setVisible(isVisible);
        }
    }

    protected void edit() {
        O obj = getSelectedRowObjects().getFirst();
        if (obj == null) return; // prevent double-triggered calls
        showDialog(obj, false);
    }



    protected O getAt(int row) {
        return tableModel.getAt(row);
    }

    protected List<O> getSelectedRowObjects() {
        int[] rows = table.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one row");
            return List.of();
        }

        List<O> selected = new ArrayList<>();
        for (int row : rows) {
            selected.add(getAt(row));
        }
        return selected;
    }
}
