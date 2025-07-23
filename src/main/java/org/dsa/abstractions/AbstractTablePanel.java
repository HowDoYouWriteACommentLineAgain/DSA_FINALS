package org.dsa.abstractions;

import org.dsa.UIPanels.components.DatePicker;
import org.dsa.utils.SizesUtil;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTablePanel<O> extends JPanel {
    protected JTable table;
    protected GenericTableModel<O> tableModel;
    protected JTextField searchField = new JTextField(20);
    protected DatePicker startDate = new DatePicker();
    protected DatePicker endDate = new DatePicker();

    private static boolean isVisible = false;

    protected ArrayList<JTextField> textFields;

    public AbstractTablePanel(GenericTableModel<O> tableModel) {
        this.tableModel = tableModel;
        setLayout(new BorderLayout());
        setupTable();
        setupControls();
        setupFilters();
    }

    protected abstract void add();

    protected abstract void loadData();

    protected abstract void edit();

    public abstract void delete();

    protected abstract void showDialog(O obj, boolean isNew);

    public abstract List<O> filter();

    public void refresh() {
        loadData();
        updateVisibility();}

    protected void setupTable() {
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    protected void setupControls() {
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadData());

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> add());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> delete());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> edit());

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(refresh);
        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        add(panel, BorderLayout.SOUTH);
    }

    protected JButton hideBtn = new JButton(isVisible ? "Hide Filter" : "Show Filter");
    protected JButton resetBtn = new JButton("Reset");
    protected JLabel startLabel = new JLabel("Range: ");
    protected JLabel endLabel = new JLabel(" - ");
    protected JPanel container = new JPanel(new FlowLayout((FlowLayout.LEFT), 4, 2));
    protected JPanel filterPanel = new JPanel(new FlowLayout((FlowLayout.LEFT), 2, 0));

    protected void setupFilters()
    {
        filterPanel.add(searchField);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) loadData();
            }
        });

        hideBtn.addActionListener(e->{
            isVisible = !isVisible;
            updateVisibility();
        });

        resetBtn.addActionListener(e->{
            startDate.setFullDate(Date.valueOf(LocalDate.of(2000, 1, 1)));
            endDate.setFullDate(Date.valueOf(LocalDate.of(2100, 12, 31)));
            searchField.setText("");
        });

        JPanel dateFilter = new JPanel(new FlowLayout((FlowLayout.LEFT), 2, 0));
        dateFilter.add(startLabel);
        startDate.setDefault(1,1,2000);
        dateFilter.add(startDate);
        dateFilter.add(endLabel);
        endDate.setDefault(31,12,2100);
        dateFilter.add(endDate);

        filterPanel.add(dateFilter);
        filterPanel.add(resetBtn);
        updateVisibility();


        container.setPreferredSize(SizesUtil.DEFAULT_BUTTON_SIZE);
        container.add(filterPanel);
        container.add(hideBtn);

        add(container, BorderLayout.NORTH);
    }

    private void updateVisibility()
    {
        filterPanel.setVisible(isVisible);
        hideBtn.setText(isVisible ? "Hide Filter" : "Show Filter");
    }

    boolean hasThirtyfirst(int month)
    {
        return switch (month) {
            case 1, 3, 5, 7, 8, 10, 12 -> true;
            default -> false;
        };
    }

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

//    protected abstract boolean validateFields(JTextField name, JComboBox cat, JTextField amt, JTextField note, JTextField date);
//    protected abstract boolean validateFields(JComboBox cat, JTextField goalAmt, JTextField maxAmt, JTextField startDate, JTextField endDate);
//    protected boolean validateDateField()
//    {
//
//    }
}
