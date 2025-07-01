package org.dsa.components;

import org.dsa.components.interfaces.screenNavigation;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;

public class MainFrame extends JFrame implements screenNavigation
{
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    public MainFrame(String txt){
        setTitle(txt);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 600));
        setMinimumSize(new Dimension(700, 400));
        setLocationRelativeTo(null);
        add(cardPanel);
        setVisible(true);
        cardPanel.setVisible(true);
    }

    public void addScreen(String name, JPanel panel)
    {
        cardPanel.add(panel, name);
    }

    public void showScreen(String name)
    {
        cardLayout.show(cardPanel, name);
        repaint();
        revalidate();
    }

}
