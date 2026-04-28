package models.users;

import enums.Language;
import enums.RequestStatus;
import models.communication.Request;

/**
 * Represents a Tech Support Specialist in the university system.
 * Views and updates the status of tech support requests.
 */
public class TechSupportSpecialist extends Employee {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a TechSupportSpecialist.
     * @param id unique user ID
     * @param name full name
     * @param email email address
     * @param password login password
     * @param language preferred language
     * @param salary employee salary
     * @param department department name
     */
    public TechSupportSpecialist(String id, String name, String email, String password,
                                 Language language, double salary, String department) {
        super(id, name, email, password, language, salary, department);
    }

    /**
     * Views all tech support requests.
     */
    public void viewRequests() {
        System.out.println("Viewing requests");
    }

    /**
     * Updates the status of a tech support request.
     * @param request the request to update
     * @param status the new status
     */
    public void updateStatus(Request request, RequestStatus status) {
        request.setStatus(status);
        System.out.println("Updated request " + request.getRequestId() + " to " + status);
    }
}