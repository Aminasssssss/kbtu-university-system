package models.users;

import enums.Language;
import enums.TeacherPosition;
import enums.UrgencyLevel;
import models.academic.Course;
import models.academic.Mark;

import java.util.List;

/**
 * Represents a Teacher in the university system.
 * Teachers can put marks, send complaints, and view their assigned courses.
 */
public class Teacher extends Employee {

    private static final long serialVersionUID = 1L;

    /** Teacher's position: TUTOR, LECTOR, SENIOR_LECTOR, or PROFESSOR. */
    private TeacherPosition position;

    /**
     * Creates a Teacher.
     * @param id unique user ID
     * @param name full name
     * @param email email address
     * @param password login password
     * @param language preferred language
     * @param salary employee salary
     * @param department department name
     * @param position teacher position
     */
    public Teacher(String id, String name, String email, String password,
                   Language language, double salary, String department,
                   TeacherPosition position) {
        super(id, name, email, password, language, salary, department);
        this.setPosition(position);
    }

    /**
     * Puts a mark for a student in a course.
     * @param student the student
     * @param course the course
     * @param mark the mark
     */
    public void putMark(Student student, Course course, Mark mark) {
        System.out.println("Mark added for " + student.getName() + " in " + course.getName());
    }

    /**
     * Sends a complaint about a student with a given urgency level.
     * @param student the student
     * @param level urgency level
     * @param text complaint description
     */
    public void sendComplaint(Student student, UrgencyLevel level, String text) {
        System.out.println("Complaint sent about " + student.getName() + " with urgency " + level);
    }

    /**
     * Views all courses assigned to this teacher.
     * Delegates to CourseRegistry to get the actual list.
     */
    public void viewCourses() {
        System.out.println("Viewing courses for " + getName());
        List<Course> courses = database.CourseRegistry.getInstance().getCoursesForTeacher(this);
        if (courses.isEmpty()) {
            System.out.println("No courses assigned.");
        } else {
            for (Course c : courses) {
                System.out.println("  - " + c.getName() + " (" + c.getCredits() + " credits)");
            }
        }
    }

    /**
     * Returns the teacher's position.
     * @return TUTOR, LECTOR, SENIOR_LECTOR, or PROFESSOR
     */
    public TeacherPosition getPosition() {
        return position;
    }

    /**
     * Sets the teacher's position.
     * @param position the position
     */
    public void setPosition(TeacherPosition position) {
        this.position = position;
    }
}