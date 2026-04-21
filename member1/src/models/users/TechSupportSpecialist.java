package models.users;

import enums.Language;
import enums.RequestStatus;

public class TechSupportSpecialist extends Employee {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates tech support specialist.
     */
    public TechSupportSpecialist(String id, String name, String email, String password,
                                 Language language, double salary, String department) {
        super(id, name, email, password, language, salary, department);
    }

    /**
     * Views requests.
     */
    public void viewRequests() {
        System.out.println("Viewing requests");
    }

    /**
     * Updates status.
     */
    public void updateStatus(Object request, RequestStatus status) {
        System.out.println("Updated");
    }
}
