package org.dsa.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnectionManager {
    private static final String DB = "dsa_db";
    private static final String URL = "jdbc:mysql://localhost:3306/ " + DB + "?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";
    public static Connection getConnection()
    {
        try{
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            throw new RuntimeException("Error connecting: ", e);
        }
    }

}
