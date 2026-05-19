package menu;

import database.Database;
import utils.Translator;
import enums.Language;
import models.users.Admin;
import models.users.Student;
import models.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for the Admin role.
 * Admins can manage users (add, remove, search), view system logs,
 * and change their preferred language.
 */
public class AdminMenu {

    private final Admin admin;
    private final Scanner scanner;

    /**
     * Creates an AdminMenu for the given admin.
     *
     * @param admin   the logged-in admin
     * @param scanner the shared input scanner
     */
    public AdminMenu(Admin admin, Scanner scanner) {
        this.admin = admin;
        this.scanner = scanner;
    }

    /**
     * Displays the admin menu and handles input until the admin logs out.
     */
    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║          ADMIN MENU              ║");
            System.out.println("║  Welcome, " + admin.getName());
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║ 1. View all users                ║");
            System.out.println("║ 2. Add student                   ║");
            System.out.println("║ 3. Remove user                   ║");
            System.out.println("║ 4. View system logs              ║");
            System.out.println("║ 5. Search user by email          ║");
            System.out.println("║ 6. Change language               ║");
            System.out.println("║ 0. Logout                        ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                viewAllUsers();
            } else if (choice.equals("2")) {
                addStudent();
            } else if (choice.equals("3")) {
                removeUser();
            } else if (choice.equals("4")) {
                viewLogs();
            } else if (choice.equals("5")) {
                searchUser();
            } else if (choice.equals("6")) {
                changeLanguage();
            } else if (choice.equals("0")) {
                admin.logout();
                running = false;
                System.out.println("Logged out.");
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Returns all users in the database as a list.
     *
     * @return list of all users
     */
    private List<User> getAllUsers() {
        return new ArrayList<>(Database.getInstance().getUsers().values());
    }

    /**
     * Displays all registered users with their role, name and email.
     */
    private void viewAllUsers() {
        List<User> users = getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users registered yet.");
            return;
        }
        System.out.println("\n── All Users ──");
        System.out.printf("  %-22s  %-25s  %s%n", "Role", "Name", "Email");
        System.out.println("  " + "─".repeat(70));
        for (User u : users) {
            System.out.printf("  %-22s  %-25s  %s%n",
                    u.getClass().getSimpleName(), u.getName(), u.getEmail());
        }
        admin.manageUsers();
    }

    /**
     * Prompts for details and adds a new student to the database.
     * Checks if the email is already taken before creating the account.
     */
    private void addStudent() {
        System.out.println("\n── Add New Student ──");
        System.out.print("ID      : ");
        String id = scanner.nextLine().trim();
        System.out.print("Name    : ");
        String name = scanner.nextLine().trim();
        System.out.print("Email   : ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (Database.getInstance().getUser(email) != null) {
            System.out.println("A user with this email already exists.");
            return;
        }

        Student student = new Student(id, name, email, password, Language.EN, 0.0, 0, 0);
        Database.getInstance().addUser(student);
        System.out.println("Student added: " + name + " (" + email + ")");
        Database.getInstance().addLog(admin.getName() + " added student: " + name);
    }

    /**
     * Removes a user from the database by email.
     * Prevents the admin from removing their own account.
     */
    private void removeUser() {
        List<User> users = getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users to remove.");
            return;
        }
        viewAllUsers();
        System.out.print("Enter email of user to remove: ");
        String email = scanner.nextLine().trim();

        if (email.equals(admin.getEmail())) {
            System.out.println("You cannot remove your own account.");
            return;
        }

        User found = Database.getInstance().getUser(email);
        if (found == null) {
            System.out.println("No user found with that email.");
            return;
        }

        Database.getInstance().removeUser(email);
        System.out.println("Removed: " + found.getName());
        Database.getInstance().addLog(admin.getName() + " removed user: " + found.getName());
    }

    /**
     * Displays the last 20 system activity log entries.
     */
    private void viewLogs() {
        List<String> logs = Database.getInstance().getLogs();
        if (logs.isEmpty()) {
            System.out.println("No activity logged yet.");
            return;
        }
        System.out.println("\n── System Logs (last 20) ──");
        int start = Math.max(0, logs.size() - 20);
        for (int i = start; i < logs.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, logs.get(i));
        }
        admin.viewLogs();
    }

    /**
     * Looks up a user by email and displays their details.
     */
    private void searchUser() {
        System.out.print("Enter email to search: ");
        String email = scanner.nextLine().trim();
        User user = Database.getInstance().getUser(email);
        if (user == null) {
            System.out.println("No user found with email: " + email);
            return;
        }
        System.out.println("\n── User Found ──");
        System.out.println("  Role  : " + user.getClass().getSimpleName());
        System.out.println("  Name  : " + user.getName());
        System.out.println("  Email : " + user.getEmail());
        System.out.println("  Lang  : " + user.getLanguage());
    }

    /**
     * Allows the admin to change their preferred language.
     */
    private void changeLanguage() {
        System.out.println("\nSelect language:");
        System.out.println("1. English (EN)");
        System.out.println("2. Қазақша (KZ)");
        System.out.println("3. Русский (RU)");
        System.out.print("Choose: ");
        String choice = scanner.nextLine().trim();
        Language lang;
        if (choice.equals("2")) {
            lang = Language.KZ;
        } else if (choice.equals("3")) {
            lang = Language.RU;
        } else {
            lang = Language.EN;
        }
        admin.setLanguage(lang);
        System.out.println("Language changed to: " + lang);
        Database.getInstance().addLog(admin.getName() + " changed language to " + lang);
    }
}
