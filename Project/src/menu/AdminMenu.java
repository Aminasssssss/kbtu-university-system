package menu;

import database.Database;
import enums.Language;
import models.users.Admin;
import models.users.Student;
import models.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for Admin role.
 * Allows managing users, viewing logs, and system administration.
 */
public class AdminMenu {

    private final Admin admin;
    private final Scanner scanner;

    /**
     * Creates an AdminMenu instance.
     * @param admin the logged-in admin
     * @param scanner the shared scanner for input
     */
    public AdminMenu(Admin admin, Scanner scanner) {
        this.admin = admin;
        this.scanner = scanner;
    }

    /**
     * Launches the admin menu loop.
     */
    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║        ADMIN MENU            ║");
            System.out.println("║  Welcome, " + admin.getName());
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║ 1. View all users            ║");
            System.out.println("║ 2. Add student               ║");
            System.out.println("║ 3. Remove user               ║");
            System.out.println("║ 4. View logs                 ║");
            System.out.println("║ 0. Logout                    ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> viewAllUsers();
                case "2" -> addStudent();
                case "3" -> removeUser();
                case "4" -> viewLogs();
                case "0" -> {
                    admin.logout();
                    running = false;
                    System.out.println("Logged out.");
                }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    /**
     * Returns all users as a list.
     * @return list of all users
     */
    private List<User> getAllUsers() {
        return new ArrayList<>(Database.getInstance().getUsers().values());
    }

    /**
     * Displays all users in the system.
     */
    private void viewAllUsers() {
        List<User> users = getAllUsers();
        if (users.isEmpty()) { System.out.println("No users."); return; }
        System.out.println("\n── All Users ──");
        for (User u : users) {
            System.out.printf("  [%-20s] %s | %s%n",
                    u.getClass().getSimpleName(), u.getName(), u.getEmail());
        }
        admin.manageUsers();
    }

    /**
     * Adds a new student to the system.
     */
    private void addStudent() {
        System.out.println("\n── Add New Student ──");
        try {
            System.out.print("ID      : "); String id = scanner.nextLine().trim();
            System.out.print("Name    : "); String name = scanner.nextLine().trim();
            System.out.print("Email   : "); String email = scanner.nextLine().trim();
            System.out.print("Password: "); String password = scanner.nextLine().trim();

            Student student = new Student(id, name, email, password,
                    Language.EN, 0.0, 0, 0);
            Database.getInstance().addUser(student);
            System.out.println("Student added: " + name);
            Database.getInstance().addLog(admin.getName() + " added student: " + name);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Removes a user from the system by email.
     */
    private void removeUser() {
        List<User> users = getAllUsers();
        if (users.isEmpty()) { System.out.println("No users."); return; }
        viewAllUsers();
        System.out.print("Enter email of user to remove: ");
        String email = scanner.nextLine().trim();
        User found = Database.getInstance().getUser(email);
        if (found == null) { System.out.println("User not found."); return; }
        Database.getInstance().removeUser(email);
        System.out.println("Removed: " + found.getName());
        Database.getInstance().addLog(admin.getName() + " removed user: " + found.getName());
    }

    /**
     * Displays the system activity log.
     */
    private void viewLogs() {
        admin.viewLogs();
    }
}