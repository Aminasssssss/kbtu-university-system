package models.users;

import enums.Language;
import enums.ManagerType;
import models.academic.Course;
import java.util.List;

/**
 * Represents a Manager in the university system.
 * Managers assign courses to teachers, approve student registrations, and create reports.
 */
public class Manager extends Employee {

    private static final long serialVersionUID = 1L;

    /** Manager type: OR or DEPARTMENT. */
    private ManagerType type;

    /**
     * Creates a Manager.
     * @param id unique user ID
     * @param name full name
     * @param email email address
     * @param password login password
     * @param language preferred language
     * @param salary employee salary
     * @param department department name
     * @param type manager type
     */
    public Manager(String id, String name, String email, String password,
                   Language language, double salary, String department,
                   ManagerType type) {
        super(id, name, email, password, language, salary, department);
        this.setType(type);
    }

    /**
     * Assigns a course to a teacher.
     * @param course the course to assign
     * @param teacher the teacher to assign to
     */
    public void assignCourse(Course course, Teacher teacher) {
        System.out.println("Course assigned: " + course.getName() + " to " + teacher.getName());
    }

    /**
     * Approves a student's registration for a course.
     * @param student the student
     * @param course the course
     */
    public void approveRegistration(Student student, Course course) {
        System.out.println("Approved " + student.getName() + " for " + course.getName());
    }

    /**
     * Creates a statistical report on students.
     * @param students list of students
     */
    public void createReport(List<Student> students) {
        System.out.println("Report created for " + students.size() + " students");
    }

    /**
     * Returns the manager type.
     * @return OR or DEPARTMENT
     */
    public ManagerType getType() {
        return type;
    }

    /**
     * Sets the manager type.
     * @param type OR or DEPARTMENT
     */
    public void setType(ManagerType type) {
        this.type = type;
    }
}