package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    // Load environment variables
    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");
//    private static final HikariDataSource dataSource;
//    
//    static {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(URL);
//        config.setUsername(USER);
//        config.setPassword(PASSWORD);
//        config.setMaximumPoolSize(10);
//        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
//
//        dataSource = new HikariDataSource(config);
//        System.out.println("");
//    }

//    public static Connection getConnection() throws SQLException {
//        return dataSource.getConnection();
//    }
    
    public static void main(String[] args) {	
    	System.out.println("Checking environment variables:");
        System.out.println("DB_URL: " + URL);
        System.out.println("DB_USER: " + USER);
        System.out.println("DB_PASSWORD: " + (PASSWORD != null ? "******" : "NOT SET")); // Mask password
    	
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to Freedb.tech successfully!");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
        
    }
    
}

