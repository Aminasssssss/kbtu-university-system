package database;

import models.academic.Course;
import models.users.Teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton registry for courses and teacher assignments.
 * Stores all courses and manages the mapping between teachers and their assigned courses.
 */
public class CourseRegistry implements Serializable {

    private static final long serialVersionUID = 1L;
    private static CourseRegistry instance;

    /** List of all courses in the system. */
    private final List<Course> courses = new ArrayList<>();
    
    /** Mapping of teacher email to their assigned courses. */
    private final Map<String, List<Course>> teacherCourses = new LinkedHashMap<>();

    /**
     * Private constructor for Singleton pattern.
     */
    private CourseRegistry() {}

    /**
     * Returns the singleton instance of CourseRegistry.
     * @return the single instance
     */
    public static CourseRegistry getInstance() {
        if (instance == null) instance = new CourseRegistry();
        return instance;
    }

    /**
     * Adds a course to the registry if not already present.
     * @param course the course to add
     */
    public void addCourse(Course course) {
        if (!courses.contains(course)) courses.add(course);
    }

    /**
     * Assigns a course to a teacher and registers the assignment.
     * @param course the course to assign
     * @param teacher the teacher to assign the course to
     */
    public void assignCourseToTeacher(Course course, Teacher teacher) {
        addCourse(course);
        teacherCourses.computeIfAbsent(teacher.getEmail(), k -> new ArrayList<>()).add(course);
        course.addTeacher(teacher);
    }

    /**
     * Returns all courses assigned to a specific teacher.
     * @param teacher the teacher
     * @return list of courses assigned to the teacher
     */
    public List<Course> getCoursesForTeacher(Teacher teacher) {
        return teacherCourses.getOrDefault(teacher.getEmail(), new ArrayList<>());
    }

    /**
     * Returns all courses in the system.
     * @return list of all courses
     */
    public List<Course> getAllCourses() {
        return courses;
    }
}