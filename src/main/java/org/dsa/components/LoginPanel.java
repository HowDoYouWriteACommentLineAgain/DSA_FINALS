package org.dsa.components;

import org.dsa.Main;
import org.dsa.Styles;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel
{
    public LoginPanel()
    {
        JLabel title = new JLabel("Financial Tracker");
        LabeledInputField usernameGroup = new LabeledInputField("Username", false);
        LabeledInputField passwordGroup = new LabeledInputField("Password", false);
        JButton loginButton = new JButton("Login");

        GridBagConstraints gbc = new GridBagConstraints();

        setLayout(new GridBagLayout());

        title.setFont(Styles.TITLE_FONT);
        Styles.setStaticSize(title, Styles.LARGE_TEXT_DIM);
        Styles.setStaticSize(loginButton, Styles.SLIM_CONTAINER);

        if (Main.debugColors) title.setBorder(Styles.debugBorder0);

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
}
