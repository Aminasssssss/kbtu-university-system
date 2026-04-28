package models.academic;

import models.users.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a student organization (club, society, committee, etc.).
 * A student can be either a regular member or the head of an organization.
 */
public class StudentOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique organization identifier. */
    private String orgId;

    /** Name of the organization. */
    private String name;

    /** Description of the organization. */
    private String description;

    /** The student who leads this organization. */
    private Student head;

    /** List of student members. */
    private List<Student> members;

    /**
     * Creates a new student organization.
     * @param orgId unique organization identifier
     * @param name organization name
     */
    public StudentOrganization(String orgId, String name) {
        this.orgId = orgId;
        this.name = name;
        this.members = new ArrayList<>();
    }

    /**
     * Adds a student as a regular member.
     * @param student the student to add
     * @return false if the student is already a member
     */
    public boolean addMember(Student student) {
        if (!members.contains(student)) {
            members.add(student);
            return true;
        }
        return false;
    }

    /**
     * Removes a student from the organization.
     * @param student the student to remove
     * @return true if removed
     */
    public boolean removeMember(Student student) {
        return members.remove(student);
    }

    /**
     * Sets a student as the head of this organization. The head is automatically added as a member.
     * @param student the student to set as head
     */
    public void setHead(Student student) {
        this.head = student;
        if (!members.contains(student)) {
            members.add(student);
        }
    }

    /**
     * Checks if a student is the head of this organization.
     * @param student the student to check
     * @return true if the student is the head
     */
    public boolean isHead(Student student) {
        return Objects.equals(head, student);
    }

    /**
     * Returns the number of members in this organization.
     * @return member count
     */
    public int getMemberCount() {
        return members.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentOrganization)) return false;
        StudentOrganization that = (StudentOrganization) o;
        return Objects.equals(orgId, that.orgId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId);
    }

    @Override
    public String toString() {
        return String.format("StudentOrganization{id='%s', name='%s', members=%d, head='%s'}",
                orgId, name, members.size(),
                head != null ? head.getName() : "None");
    }


    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Student getHead() { return head; }

    public List<Student> getMembers() { return members; }
}