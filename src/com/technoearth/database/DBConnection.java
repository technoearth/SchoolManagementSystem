package com.technoearth.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_URL = "jdbc:sqlite:school.db";
    
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("✅ Connected to SQLite");
        } catch (SQLException e) {
            System.out.println("❌ SQLite connection failed: " + e.getMessage());
        }
        return conn;
    }
}
