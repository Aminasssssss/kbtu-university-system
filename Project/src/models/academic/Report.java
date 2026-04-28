package models.academic;

import enums.ReportType;
import models.users.Student;
import models.users.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an official academic or administrative report.
 * Reports can cover academic performance statistics, room bookings, events, etc.
 * Corresponds to "Working official messages" about events inside the university.
 */
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique report identifier. */
    private String reportId;

    /** Type of the report. */
    private ReportType reportType;

    /** The user who generated this report. */
    private User generatedBy;

    /** Timestamp when the report was generated. */
    private LocalDateTime generatedAt;

    /** Report title. */
    private String title;

    /** List of students included in the report. */
    private List<Student> students;

    /** Course the report is about. */
    private Course course;

    /** Report content. */
    private String content;

    /** Average grade for academic performance reports. */
    private double averageGrade;

    /** For event-related reports: the referenced resource (e.g., a room booking). */
    private String relatedResource;

    /**
     * Creates a new report.
     * @param reportId unique report identifier
     * @param reportType type of report
     * @param generatedBy the user generating the report
     * @param title report title
     */
    public Report(String reportId, ReportType reportType, User generatedBy, String title) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.generatedBy = generatedBy;
        this.title = title;
        this.students = new ArrayList<>();
        this.generatedAt = LocalDateTime.now();
    }

    /**
     * Generates the report content.
     */
    public void generate() {
        this.generatedAt = LocalDateTime.now();
        System.out.println("Report generated: " + title);
    }

    /**
     * Returns statistics about the report.
     * @return statistics string
     */
    public String getStatistics() {
        return "Report: " + title + " | Type: " + reportType + 
               " | Generated: " + generatedAt + 
               " | Students: " + students.size() +
               " | Average Grade: " + averageGrade;
    }

    /**
     * Prints the report to standard output.
     */
    public void print() {
        System.out.println("═══════════════════════════════════════════════");
        System.out.printf("  REPORT: %s%n", title.toUpperCase());
        System.out.println("═══════════════════════════════════════════════");
        System.out.printf("ID        : %s%n", reportId);
        System.out.printf("Type      : %s%n", reportType);
        System.out.printf("Course    : %s%n", course != null ? course.getName() : "N/A");
        System.out.printf("Students  : %d%n", students.size());
        System.out.printf("Generated : %s%n", generatedAt);
        System.out.printf("By        : %s%n", generatedBy.getName());
        System.out.printf("Avg Grade : %.2f%n", averageGrade);
        if (relatedResource != null) {
            System.out.printf("Resource  : %s%n", relatedResource);
        }
        System.out.println("───────────────────────────────────────────────");
        System.out.println(content != null ? content : "[No content]");
        System.out.println("═══════════════════════════════════════════════");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report)) return false;
        Report report = (Report) o;
        return Objects.equals(reportId, report.reportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportId);
    }

    @Override
    public String toString() {
        return String.format("Report{id='%s', type=%s, title='%s', generatedAt=%s}",
                reportId, reportType, title, generatedAt);
    }


    public String getReportId() { return reportId; }

    public ReportType getReportType() { return reportType; }
    public void setReportType(ReportType reportType) { this.reportType = reportType; }

    public User getGeneratedBy() { return generatedBy; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public double getAverageGrade() { return averageGrade; }
    public void setAverageGrade(double averageGrade) { this.averageGrade = averageGrade; }

    public String getRelatedResource() { return relatedResource; }
    public void setRelatedResource(String relatedResource) { this.relatedResource = relatedResource; }
}