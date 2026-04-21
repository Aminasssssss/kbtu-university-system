package models.users;

import enums.Language;

public abstract class Employee extends User {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double salary;
    private String department;

    /**
     * Creates employee.
     */
    public Employee(String id, String name, String email, String password,
                    Language language, double salary, String department) {
        super(id, name, email, password, language);
        this.salary = salary;
        this.department = department;
    }

    /**
     * Returns salary.
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Returns department.
     */
    public String getDepartment() {
        return department;
    }
}
