package org.dsa.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnectionManager {
    private static final String DB = "DSA_DB";
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "krystian";
    private static final String PASS = "";
    public static Connection getConnection()
    {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL+DB, USER, PASS);
        } catch (Exception e) {
            throw new RuntimeException("Error connecting: ", e);
        }
    }
}
