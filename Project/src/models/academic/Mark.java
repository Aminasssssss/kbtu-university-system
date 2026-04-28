package models.academic;

import models.users.Student;
import models.users.Teacher;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the mark (grade) for a student in a specific course.
 * A mark consists of three components: 1st attestation, 2nd attestation, and final exam.
 * Students cannot fail a course more than 3 times total.
 */
public class Mark implements Serializable, Comparable<Mark> {

    private static final long serialVersionUID = 1L;

    /** The student being graded. */
    private Student student;

    /** The course the mark is for. */
    private Course course;

    /** The teacher who assigned this mark. */
    private Teacher assignedBy;

    /** Score for the 1st attestation (0-100). */
    private double firstAttestation;

    /** Score for the 2nd attestation (0-100). */
    private double secondAttestation;

    /** Final exam score (0-100). */
    private double finalExam;

    /**
     * Creates a new mark.
     * @param student the student
     * @param course the course
     * @param assignedBy the teacher assigning the mark
     */
    public Mark(Student student, Course course, Teacher assignedBy) {
        this.student = student;
        this.course = course;
        this.assignedBy = assignedBy;
    }

    /**
     * Returns the total weighted score.
     * Attestations each contribute 30%, final exam contributes 40%.
     * @return total score as percentage
     */
    public double getTotal() {
        return (firstAttestation * 0.3) + (secondAttestation * 0.3) + (finalExam * 0.4);
    }

    /**
     * Returns the letter grade based on total score.
     * @return letter grade (A, B, C, D, or F)
     */
    public String getLetterGrade() {
        double total = getTotal();
        if (total >= 90) return "A";
        if (total >= 80) return "B";
        if (total >= 70) return "C";
        if (total >= 60) return "D";
        return "F";
    }

    /**
     * Returns the GPA point for this mark (4.0 scale).
     * @return GPA points
     */
    public double getGpaPoints() {
        double total = getTotal();
        if (total >= 90) return 4.0;
        if (total >= 80) return 3.0;
        if (total >= 70) return 2.0;
        if (total >= 60) return 1.0;
        return 0.0;
    }

    /**
     * Returns true if the student failed this course (score < 50).
     * @return true if failed
     */
    public boolean isFailed() {
        return getTotal() < 50;
    }

    @Override
    public int compareTo(Mark other) {
        return Double.compare(other.getTotal(), this.getTotal());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mark)) return false;
        Mark mark = (Mark) o;
        return Objects.equals(student, mark.student) && Objects.equals(course, mark.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }

    @Override
    public String toString() {
        return String.format("Mark{student='%s', course='%s', att1=%.1f, att2=%.1f, final=%.1f, total=%.2f (%s)}",
                student.getName(), course.getName(),
                firstAttestation, secondAttestation, finalExam,
                getTotal(), getLetterGrade());
    }


    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Teacher getAssignedBy() { return assignedBy; }
    public void setAssignedBy(Teacher assignedBy) { this.assignedBy = assignedBy; }

    public double getFirstAttestation() { return firstAttestation; }
    public void setFirstAttestation(double firstAttestation) {
        if (firstAttestation < 0 || firstAttestation > 100)
            throw new IllegalArgumentException("Score must be between 0 and 100.");
        this.firstAttestation = firstAttestation;
    }

    public double getSecondAttestation() { return secondAttestation; }
    public void setSecondAttestation(double secondAttestation) {
        if (secondAttestation < 0 || secondAttestation > 100)
            throw new IllegalArgumentException("Score must be between 0 and 100.");
        this.secondAttestation = secondAttestation;
    }

    public double getFinalExam() { return finalExam; }
    public void setFinalExam(double finalExam) {
        if (finalExam < 0 || finalExam > 100)
            throw new IllegalArgumentException("Score must be between 0 and 100.");
        this.finalExam = finalExam;
    }
}