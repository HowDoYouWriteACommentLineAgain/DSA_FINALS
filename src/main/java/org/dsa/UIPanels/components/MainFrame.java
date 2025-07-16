package org.dsa.UIPanels.components;

import org.dsa.AppManager;
import org.dsa.utils.SizesUtil;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrame extends JFrame
{
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private NavigationBar navbar;

    public MainFrame(String txt){
        setupFrame(txt);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(cardPanel);
    }

    public void setupFrame(String txt)
    {
        setTitle(txt);
        setPreferredSize(SizesUtil.DEFAULT_WINDOW_SIZE);
        setMinimumSize(SizesUtil.DEFAULT_WINDOW_SIZE);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                AppManager.getInstance().handleLogout();
            }
        });
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

    public void addNavbar(NavigationBar navbar)
    {
        this.navbar = navbar;
        getContentPane().add(navbar, BorderLayout.PAGE_START);
    }

}
