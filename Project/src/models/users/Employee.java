package models.users;

import enums.Language;

/**
 * Abstract class representing an employee in the university system.
 * Extends User with salary and department fields.
 */
public abstract class Employee extends User {

    private static final long serialVersionUID = 1L;

    /** Employee salary. */
    private double salary;

    /** Department where the employee works. */
    private String department;

    /**
     * Creates an Employee.
     * @param id unique user ID
     * @param name full name
     * @param email email address
     * @param password login password
     * @param language preferred language
     * @param salary employee salary
     * @param department department name
     */
    public Employee(String id, String name, String email, String password,
                    Language language, double salary, String department) {
        super(id, name, email, password, language);
        this.salary = salary;
        this.department = department;
    }

    /**
     * Returns the employee's salary.
     * @return salary
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Returns the employee's department.
     * @return department name
     */
    public String getDepartment() {
        return department;
    }
}