package org.dsa.UIPanels.components;

import org.dsa.Main;
import org.dsa.utils.ColorUtil;
import org.dsa.utils.FontsUtil;
import org.dsa.utils.SizesUtil;

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
        label.setPreferredSize(SizesUtil.DEFAULT_FIELD_SIZE);
        label.setMinimumSize(SizesUtil.DEFAULT_FIELD_SIZE);
        field.setPreferredSize(SizesUtil.DEFAULT_FIELD_SIZE);
        field.setMinimumSize(SizesUtil.DEFAULT_FIELD_SIZE);

//        setMinimumSize(new Dimension(200, 80));
        if (Main.debugColors) setBorder(ColorUtil.debugBorder0);
        if (Main.debugColors) field.setBorder(ColorUtil.debugBorder1);
        if (Main.debugColors) label.setBorder(ColorUtil.debugBorder1);

        setLayout(isHori ? horizontal : vertical);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(label);
        if (isHori) add(rigidBox);
        add(field);

        label.setFont(FontsUtil.FIELD_FONT);
        field.setFont(FontsUtil.FIELD_FONT);
    }


}
