package com.library.ui;

import com.library.dao.ScenarioDao;
import com.library.model.ScenarioEntity;

import java.util.List;
import java.util.Scanner;

public class ScenarioMenu {

    private static final String LINE = "=".repeat(52);
    private static final String TABLE_LINE =
            "+----+----------------------+----------------------+---------------+------------+";

    private final Scanner scanner = new Scanner(System.in);
    private final ScenarioDao dao = new ScenarioDao();

    // Main loop: keeps showing the menu until the user chooses [3]
    public void start() {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = readMenuChoice();

            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewCatalog();
                case 3 -> {
                    System.out.println("\nExiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println("\nERROR: Please choose 1, 2, or 3.");
            }
        }

        scanner.close();
    }

    private void showMenu() {
        System.out.println();
        System.out.println(LINE);
        System.out.println("              LIBRARY BOOK CATALOG");
        System.out.println(LINE);
        System.out.println("[1] Add New Book");
        System.out.println("[2] View Book Catalog");
        System.out.println("[3] Exit System");
        System.out.println(LINE);
    }

    // Reads the whole line, then converts it, so no leftover newline stays in the buffer
    private int readMenuChoice() {
        System.out.print("Enter choice: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // [1] Add New Book
    private void addBook() {
        System.out.println("\n--- ADD NEW BOOK ---");

        String title = readRequired("Enter Title: ");
        String author = readRequired("Enter Author: ");
        String isbn = readIsbn();
        String status = readStatus();

        ScenarioEntity book = new ScenarioEntity(title, author, isbn, status);

        if (dao.insertBook(book)) {
            System.out.println("\nSUCCESS: Book cataloged into database!");
        } else {
            System.out.println("\nERROR: Book could not be saved.");
        }
    }

    // Keeps asking until the input is not empty
    private String readRequired(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("ERROR: This field cannot be empty.");
        }
    }

    // ISBN must be exactly 13 digits
    private String readIsbn() {
        while (true) {
            System.out.print("Enter ISBN: ");
            String isbn = scanner.nextLine().trim();
            if (isbn.matches("\\d{13}")) {
                return isbn;
            }
            System.out.println("ERROR: ISBN must be exactly 13 digits.");
        }
    }

    // Status must be AVAILABLE or BORROWED (not case-sensitive)
    private String readStatus() {
        while (true) {
            System.out.print("Enter Status (AVAILABLE / BORROWED): ");
            String status = scanner.nextLine().trim().toUpperCase();
            if (status.equals("AVAILABLE") || status.equals("BORROWED")) {
                return status;
            }
            System.out.println("ERROR: Status must be AVAILABLE or BORROWED.");
        }
    }

    // [2] View Book Catalog: prints the aligned ASCII table
    private void viewCatalog() {
        List<ScenarioEntity> books = dao.getAllBooks();

        System.out.println();
        System.out.println("=".repeat(TABLE_LINE.length()));
        System.out.println("                              LIBRARY BOOK CATALOG");
        System.out.println("=".repeat(TABLE_LINE.length()));

        if (books.isEmpty()) {
            System.out.println("No books cataloged yet.");
            return;
        }

        System.out.println(TABLE_LINE);
        System.out.printf("| %-2s | %-20s | %-20s | %-13s | %-10s |%n",
                "ID", "TITLE", "AUTHOR", "ISBN", "STATUS");
        System.out.println(TABLE_LINE);

        for (ScenarioEntity b : books) {
            System.out.printf("| %-2d | %-20s | %-20s | %-13s | %-10s |%n",
                    b.id(), cut(b.title(), 20), cut(b.author(), 20),
                    b.isbn(), b.status());
        }

        System.out.println(TABLE_LINE);
        System.out.println("Total Records Displayed: " + books.size());
    }

    // Shortens long text so it doesn't break the table alignment
    private String cut(String text, int max) {
        return text.length() <= max ? text : text.substring(0, max - 3) + "...";
    }
}