package main.java.tubes.utils;

public class AppConfig {
    public static final String DB_HOST = "localhost";
    public static final String DB_PORT = "5432"; 
    public static final String DB_NAME = "mcd_pbo"; 
    public static final String DB_USER = "postgres";
    public static final String DB_PASSWORD = "developer"; 

    public static String getJdbcUrl() {
        return "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    }
}