package org.dsa.UIPanels.components;

import org.dsa.utils.Constants.Screens;
import org.dsa.UIPanels.components.interfaces.ScreenNavigation;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class NavigationBar extends JPanel {
    String[] screens = Screens.getScreens();
    public NavigationBar(ScreenNavigation NavigatingFrame)
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        for (String screenName : screens)
        {
            JButton btn = new JButton(screenName);
            btn.addActionListener(e -> NavigatingFrame.showScreen(screenName));
            add(btn);
        }
    }
}
