package org.dsa.UIPanels.components;

import org.dsa.Main;
import org.dsa.Styles;
import org.dsa.utils.LayoutUtil;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;

public class LabeledInputField extends JPanel {
    public static final int defaultColWidth = 15;
    private final BoxLayout vertical = new BoxLayout(this, BoxLayout.Y_AXIS);
    private final BoxLayout horizontal = new BoxLayout(this, BoxLayout.X_AXIS);
    private final boolean isHori;
    private final JLabel label;
    private final JTextField field;
    private final Component rigidBox = Box.createRigidArea(new Dimension(10, 0));

    public LabeledInputField(String txt, boolean isHorizontal) {
        label = new JLabel(txt);
        field = new JTextField(30);
        isHori = isHorizontal;
        setStyles();
    }

    private void setStyles()
    {
        setPreferredSize(new Dimension(200, 50));
        setMaximumSize(new Dimension(220, 50));
        setMinimumSize(new Dimension(200, 50));
        if (Main.debugColors) setBorder(Styles.debugBorder0);
        if (Main.debugColors) field.setBorder(Styles.debugBorder1);
        if (Main.debugColors) label.setBorder(Styles.debugBorder1);

        setLayout(isHori ? horizontal : vertical);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(label);
        if (isHori) add(rigidBox);
        add(field);

        label.setFont(Styles.FIELD_FONT);
        field.setFont(Styles.FIELD_FONT);
    }


}
