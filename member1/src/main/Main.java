package main;

import database.Database;
import enums.Language;
import models.users.Student;
import models.users.User;

/**
 * Main class.
 */
public class Main {

    /**
     * Starts program.
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

        boolean success = student.login();
        System.out.println("Login success: " + success);

        student.logout();

        System.out.println(db);
        System.out.println(db.getLogs());
    }
}