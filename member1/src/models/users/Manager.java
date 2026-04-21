package models.users;

import enums.Language;
import enums.ManagerType;

public class Manager extends Employee {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ManagerType type;

    /**
     * Creates manager.
     */
    public Manager(String id, String name, String email, String password,
                   Language language, double salary, String department,
                   ManagerType type) {
        super(id, name, email, password, language, salary, department);
        this.setType(type);
    }

    /**
     * Assigns course.
     */
    public void assignCourse(Object course, Teacher teacher) {
        System.out.println("Course assigned");
    }

    /**
     * Approves registration.
     */
    public void approveRegistration(Student student, Object course) {
        System.out.println("Approved");
    }

    /**
     * Creates report.
     */
    public void createReport(Object students) {
        System.out.println("Report created");
    }

	public ManagerType getType() {
		return type;
	}

	public void setType(ManagerType type) {
		this.type = type;
	}
}
