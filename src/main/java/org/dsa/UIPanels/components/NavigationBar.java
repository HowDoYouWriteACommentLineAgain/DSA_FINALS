package org.dsa.UIPanels.components;

import org.dsa.AppManager;
import org.dsa.utils.Constants.Screens;
import org.dsa.utils.SizesUtil;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class NavigationBar extends JPanel {
    String[] screens = Screens.getScreens();
    JButton[] buttons = new JButton[screens.length];
    JLabel welcomeMessage = new JLabel("Welcome: User");
    public NavigationBar()
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        int index = 0;
        for (String screenName : screens)
        {
            JButton btn = new JButton(screenName);
            btn.setPreferredSize(SizesUtil.DEFAULT_BUTTON_SIZE);
            buttons[index++] = btn;
            if(screenName.equals(Screens.LOGIN))
                btn.addActionListener(e-> AppManager.getInstance().handleLogout());

            btn.addActionListener(e -> AppManager.getInstance().handleNavigation(screenName));
            add(btn);
        }
        welcomeMessage.setPreferredSize(SizesUtil.DEFAULT_FIELD_SIZE);
        add(welcomeMessage);
    }

    public void toggleBtnInvisible(String name)
    {
        for(JButton button : buttons)
        {
            button.setVisible(true);
            if(button.getText().equals(name))
                button.setVisible(false);
        }
    }
}
