package models.users;

import enums.Language;
import database.Database;

/**
 * Represents an Admin user in the university system.
 * Admins can manage users and view system logs.
 */
public class Admin extends Employee {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an Admin.
     * @param id unique user ID
     * @param name full name
     * @param email email address
     * @param password login password
     * @param language preferred language
     * @param salary employee salary
     * @param department department name
     */
    public Admin(String id, String name, String email, String password,
                 Language language, double salary, String department) {
        super(id, name, email, password, language, salary, department);
    }

    /**
     * Manages users in the system.
     */
    public void manageUsers() {
        System.out.println("Managing users");
    }

    /**
     * Views the system activity logs.
     */
    public void viewLogs() {
        System.out.println(Database.getInstance().getLogs());
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Admin{" +
                "employee=" + super.toString() +
                '}';
    }
}