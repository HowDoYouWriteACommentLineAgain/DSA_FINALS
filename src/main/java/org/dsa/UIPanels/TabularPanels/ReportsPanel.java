package org.dsa.UIPanels.TabularPanels;

import org.dsa.AppManager;
import org.dsa.UIPanels.components.DatePicker;
import org.dsa.abstractions.GenericService;
import org.dsa.dao.BudgetDAO;
import org.dsa.dao.ExpenseDAO;
import org.dsa.dao.IncomeDAO;
import org.dsa.models.enums.Period;
import org.dsa.models.objects.Budget;
import org.dsa.models.objects.Expense;
import org.dsa.models.objects.Income;
import org.dsa.models.objects.Report;
import org.dsa.models.tableModels.ReportTableModel;
import org.dsa.utils.ColorUtil;
import org.dsa.tableUtils.CustomTableCellRenderer;
import org.dsa.utils.DateUtil;
import org.dsa.utils.FontsUtil;
import org.dsa.utils.SizesUtil;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportsPanel extends JPanel{
    protected JPanel contentPanel = new JPanel(new BorderLayout());
    protected JLabel titleLabel;
    protected JPanel titlePanel = new JPanel(new BorderLayout());
    private final ArrayList<Report> reList = new ArrayList<>();
    private final ReportTableModel repTModel = new ReportTableModel(new ArrayList<>());
    private JTable repTable;

    private final GenericService<Income, IncomeDAO> inSer;
    private final GenericService<Expense, ExpenseDAO> exSer;
    private final GenericService<Budget, BudgetDAO> buSer;

    private final DatePicker startDateBox = new DatePicker(DateUtil.yesterdayDate);
    private final DatePicker endDateBox = new DatePicker(DateUtil.oneYearLater);
    public JComboBox<Period> periodPicker = new JComboBox<>(Period.values());

    private Date startDate;
    private Date endDate;


    public ReportsPanel(GenericService<Income, IncomeDAO>  inSer, GenericService<Expense, ExpenseDAO> exSer, GenericService<Budget, BudgetDAO> buSer, String title) {
        this.inSer = inSer;
        this.exSer = exSer;
        this.buSer = buSer;
        setLayout(new BorderLayout());
        setContentPanel(title);
        setupFilters();
        setupTable();
        setStyles();
    }

    public void refresh() {
        AppManager.getInstance().runWithLoading(
                this::loadData,
                this::updateVisibility
        );
    }

    public void setContentPanel(String title)
    {
        titleLabel = new JLabel(title);
        titleLabel.setFont(FontsUtil.TITLE_FONT);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void loadData()
    {

        reList.clear();
        startDate = startDateBox.getFullDate();
        endDate = endDateBox.getFullDate();

        if(inSer == null || exSer == null || buSer == null) return;
        ArrayList<Income> inList = inSer.getAll();
        ArrayList<Expense> exList = exSer.getAll();

        List<Income> inListFiltered = inList.stream().filter(i -> !i.date().before(startDate) && !i.date().after(endDate)).toList();
        List<Expense> exListFiltered = exList.stream().filter(i -> !i.date().before(startDate) && !i.date().after(endDate)).toList();

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

        LocalDate localDatePeriodStart = startDate.toLocalDate();
        int period = 1;
        while(Date.valueOf(localDatePeriodStart).before(endDate)) {

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
            period += 1;
        }

        repTModel.setData(reList);
        repTModel.fireTableDataChanged();
    }

    protected void setupTable() {
        repTable = new JTable(repTModel);
        repTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(true));
        contentPanel.add(new JScrollPane(repTable));
    }

    private static boolean isVisible = true;
    private final JButton hideBtn = new JButton(isVisible ? "Minimize filters" : "Maximize Filters");
    private final JPanel filterPanel = new JPanel(new FlowLayout((FlowLayout.LEFT), 2, 0));

    private void setupFilters() {
        JPanel container = new JPanel(new FlowLayout((FlowLayout.LEFT), 4, 2));
        JButton resetBtn = new JButton("Reset");
        JButton applyBtn = new JButton("Apply");
        JLabel startLabel = new JLabel("Range: ");
        JLabel endLabel = new JLabel(" - ");

        hideBtn.addActionListener(_ -> {
            isVisible = !isVisible;
            updateVisibility();
        });

        resetBtn.addActionListener(_ -> {
            startDateBox.setDefault(DateUtil.yesterdayDate);
            endDateBox.setDefault(DateUtil.oneYearLater);
            refresh();
        });

        applyBtn.addActionListener(_ -> {
            refresh();
        });

        JPanel dateFilter = new JPanel(new FlowLayout((FlowLayout.LEFT), 2, 0));
        dateFilter.add(startLabel);
        dateFilter.add(startDateBox);

        dateFilter.add(endLabel);
        dateFilter.add(endDateBox);

        filterPanel.add(dateFilter);
        filterPanel.add(resetBtn);
        periodPicker.setSelectedItem(Period._30DAYS);

        updateVisibility();

        container.setPreferredSize(SizesUtil.DEFAULT_BUTTON_SIZE);
        container.add(filterPanel);
        container.add(hideBtn);
        container.add(periodPicker);
        container.add(applyBtn);
        contentPanel.add(container, BorderLayout.NORTH);
    }

    private void setStyles()
    {
//        topPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
        contentPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        titlePanel.setOpaque(true);
        titlePanel.setBackground(ColorUtil.getBackgroundColorDarker());
        titleLabel.setForeground(ColorUtil.getPrimaryTextColor());

        JTableHeader header = repTable.getTableHeader();
        header.setFont(FontsUtil.TITLE_FONT.deriveFont(14f));
        header.setBackground(ColorUtil.getHeaderColor());
    }

    public void updateVisibility()
    {
        filterPanel.setVisible(isVisible);
        hideBtn.setText(isVisible ? "Hide Range" : "Show Range");
    }
}
