package models.users;

import enums.DegreeType;
import enums.Language;
import models.research.Researcher;

import java.util.Objects;

public class GraduateStudent extends Student {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DegreeType degree;
    private Researcher supervisor;

    /**
     * Creates graduate student.
     */
    public GraduateStudent(String id, String name, String email, String password,
                           Language language, double gpa, int credits, int failCount,
                           DegreeType degree, Researcher supervisor) {
        super(id, name, email, password, language, gpa, credits, failCount);
        this.degree = degree;
        this.supervisor = supervisor;
    }

    /**
     * Returns degree.
     */
    public DegreeType getDegree() {
        return degree;
    }
    
    /**
     * Returns supervisor.
     */
    public Researcher getSupervisor() {
        return supervisor;
    }
    
    /**
     * Sets supervisor.
     */
    public void setSupervisor(Researcher supervisor) {
        this.supervisor = supervisor;
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