package org.dsa.UIPanels.components;

import org.dsa.UIPanels.components.interfaces.FrameController;
import org.dsa.utils.SizesUtil;

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
    private NavigationBar navbar;

    public MainFrame(String txt){
        setupFrame(txt);
        getContentPane().setLayout(new BorderLayout());
        showNavigationBar();
        getContentPane().add(cardPanel);
    }

    public void setupFrame(String txt)
    {
        setTitle(txt);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(SizesUtil.DEFAULT_WINDOW_SIZE);
        setMinimumSize(SizesUtil.DEFAULT_WINDOW_SIZE);
        setLocationRelativeTo(null);
    }

    public void setNavbar(NavigationBar navbar)
    {
        this.navbar = navbar;
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
        if(navbar == null)
            new NavigationBar(this);

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
