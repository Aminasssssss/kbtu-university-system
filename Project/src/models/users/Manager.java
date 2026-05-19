package models.users;

import database.CourseRegistry;
import enums.Language;
import enums.ManagerType;
import models.academic.Course;

import java.util.List;

/**
 * Represents a Manager in the university system.
 * Managers assign courses to teachers, approve student registrations,
 * and create statistical reports.
 */
public class Manager extends Employee {

    private static final long serialVersionUID = 1L;

    private ManagerType type;

    /**
     * Creates a Manager.
     *
     * @param id         unique user ID
     * @param name       full name
     * @param email      email address
     * @param password   login password
     * @param language   preferred language
     * @param salary     employee salary
     * @param department department name
     * @param type       manager type (OR or DEPARTMENT)
     */
    public Manager(String id, String name, String email, String password,
                   Language language, double salary, String department,
                   ManagerType type) {
        super(id, name, email, password, language, salary, department);
        this.type = type;
    }

    /**
     * Assigns a course to a teacher via CourseRegistry.
     *
     * @param course  the course to assign
     * @param teacher the teacher to assign the course to
     */
    public void assignCourse(Course course, Teacher teacher) {
        CourseRegistry.getInstance().assignCourseToTeacher(course, teacher);
        System.out.println("Course assigned: " + course.getName() + " to " + teacher.getName());
    }

    /**
     * Approves a student's registration for a course.
     * Actually adds the student to the course and updates their credits.
     *
     * @param student the student to approve
     * @param course  the course to approve them for
     */
    public void approveRegistration(Student student, Course course) {
        boolean alreadyEnrolled = course.getStudents().contains(student);
        if (alreadyEnrolled) {
            System.out.println(student.getName() + " is already enrolled in " + course.getName());
            return;
        }
        course.addStudent(student);
        student.addRegisteredCourse(course);
        try {
            student.registerForCourse(course.getCredits());
        } catch (exceptions.CourseOverloadException e) {
            System.out.println("Warning: " + e.getMessage());
        }
        System.out.println("Approved: " + student.getName() + " for " + course.getName());
        database.Database.getInstance().addLog("Manager approved "
                + student.getName() + " for " + course.getName());
    }

    /**
     * Creates a statistical report on students.
     *
     * @param students list of students to include in the report
     */
    public void createReport(List<Student> students) {
        System.out.println("Report created for " + students.size() + " students.");
    }

    /**
     * Returns the manager type.
     *
     * @return OR or DEPARTMENT
     */
    public ManagerType getType() {
        return type;
    }

    /**
     * Sets the manager type.
     *
     * @param type the new type
     */
    public void setType(ManagerType type) {
        this.type = type;
    }
}
