package org.dsa.UIPanels;

import org.dsa.Constants.Screen;
import org.dsa.components.abstracts.CustomPanel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class TransactionPanel extends CustomPanel
{
    public static JButton budgetButton = new JButton(Screen.BUDGET);
    public static JButton dashboardButton = new JButton(Screen.DASHBOARD);
    public static JButton goalButton = new JButton(Screen.GOALS);
    public static JButton loginButton = new JButton(Screen.LOGIN);

    CustomPanel topNavigationPanel = CustomPanel.createTopNavigation(new BoxLayout(this, BoxLayout.Y_AXIS));

    public TransactionPanel()
    {
        super(new BorderLayout());
        addTop();

    }

    public void addTop()
    {
        topNavigationPanel.add(budgetButton);
        topNavigationPanel.add(dashboardButton);
        topNavigationPanel.add(goalButton);
        topNavigationPanel.add(loginButton);

        add(topNavigationPanel);
    }

    public void addCenter()
    {

    };

    public void addBottom()
    {

    };
}
