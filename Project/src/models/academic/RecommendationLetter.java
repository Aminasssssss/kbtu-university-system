package models.academic;

import models.users.Student;
import models.users.Teacher;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an official recommendation letter written by a teacher for a student.
 * This is a bonus feature for advanced system completeness.
 */
public class RecommendationLetter implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique letter identifier. */
    private String letterId;

    /** The student receiving the recommendation. */
    private Student recipient;

    /** The teacher writing the recommendation. */
    private Teacher author;

    /** Purpose of the recommendation (e.g., "Graduate School Application"). */
    private String purpose;

    /** Content text of the letter. */
    private String content;

    /** Date when the letter was issued. */
    private LocalDate date;

    /** Whether the letter has been signed. */
    private boolean isSigned;

    /**
     * Creates a new recommendation letter.
     * @param letterId unique letter identifier
     * @param recipient the student receiving the letter
     * @param author the teacher writing the letter
     * @param purpose the purpose of the recommendation
     */
    public RecommendationLetter(String letterId, Student recipient, Teacher author, String purpose) {
        this.letterId = letterId;
        this.recipient = recipient;
        this.author = author;
        this.purpose = purpose;
        this.date = LocalDate.now();
        this.isSigned = false;
    }

    /**
     * Generates the recommendation letter content.
     * @return the generated letter as a string
     */
    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════\n");
        sb.append("          LETTER OF RECOMMENDATION\n");
        sb.append("═══════════════════════════════════════════════\n");
        sb.append("Date    : ").append(date).append("\n");
        sb.append("To Whom It May Concern,\n\n");
        sb.append("Purpose : ").append(purpose).append("\n\n");
        sb.append(content != null ? content : "[Content not yet written]").append("\n\n");
        sb.append("Sincerely,\n").append(author.getName()).append("\n");
        if (isSigned) sb.append("[Signed ✓]\n");
        sb.append("═══════════════════════════════════════════════\n");
        return sb.toString();
    }

    /**
     * Signs the letter, making it official.
     */
    public void sign() {
        this.isSigned = true;
    }

    /**
     * Prints the recommendation letter to standard output.
     */
    public void print() {
        System.out.println(generate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecommendationLetter)) return false;
        RecommendationLetter that = (RecommendationLetter) o;
        return Objects.equals(letterId, that.letterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(letterId);
    }

    @Override
    public String toString() {
        return String.format("RecommendationLetter{id='%s', recipient='%s', author='%s', signed=%b}",
                letterId, recipient.getName(), author.getName(), isSigned);
    }


    public String getLetterId() { return letterId; }

    public Student getRecipient() { return recipient; }
    public void setRecipient(Student recipient) { this.recipient = recipient; }

    public Teacher getAuthor() { return author; }
    public void setAuthor(Teacher author) { this.author = author; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public boolean isSigned() { return isSigned; }
}