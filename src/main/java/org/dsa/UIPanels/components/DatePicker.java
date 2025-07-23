package org.dsa.UIPanels.components;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.sql.Date;
import java.time.LocalDate;

public class DatePicker extends JPanel {
    JComboBox<Integer> DayBox = new JComboBox<>();
    JComboBox<Integer> MonthBox = new JComboBox<>();
    JComboBox<Integer> YearBox = new JComboBox<>();
    public DatePicker()
    {
        super(new FlowLayout(FlowLayout.LEFT,2,0));
        fillDateBoxes();
        setupLayout();
    }

    public DatePicker(LayoutManager lm)
    {
        super(lm);
        fillDateBoxes();
        setupLayout();
    }

    private void fillDateBoxes()
    {
        for(int d = 1; d <= 31; d++) DayBox.addItem(d);
        for(int m = 1; m <= 12; m++) MonthBox.addItem(m);
        for(int y = 2000; y <= 2100; y++) YearBox.addItem(y);
    }

    private void setupLayout()
    {
        setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        add(DayBox);
        add(MonthBox);
        add(YearBox);
    }

    public Date getFullDate()
    {
        Integer day = (Integer) DayBox.getSelectedItem();
        Integer month = (Integer) MonthBox.getSelectedItem();
        Integer year = (Integer) YearBox.getSelectedItem();

        String shortDate = STR."\{year}-\{month}-\{day}";

        return Date.valueOf(shortDate);
    }

    public void setFullDate(Date shortdate)
    {
        LocalDate locDate = shortdate.toLocalDate();
        DayBox.setSelectedItem(locDate.getDayOfMonth());
        MonthBox.setSelectedItem(locDate.getMonthValue());
        YearBox.setSelectedItem(locDate.getYear());
    }

    public void setDefault(int day, int month, int year)
    {
        DayBox.setSelectedItem(day);
        MonthBox.setSelectedItem(month);
        YearBox.setSelectedItem(year);
    }
}
