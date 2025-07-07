package com.technoearth.database;

import java.sql.Connection;
import java.sql.Statement;

public class DBInitializer {
    public static void createTables() {
        String sql = """
            CREATE TABLE IF NOT EXISTS students (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                grade TEXT NOT NULL,
                section TEXT,
                attendance INTEGER DEFAULT 0,
                assignments INTEGER DEFAULT 0,
                homework INTEGER DEFAULT 0
            );
        """;

        try (Connection conn = DBConnection.connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Students table ready.");
        } catch (Exception e) {
            System.out.println("❌ Error creating table: " + e.getMessage());
        }
    }
}
