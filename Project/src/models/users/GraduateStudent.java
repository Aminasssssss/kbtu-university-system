package models.users;

import enums.DegreeType;
import enums.Language;
import models.academic.DiplomaProject;
import models.research.Researcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a graduate student (Master or PhD) in the university system.
 * Graduate students have a degree type, a research supervisor, and diploma projects.
 */
public class GraduateStudent extends Student {

    private static final long serialVersionUID = 1L;

    /** Degree type: MASTER or PHD. */
    private DegreeType degree;

    /** Research supervisor. */
    private Researcher supervisor;
    
    /** List of diploma projects. */
    private List<DiplomaProject> diplomaProjects;

    /**
     * Creates a graduate student.
     * @param id unique user ID
     * @param name full name
     * @param email email address
     * @param password login password
     * @param language preferred language
     * @param gpa grade point average
     * @param credits current credits
     * @param failCount number of failed courses
     * @param degree MASTER or PHD
     * @param supervisor research supervisor
     */
    public GraduateStudent(String id, String name, String email, String password,
                           Language language, double gpa, int credits, int failCount,
                           DegreeType degree, Researcher supervisor) {
        super(id, name, email, password, language, gpa, credits, failCount);
        this.degree = degree;
        this.supervisor = supervisor;
        this.diplomaProjects = new ArrayList<>();
    }

    /**
     * Returns the degree type.
     * @return MASTER or PHD
     */
    public DegreeType getDegree() {
        return degree;
    }

    /**
     * Returns the research supervisor.
     * @return supervisor
     */
    public Researcher getSupervisor() {
        return supervisor;
    }

    /**
     * Sets a new research supervisor.
     * @param supervisor the new supervisor
     */
    public void setSupervisor(Researcher supervisor) {
        this.supervisor = supervisor;
    }

    /**
     * Adds a diploma project to the graduate student's list.
     * @param project the diploma project to add
     */
    public void addDiplomaProject(DiplomaProject project) {
        diplomaProjects.add(project);
    }
    
    /**
     * Returns the list of diploma projects.
     * @return list of diploma projects
     */
    public List<DiplomaProject> getDiplomaProjects() {
        return diplomaProjects;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        GraduateStudent that = (GraduateStudent) o;
        return degree == that.degree &&
                Objects.equals(supervisor, that.supervisor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), degree, supervisor);
    }

    @Override
    public String toString() {
        return "GraduateStudent{" +
                "student=" + super.toString() +
                ", degree=" + degree +
                ", supervisor=" + supervisor +
                '}';
    }
}