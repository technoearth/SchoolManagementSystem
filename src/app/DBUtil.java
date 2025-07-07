package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private static final String DB_URL = "jdbc:sqlite:school.db";

    // Use this to create the attendance table
    public static void createAttendanceTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS attendance (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "student_id INTEGER NOT NULL, " +
                         "date TEXT NOT NULL, " +
                         "present BOOLEAN NOT NULL)");

            System.out.println("✅ Attendance table recreated with 'present' column.");

        } catch (SQLException e) {
            System.out.println("❌ Error recreating attendance table: " + e.getMessage());
        }
    }
    public static void createStudentsTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "name TEXT NOT NULL, " +
                         "roll_no TEXT NOT NULL, " +
                         "student_class TEXT NOT NULL, " +
                         "section TEXT NOT NULL)");

            System.out.println("✅ Students table ready.");
        } catch (SQLException e) {
            System.out.println("❌ Error creating students table: " + e.getMessage());
        }
    }

    // Add this now to prepare for future use
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
