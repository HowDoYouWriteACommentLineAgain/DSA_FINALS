package org.dsa.UIPanels.TabularPanels;
import org.dsa.AppManager;
import org.dsa.UIPanels.components.DatePicker;
import org.dsa.abstractions.GenericService;
import org.dsa.dao.BudgetDAO;
import org.dsa.dao.ExpenseDAO;
import org.dsa.dao.IncomeDAO;
import org.dsa.models.objects.Budget;
import org.dsa.models.objects.Expense;
import org.dsa.models.objects.Income;
import org.dsa.models.objects.Report;
import org.dsa.models.tableModels.ReportTableModel;
import org.dsa.utils.ColorUtil;
import org.dsa.utils.CustomTableCellRenderer;
import org.dsa.utils.FontsUtil;
import org.dsa.utils.SizesUtil;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportsPanel extends JPanel{
    private final ArrayList<Report> reList = new ArrayList<>();
    private final ReportTableModel repTModel = new ReportTableModel(new ArrayList<>());
    private JTable repTable;

    private final GenericService<Income, IncomeDAO> inSer;
    private final GenericService<Expense, ExpenseDAO> exSer;
    private final GenericService<Budget, BudgetDAO> buSer;

    private final DatePicker startDateBox = new DatePicker();
    private final DatePicker endDateBox = new DatePicker();
    public JComboBox<Period> periodPicker = new JComboBox<>(Period.values());

    private Date startDate;
    private Date endDate;

    public ReportsPanel(GenericService<Income, IncomeDAO>  inSer, GenericService<Expense, ExpenseDAO> exSer, GenericService<Budget, BudgetDAO> buSer) {
        setLayout(new BorderLayout());
        this.inSer = inSer;
        this.exSer = exSer;
        this.buSer = buSer;
        startDateBox.setDefault(1,1,2020);
        endDateBox.setDefault(1,1,2100);
        setupFilters();
        setupTable();
//        loadData();
    }

    public void refresh() {
        AppManager.getInstance().runWithLoading(
                this::loadData,
                this::updateVisibility
        );
    }

    public void loadData()
    {
        reList.clear();
        startDate = startDateBox.getFullDate();
        endDate = endDateBox.getFullDate();

        if(inSer == null || exSer == null || buSer == null) return;
        ArrayList<Income> inList = inSer.getAll();
        ArrayList<Expense> exList = exSer.getAll();
        ArrayList<Budget> buList = buSer.getAll();

        List<Income> inListFiltered = inList.stream().filter(i -> !i.date().before(startDate) && !i.date().after(endDate)).toList();
        List<Expense> exListFiltered = exList.stream().filter(i -> !i.date().before(startDate) && !i.date().after(endDate)).toList();
        List<Budget> buListFiltered = buList.stream().filter(i -> !i.start_date().before(startDate) && !i.end_date().after(endDate)).toList();

        int periodLength = switch ((Period) periodPicker.getSelectedItem())
        {
            case DAILY  -> 1;
            case WEEKLY  -> 7;
            case FORTNIGHTLY  -> 14;
            case _30DAYS  -> 30;
            case _365DAYS  -> 365;
            case _5YEARS  -> 365 * 5;
            case _10YEARS   -> 365 * 10;
//            case null ->  365 * 10;
        };


        System.out.println("Start: " + startDate);
        System.out.println("End: " + endDate);

        LocalDate localDatePeriodStart = startDate.toLocalDate();
        int period = 1;
//        int maxPeriods = 1000;
        while(Date.valueOf(localDatePeriodStart).before(endDate)) {
//
//            if (period > maxPeriods)
//            {
//                JOptionPane.showMessageDialog(this,"Error: Please reduce Date range or increase period length");
//                break;
//            }

            LocalDate localDatePeriodEnd = localDatePeriodStart.plusDays(periodLength);
            Date sqlStart = Date.valueOf(localDatePeriodStart);
            Date sqlEnd = Date.valueOf(localDatePeriodEnd);

            double totalIncome = inListFiltered.stream()
                    .filter(in -> !in.date().before(sqlStart) && !in.date().after(sqlEnd))
                    .mapToDouble(Income::amount)
                    .sum();

            double totalExpense = exListFiltered.stream()
                    .filter(in -> !in.date().before(sqlStart) && !in.date().after(sqlEnd))
                    .mapToDouble(Expense::amount)
                    .sum();

            double netSavings = totalIncome - totalExpense;

            double singleLargestExpense = exListFiltered.stream()
                    .filter(in -> !in.date().before(sqlStart) && !in.date().after(sqlEnd))
                    .mapToDouble(Expense::amount)
                    .max()
                    .orElse(0.0);

            reList.add(new Report(period, totalIncome, totalExpense, netSavings, singleLargestExpense));
            localDatePeriodStart = localDatePeriodStart.plusDays(periodLength);
            localDatePeriodEnd = localDatePeriodEnd.plusDays(periodLength);
            period += 1;
        }

        System.out.println("REPORTS PANEL:");
        for(Report re : reList)
            System.out.printf("p: %d in: %.2f ex: %.2f net: %.2f lar:%.2f\n", re.period(), re.totalIncome(), re.totalExpense(), re.netSavings(), re.largestExpense());
        repTModel.setData(reList);
        repTModel.fireTableDataChanged();
    }

    protected void setupTable() {
        repTable = new JTable(repTModel);
//        repTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

//        TableColumnModel colModel = repTable.getColumnModel();
//        colModel.getColumn(0).setPreferredWidth(100);

        repTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(true));
        add(new JScrollPane(repTable));
    }

    private static boolean isVisible = true;
    private final JButton hideBtn = new JButton(isVisible ? "Hide Filters" : "Show Filters");
    private final JPanel filterPanel = new JPanel(new FlowLayout((FlowLayout.LEFT), 2, 0));

    private void setupFilters() {
        JPanel container = new JPanel(new FlowLayout((FlowLayout.LEFT), 4, 2));
        JButton resetBtn = new JButton("Reset");
        JButton applyBtn = new JButton("Apply");
        JLabel startLabel = new JLabel("Range: ");
        JLabel endLabel = new JLabel(" - ");

        hideBtn.addActionListener(e -> {
            isVisible = !isVisible;
            updateVisibility();
        });

        resetBtn.addActionListener(e -> {
            startDateBox.setDefault(Date.valueOf(LocalDate.of(2000, 1, 1)));
            endDateBox.setDefault(Date.valueOf(LocalDate.of(2100, 12, 31)));
            refresh();
        });

        applyBtn.addActionListener(e -> {refresh();});

        JPanel dateFilter = new JPanel(new FlowLayout((FlowLayout.LEFT), 2, 0));
        dateFilter.add(startLabel);
        startDateBox.setDefault(1,1,2000);
        dateFilter.add(startDateBox);

        dateFilter.add(endLabel);
        endDateBox.setDefault(31,12,2100);
        dateFilter.add(endDateBox);

        filterPanel.add(dateFilter);
        filterPanel.add(resetBtn);
        periodPicker.setSelectedItem(Period._10YEARS);

        updateVisibility();

        container.setPreferredSize(SizesUtil.DEFAULT_BUTTON_SIZE);
        container.add(filterPanel);
        container.add(hideBtn);
        container.add(periodPicker);
        container.add(applyBtn);
        add(container, BorderLayout.NORTH);
    }

    public void updateVisibility()
    {
        filterPanel.setVisible(isVisible);
        hideBtn.setText(isVisible ? "Hide Filters" : "Show Filters");
    }

    public void setStartDate(Date sqlDate)
    {
        this.startDate = sqlDate;
    }
    public void setEndDate(Date sqlDate)
    {
        this.endDate = sqlDate;
    }
}

enum Period {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    FORTNIGHTLY("Fortnightly (14d)"),
    _30DAYS("30 Days"),
    _365DAYS("1 Year (365d)"),
    _5YEARS("5 Years (365d)"),
    _10YEARS("10 Years (365d)");

    private final String label;
    Period(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
