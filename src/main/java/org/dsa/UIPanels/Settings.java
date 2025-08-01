package org.dsa.UIPanels;

import org.dsa.AppManager;
import org.dsa.dao.*;
import org.dsa.UIPanels.components.CategoryManagerDialog;
import org.dsa.utils.ColorUtil;
import org.dsa.utils.CsvUtil;
import org.dsa.utils.FontsUtil;
import org.dsa.utils.ThemeManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Settings extends JPanel {

    private final IncomeDAO incomeDAO;
    private final ExpenseDAO expenseDAO;
    private final BudgetDAO budgetDAO;
    private final IncomeCatDAO incomeCatDAO;
    private final ExpenseCatDAO expenseCatDAO;

    private final JLabel titleLabel = new JLabel();
    private final JPanel titlePanel = new JPanel(new BorderLayout());
    private final JPanel contentWrapper = new JPanel();
    private final JScrollPane scrollPane;

    public Settings(IncomeDAO incomeDAO, ExpenseDAO expenseDAO, BudgetDAO budgetDAO,
                    IncomeCatDAO incomeCatDAO, ExpenseCatDAO expenseCatDAO) {

        this.incomeDAO = incomeDAO;
        this.expenseDAO = expenseDAO;
        this.budgetDAO = budgetDAO;
        this.incomeCatDAO = incomeCatDAO;
        this.expenseCatDAO = expenseCatDAO;

        setLayout(new BorderLayout());

        titleLabel.setText("Settings");
        titleLabel.setFont(FontsUtil.MANROPE_BOLD.deriveFont(36f));
        titleLabel.setForeground(ColorUtil.getPrimaryTextColor());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        titlePanel.setBackground(ColorUtil.getBackgroundColorDarker());
        titlePanel.add(titleLabel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);

        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentWrapper.setOpaque(false);

        scrollPane = new JScrollPane(contentWrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        addSection("Theme", createThemeSection());
        addSection("Import & Export", createImportExportSection());
        addSection("Manage Categories", createManageCategoriesSection());
    }

    private void addSection(String title, JPanel content) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.add(content);
        wrapper.setFont(FontsUtil.MANROPE_REGULAR);
        setFontRecursive(wrapper, FontsUtil.MANROPE_REGULAR);
        wrapper.setBackground(content.getBackground());
        wrapper.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(FontsUtil.MANROPE_BOLD.deriveFont(26f));
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

    private JPanel createThemeSection() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(ColorUtil.getBackgroundColorBrighter());

        JLabel label = new JLabel("Choose Theme:");
        label.setFont(FontsUtil.MANROPE_REGULAR);

        String[] themes = {
            ThemeManager.THEME_LIGHT,
            ThemeManager.THEME_DARK,
            ThemeManager.THEME_ONE_DARK,
            ThemeManager.THEME_NORD,
            ThemeManager.THEME_DARK_PURPLE,
            ThemeManager.THEME_CARBON,
            ThemeManager.THEME_MONOCAI,
            ThemeManager.THEME_CYAN_LIGHT
        };

        JComboBox<String> themeBox = new JComboBox<>(themes);
        themeBox.setFont(FontsUtil.MANROPE_REGULAR);
        themeBox.setSelectedItem(ThemeManager.getCurrentTheme());

        JButton applyBtn = new JButton("Apply Theme");
        applyBtn.setFont(FontsUtil.MANROPE_REGULAR);
        applyBtn.addActionListener(e -> {
            String selected = (String) themeBox.getSelectedItem();
            ThemeManager.setTheme(selected);
            ThemeManager.applyTheme();
            restartApp();
        });

        panel.add(label);
        panel.add(themeBox);
        panel.add(applyBtn);
        return panel;
    }

    private JPanel createImportExportSection() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(ColorUtil.getBackgroundColorBrighter());

        JButton exportBtn = new JButton("Export to CSV");
        JButton importBtn = new JButton("Import from CSV");

        exportBtn.setFont(FontsUtil.MANROPE_REGULAR);
        importBtn.setFont(FontsUtil.MANROPE_REGULAR);

        exportBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Export Data to CSV");
            int choice = fileChooser.showSaveDialog(this);

            if (choice == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    CsvUtil.exportDataToCSV(
                        incomeDAO.getAllSafe(),
                        expenseDAO.getAllSafe(),
                        budgetDAO.getAllSafe(),
                        file.getAbsolutePath()
                    );
                    JOptionPane.showMessageDialog(this, "Data exported successfully.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to export data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        importBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Import Data from CSV");
            int choice = fileChooser.showOpenDialog(this);

            if (choice == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    CsvUtil.importDataFromCSV(
                        file.getAbsolutePath(),
                        incomeDAO,
                        expenseDAO,
                        budgetDAO
                    );
                    JOptionPane.showMessageDialog(this, "Data imported successfully. Please restart the app to see changes.");
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to import data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        panel.add(importBtn);
        panel.add(exportBtn);
        return panel;
    }

    private JPanel createManageCategoriesSection() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(ColorUtil.getBackgroundColorBrighter());

        JButton manageBtn = new JButton("Open Category Manager");
        manageBtn.setFont(FontsUtil.MANROPE_REGULAR);

        manageBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

            Runnable incomeRefresh = () -> AppManager.getInstance().getIncomePanel().reloadCategories();
            Runnable expenseRefresh = () -> AppManager.getInstance().getExpensePanel().reloadCategories();

            CategoryManagerDialog dialog = new CategoryManagerDialog(
                parentFrame,
                incomeCatDAO,
                expenseCatDAO,
                incomeRefresh,
                expenseRefresh
            );
            dialog.setVisible(true);
        });

        panel.add(manageBtn);
        return panel;
    }

    private void restartApp() {
        SwingUtilities.invokeLater(() -> AppManager.getInstance().start());
    }

    private static void setFontRecursive(Component comp, Font font) {
        comp.setFont(font);
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                setFontRecursive(child, font);
            }
        }
    }
}
