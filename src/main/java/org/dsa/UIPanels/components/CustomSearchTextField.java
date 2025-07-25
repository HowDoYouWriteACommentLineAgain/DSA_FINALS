package org.dsa.UIPanels.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CustomSearchTextField extends JTextField {
    private final String placeholder;
    private boolean isActive;

    public CustomSearchTextField(String placeholder, int columns) {
        super(placeholder, columns);
        this.placeholder = placeholder;
        this.isActive = false;
        setForeground(Color.GRAY);

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!isActive && getText().equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK);
                    isActive = true;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(Color.GRAY);
                    isActive = false;
                }
            }
        });
    }

    @Override
    public String getText() {
        return isActive ? super.getText() : "";
    }

    public void reset() {
        setText(placeholder);
        setForeground(Color.GRAY);
        isActive = false;
    }

}
