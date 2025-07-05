package org.dsa.UIPanels.components;

import org.dsa.UIPanels.components.interfaces.FrameController;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

public class MainFrame extends JFrame implements FrameController
{
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private boolean isLoggedIn = false;
//    private final NavigationBar navbar = new NavigationBar(this);
    private final NavigationBar navbar;

    public MainFrame(String txt, NavigationBar navbar){
        this.navbar = navbar;

        setupFrame(txt);
        getContentPane().setLayout(new BorderLayout());
        showNavigationBar();
        getContentPane().add(cardPanel);
    }

    public void setupFrame(String txt)
    {
        setTitle(txt);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 600));
        setMinimumSize(new Dimension(700, 400));
        setLocationRelativeTo(null);
    }

    public void addScreen(String name, JPanel panel)
    {
        cardPanel.add(panel, name);
    }

    public void showScreen(String name) {
        cardLayout.show(cardPanel, name);
        navbar.toggleBtnInvisible(name);
        repaint();
        revalidate();
        setVisible(true);
    }

    public void showNavigationBar()
    {
        if (isLoggedIn)
        {
            getContentPane().add(navbar, BorderLayout.PAGE_START);
        }
    }

    public boolean getLoginStatus()
    {
        return isLoggedIn;
    }

    public void authLogin()
    {
        // add code authentication later
        isLoggedIn = true;
    }
    public void authLogout()
    {
        // add code logout later
        isLoggedIn = false;
        updateUIOnAuthChange();
    }

    private void updateUIOnAuthChange()
    {
        getContentPane().remove(navbar);
    }
}
