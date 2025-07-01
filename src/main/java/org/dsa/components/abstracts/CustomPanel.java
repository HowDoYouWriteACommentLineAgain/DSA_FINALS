package org.dsa.components.abstracts;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class CustomPanel extends JPanel {
    public CustomPanel()
    {
        super();
    }

    public CustomPanel(LayoutManager lm)
    {
        super(lm);
    }

    public void add(JComponent c)
    {
        add(c);
    }

    public static CustomPanel createTopNavigation(LayoutManager lm) {
        return createFullRowPanel(lm);
    }

    public static CustomPanel createFullRowPanel(LayoutManager lm)
    {
        CustomPanel cp = new CustomPanel(lm);
        cp.setPreferredSize(new Dimension(1200, 300));
        cp.setMaximumSize(new Dimension(1200, 600));
        cp.setMinimumSize(new Dimension(200, 60));
        return cp;
    }
}
