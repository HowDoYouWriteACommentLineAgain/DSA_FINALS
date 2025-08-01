package org.dsa.UIPanels.components;

import org.dsa.dao.ExpenseCatDAO;
import org.dsa.dao.IncomeCatDAO;
import org.dsa.utils.ColorUtil;
import org.dsa.utils.FontsUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class CategoryManagerDialog extends JDialog {

    private final DefaultListModel<String> incomeModel = new DefaultListModel<>();
    private final DefaultListModel<String> expenseModel = new DefaultListModel<>();
    private final JList<String> incomeList = new JList<>(incomeModel);
    private final JList<String> expenseList = new JList<>(expenseModel);

    private final IncomeCatDAO incomeCatDAO;
    private final ExpenseCatDAO expenseCatDAO;

    private final Runnable refreshIncomePanel;
    private final Runnable refreshExpensePanel;

    public CategoryManagerDialog(JFrame parent,
                                 IncomeCatDAO incomeCatDAO,
                                 ExpenseCatDAO expenseCatDAO,
                                 Runnable refreshIncomePanel,
                                 Runnable refreshExpensePanel) {
        super(parent, "Manage Categories", true);
        this.incomeCatDAO = incomeCatDAO;
        this.expenseCatDAO = expenseCatDAO;
        this.refreshIncomePanel = refreshIncomePanel;
        this.refreshExpensePanel = refreshExpensePanel;

        setTitle("Manage Categories");
        setSize(700, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(ColorUtil.getBackgroundColor());

        JPanel content = new JPanel();
        content.setLayout(new GridLayout(1, 2, 20, 0));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        content.setOpaque(false);

        content.add(createCategoryPanel("Income Categories", incomeModel, incomeList, true));
        content.add(createCategoryPanel("Expense Categories", expenseModel, expenseList, false));

        add(content, BorderLayout.CENTER);
        loadCategories();
    }

    private JPanel createCategoryPanel(String title, DefaultListModel<String> model, JList<String> list, boolean isIncome) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(ColorUtil.getBackgroundColorBrighter());
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ColorUtil.getBorderColor(), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontsUtil.MANROPE_BOLD.deriveFont(18f));
        titleLabel.setForeground(ColorUtil.getPrimaryTextColor());
        panel.add(titleLabel, BorderLayout.NORTH);

        list.setFont(FontsUtil.MANROPE_REGULAR.deriveFont(15f));
        list.setBackground(ColorUtil.getBackgroundColor());
        list.setForeground(ColorUtil.getPrimaryTextColor());
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorUtil.getBorderColor()));
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete");

        addBtn.setFont(FontsUtil.MANROPE_REGULAR);
        deleteBtn.setFont(FontsUtil.MANROPE_REGULAR);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(addBtn);
        buttons.add(deleteBtn);

        panel.add(buttons, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> handleAdd(model, isIncome));
        deleteBtn.addActionListener(e -> handleDelete(list, isIncome));

        return panel;
    }

    private void loadCategories() {
        incomeModel.clear();
        expenseModel.clear();

        List<IncomeCatDAO.CategoryItem> incomes = incomeCatDAO.getAllCategories();
        for (IncomeCatDAO.CategoryItem item : incomes) {
            incomeModel.addElement(item.id() + ": " + item.name());
        }

        List<ExpenseCatDAO.CategoryItem> expenses = expenseCatDAO.getAllCategories();
        for (ExpenseCatDAO.CategoryItem item : expenses) {
            expenseModel.addElement(item.id() + ": " + item.name());
        }
    }

    private void handleAdd(DefaultListModel<String> model, boolean isIncome) {
        String name = JOptionPane.showInputDialog(this, "Enter new category name:");
        if (name != null && !name.trim().isEmpty()) {
            if (isIncome) {
                incomeCatDAO.addCategory(name.trim());
                if (refreshIncomePanel != null) refreshIncomePanel.run();
            } else {
                expenseCatDAO.addCategory(name.trim());
                if (refreshExpensePanel != null) refreshExpensePanel.run();
            }
            loadCategories();
        }
    }

    private void handleDelete(JList<String> list, boolean isIncome) {
        String selected = list.getSelectedValue();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this category?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(selected.split(":")[0]);
                if (isIncome) {
                    incomeCatDAO.deleteCategory(id);
                    if (refreshIncomePanel != null) refreshIncomePanel.run();
                } else {
                    expenseCatDAO.deleteCategory(id);
                    if (refreshExpensePanel != null) refreshExpensePanel.run();
                }
                loadCategories();
            }
        }
    }
}
