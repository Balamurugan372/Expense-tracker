package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection getConnection() {

        String url = "jdbc:sqlite:expense.db";

        try {
            Connection con = DriverManager.getConnection(url);
            return con;

        } catch (Exception e) {
            System.out.println("Database connection failed!");
            System.out.println(e.getMessage());
            return null;
        }
    }
}