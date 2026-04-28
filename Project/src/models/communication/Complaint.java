package models.communication;

import enums.UrgencyLevel;
import models.users.Student;
import models.users.Teacher;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a complaint submitted by a teacher about a student.
 * The complaint is sent to the dean and has an urgency level: LOW, MEDIUM, or HIGH.
 */
public class Complaint implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique complaint identifier. */
    private String complaintId;

    /** The teacher who submitted the complaint. */
    private Teacher submittedBy;

    /** The student being reported. */
    private Student reportedStudent;

    /** Description of the complaint. */
    private String description;

    /** Urgency level of the complaint. */
    private UrgencyLevel urgencyLevel;

    /** Timestamp when the complaint was submitted. */
    private LocalDateTime submittedAt;

    /** Whether the complaint has been resolved. */
    private boolean isResolved;

    /** The dean's response to the complaint. */
    private String deanResponse;

    /**
     * Creates a new complaint.
     * @param complaintId unique complaint identifier
     * @param submittedBy the teacher submitting the complaint
     * @param reportedStudent the student being reported
     * @param description description of the complaint
     * @param urgencyLevel urgency level
     */
    public Complaint(String complaintId, Teacher submittedBy,
                     Student reportedStudent, String description, UrgencyLevel urgencyLevel) {
        this.complaintId = complaintId;
        this.submittedBy = submittedBy;
        this.reportedStudent = reportedStudent;
        this.description = description;
        this.urgencyLevel = urgencyLevel;
        this.submittedAt = LocalDateTime.now();
        this.isResolved = false;
    }

    /**
     * Resolves the complaint with a dean's response.
     * @param response the dean's response
     */
    public void resolve(String response) {
        this.isResolved = true;
        this.deanResponse = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Complaint)) return false;
        Complaint complaint = (Complaint) o;
        return Objects.equals(complaintId, complaint.complaintId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(complaintId);
    }

    @Override
    public String toString() {
        return String.format("Complaint{id='%s', teacher='%s', student='%s', urgency=%s, resolved=%b}",
                complaintId, submittedBy.getName(), reportedStudent.getName(),
                urgencyLevel, isResolved);
    }


    public String getComplaintId() { return complaintId; }

    public Teacher getSubmittedBy() { return submittedBy; }

    public Student getReportedStudent() { return reportedStudent; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public UrgencyLevel getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }

    public boolean isResolved() { return isResolved; }

    public String getDeanResponse() { return deanResponse; }
}