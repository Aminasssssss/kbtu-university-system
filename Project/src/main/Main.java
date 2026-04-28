package main;

import menu.MainMenu;
import database.Database;
import enums.Language;
import models.users.Student;
import models.users.User;

/**
 * Main entry point for the KBTU University Information System.
 * Initializes the database with test data and launches the console menu.
 */
public class Main {

    /**
     * Starts the application.
     * Adds a test student and launches the main menu.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        Database db = Database.getInstance();

        User student = new Student(
                "S001",
                "Gala",
                "gala@kbtu.kz",
                "12345",
                Language.EN,
                3.5,
                18,
                0
        );
        db.addUser(student);

        new MainMenu().start();
    }
}