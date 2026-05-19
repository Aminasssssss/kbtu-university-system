package menu;

import database.Database;
import utils.Translator;
import enums.RequestStatus;
import models.communication.Request;
import models.users.TechSupportSpecialist;
import models.users.User;

import java.util.List;
import java.util.Scanner;

/**
 * Console menu for the Tech Support Specialist role.
 * Requests are stored in Database so they persist between sessions.
 *
 * Status flow: VIEWED → ACCEPTED or REJECTED → DONE
 */
public class TechSupportMenu {

    private final TechSupportSpecialist specialist;
    private final Scanner scanner;

    private static int requestCounter = 1;

    /**
     * Creates a TechSupportMenu for the given specialist.
     *
     * @param specialist the logged-in tech support specialist
     * @param scanner    the shared input scanner
     */
    public TechSupportMenu(TechSupportSpecialist specialist, Scanner scanner) {
        this.specialist = specialist;
        this.scanner = scanner;
    }

    /**
     * Called from Student and Teacher menus when they submit a request.
     * The request is saved in Database so it survives application restarts.
     *
     * @param description what the user needs help with
     * @param sender      the user submitting the request
     */
    public static void submitRequest(String description, User sender) {
        String id = "REQ-" + String.format("%03d", requestCounter++);
        Request request = new Request(id, description, sender);
        Database.getInstance().addTechRequest(request);
        System.out.println("Request submitted successfully. ID: " + id);
        Database.getInstance().addLog("Tech request submitted by " + sender.getName() + ": " + description);
    }

    /**
     * Returns all tech support requests from the Database.
     * Used by ManagerMenu and DeanMenu to display pending requests.
     *
     * @return list of all requests
     */
    public static List<Request> getRequestPool() {
        return Database.getInstance().getTechRequests();
    }

    /**
     * Displays the tech support menu and handles input until the specialist logs out.
     */
    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║      TECH SUPPORT MENU           ║");
            System.out.println("║  Welcome, " + specialist.getName());
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║ " + utils.Translator.get("view_all_requests", specialist.getLanguage()));
            System.out.println("║ " + utils.Translator.get("accept_request", specialist.getLanguage()));
            System.out.println("║ " + utils.Translator.get("reject_request", specialist.getLanguage()));
            System.out.println("║ " + utils.Translator.get("mark_done", specialist.getLanguage()));
            System.out.println("║ " + utils.Translator.get("logout_option", specialist.getLanguage()));
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                viewRequests();
            } else if (choice.equals("2")) {
                changeStatus(RequestStatus.ACCEPTED);
            } else if (choice.equals("3")) {
                changeStatus(RequestStatus.REJECTED);
            } else if (choice.equals("4")) {
                changeStatus(RequestStatus.DONE);
            } else if (choice.equals("0")) {
                specialist.logout();
                running = false;
                System.out.println("Logged out.");
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Displays all requests in the system with their current status.
     */
    private void viewRequests() {
        List<Request> requests = Database.getInstance().getTechRequests();
        if (requests.isEmpty()) {
            System.out.println("No requests in the system yet.");
            return;
        }
        System.out.println("\n── All Requests ──");
        for (int i = 0; i < requests.size(); i++) {
            Request r = requests.get(i);
            System.out.printf("%d. [%-8s]  %s  |  From: %-20s  |  \"%s\"%n",
                    i + 1,
                    r.getStatus(),
                    r.getRequestId(),
                    r.getSender().getName(),
                    r.getDescription());
        }
    }

    /**
     * Prompts the specialist to select a request and updates its status.
     *
     * @param newStatus the new status to apply
     */
    private void changeStatus(RequestStatus newStatus) {
        List<Request> requests = Database.getInstance().getTechRequests();
        if (requests.isEmpty()) {
            System.out.println("No requests available.");
            return;
        }
        viewRequests();
        System.out.print("Enter request number: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= requests.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            Request request = requests.get(idx);
            specialist.updateStatus(request, newStatus);
            System.out.println("Request " + request.getRequestId() + " is now " + newStatus);
            Database.getInstance().addLog(specialist.getName() + " updated request "
                    + request.getRequestId() + " to " + newStatus);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
}
