package models.academic;

import models.users.Student;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Official academic transcript for a student.
 * Aggregates all marks across all semesters and computes cumulative GPA.
 */
public class Transcript implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The student this transcript belongs to. */
    private Student student;

    /** List of marks included in the transcript. */
    private List<Mark> marks;

    /** Date when the transcript was generated. */
    private LocalDate generatedDate;

    /**
     * Creates a new transcript for a student.
     * @param student the student
     */
    public Transcript(Student student) {
        this.student = student;
        this.marks = new ArrayList<>();
        this.generatedDate = LocalDate.now();
    }

    /**
     * Adds a mark entry to the transcript.
     * @param mark the mark to add
     */
    public void addMark(Mark mark) {
        marks.add(mark);
    }

    /**
     * Computes the cumulative GPA across all courses.
     * GPA = sum(gpaPoints * credits) / sum(credits)
     * @return the cumulative GPA
     */
    public double computeGPA() {
        double totalWeighted = 0;
        int totalCredits = 0;
        for (Mark mark : marks) {
            int credits = mark.getCourse().getCredits();
            totalWeighted += mark.getGpaPoints() * credits;
            totalCredits += credits;
        }
        return totalCredits == 0 ? 0.0 : totalWeighted / totalCredits;
    }

    /**
     * Returns the total number of credits earned (passed courses only).
     * @return total earned credits
     */
    public int earnedCredits() {
        return marks.stream()
                .filter(m -> !m.isFailed())
                .mapToInt(m -> m.getCourse().getCredits())
                .sum();
    }

    /**
     * Generates and prints a formatted transcript to standard output.
     */
    public void generate() {
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("           ACADEMIC TRANSCRIPT");
        System.out.println("═══════════════════════════════════════════════");
        System.out.printf("Student : %s%n", student.getName());
        System.out.printf("ID      : %s%n", student.getId());
        System.out.printf("Date    : %s%n", generatedDate);
        System.out.println("───────────────────────────────────────────────");
        System.out.printf("%-30s %-4s %-6s %-6s%n", "Course", "Cr", "Score", "Grade");
        System.out.println("───────────────────────────────────────────────");
        for (Mark m : marks) {
            System.out.printf("%-30s %-4d %-6.2f %-6s%n",
                    m.getCourse().getName(),
                    m.getCourse().getCredits(),
                    m.getTotal(),
                    m.getLetterGrade());
        }
        System.out.println("───────────────────────────────────────────────");
        System.out.printf("Cumulative GPA   : %.2f%n", computeGPA());
        System.out.printf("Total Credits    : %d%n", earnedCredits());
        System.out.println("═══════════════════════════════════════════════");
    }


    public Student getStudent() { return student; }

    public List<Mark> getMarks() { return marks; }

    public LocalDate getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(LocalDate generatedDate) { this.generatedDate = generatedDate; }

    @Override
    public String toString() {
        return String.format("Transcript{student='%s', courses=%d, GPA=%.2f}",
                student.getName(), marks.size(), computeGPA());
    }
}