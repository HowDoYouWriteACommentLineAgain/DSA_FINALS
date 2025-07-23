package org.dsa.UIPanels.components;

import org.dsa.models.objects.Budget;
import org.dsa.models.objects.Expense;
import org.dsa.utils.ColorUtil;
import org.dsa.utils.FontsUtil;
import org.dsa.utils.SizesUtil;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map;

public class ProgressBarScrollPanel extends JScrollPane {
    private final JPanel contentPanel;
    private ArrayList<Budget> budgets;
    private ArrayList<Expense> expenses;
    private Map<Integer, String> idNameMap;

    public ProgressBarScrollPanel()
    {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public ProgressBarScrollPanel(ArrayList<Budget> budgets, ArrayList<Expense> expenses)
    {
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        setViewportView(contentPanel);

        this.budgets = budgets;
        this.expenses = expenses;
        buildList();
    }

    private void refresh() {
        contentPanel.removeAll();
        buildList();
        revalidate();
        repaint();
    }

    public ProgressBarScrollPanel getPanel()
    {
        return this;
    }

    public void setBudgetList(ArrayList<Budget> budgets)
    {
        this.budgets = budgets;
        refresh();
    }

    public void setExpenseList(ArrayList<Expense> expenses)
    {
        this.expenses = expenses;
        refresh();
    }

    public void setIdNameMap(Map<Integer, String> map)
    {
        this.idNameMap = map;
    }

    private void buildList()
    {
        if (budgets == null || expenses == null || idNameMap == null) return;

        for (Budget budget : budgets) {
            double totalExpenditure = expenses.stream()
                    .filter(e-> e.expense_cat() == budget.expense_cat())
                    .filter(e -> !e.date().before(budget.start_date()) && !e.date().after(budget.end_date()))
                    .mapToDouble(Expense::amount)
                    .sum();
            double max = budget.max_amount();

            System.out.println("Progress bar generating");
            System.out.printf("%s: %.2f/%.2f", idNameMap.get(budget.expense_cat()), totalExpenditure, max);
            String labelText = String.format("%s: %.2f/%.2f", idNameMap.get(budget.expense_cat()), totalExpenditure, max);

            JPanel row = new JPanel(new BorderLayout());
            JLabel info = new JLabel(labelText);
            info.setMinimumSize(SizesUtil.HALF_WINDOW_WITDH);
            row.add(info, BorderLayout.WEST);
            row.setBorder(BorderFactory.createMatteBorder(0, 4, 1, 4, ColorUtil.BORDER_COLOR));

            JProgressBar bar = new JProgressBar(0, (int) max);
            bar.setPreferredSize(new Dimension(1200, 40));
            bar.setMaximumSize(SizesUtil.HALF_WINDOW_WITDH);
            bar.setValue((int) totalExpenditure);
            bar.setStringPainted(true);
            bar.setFont(FontsUtil.REGULAR_BOLD);
            bar.setForeground(ColorUtil.PRIMARY_TEXT_COLOR);

            double ratio = totalExpenditure/max;
            if( ratio >= 1.0 )
            {
                bar.setForeground(ColorUtil.OVER_BUDGET_COLOR);
            }
            else if(ratio > 0.8)
            {
                bar.setForeground(ColorUtil.OVER_THRESHOLD_COLOR);
            }
            else
            {
                bar.setForeground(ColorUtil.ACCENT_COLOR);
            }

            row.add(bar, BorderLayout.EAST);
            contentPanel.add(row);
        }
    }

}
