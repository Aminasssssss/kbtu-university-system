package menu;

import database.Database;
import models.users.*;

import java.util.Scanner;

/**
 * Entry point for the console UI.
 * Handles login and routes the user to their role-specific menu.
 */
public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Starts the application loop.
     * Displays the main menu with login and exit options.
     */
    public void start() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   KBTU University Information System ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Login");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> login();
                case "0" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Handles login: prompts for email and password,
     * authenticates via Database, then routes to the correct menu.
     */
    private void login() {
        System.out.print("Email   : ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User user = Database.getInstance().getUser(email);
        if (user == null) { System.out.println("User not found."); return; }
        if (!user.getPassword().equals(password)) { System.out.println("Wrong password."); return; }

        boolean success = user.login();
        if (!success) { System.out.println("Login failed."); return; }

        System.out.println("Welcome, " + user.getName() + "!");
        routeToMenu(user);
    }

    /**
     * Routes the logged-in user to the appropriate role-specific menu.
     * @param user the authenticated user
     */
    private void routeToMenu(User user) {
        if (user instanceof Admin admin) {
            new AdminMenu(admin, scanner).show();
        } else if (user instanceof Manager manager) {
            new ManagerMenu(manager, scanner).show();
        } else if (user instanceof Teacher teacher) {
            new TeacherMenu(teacher, scanner).show();
        } else if (user instanceof Student student) {
            new StudentMenu(student, scanner).show();
        } else {
            System.out.println("No menu available for this role.");
        }
    }
}