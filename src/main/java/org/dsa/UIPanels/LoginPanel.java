package org.dsa.UIPanels;

import org.dsa.Main;
import org.dsa.utils.ColorUtil;
import org.dsa.UIPanels.components.LabeledInputField;
import org.dsa.utils.FontsUtil;
import org.dsa.utils.SizesUtil;

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

    public LoginPanel()
    {
        setStyles();
        addY(title);
        addY(Box.createVerticalStrut(30));
        addY(usernameGroup);
        addY(Box.createVerticalStrut(4));
        addY(passwordGroup);
        addY(Box.createVerticalStrut(4));
        addLastItem(loginButton);
        setVisible(true);
    }

    public void setStyles() {
        setLayout(gridBagLayout);
        setGBC();
        setAlignmentY(CENTER_ALIGNMENT);
        title.setFont(FontsUtil.TITLE_FONT);
        title.setAlignmentX(CENTER_ALIGNMENT);
        loginButton.setPreferredSize(SizesUtil.DEFAULT_BUTTON_SIZE);
        loginButton.setMinimumSize(SizesUtil.DEFAULT_BUTTON_SIZE);
        setMaximumSize(SizesUtil.DEFAULT_WINDOW_SIZE);
        setMinimumSize(SizesUtil.DEFAULT_WINDOW_SIZE);
    }

    public void setGBC(){
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.NONE;
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
        gbc.weightx = 0.3f;
        gbc.gridwidth = 1;
        add(comp, gbc);
    }

}
