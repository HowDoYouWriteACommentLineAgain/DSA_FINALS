package org.dsa.UIPanels;

import org.dsa.abstractions.GenericService;
import org.dsa.dao.BudgetDAO;
import org.dsa.dao.ExpenseDAO;
import org.dsa.dao.IncomeDAO;
import org.dsa.models.objects.Budget;
import org.dsa.models.objects.Expense;
import org.dsa.models.objects.Income;
import org.dsa.utils.ColorUtil;
import org.dsa.utils.DateUtil;
import org.dsa.utils.FontsUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardPanel extends JPanel {

    private final GenericService<Income, IncomeDAO> inSer;
    private final GenericService<Expense, ExpenseDAO> exSer;
    private final GenericService<Budget, BudgetDAO> buSer;

    protected final JLabel titleLabel = new JLabel();
    protected JPanel titlePanel = new JPanel(new BorderLayout());
    protected JPanel contentWrapper = new JPanel();
    protected JScrollPane scrollPane;

    public DashboardPanel(GenericService<Income, IncomeDAO> inSer,
                          GenericService<Expense, ExpenseDAO> exSer,
                          GenericService<Budget, BudgetDAO> buSer,
                          String title) {
        super();
        this.inSer = inSer;
        this.exSer = exSer;
        this.buSer = buSer;

        setupContentPanel(title);

        setAtAGlancePanel();
        setExpensesPanel();
        setBudgetAdherencePanel();
    }

    private void setupContentPanel(String title) {
        if (title == null || title.isEmpty())
            throw new IllegalArgumentException("Title cannot be null or empty");

        setLayout(new BorderLayout());

        titleLabel.setText(title);
        titleLabel.setFont(FontsUtil.TITLE_FONT);
        titleLabel.setForeground(ColorUtil.getPrimaryTextColor());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        titlePanel.setBackground(ColorUtil.getBackgroundColorDarker());
        titlePanel.add(titleLabel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);

        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBorder(new EmptyBorder(10, 20, 10, 20));
        contentWrapper.setOpaque(false);

        scrollPane = new JScrollPane(contentWrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void newPanel(String headerTitle, JPanel content) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.add(content);
        wrapper.setFont(FontsUtil.REGULAR);
        setFontRecursive(wrapper, FontsUtil.REGULAR);
        wrapper.setBackground(content.getBackground());
        wrapper.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JLabel sectionTitle = new JLabel(headerTitle);
        sectionTitle.setFont(FontsUtil.TITLE_FONT);
        sectionTitle.setForeground(ColorUtil.getPrimaryTextColor());

        JPanel sectionHeader = new JPanel(new BorderLayout());
        sectionHeader.setBackground(ColorUtil.getBackgroundColorDarker());
        sectionHeader.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        sectionHeader.add(sectionTitle, BorderLayout.WEST);

        sectionPanel.add(sectionHeader, BorderLayout.NORTH);
        sectionPanel.add(wrapper, BorderLayout.CENTER);

        this.contentWrapper.add(sectionPanel);
        this.contentWrapper.revalidate();
        this.contentWrapper.repaint();
    }

    private void setAtAGlancePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ColorUtil.getBackgroundColorBrighter());

        double totalIncome = inSer.getAll().stream().mapToDouble(Income::amount).sum();
        double totalExpense = exSer.getAll().stream().mapToDouble(Expense::amount).sum();
        double balance = totalIncome - totalExpense;

        JLabel balanceLabel = new JLabel(String.format("<html><b>Current Balance:</b> %.2f</html>", balance));
        panel.add(balanceLabel);

        Date monthAgo = DateUtil.toDate(DateUtil.thirtyDaysBefore);

        inSer.getAll().stream()
                .filter(i -> !i.date().before(monthAgo))
                .max(Comparator.comparing(Income::date))
                .ifPresentOrElse(
                        i -> panel.add(new JLabel(String.format("<html><b>Recent Income stream:</b> %s - %.2f</html>", i.name(), i.amount()))),
                        () -> panel.add(new JLabel("No recent income found."))
                );

        exSer.getAll().stream()
                .filter(e -> !e.date().before(monthAgo))
                .max(Comparator.comparing(Expense::date))
                .ifPresentOrElse(
                        e -> panel.add(new JLabel(String.format("<html><b>Recent Expense:</b> %s - %.2f</html>", e.name(), e.amount()))),
                        () -> panel.add(new JLabel("No recent expense found."))
                );

        newPanel("At a Glance", panel);
    }

    private void setExpensesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ColorUtil.getBackgroundColorBrighter());

        Date weekAgo = DateUtil.toDate(DateUtil.weekBeforeDate);
        List<Expense> recent = exSer.getAll().stream()
                .filter(e -> !e.date().before(weekAgo))
                .sorted(Comparator.comparing(Expense::date).reversed())
                .limit(5)
                .collect(Collectors.toList());

        panel.add(new JLabel("<html><b>Recent Expenses (Past Week):</b></html>"));
        if (recent.isEmpty()) {
            panel.add(new JLabel("No expenses in the past week."));
        } else {
            for (Expense e : recent) {
                panel.add(new JLabel(String.format("%s - %.2f", e.name(), e.amount())));
            }
        }

        Date sixMonthsAgo = DateUtil.toDate(DateUtil.sixMonthsBefore);
        List<Expense> topExpenses = exSer.getAll().stream()
                .filter(e -> !e.date().before(sixMonthsAgo))
                .sorted(Comparator.comparingDouble(Expense::amount).reversed())
                .limit(5)
                .collect(Collectors.toList());

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("<html><b>Top 5 Expenses (6 Months):</b></html>"));
        if (topExpenses.isEmpty()) {
            panel.add(new JLabel("No large expenses found."));
        } else {
            for (Expense e : topExpenses) {
                panel.add(new JLabel(String.format("%s - %.2f", e.name(), e.amount())));
            }
        }

        Map<Integer, Long> freqMap = exSer.getAll().stream()
                .collect(Collectors.groupingBy(Expense::expense_cat, Collectors.counting()));

        freqMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry -> panel.add(new JLabel("<html><b>Most Frequent Category:</b> " + entry.getKey() + "</html>")));

        newPanel("Expenses", panel);
    }

    private void setBudgetAdherencePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ColorUtil.getBackgroundColorBrighter());

        List<Budget> budgets = buSer.getAll();
        Map<Integer, String> map = buSer.getIdNameExpenseCatMap();
        List<Expense> expenses = exSer.getAll();

        panel.add(new JLabel("<html><b>Budgets > 70% Used:</b></html>"));
        for (Budget b : budgets) {
            double spent = expenses.stream()
                    .filter(e -> e.expense_cat() == b.expense_cat())
                    .mapToDouble(Expense::amount)
                    .sum();
            double pct = (spent / b.max_amount()) * 100;

            if (pct >= 70 && pct <= 100) {
                panel.add(new JLabel(String.format("%s: %.0f%% used", map.get(b.expense_cat()), pct)));
            }
        }

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("<html><b>Budgets Over Limit:</b></html>"));
        for (Budget b : budgets) {
            double spent = expenses.stream()
                    .filter(e -> e.expense_cat() == b.expense_cat())
                    .mapToDouble(Expense::amount)
                    .sum();
            double pct = (spent / b.max_amount()) * 100;

            if (pct > 100) {
                panel.add(new JLabel(String.format("%s: %.0f%% used (OVER)", map.get(b.expense_cat()), pct)));
            }
        }

        newPanel("Budget Adherence", panel);
    }

    public void loadData() {
        contentWrapper.removeAll();

        setAtAGlancePanel();
        setExpensesPanel();
        setBudgetAdherencePanel();

        contentWrapper.revalidate();
        contentWrapper.repaint();
    }

    public void refresh() {
        loadData();
    }

    public static void setFontRecursive(Component comp, Font font) {
        comp.setFont(font);
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                setFontRecursive(child, font);
            }
        }
    }

}
