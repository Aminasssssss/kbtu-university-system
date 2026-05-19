package models.users;

import enums.Language;
import exceptions.CourseOverloadException;
import exceptions.FailLimitException;
import models.academic.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Student in the university system.
 * Students can register for courses (max 21 credits), view marks,
 * get transcripts, rate teachers, and cannot fail more than 3 times.
 * Implements Comparable for sorting by GPA.
 */
public class Student extends User implements Comparable<Student> {

    private static final long serialVersionUID = 1L;

    /** Maximum allowed credits per semester. */
    public static final int MAX_CREDITS = 21;

    /** Maximum number of times a student can fail a course. */
    public static final int MAX_FAILS = 3;

    /** Grade Point Average. */
    private double gpa;

    /** Current number of registered credits. */
    private int credits;

    /** Number of failed courses so far. */
    private int failCount;

    /** List of courses this student is currently enrolled in. */
    private List<Course> registeredCourses;

    /**
     * Creates a Student.
     *
     * @param id        unique user ID
     * @param name      full name
     * @param email     email address
     * @param password  login password (will be hashed)
     * @param language  preferred language
     * @param gpa       initial grade point average
     * @param credits   current number of credits
     * @param failCount current fail count
     */
    public Student(String id, String name, String email, String password,
                   Language language, double gpa, int credits, int failCount) {
        super(id, name, email, password, language);
        this.gpa = gpa;
        this.credits = credits;
        this.failCount = failCount;
        this.registeredCourses = new ArrayList<>();
    }

    /**
     * Registers this student for a course by adding its credits to the total.
     * Throws CourseOverloadException if the result would exceed MAX_CREDITS (21).
     *
     * @param creditAmount the number of credits to add
     * @throws CourseOverloadException if total would exceed 21
     */
    public void registerForCourse(int creditAmount) throws CourseOverloadException {
        if (this.credits + creditAmount > MAX_CREDITS) {
            throw new CourseOverloadException(
                "Cannot register: adding " + creditAmount + " credits would exceed the limit of "
                + MAX_CREDITS + ". Current credits: " + this.credits
            );
        }
        this.credits += creditAmount;
    }

    /**
     * Drops a course, reducing the student's credit count.
     * Does nothing if the course is not in the registered list.
     *
     * @param course the course to drop
     */
    public void dropCourse(Course course) {
        if (registeredCourses.contains(course)) {
            registeredCourses.remove(course);
            this.credits -= course.getCredits();
            if (this.credits < 0) {
                this.credits = 0;
            }
        }
    }

    /**
     * Checks whether this student is already enrolled in the given course.
     *
     * @param course the course to check
     * @return true if already enrolled
     */
    public boolean isEnrolledIn(Course course) {
        return registeredCourses.contains(course);
    }

    /**
     * Adds a course to the student's registered course list.
     * Called alongside course.addStudent(student).
     *
     * @param course the course to add
     */
    public void addRegisteredCourse(Course course) {
        if (!registeredCourses.contains(course)) {
            registeredCourses.add(course);
        }
    }

    /**
     * Returns all courses this student is currently enrolled in.
     *
     * @return list of registered courses
     */
    public List<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    /**
     * Increments the fail count by one.
     * Throws FailLimitException if the count exceeds MAX_FAILS (3).
     *
     * @throws FailLimitException if the student has failed too many times
     */
    public void incrementFailCount() throws FailLimitException {
        failCount++;
        if (failCount > MAX_FAILS) {
            throw new FailLimitException(
                getName() + " has exceeded the maximum number of allowed course failures (" + MAX_FAILS + ")."
            );
        }
    }

    /**
     * Updates the student's GPA. Called after each new mark is recorded.
     *
     * @param newGpa the updated GPA value
     */
    public void setGpa(double newGpa) {
        this.gpa = newGpa;
    }

    /**
     * Returns the student's current GPA.
     *
     * @return grade point average
     */
    public double getGpa() {
        return gpa;
    }

    /**
     * Returns the student's current credit count.
     *
     * @return number of credits registered
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Returns the number of times this student has failed a course.
     *
     * @return fail count
     */
    public int getFailCount() {
        return failCount;
    }

    /** Placeholder — actual display is handled by StudentMenu. */
    public void viewMarks() {
        System.out.println("Viewing marks for " + getName());
    }

    /** Placeholder — actual generation is handled by Transcript. */
    public void getTranscript() {
        System.out.println("Getting transcript for " + getName());
    }

    /** Placeholder — actual rating is handled by StudentMenu. */
    public void rateTeacher() {
        System.out.println("Rate teacher option for " + getName());
    }

    @Override
    public int compareTo(Student o) {
        return Double.compare(this.gpa, o.gpa);
    }

    /**
     * Two students are equal if they have the same ID and email.
     * Mutable fields like GPA and credits are NOT compared
     * because the same student can exist as different instances
     * across serialized objects (Database vs CourseRegistry).
     */
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
        return "Student{"
                + "user=" + super.toString()
                + ", gpa=" + gpa
                + ", credits=" + credits
                + ", failCount=" + failCount
                + '}';
    }
}
