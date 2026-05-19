package menu;

import database.Database;
import utils.Translator;
import enums.Language;
import models.communication.Complaint;
import models.communication.Request;
import models.users.Dean;

import java.util.List;
import java.util.Scanner;

/**
 * Console menu for the Dean role.
 * Deans can view and resolve complaints filed by teachers,
 * sign tech support requests, and view system logs.
 */
public class DeanMenu {

    private final Dean dean;
    private final Scanner scanner;

    /**
     * Creates a DeanMenu for the given dean.
     *
     * @param dean    the logged-in dean
     * @param scanner the shared input scanner
     */
    public DeanMenu(Dean dean, Scanner scanner) {
        this.dean = dean;
        this.scanner = scanner;
    }

    /**
     * Displays the dean menu and handles input until the dean logs out.
     */
    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║          DEAN MENU               ║");
            System.out.println("║  Dean " + dean.getName());
            System.out.println("║  Level: " + dean.getDeanLevel());
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║ 1. View all complaints           ║");
            System.out.println("║ 2. Resolve a complaint           ║");
            System.out.println("║ 3. Sign a tech support request   ║");
            System.out.println("║ 4. View system logs              ║");
            System.out.println("║ 5. Change language               ║");
            System.out.println("║ 0. Logout                        ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                viewComplaints();
            } else if (choice.equals("2")) {
                resolveComplaint();
            } else if (choice.equals("3")) {
                signRequest();
            } else if (choice.equals("4")) {
                viewLogs();
            } else if (choice.equals("5")) {
                changeLanguage();
            } else if (choice.equals("0")) {
                dean.logout();
                running = false;
                System.out.println("Logged out.");
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Displays all complaints filed by teachers, including resolved ones.
     */
    private void viewComplaints() {
        List<Complaint> complaints = TeacherMenu.getComplaints();
        if (complaints.isEmpty()) {
            System.out.println("No complaints have been filed.");
            return;
        }
        dean.viewComplaints();
        System.out.println("\n── All Complaints ──");
        for (int i = 0; i < complaints.size(); i++) {
            Complaint c = complaints.get(i);
            System.out.printf("%d. [%-6s] %s | %s → %s | %s%n",
                    i + 1,
                    c.getUrgencyLevel(),
                    c.getComplaintId(),
                    c.getSubmittedBy().getName(),
                    c.getReportedStudent().getName(),
                    getStatus(c));
            System.out.println("   " + c.getDescription());
        }
    }

    /**
     * Allows the dean to resolve an open complaint and add a resolution note.
     */
    private void resolveComplaint() {
        List<Complaint> complaints = TeacherMenu.getComplaints();
        List<Complaint> openComplaints = new java.util.ArrayList<>();
        for (Complaint c : complaints) {
            if (!c.isResolved()) {
                openComplaints.add(c);
            }
        }
        if (openComplaints.isEmpty()) {
            System.out.println("No open complaints to resolve.");
            return;
        }
        viewComplaints();
        System.out.print("Enter complaint number to resolve: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= complaints.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            Complaint complaint = complaints.get(idx);
            if (complaint.isResolved()) {
                System.out.println("This complaint is already resolved.");
                return;
            }
            System.out.print("Resolution note: ");
            String note = scanner.nextLine().trim();
            if (note.isBlank()) {
                note = "Resolved by Dean " + dean.getName();
            }
            complaint.resolve(note);
            dean.resolveComplaint(complaint);
            System.out.println("Complaint " + complaint.getComplaintId() + " has been resolved.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    /**
     * Allows the dean to sign a tech support request, as required by the system.
     */
    private void signRequest() {
        List<Request> requests = TechSupportMenu.getRequestPool();
        if (requests.isEmpty()) {
            System.out.println("No requests to sign.");
            return;
        }
        System.out.println("\n── Requests ──");
        for (int i = 0; i < requests.size(); i++) {
            Request r = requests.get(i);
            System.out.printf("%d. [%-8s] %s — %s%n",
                    i + 1, r.getStatus(), r.getRequestId(), r.getDescription());
        }
        System.out.print("Select request to sign: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= requests.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            dean.signRequest(requests.get(idx));
            System.out.println("Request signed by Dean " + dean.getName());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    /**
     * Displays the last 15 system activity log entries.
     */
    private void viewLogs() {
        List<String> logs = Database.getInstance().getLogs();
        System.out.println("\n── Last 15 System Events ──");
        int start = Math.max(0, logs.size() - 15);
        for (int i = start; i < logs.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, logs.get(i));
        }
    }

    /**
     * Allows the dean to change their preferred language.
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
        dean.setLanguage(lang);
        System.out.println("Language changed to: " + lang);
        Database.getInstance().addLog(dean.getName() + " changed language to " + lang);
    }

    /**
     * Returns a readable status string for a complaint.
     *
     * @param complaint the complaint
     * @return "RESOLVED" or "OPEN"
     */
    private String getStatus(models.communication.Complaint complaint) {
        if (complaint.isResolved()) {
            return "RESOLVED";
        }
        return "OPEN";
    }
}
