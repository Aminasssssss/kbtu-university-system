package menu;

import database.CourseRegistry;
import database.Database;
import enums.UrgencyLevel;
import models.academic.Course;
import models.academic.Mark;
import models.users.Student;
import models.users.Teacher;
import models.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for Teacher role.
 * Allows viewing courses, putting marks, sending complaints, and viewing students.
 */
public class TeacherMenu {

    private final Teacher teacher;
    private final Scanner scanner;

    /**
     * Creates a TeacherMenu instance.
     * @param teacher the logged-in teacher
     * @param scanner the shared scanner for input
     */
    public TeacherMenu(Teacher teacher, Scanner scanner) {
        this.teacher = teacher;
        this.scanner = scanner;
    }

    /**
     * Launches the teacher menu loop.
     */
    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║       TEACHER MENU           ║");
            System.out.println("║  Welcome, " + teacher.getName());
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║ 1. View my courses           ║");
            System.out.println("║ 2. Put mark for student      ║");
            System.out.println("║ 3. Send complaint to dean    ║");
            System.out.println("║ 4. View all students         ║");
            System.out.println("║ 0. Logout                    ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> viewMyCourses();
                case "2" -> putMark();
                case "3" -> sendComplaint();
                case "4" -> viewStudents();
                case "0" -> { teacher.logout(); running = false; System.out.println("Logged out."); }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    /**
     * Returns all Student objects from the database.
     * @return list of students
     */
    private List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        for (User u : Database.getInstance().getUsers().values()) {
            if (u instanceof Student s) list.add(s);
        }
        return list;
    }

    /**
     * Displays courses assigned to this teacher via CourseRegistry.
     */
    private void viewMyCourses() {
        List<Course> courses = CourseRegistry.getInstance().getCoursesForTeacher(teacher);
        if (courses.isEmpty()) { System.out.println("No courses assigned."); return; }
        System.out.println("\n── Your Courses ──");
        for (int i = 0; i < courses.size(); i++)
            System.out.printf("%d. %s (%d credits)%n", i + 1,
                    courses.get(i).getName(), courses.get(i).getCredits());
    }

    /**
     * Puts a mark for a student in one of the teacher's courses.
     */
    private void putMark() {
        List<Course> courses = CourseRegistry.getInstance().getCoursesForTeacher(teacher);
        if (courses.isEmpty()) { System.out.println("No courses assigned."); return; }
        viewMyCourses();
        System.out.print("Select course number: ");
        try {
            int cIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (cIndex < 0 || cIndex >= courses.size()) { System.out.println("Invalid."); return; }
            Course course = courses.get(cIndex);

            List<Student> students = course.getStudents();
            if (students.isEmpty()) { System.out.println("No students enrolled."); return; }
            System.out.println("\n── Enrolled Students ──");
            for (int i = 0; i < students.size(); i++)
                System.out.printf("%d. %s%n", i + 1, students.get(i).getName());
            System.out.print("Select student number: ");
            int sIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (sIndex < 0 || sIndex >= students.size()) { System.out.println("Invalid."); return; }
            Student student = students.get(sIndex);

            System.out.print("1st attestation (0-100): ");
            double att1 = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("2nd attestation (0-100): ");
            double att2 = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Final exam (0-100): ");
            double fin = Double.parseDouble(scanner.nextLine().trim());

            Mark mark = new Mark(student, course, teacher);
            mark.setFirstAttestation(att1);
            mark.setSecondAttestation(att2);
            mark.setFinalExam(fin);
            course.addMark(mark);

            teacher.putMark(student, course, mark);
            System.out.printf("Mark saved. Total: %.2f (%s)%n", mark.getTotal(), mark.getLetterGrade());
            Database.getInstance().addLog(teacher.getName() + " put mark for "
                    + student.getName() + " in " + course.getName());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Sends a complaint about a student to the dean with urgency level.
     */
    private void sendComplaint() {
        List<Student> students = getAllStudents();
        if (students.isEmpty()) { System.out.println("No students found."); return; }
        System.out.println("\n── Students ──");
        for (int i = 0; i < students.size(); i++)
            System.out.printf("%d. %s%n", i + 1, students.get(i).getName());
        System.out.print("Select student number: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index < 0 || index >= students.size()) { System.out.println("Invalid."); return; }
            Student student = students.get(index);

            System.out.println("Urgency: 1. LOW  2. MEDIUM  3. HIGH");
            System.out.print("Choose: ");
            int uChoice = Integer.parseInt(scanner.nextLine().trim());
            UrgencyLevel level = switch (uChoice) {
                case 2 -> UrgencyLevel.MEDIUM;
                case 3 -> UrgencyLevel.HIGH;
                default -> UrgencyLevel.LOW;
            };

            System.out.print("Describe the complaint: ");
            String text = scanner.nextLine().trim();
            teacher.sendComplaint(student, level, text);
            Database.getInstance().addLog(teacher.getName() + " filed complaint against "
                    + student.getName() + " [" + level + "]");
            System.out.println("Complaint sent with urgency: " + level);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    /**
     * Displays all students sorted by GPA.
     */
    private void viewStudents() {
        List<Student> students = getAllStudents();
        if (students.isEmpty()) { System.out.println("No students."); return; }
        System.out.println("\n── All Students ──");
        students.stream().sorted()
                .forEach(s -> System.out.printf("  %-20s GPA: %.2f%n", s.getName(), s.getGpa()));
    }
}