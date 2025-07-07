package org.dsa.abstractions;

import java.sql.SQLException;
import java.util.ArrayList;

public abstract class GenericService<O extends objectModel, D extends GenericDAO<O>>{

    public D dao;

    public abstract ArrayList<O> getAllByUser(int id);

    public O getById(int id) {
        try {
            return dao.getOneById(id);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch records" + e.getMessage(), e);
        }
    }

    public boolean edit(int id, O obj)
    {
        if(obj.validate() == false)
            throw new IllegalArgumentException("Invalid transaction arguments");

        try {
            return dao.updateById(id, obj);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to update Record" + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        try {
            dao.delete(id);
        }catch (SQLException e)
        {
            throw new RuntimeException("Failed to delete Record");
        }
    }

    public void insert(O obj){
        if(obj.validate() == false)
            throw new IllegalArgumentException("Invalid transaction arguments ");

        try{
            dao.insert(obj);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to save record:" + e.getMessage(), e);
        }
    }
}
