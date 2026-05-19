package menu;

import database.CourseRegistry;
import database.Database;
import models.users.Admin;
import models.users.Dean;
import models.users.GraduateStudent;
import models.users.Manager;
import models.users.Student;
import models.users.Teacher;
import models.users.TechSupportSpecialist;
import models.users.User;
import patterns.ResearcherDecorator;
import patterns.ResearcherDecorator;
import patterns.ResearcherRegistry;
import utils.PasswordUtils;
import utils.Translator;

import java.util.Scanner;

/**
 * Entry point for the console UI.
 * Handles login with SHA-256 password verification and routes
 * each user to their role-specific menu.
 * Saves all data on exit and loads it on the next startup.
 */
public class MainMenu {

    private static final String DB_FILE = "university_data.ser";
    private static final String REGISTRY_FILE = "course_registry.ser";
    private static final String RESEARCHER_FILE = "researcher_registry.ser";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Starts the application. Loads saved data if available,
     * then runs the main loop until the user exits.
     */
    public void start() {
        Database.loadFromFile(DB_FILE);
        CourseRegistry.loadFromFile(REGISTRY_FILE);
        ResearcherRegistry.loadFromFile(RESEARCHER_FILE);

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   KBTU University Information System ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Login");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                login();
            } else if (choice.equals("0")) {
                Database.getInstance().saveToFile(DB_FILE);
                CourseRegistry.saveToFile(REGISTRY_FILE);
                ResearcherRegistry.saveToFile(RESEARCHER_FILE);
                System.out.println("Goodbye!");
                running = false;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Prompts for email and password, verifies with SHA-256,
     * and routes the user to the correct menu if login succeeds.
     */
    private void login() {
        System.out.print("Email   : ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String rawPassword = scanner.nextLine().trim();

        User user = Database.getInstance().getUser(email);
        if (user == null) {
            System.out.println("No account found with that email.");
            return;
        }

        boolean correct = PasswordUtils.verify(rawPassword, user.getPassword());
        if (!correct) {
            User tempUser = Database.getInstance().getUser(email);
            if (tempUser != null) {
                System.out.println(Translator.get("wrong_password", tempUser.getLanguage()));
            } else {
                System.out.println("Wrong password.");
            }
            Database.getInstance().addLog("Failed login attempt: " + email);
            return;
        }

        user.login();
        System.out.println(Translator.get("welcome", user.getLanguage()) + ", " + user.getName() + "!");
        routeToMenu(user);
    }

    /**
     * Routes the authenticated user to the correct role-specific menu.
     * GraduateStudent is checked before Student because it is a subclass.
     *
     * @param user the authenticated user
     */
    private void routeToMenu(User user) {
        if (user instanceof Admin admin) {
            new AdminMenu(admin, scanner).show();
        } else if (user instanceof Dean dean) {
            new DeanMenu(dean, scanner).show();
        } else if (user instanceof Manager manager) {
            new ManagerMenu(manager, scanner).show();
        } else if (user instanceof Teacher teacher) {
            new TeacherMenu(teacher, scanner).show();
        } else if (user instanceof GraduateStudent gs) {
            ResearcherDecorator rd = ResearcherRegistry.getInstance().getResearcher(gs.getEmail());
            new StudentMenu(gs, scanner, rd).show();
        } else if (user instanceof Student student) {
            ResearcherDecorator rd = ResearcherRegistry.getInstance().getResearcher(student.getEmail());
            new StudentMenu(student, scanner, rd).show();
        } else if (user instanceof TechSupportSpecialist ts) {
            new TechSupportMenu(ts, scanner).show();
        } else {
            System.out.println("No menu configured for this role.");
        }
    }
}
