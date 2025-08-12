package db;
import utils.Config;

import java.sql.*;

public class DatabaseHelper {
    private static final String DB_URL = Config.getUrlLite();

    public static void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS test_execution (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                scenario_name TEXT NOT NULL,
                status TEXT NOT NULL,
                execution_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                error_message TEXT
            );
        """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveTestResult(String scenario, String status, String errorMessage) {
        String sql = "INSERT INTO test_execution (scenario_name, status, error_message) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, scenario);
            pstmt.setString(2, status);
            pstmt.setString(3, errorMessage);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
