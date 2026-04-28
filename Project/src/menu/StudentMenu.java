package menu;

import database.CourseRegistry;
import database.Database;
import exceptions.CourseOverloadException;
import models.academic.Course;
import models.academic.Mark;
import models.academic.Transcript;
import models.users.Student;
import models.users.Teacher;
import models.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for Student role.
 * Allows viewing courses, registering for courses, viewing marks, transcript, and rating teachers.
 */
public class StudentMenu {

    private final Student student;
    private final Scanner scanner;

    /**
     * Creates a StudentMenu instance.
     * @param student the logged-in student
     * @param scanner the shared scanner for input
     */
    public StudentMenu(Student student, Scanner scanner) {
        this.student = student;
        this.scanner = scanner;
    }

    /**
     * Launches the student menu loop.
     */
    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║       STUDENT MENU           ║");
            System.out.println("║  Welcome, " + student.getName());
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║ 1. View available courses    ║");
            System.out.println("║ 2. Register for a course     ║");
            System.out.println("║ 3. View my marks             ║");
            System.out.println("║ 4. View transcript           ║");
            System.out.println("║ 5. Rate a teacher            ║");
            System.out.println("║ 0. Logout                    ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> viewCourses();
                case "2" -> registerForCourse();
                case "3" -> viewMarks();
                case "4" -> viewTranscript();
                case "5" -> rateTeacher();
                case "0" -> { student.logout(); running = false; System.out.println("Logged out."); }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    /**
     * Returns all Teacher objects from the database.
     * @return list of teachers
     */
    private List<Teacher> getAllTeachers() {
        List<Teacher> list = new ArrayList<>();
        for (User u : Database.getInstance().getUsers().values()) {
            if (u instanceof Teacher t) list.add(t);
        }
        return list;
    }

    /**
     * Returns marks for this student from all courses.
     * @return list of marks
     */
    private List<Mark> getMyMarks() {
        List<Mark> marks = new ArrayList<>();
        for (Course c : CourseRegistry.getInstance().getAllCourses()) {
            for (Mark m : c.getMarks()) {
                if (m.getStudent().equals(student)) marks.add(m);
            }
        }
        return marks;
    }

    /**
     * Displays all available courses.
     */
    private void viewCourses() {
        List<Course> courses = CourseRegistry.getInstance().getAllCourses();
        if (courses.isEmpty()) { System.out.println("No courses available."); return; }
        System.out.println("\n── Available Courses ──");
        for (int i = 0; i < courses.size(); i++) {
            System.out.printf("%d. %s (%d credits) [%s]%n", i + 1,
                    courses.get(i).getName(), courses.get(i).getCredits(), courses.get(i).getCourseType());
        }
    }

    /**
     * Registers the student for a selected course.
     * Enforces the 21-credit limit via CourseOverloadException.
     */
    private void registerForCourse() {
        List<Course> courses = CourseRegistry.getInstance().getAllCourses();
        if (courses.isEmpty()) { System.out.println("No courses available."); return; }
        viewCourses();
        System.out.print("Enter course number: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index < 0 || index >= courses.size()) { System.out.println("Invalid."); return; }
            Course selected = courses.get(index);
            student.registerForCourse(selected.getCredits());
            selected.addStudent(student);
            System.out.println("Registered for: " + selected.getName());
            Database.getInstance().addLog(student.getName() + " registered for " + selected.getName());
        } catch (CourseOverloadException e) {
            System.out.println("Cannot register: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    /**
     * Displays all marks for the student.
     */
    private void viewMarks() {
        List<Mark> marks = getMyMarks();
        if (marks.isEmpty()) { System.out.println("No marks yet."); return; }
        System.out.println("\n── Your Marks ──");
        for (Mark m : marks) {
            System.out.printf("%-25s Att1:%.1f Att2:%.1f Final:%.1f Total:%.2f (%s)%n",
                    m.getCourse().getName(), m.getFirstAttestation(),
                    m.getSecondAttestation(), m.getFinalExam(),
                    m.getTotal(), m.getLetterGrade());
        }
    }

    /**
     * Generates and prints the student's transcript.
     */
    private void viewTranscript() {
        Transcript transcript = new Transcript(student);
        getMyMarks().forEach(transcript::addMark);
        transcript.generate();
    }

    /**
     * Allows the student to rate a teacher (1-5).
     */
    private void rateTeacher() {
        List<Teacher> teachers = getAllTeachers();
        if (teachers.isEmpty()) { System.out.println("No teachers found."); return; }
        System.out.println("\n── Teachers ──");
        for (int i = 0; i < teachers.size(); i++)
            System.out.printf("%d. %s%n", i + 1, teachers.get(i).getName());
        System.out.print("Select teacher number: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index < 0 || index >= teachers.size()) { System.out.println("Invalid."); return; }
            System.out.print("Enter rating (1-5): ");
            int rating = Integer.parseInt(scanner.nextLine().trim());
            if (rating < 1 || rating > 5) { System.out.println("Rating must be 1-5."); return; }
            Teacher teacher = teachers.get(index);
            System.out.printf("You rated %s: %d/5%n", teacher.getName(), rating);
            Database.getInstance().addLog(student.getName() + " rated " + teacher.getName() + ": " + rating);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
}