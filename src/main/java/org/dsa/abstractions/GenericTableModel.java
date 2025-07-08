package org.dsa.abstractions;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collections;

public abstract class GenericTableModel<O> extends AbstractTableModel {

    protected ArrayList<String> columns = new ArrayList<>();
    protected final ArrayList<O> objList;

    public GenericTableModel(ArrayList<O> objList)
    {
        this.objList = objList;
        new DefaultTableModel();
    }

    public void setData(ArrayList<O> data)
    {
        this.objList.clear();
        this.objList.addAll(data);
        fireTableDataChanged();
    }

    public void setColumns(String[] columnNames){
        Collections.addAll(columns, columnNames);
    }

    public O getAt(int rowIndex)
    {
        return objList.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return objList.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getColumnName(int column)
    {
        return this.columns.get(column);
    }

    @Override
    public abstract Object getValueAt(int rowIndex, int columnIndex);
}
/*


 */