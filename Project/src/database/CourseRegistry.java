package database;

import models.academic.Course;
import models.users.Teacher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton registry for courses and teacher assignments.
 * Supports serialization so course enrollment data persists between sessions.
 */
public class CourseRegistry implements Serializable {

    private static final long serialVersionUID = 1L;
    private static CourseRegistry instance;

    private final List<Course> courses = new ArrayList<>();
    private final Map<String, List<Course>> teacherCourses = new LinkedHashMap<>();

    private CourseRegistry() {}

    public static CourseRegistry getInstance() {
        if (instance == null) {
            instance = new CourseRegistry();
        }
        return instance;
    }

    public void addCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
        }
    }

    public void assignCourseToTeacher(Course course, Teacher teacher) {
        addCourse(course);
        if (!teacherCourses.containsKey(teacher.getEmail())) {
            teacherCourses.put(teacher.getEmail(), new ArrayList<>());
        }
        teacherCourses.get(teacher.getEmail()).add(course);
        course.addTeacher(teacher);
    }

    public List<Course> getCoursesForTeacher(Teacher teacher) {
        if (teacherCourses.containsKey(teacher.getEmail())) {
            return teacherCourses.get(teacher.getEmail());
        }
        return new ArrayList<>();
    }

    public List<Course> getAllCourses() {
        return courses;
    }

    public static void saveToFile(String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(instance);
        } catch (IOException e) {
            System.out.println("Could not save course registry: " + e.getMessage());
        }
    }

    public static void loadFromFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            instance = (CourseRegistry) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not load course registry: " + e.getMessage());
        }
    }
}
