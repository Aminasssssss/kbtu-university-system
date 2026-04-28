package models.academic;

import enums.CourseType;
import models.users.Student;
import models.users.Teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a university course.
 * A course can have multiple instructors, enrolled students, lessons, and marks.
 * Students may not exceed 21 credits in total registration.
 */
public class Course implements Serializable, Comparable<Course> {

    private static final long serialVersionUID = 1L;

    /** Course code. */
    private String code;

    /** Course name. */
    private String name;

    /** Course description. */
    private String description;

    /** Number of credits for this course. */
    private int credits;

    /** Type of course: MAJOR, MINOR, or FREE_ELECTIVE. */
    private CourseType courseType;

    /** Instructors assigned to this course. */
    private List<Teacher> instructors;

    /** Students enrolled in this course. */
    private List<Student> enrolledStudents;

    /** Lessons associated with this course. */
    private List<Lesson> lessons;

    /** Marks given in this course. */
    private List<Mark> marks;

    /** The school/department that owns this course. */
    private String school;

    /** Year of study for which this course is intended. */
    private int targetYear;

    /**
     * Creates a new course.
     * @param code unique course code
     * @param name course name
     * @param credits number of credits
     * @param courseType type of course
     * @param school owning school/department
     */
    public Course(String code, String name, int credits, CourseType courseType, String school) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.courseType = courseType;
        this.school = school;
        this.instructors = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.marks = new ArrayList<>();
    }

    /**
     * Adds a teacher to this course.
     * @param teacher the teacher to add
     */
    public void addTeacher(Teacher teacher) {
        if (!instructors.contains(teacher)) {
            instructors.add(teacher);
        }
    }

    /**
     * Removes a teacher from this course.
     * @param teacher the teacher to remove
     */
    public void removeTeacher(Teacher teacher) {
        instructors.remove(teacher);
    }

    /**
     * Adds a student to this course.
     * @param student the student to add
     * @return true if the student was added
     */
    public boolean addStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            return true;
        }
        return false;
    }

    /**
     * Removes a student from this course.
     * @param student the student to remove
     * @return true if the student was removed
     */
    public boolean removeStudent(Student student) {
        return enrolledStudents.remove(student);
    }

    /**
     * Adds a lesson to this course.
     * @param lesson the lesson to add
     */
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    /**
     * Returns the list of enrolled students.
     * @return list of students
     */
    public List<Student> getStudents() {
        return enrolledStudents;
    }

    @Override
    public int compareTo(Course other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(code, course.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return String.format("Course{code='%s', name='%s', credits=%d, type=%s, school='%s'}",
                code, name, credits, courseType, school);
    }


    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public CourseType getCourseType() { return courseType; }
    public void setCourseType(CourseType courseType) { this.courseType = courseType; }

    public List<Teacher> getInstructors() { return instructors; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public int getTargetYear() { return targetYear; }
    public void setTargetYear(int targetYear) { this.targetYear = targetYear; }

    /** Returns all marks for this course. */
    public List<Mark> getMarks() { return marks; }

    /** Adds a mark to this course. */
    public void addMark(Mark mark) { marks.add(mark); }
}