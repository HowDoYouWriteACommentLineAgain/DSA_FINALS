package org.dsa.components;

import org.dsa.Main;
import org.dsa.Styles;
import org.dsa.components.interfaces.UIPanels;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel implements UIPanels
{
    JLabel title = new JLabel("Financial Tracker");
    LabeledInputField usernameGroup = new LabeledInputField("Username", false);
    LabeledInputField passwordGroup = new LabeledInputField("Password", false);
    JButton loginButton = new JButton("Login");
    GridBagConstraints gbc = new GridBagConstraints();

    public LoginPanel(MainFrame mainFrame)
    {

        handleNavigation(mainFrame);
        implementStyles();
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

    @Override
    public void implementStyles() {
        title.setFont(Styles.TITLE_FONT);
        Styles.setStaticSize(title, Styles.LARGE_TEXT_DIM);
        Styles.setStaticSize(loginButton, Styles.SLIM_CONTAINER);

        if (Main.debugColors)
        {
            setBorder(Styles.debugBorder0);
            title.setBorder(Styles.debugBorder1);
            usernameGroup.setBorder(Styles.debugBorder1);
            passwordGroup.setBorder(Styles.debugBorder1);
            loginButton.setBorder(Styles.debugBorder1);
        }
    }

    @Override
    public void setInnerLayout() {
        setLayout(new GridBagLayout());
    }

    @Override
    public void handleNavigation(MainFrame mainFrame)
    {
        loginButton.addActionListener(e->mainFrame.showScreen("Dashboard"));
    }

}
