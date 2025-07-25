package org.dsa.dao;

import org.dsa.abstractions.GenericDAO;
import org.dsa.models.objects.Report;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportsDAO extends GenericDAO<Report> {

    public ReportsDAO(Connection conn)
    {
        super(conn);
    }



    @Override
    public String getTableName() {
        return "Reports";
    }

    @Override public ArrayList<Report> getAll() throws SQLException {return null;}
    @Override public void delete(int id) throws SQLException {}
    @Override public boolean update(int id, Report obj) throws SQLException {return false;}
    @Override public void insert(Report obj) throws SQLException {}
    @Override public Report getOneById(int id) throws SQLException {return null;}
}
