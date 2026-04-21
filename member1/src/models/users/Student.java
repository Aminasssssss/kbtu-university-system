package models.users;

import enums.Language;
import exceptions.CourseOverloadException;
import exceptions.FailLimitException;

import java.util.Objects;

public class Student extends User implements Comparable<Student> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double gpa;
    private int credits;
    private int failCount;

    /**
     * Creates student.
     */
    public Student(String id, String name, String email, String password,
                   Language language, double gpa, int credits, int failCount) {
        super(id, name, email, password, language);
        this.gpa = gpa;
        this.credits = credits;
        this.failCount = failCount;
    }

    /**
     * Adds credits.
     */
    public void registerForCourse(int credits) throws CourseOverloadException {
        if (this.credits + credits > 21) {
            throw new CourseOverloadException("Too many credits");
        }
        this.credits += credits;
    }

    /**
     * Increases fail count.
     */
    public void incrementFailCount() throws FailLimitException {
        failCount++;
        if (failCount > 3) {
            throw new FailLimitException("Too many fails");
        }
    }

    /**
     * Returns GPA.
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
