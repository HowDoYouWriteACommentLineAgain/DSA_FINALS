package org.dsa.UIPanels;

import org.dsa.utils.Constants.Screens;
import org.dsa.Main;
import org.dsa.Styles;
import org.dsa.UIPanels.components.LabeledInputField;
import org.dsa.UIPanels.components.MainFrame;
import org.dsa.UIPanels.components.interfaces.ScreenNavigation;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel
{
    JLabel title = new JLabel("Financial Tracker");
    LabeledInputField usernameGroup = new LabeledInputField("Username", false);
    LabeledInputField passwordGroup = new LabeledInputField("Password", false);
    JButton loginButton = new JButton("Login");
    GridBagConstraints gbc = new GridBagConstraints();

    public LoginPanel(MainFrame mainFrame)
    {
        handleNavigation(mainFrame);
        setStyles();
        setInnerLayout();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        add(title, gbc);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(usernameGroup, gbc);

        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(passwordGroup, gbc);

        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);
        setVisible(true);
    }

    public void setStyles() {
        title.setFont(Styles.TITLE_FONT);

        if (Main.debugColors)
        {
            setBorder(Styles.debugBorder0);
            title.setBorder(Styles.debugBorder1);
            usernameGroup.setBorder(Styles.debugBorder1);
            passwordGroup.setBorder(Styles.debugBorder1);
            loginButton.setBorder(Styles.debugBorder1);
        }
    }

    public void setInnerLayout() {
        setLayout(new GridBagLayout());
    }

    public void handleNavigation(ScreenNavigation navigator)
    {
        loginButton.addActionListener(e->{
            navigator.authLogin();
            navigator.showScreen(Screens.DASHBOARD);
            navigator.showNavigationBar();
        });

    }

}
