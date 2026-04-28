package models.academic;

import models.research.ResearchPaper;
import models.research.Researcher;
import models.users.GraduateStudent;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a diploma project (thesis) for a graduate student (Master or PhD).
 * Graduated students must have a list of published research papers as diploma projects.
 * The project is supervised by a Researcher whose h-index must be >= 3.
 */
public class DiplomaProject implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique project identifier. */
    private String projectId;

    /** Title of the diploma project. */
    private String title;

    /** The graduate student working on this project. */
    private GraduateStudent student;

    /** The research supervisor for this project. */
    private Researcher supervisor;

    /** Published research papers that form part of this diploma project. */
    private List<ResearchPaper> publishedPapers;

    /** Abstract text of the project. */
    private String abstractText;

    /** Date when the project was submitted. */
    private LocalDate submittedDate;

    /** Whether the project has been approved. */
    private boolean isApproved;

    /** The grade received for the defense. */
    private String defenseGrade;

    /**
     * Creates a new diploma project.
     * @param projectId unique project identifier
     * @param title the project title
     * @param student the graduate student
     * @param supervisor the research supervisor
     */
    public DiplomaProject(String projectId, String title, GraduateStudent student, Researcher supervisor) {
        this.projectId = projectId;
        this.title = title;
        this.student = student;
        this.supervisor = supervisor;
        this.publishedPapers = new ArrayList<>();
        this.submittedDate = LocalDate.now();
        this.isApproved = false;
    }

    /**
     * Adds a research paper to the diploma project's list of publications.
     * @param paper the research paper to add
     */
    public void addPublishedPaper(ResearchPaper paper) {
        if (!publishedPapers.contains(paper)) {
            publishedPapers.add(paper);
        }
    }

    /**
     * Approves the diploma project after successful defense.
     * @param grade the defense grade
     */
    public void approve(String grade) {
        this.isApproved = true;
        this.defenseGrade = grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiplomaProject)) return false;
        DiplomaProject that = (DiplomaProject) o;
        return Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId);
    }

    @Override
    public String toString() {
        String supervisorName = (supervisor instanceof models.users.User)
                ? ((models.users.User) supervisor).getName()
                : supervisor.toString();
        return String.format("DiplomaProject{id='%s', title='%s', student='%s', supervisor='%s', approved=%b}",
                projectId, title, student.getName(), supervisorName, isApproved);
    }


    public String getProjectId() { return projectId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public GraduateStudent getStudent() { return student; }

    public Researcher getSupervisor() { return supervisor; }
    public void setSupervisor(Researcher supervisor) { this.supervisor = supervisor; }

    public List<ResearchPaper> getPublishedPapers() { return publishedPapers; }

    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }

    public LocalDate getSubmittedDate() { return submittedDate; }

    public boolean isApproved() { return isApproved; }

    public String getDefenseGrade() { return defenseGrade; }
}