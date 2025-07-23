package org.dsa.UIPanels.components;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.sql.Date;
import java.time.LocalDate;

public class DatePicker extends JPanel {
    JComboBox<Integer> dayBox = new JComboBox<>();
    JComboBox<Integer> monthBox = new JComboBox<>();
    JComboBox<Integer> yearBox = new JComboBox<>();
    public DatePicker()
    {
        super(new FlowLayout(FlowLayout.LEFT,2,0));
        fillDateBoxes();
        setupLayout();
        setDefault(1,1, 2025);
    }

    public DatePicker(int day, int month, int year)
    {
        super(new FlowLayout(FlowLayout.LEFT,2,0));
        fillDateBoxes();
        setupLayout();
        setDefault(day,month, year);
    }

    public DatePicker(Date shortdate)
    {
        super(new FlowLayout(FlowLayout.LEFT,2,0));
        fillDateBoxes();
        setupLayout();
        setDefault(shortdate);
    }

    public DatePicker(LayoutManager lm)
    {
        super(lm);
        fillDateBoxes();
        setupLayout();
        setDefault(1,1, 2025);
    }

    private void fillDateBoxes()
    {
        for(int d = 1; d <= 31; d++) dayBox.addItem(d);
        for(int m = 1; m <= 12; m++) monthBox.addItem(m);
        for(int y = 2000; y <= 2100; y++) yearBox.addItem(y);
    }

    private void setupLayout()
    {
        setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        add(dayBox);
        add(new JLabel(" : "));
        add(monthBox);
        add(new JLabel(" : "));
        add(yearBox);
    }

    public Date getFullDate()
    {
        Integer day = (Integer) dayBox.getSelectedItem();
        Integer month = (Integer) monthBox.getSelectedItem();
        Integer year = (Integer) yearBox.getSelectedItem();

        String shortDate = STR."\{year}-\{month}-\{day}";

        return Date.valueOf(shortDate);
    }

    public LocalDate getLocalDate()
    {
        Integer day = (Integer) dayBox.getSelectedItem();
        Integer month = (Integer) monthBox.getSelectedItem();
        Integer year = (Integer) yearBox.getSelectedItem();

        String shortDate = STR."\{year}-\{month}-\{day}";

        return Date.valueOf(shortDate).toLocalDate();
    }

    public void setDefault(Date shortdate)
    {
        LocalDate locDate = shortdate.toLocalDate();
        dayBox.setSelectedItem(locDate.getDayOfMonth());
        monthBox.setSelectedItem(locDate.getMonthValue());
        yearBox.setSelectedItem(locDate.getYear());
    }

    public void setDefault(int day, int month, int year)
    {
        dayBox.setSelectedItem(day);
        monthBox.setSelectedItem(month);
        yearBox.setSelectedItem(year);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (dayBox != null) dayBox.setBackground(bg);
        if (monthBox != null) monthBox.setBackground(bg);
        if (yearBox != null) yearBox.setBackground(bg);
    }
}

