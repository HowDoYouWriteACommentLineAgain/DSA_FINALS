package org.dsa.UIPanels;

import org.dsa.utils.ThemeManager;
import org.dsa.AppManager;

import javax.swing.*;
import java.awt.*;

public class Settings extends JPanel {
    public Settings() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("Theme:");
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
        themeBox.setSelectedItem(ThemeManager.getCurrentTheme());

        JButton applyBtn = new JButton("Apply");
        applyBtn.addActionListener(e -> {
            String selected = (String) themeBox.getSelectedItem();
            ThemeManager.setTheme((String) themeBox.getSelectedItem());
            ThemeManager.setTheme(selected);
            ThemeManager.applyTheme();
            restartApp();
        });

        add(label);
        add(themeBox);
        add(applyBtn);
    }

    private void restartApp() {
        SwingUtilities.invokeLater(() -> {
            AppManager.getInstance().start(); // will dispose old frame
        });
    }
}
