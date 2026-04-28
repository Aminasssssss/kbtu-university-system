package models.users;

import enums.Language;
import exceptions.CourseOverloadException;
import exceptions.FailLimitException;

import java.util.Objects;

/**
 * Represents a Student in the university system.
 * Students can register for courses (max 21 credits), view marks, 
 * get transcripts, rate teachers, and cannot fail more than 3 times.
 * Implements Comparable for sorting by GPA.
 */
public class Student extends User implements Comparable<Student> {

    private static final long serialVersionUID = 1L;

    /** Grade Point Average. */
    private double gpa;

    /** Current number of credits. */
    private int credits;

    /** Number of failed courses. */
    private int failCount;

    /**
     * Creates a Student.
     * @param id unique user ID
     * @param name full name
     * @param email email address
     * @param password login password
     * @param language preferred language
     * @param gpa grade point average
     * @param credits current credits
     * @param failCount number of failed courses
     */
    public Student(String id, String name, String email, String password,
                   Language language, double gpa, int credits, int failCount) {
        super(id, name, email, password, language);
        this.gpa = gpa;
        this.credits = credits;
        this.failCount = failCount;
    }

    /**
     * Registers for a course by adding credits.
     * @param credits the number of credits to add
     * @throws CourseOverloadException if total credits exceed 21
     */
    public void registerForCourse(int credits) throws CourseOverloadException {
        if (this.credits + credits > 21) {
            throw new CourseOverloadException("Too many credits");
        }
        this.credits += credits;
    }

    /**
     * Increments the fail count.
     * @throws FailLimitException if fail count exceeds 3
     */
    public void incrementFailCount() throws FailLimitException {
        failCount++;
        if (failCount > 3) {
            throw new FailLimitException("Too many fails");
        }
    }

    /**
     * Views all marks for this student.
     */
    public void viewMarks() {
        System.out.println("Viewing marks for " + getName());
        // Delegates to menu layer for actual display
    }

    /**
     * Gets the student's transcript.
     */
    public void getTranscript() {
        System.out.println("Getting transcript for " + getName());
        // Delegates to Transcript class for actual generation
    }

    /**
     * Rates a teacher.
     */
    public void rateTeacher() {
        System.out.println("Rate teacher for " + getName());
        // Delegates to menu layer for actual rating input
    }

    /**
     * Returns the GPA.
     * @return grade point average
     */
    public double getGpa() {
        return gpa;
    }

    @Override
    public int compareTo(Student o) {
        return Double.compare(this.gpa, o.gpa);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Double.compare(gpa, student.gpa) == 0 &&
                credits == student.credits &&
                failCount == student.failCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gpa, credits, failCount);
    }

    @Override
    public String toString() {
        return "Student{" +
                "user=" + super.toString() +
                ", gpa=" + gpa +
                ", credits=" + credits +
                ", failCount=" + failCount +
                '}';
    }
}