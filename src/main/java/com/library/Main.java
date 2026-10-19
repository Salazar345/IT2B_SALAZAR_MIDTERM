package com.library;

import com.library.dao.DatabaseConnection;
import com.library.ui.ScenarioMenu;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();   // creates scenario.db and the table if needed
        new ScenarioMenu().start();                // starts the menu loop
    }
}