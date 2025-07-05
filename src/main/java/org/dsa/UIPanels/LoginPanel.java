package org.dsa.UIPanels;

import org.dsa.UIPanels.components.interfaces.FrameController;
import org.dsa.utils.Constants.Screens;
import org.dsa.Main;
import org.dsa.Styles;
import org.dsa.UIPanels.components.LabeledInputField;
import org.dsa.UIPanels.components.MainFrame;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel
{
    JLabel title = new JLabel("Financial Tracker");
    LabeledInputField usernameGroup = new LabeledInputField("Username", false);
    LabeledInputField passwordGroup = new LabeledInputField("Password", false);
    JButton loginButton = new JButton("Login");
    LayoutManager gridBagLayout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    public LoginPanel(MainFrame mainFrame)
    {
        setStyles();
        addY(title);
        addY(Box.createVerticalStrut(30));
        addY(usernameGroup);
        addY(Box.createVerticalStrut(4));
        addY(passwordGroup);
        addY(Box.createVerticalStrut(4));
        addLastItem(loginButton);

        handleNavigation(mainFrame);
        setVisible(true);
    }

    public void setStyles() {
        setLayout(gridBagLayout);
        setGBC();
        setAlignmentY(CENTER_ALIGNMENT);
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

    public void setGBC(){
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0f;
    }

    public void addY(Component comp)
    {
        add(comp, gbc);
        gbc.gridy++;
    }

    public void addLastItem(Component comp)
    {
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        add(comp, gbc);
    }

    public void handleNavigation(FrameController authenticator)
    {
        loginButton.addActionListener(e->{
            authenticator.authLogin();
            authenticator.showScreen(Screens.DASHBOARD);
            authenticator.showNavigationBar();
        });
    }

}
