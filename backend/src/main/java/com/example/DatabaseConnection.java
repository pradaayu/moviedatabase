package com.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    // Load environment variables
    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");
    // Connection config
    private static final HikariDataSource dataSource;
    
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(10);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
    
    public static void main(String[] args) {
    	try (Statement stmt = dataSource.getConnection().createStatement()) {
//    		String droputable = "DROP TABLE IF EXISTS User";
//    		stmt.execute(droputable);
    		String utable = "CREATE TABLE IF NOT EXISTS User"
    				  + "(id char(12) PRIMARY KEY, "
    				  + "name varchar(255) NOT NULL, "
    				  + "date_of_birth DATE, "
    				  + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP) "
    				  ;
    		stmt.execute(utable);
    		
//    		String dropLoginTable = "DROP TABLE IF EXISTS UserCredential";
//    		stmt.execute(dropLoginTable);
    		String loginTable = "CREATE TABLE IF NOT EXISTS UserCredential"
    				+ "(id char(12) PRIMARY KEY, "
    				+ "user_id char(12) NOT NULL, "
    				+ "email varchar(255) UNIQUE NOT NULL, "
    				+ "salt varchar(255) NOT NULL, "
    				+ "password varchar(255) NOT NULL, "
    				+ "FOREIGN KEY (user_id) REFERENCES User(id));"
    				;
    		stmt.execute(loginTable);
    		
//    		String dropReviewTable = "DROP TABLE IF EXISTS UserReview";
//    		stmt.execute(dropReviewTable);
    		String reviewTable = "CREATE TABLE IF NOT EXISTS UserReview"
    				+ "(user_id char(12) NOT NULL, "
    				+ "movie_ref varchar(255) NOT NULL, "
    				+ "content MEDIUMTEXT NOT NULL, "
    				+ "rating TINYINT, "
    				+ "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
    				+ "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
    				+ "PRIMARY KEY (user_id, movie_ref), "
    				+ "FOREIGN KEY (user_id) REFERENCES User(id));"
    				;
    		stmt.execute(reviewTable);
    		
//    		String dropWatchlistTable = "DROP TABLE IF EXISTS UserMovie";
//    		stmt.execute(dropWatchlistTable);
    		String watchlistTable = "CREATE TABLE IF NOT EXISTS UserMovie"
    				+ "(user_id char(12) NOT NULL, "
    				+ "movie_ref varchar(255) NOT NULL, "
    				+ "added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
    				+ "PRIMARY KEY (user_id, movie_ref), "
    				+ "FOREIGN KEY (user_id) REFERENCES User(id) on DELETE CASCADE);"
    				;
    		stmt.execute(watchlistTable);
    		
    		close();
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
	}
    
}

