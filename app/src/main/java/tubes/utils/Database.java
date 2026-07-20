package main.java.tubes.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(
                AppConfig.getJdbcUrl(), 
                AppConfig.DB_USER, 
                AppConfig.DB_PASSWORD
            );
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL tidak ditemukan!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Gagal terhubung ke database!");
            e.printStackTrace();
        }
        return conn;
    }
}