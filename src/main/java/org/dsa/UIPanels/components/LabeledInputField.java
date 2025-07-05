package org.dsa.UIPanels.components;

import org.dsa.Main;
import org.dsa.Styles;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;

public class LabeledInputField extends JPanel {
    public static final int defaultColWidth = 15;
    public final BoxLayout vertical = new BoxLayout(this, BoxLayout.Y_AXIS);
    public final BoxLayout horizontal = new BoxLayout(this, BoxLayout.X_AXIS);

    public LabeledInputField(String txt) {
        this(txt, defaultColWidth, true);
    }

    public LabeledInputField(String txt, boolean isHorizontal) {
        this(txt, defaultColWidth, isHorizontal);
    }

    public LabeledInputField(String txt, int colWidth, boolean isHorizontal) {
        final JLabel label = new JLabel(txt);
        final JTextField field = new JTextField(colWidth);

        if (Main.debugColors) setBorder(Styles.debugBorder0);
        if (Main.debugColors) field.setBorder(Styles.debugBorder1);
        if (Main.debugColors) label.setBorder(Styles.debugBorder1);

        setLayout(isHorizontal ? horizontal: vertical);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(label);
        if (isHorizontal) add(Box.createRigidArea(new Dimension(10, 0)));
        add(field);

        label.setFont(Styles.FIELD_FONT);
        field.setFont(Styles.FIELD_FONT);
    }
}
