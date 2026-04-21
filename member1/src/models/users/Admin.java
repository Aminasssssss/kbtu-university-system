package models.users;

import enums.Language;
import database.Database;

public class Admin extends Employee {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates admin.
     */
    public Admin(String id, String name, String email, String password,
                 Language language, double salary, String department) {
        super(id, name, email, password, language, salary, department);
    }

    /**
     * Manages users.
     */
    public void manageUsers() {
        System.out.println("Managing users");
    }

    /**
     * Views logs.
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
