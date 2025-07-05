package org.dsa.UIPanels.components;

import org.dsa.UIPanels.components.interfaces.ScreenNavigation;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

public class MainFrame extends JFrame implements ScreenNavigation
{
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private boolean isLoggedIn = false;
    private final NavigationBar navbar = new NavigationBar(this);

    public MainFrame(String txt){
        setTitle(txt);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 600));
        setMinimumSize(new Dimension(700, 400));
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        showNavigationBar();
        getContentPane().add(cardPanel);
    }

    public void addScreen(String name, JPanel panel)
    {
        cardPanel.add(panel, name);
    }

    public void showScreen(String name) {
        cardLayout.show(cardPanel, name);
        repaint();
        revalidate();
        pack();
        setVisible(true);
    }

    public void showNavigationBar()
    {
        if (isLoggedIn)
        {
            getContentPane().add(navbar, BorderLayout.PAGE_START);
        }
    }

    public void authLogin()
    {
        isLoggedIn = true;
    }

}
