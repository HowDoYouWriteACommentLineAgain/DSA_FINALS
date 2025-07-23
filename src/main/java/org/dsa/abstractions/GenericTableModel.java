package org.dsa.abstractions;

import org.dsa.additionalServices.AnalyzeAdherence;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericTableModel<O> extends AbstractTableModel {

    protected ArrayList<String> columns = new ArrayList<>();
    protected final ArrayList<O> objList;

    protected Map<Integer, String> categoryMap = new HashMap<>();
    protected Map<Integer, Double> ExpenseMap;

    public void setCategoryMap(Map<Integer, String> categoryMap) {this.categoryMap = categoryMap;}
    public void setExpenseMap(Map<Integer, Double> expenseMap) {this.ExpenseMap = expenseMap;}

    public GenericTableModel(ArrayList<O> objList)
    {
        this.objList = objList;
        setColumns();
        new DefaultTableModel();
    }

    public void setData(ArrayList<O> data)
    {
        this.objList.clear();
        this.objList.addAll(data);
        fireTableDataChanged();
    }



    public abstract ArrayList<String> returnColumnNames();

    public void setColumns(){columns.addAll(returnColumnNames());}

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