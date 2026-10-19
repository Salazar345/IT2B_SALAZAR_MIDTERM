package com.library.dao;

import com.library.model.ScenarioEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScenarioDao {

    // [1] Add Record: inserts one book using a PreparedStatement
    public boolean insertBook(ScenarioEntity book) {
        String sql = "INSERT INTO books (title, author, isbn, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.title());
            ps.setString(2, book.author());
            ps.setString(3, book.isbn());
            ps.setString(4, book.status());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: " + e.getMessage());
            return false;
        }
    }

    // [2] View Records: fetches every book from the database
    public List<ScenarioEntity> getAllBooks() {
        List<ScenarioEntity> books = new ArrayList<>();
        String sql = "SELECT id, title, author, isbn, status FROM books ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                books.add(new ScenarioEntity(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: " + e.getMessage());
        }

        return books;
    }
}