package models.users;

import enums.Language;
import models.communication.Complaint;
import models.communication.Request;
import database.Database;

/**
 * Represents a Dean in the university system.
 * Handles complaints and signs requests.
 */
public class Dean extends Employee {

    private static final long serialVersionUID = 1L;
    
    /** Dean level: SCHOOL or UNIVERSITY. */
    private String deanLevel;

    /**
     * Creates a Dean.
     * @param id unique user ID
     * @param name full name
     * @param email email address
     * @param password login password
     * @param language preferred language
     * @param salary employee salary
     * @param department department name
     * @param deanLevel SCHOOL or UNIVERSITY
     */
    public Dean(String id, String name, String email, String password,
                Language language, double salary, String department,
                String deanLevel) {
        super(id, name, email, password, language, salary, department);
        this.deanLevel = deanLevel;
    }

    /**
     * Returns the dean level.
     * @return SCHOOL or UNIVERSITY
     */
    public String getDeanLevel() {
        return deanLevel;
    }

    /**
     * Sets the dean level.
     * @param deanLevel SCHOOL or UNIVERSITY
     */
    public void setDeanLevel(String deanLevel) {
        this.deanLevel = deanLevel;
    }

    /**
     * Views all complaints assigned to this dean.
     */
    public void viewComplaints() {
        System.out.println("Dean " + getName() + " viewing complaints...");
        Database.getInstance().addLog("Dean " + getName() + " viewed complaints");
    }

    /**
     * Resolves a specific complaint.
     * @param complaint the complaint to resolve
     */
    public void resolveComplaint(Complaint complaint) {
        System.out.println("Complaint resolved by Dean " + getName());
        Database.getInstance().addLog("Dean " + getName() + " resolved complaint: " + complaint.toString());
    }

    /**
     * Signs a request.
     * @param request the request to sign
     */
    public void signRequest(Request request) {
        System.out.println("Request signed by Dean " + getName());
        Database.getInstance().addLog("Dean " + getName() + " signed request: " + request.toString());
    }

    @Override
    public String toString() {
        return "Dean{" +
                super.toString() +
                ", deanLevel='" + deanLevel + '\'' +
                '}';
    }
}