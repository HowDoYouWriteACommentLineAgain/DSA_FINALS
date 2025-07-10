package org.dsa.abstractions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenericService<O extends objectModel, DAO extends GenericDAO<O>>{
    public DAO dao;

    public GenericService(DAO dao){
        this.dao = dao;
    }

    public O getById(int id) {
        try {
            return dao.getOneById(id);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch records" + e.getMessage(), e);
        }
    }

    public ArrayList<O> getAll() {
        try {
            return dao.getAll();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch records" + e.getMessage(), e);
        }
    }

    public boolean edit(int id, O obj)
    {

        if(id < 0)
            throw new IllegalArgumentException("id invalid");

        if(obj.validate() == false)
            throw new IllegalArgumentException("Invalid transaction arguments");

        try {
            return dao.update(id, obj);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to update Record" + e.getMessage(), e);
        }
    }

    public void delete(int id) {

        if(id < 0)
            throw new IllegalArgumentException("id invalid");

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

    public Map<Integer, String> getIdNameIncomeCatMap(){
        try{
            return dao.getIncomeCatMap();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch map:" + e.getMessage(), e);
        }
    }

    public Map<String, Integer> getNameIdIncomeCatMap() {
        try{
            Map<Integer, String> idToName;
            Map<String, Integer> nameToId = new HashMap<>();

            idToName = dao.getIncomeCatMap();

            for (Map.Entry<Integer, String> entry : idToName.entrySet()) {
                nameToId.put(entry.getValue(), entry.getKey());
            }
            return nameToId;
        }
        catch (SQLException e) {throw new RuntimeException("Failed to fetch map:" + e.getMessage(), e);}
    }

    public Map<Integer, String> getExpenseCatsMap(){
        try{
            return dao.getExpenseCatsMap();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch map:" + e.getMessage(), e);
        }
    }


}
