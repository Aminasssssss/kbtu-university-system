package models.users;

import enums.Language;
import enums.TeacherPosition;
import enums.UrgencyLevel;

public class Teacher extends Employee {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TeacherPosition position;

    /**
     * Creates teacher.
     */
    public Teacher(String id, String name, String email, String password,
                   Language language, double salary, String department,
                   TeacherPosition position) {
        super(id, name, email, password, language, salary, department);
        this.setPosition(position);
    }

    /**
     * Puts mark.
     */
    public void putMark(Object student, Object course, Object mark) {
        System.out.println("Mark added");
    }

    /**
     * Sends complaint.
     */
    public void sendComplaint(Student student, UrgencyLevel level, String text) {
        System.out.println("Complaint sent");
    }

	public TeacherPosition getPosition() {
		return position;
	}

	public void setPosition(TeacherPosition position) {
		this.position = position;
	}
}