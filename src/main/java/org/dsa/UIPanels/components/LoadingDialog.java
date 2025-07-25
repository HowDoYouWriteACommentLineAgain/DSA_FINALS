package org.dsa.UIPanels.components;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class LoadingDialog extends JDialog {
    public LoadingDialog(JFrame parent) {
        super(parent, "Loading", true);
        JLabel label = new JLabel("Loading, please wait...");
        add(label);
        setSize(200, 100);
        setLocationRelativeTo(parent);
    }
}

