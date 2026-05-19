package models.users;

import enums.DegreeType;
import enums.Language;
import exceptions.LowHIndexException;
import models.academic.DiplomaProject;
import models.research.Researcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a graduate student (Master or PhD).
 * Graduate students always have a research supervisor.
 * The supervisor must have h-index >= 3, otherwise LowHIndexException is thrown.
 */
public class GraduateStudent extends Student {

    private static final long serialVersionUID = 1L;

    private DegreeType degree;
    private Researcher supervisor;
    private List<DiplomaProject> diplomaProjects;

    public GraduateStudent(String id, String name, String email, String password,
                           Language language, double gpa, int credits, int failCount,
                           DegreeType degree, Researcher supervisor) {
        super(id, name, email, password, language, gpa, credits, failCount);
        this.degree = degree;
        this.supervisor = supervisor; // can be null initially
        this.diplomaProjects = new ArrayList<>();
    }

    public DegreeType getDegree() {
        return degree;
    }

    public Researcher getSupervisor() {
        return supervisor;
    }

    /**
     * Assigns a research supervisor to this graduate student.
     * Throws LowHIndexException if the supervisor's h-index is less than 3.
     *
     * @param supervisor the researcher to assign as supervisor
     * @throws LowHIndexException if h-index < 3
     */
    public void setSupervisor(Researcher supervisor) throws LowHIndexException {
        if (supervisor == null) {
            this.supervisor = null;
            return;
        }
        int hIndex = supervisor.calculateHIndex();
        if (hIndex < 3) {
            throw new LowHIndexException(
                "Cannot assign supervisor: h-index is " + hIndex +
                ", but minimum required is 3. Please choose a more experienced researcher."
            );
        }
        this.supervisor = supervisor;
        System.out.println("Supervisor assigned successfully (h-index: " + hIndex + ")");
    }

    public void addDiplomaProject(DiplomaProject project) {
        diplomaProjects.add(project);
    }

    public List<DiplomaProject> getDiplomaProjects() {
        return diplomaProjects;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        GraduateStudent that = (GraduateStudent) o;
        return degree == that.degree && Objects.equals(supervisor, that.supervisor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), degree, supervisor);
    }

    @Override
    public String toString() {
        String supName;
        if (supervisor instanceof User u) {
            supName = u.getName();
        } else {
            supName = "None";
        }
        return "GraduateStudent{" + super.toString() +
               ", degree=" + degree +
               ", supervisor=" + supName + "}";
    }
}
