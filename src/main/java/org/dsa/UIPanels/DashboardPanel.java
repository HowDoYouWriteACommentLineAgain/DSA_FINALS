package org.dsa.UIPanels;

import org.dsa.Constants.Styles;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.GridLayout;

public class DashboardPanel extends JPanel{
    public DashboardPanel()
    {
        super(new GridLayout());
        JLabel title = new JLabel("Dashboard");
        title.setFont(Styles.TITLE_FONT);
        Styles.setStaticSize(title, Styles.LARGE_TEXT_DIM);
        add(title);

        final JTable table = getJTable();
        table.setPreferredScrollableViewportSize(Styles.LARGE_CONTAINER);
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);
    }

    private static JTable getJTable() {
        String[] columnNames = {
                "First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"
        };

        Object[][] data = {
                {"Kathy", "Smith", "Snowboarding", 5, false},
                {"John", "Doe","Rowing", 3, true},
                {"Sue", "Black", "Knitting", 2, false},
                {"Jane", "White", "Speed reading", 20, true},
                {"Joe", "Brown", "Pool", 10, false}
        };

        return new JTable(data, columnNames);
    }
}
