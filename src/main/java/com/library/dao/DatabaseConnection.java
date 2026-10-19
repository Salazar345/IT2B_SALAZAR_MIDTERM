package com.library.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:scenario.db";

    // Opens a new connection to scenario.db
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    // Creates the books table if it does not exist yet
    public static void initializeDatabase() {
        String sql = """
                CREATE TABLE IF NOT EXISTS books (
                    id     INTEGER PRIMARY KEY AUTOINCREMENT,
                    title  TEXT NOT NULL,
                    author TEXT NOT NULL,
                    isbn   TEXT NOT NULL,
                    status TEXT NOT NULL CHECK (status IN ('AVAILABLE', 'BORROWED'))
                )
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: " + e.getMessage());
        }
    }
}