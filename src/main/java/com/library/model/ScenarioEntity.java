package com.library.model;

public record ScenarioEntity(
        int id,
        String title,
        String author,
        String isbn,
        String status
) {
    // Used when adding a new book: the database assigns the ID automatically
    public ScenarioEntity(String title, String author, String isbn, String status) {
        this(0, title, author, isbn, status);
    }
}