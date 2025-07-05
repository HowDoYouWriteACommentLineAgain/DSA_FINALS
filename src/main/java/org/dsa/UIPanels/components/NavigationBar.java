package org.dsa.UIPanels.components;

import org.dsa.UIPanels.components.interfaces.FrameController;
import org.dsa.utils.Constants.Screens;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class NavigationBar extends JPanel {
    String[] screens = Screens.getScreens();
    JButton[] buttons = new JButton[screens.length];

    public NavigationBar(FrameController controller)
    {
        controller.setNavbar(this);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        int index = 0;
        for (String screenName : screens)
        {
            JButton btn = new JButton(screenName);
            buttons[index++] = btn;
            if(screenName.equals(Screens.LOGIN))
                btn.addActionListener(e->controller.authLogout());

            btn.addActionListener(e -> controller.showScreen(screenName));
            add(btn);
        }
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
