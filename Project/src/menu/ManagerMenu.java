package menu;

import database.CourseRegistry;
import database.Database;
import models.academic.Course;
import models.academic.Mark;
import models.users.Manager;
import models.users.Student;
import models.users.Teacher;
import models.users.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for Manager role.
 * Allows assigning courses, approving registrations, viewing statistics, and generating reports.
 */
public class ManagerMenu {

    private final Manager manager;
    private final Scanner scanner;

    /**
     * Creates a ManagerMenu instance.
     * @param manager the logged-in manager
     * @param scanner the shared scanner for input
     */
    public ManagerMenu(Manager manager, Scanner scanner) {
        this.manager = manager;
        this.scanner = scanner;
    }

    /**
     * Launches the manager menu loop.
     */
    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║       MANAGER MENU           ║");
            System.out.println("║  Welcome, " + manager.getName());
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║ 1. Assign course to teacher  ║");
            System.out.println("║ 2. Approve student reg.      ║");
            System.out.println("║ 3. View students (sorted)    ║");
            System.out.println("║ 4. View teachers             ║");
            System.out.println("║ 5. Generate statistics report║");
            System.out.println("║ 0. Logout                    ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> assignCourse();
                case "2" -> approveRegistration();
                case "3" -> viewStudentsSorted();
                case "4" -> viewTeachers();
                case "5" -> generateReport();
                case "0" -> { manager.logout(); running = false; System.out.println("Logged out."); }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    /**
     * Returns all teachers from the database.
     * @return list of teachers
     */
    private List<Teacher> getAllTeachers() {
        List<Teacher> list = new ArrayList<>();
        for (User u : Database.getInstance().getUsers().values())
            if (u instanceof Teacher t) list.add(t);
        return list;
    }

    /**
     * Returns all students from the database.
     * @return list of students
     */
    private List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        for (User u : Database.getInstance().getUsers().values())
            if (u instanceof Student s) list.add(s);
        return list;
    }

    /**
     * Returns all marks from all courses.
     * @return list of marks
     */
    private List<Mark> getAllMarks() {
        List<Mark> list = new ArrayList<>();
        for (Course c : CourseRegistry.getInstance().getAllCourses())
            list.addAll(c.getMarks());
        return list;
    }

    /**
     * Assigns a course to a selected teacher.
     */
    private void assignCourse() {
        List<Course> courses = CourseRegistry.getInstance().getAllCourses();
        List<Teacher> teachers = getAllTeachers();
        if (courses.isEmpty() || teachers.isEmpty()) {
            System.out.println("No courses or teachers available."); return;
        }
        System.out.println("\n── Courses ──");
        for (int i = 0; i < courses.size(); i++)
            System.out.printf("%d. %s%n", i + 1, courses.get(i).getName());
        System.out.print("Select course: ");
        try {
            int cIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (cIndex < 0 || cIndex >= courses.size()) { System.out.println("Invalid."); return; }

            System.out.println("\n── Teachers ──");
            for (int i = 0; i < teachers.size(); i++)
                System.out.printf("%d. %s (%s)%n", i + 1, teachers.get(i).getName(), teachers.get(i).getPosition());
            System.out.print("Select teacher: ");
            int tIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (tIndex < 0 || tIndex >= teachers.size()) { System.out.println("Invalid."); return; }

            Course course = courses.get(cIndex);
            Teacher teacher = teachers.get(tIndex);
            CourseRegistry.getInstance().assignCourseToTeacher(course, teacher);
            manager.assignCourse(course, teacher);
            System.out.printf("Assigned '%s' to %s.%n", course.getName(), teacher.getName());
            Database.getInstance().addLog(manager.getName() + " assigned " + course.getName() + " to " + teacher.getName());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    /**
     * Approves a student's course registration.
     */
    private void approveRegistration() {
        List<Student> students = getAllStudents();
        List<Course> courses = CourseRegistry.getInstance().getAllCourses();
        if (students.isEmpty() || courses.isEmpty()) {
            System.out.println("No students or courses available."); return;
        }
        System.out.println("\n── Students ──");
        for (int i = 0; i < students.size(); i++)
            System.out.printf("%d. %s%n", i + 1, students.get(i).getName());
        System.out.print("Select student: ");
        try {
            int sIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (sIndex < 0 || sIndex >= students.size()) { System.out.println("Invalid."); return; }

            System.out.println("\n── Courses ──");
            for (int i = 0; i < courses.size(); i++)
                System.out.printf("%d. %s%n", i + 1, courses.get(i).getName());
            System.out.print("Select course: ");
            int cIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (cIndex < 0 || cIndex >= courses.size()) { System.out.println("Invalid."); return; }

            Student student = students.get(sIndex);
            Course course = courses.get(cIndex);
            manager.approveRegistration(student, course);
            System.out.printf("Approved %s for '%s'.%n", student.getName(), course.getName());
            Database.getInstance().addLog(manager.getName() + " approved " + student.getName() + " for " + course.getName());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    /**
     * Displays students sorted by GPA or alphabetically.
     */
    private void viewStudentsSorted() {
        System.out.println("Sort by: 1. GPA (desc)  2. Name (A-Z)");
        System.out.print("Choose: ");
        String choice = scanner.nextLine().trim();
        List<Student> students = getAllStudents();
        if (students.isEmpty()) { System.out.println("No students."); return; }
        if (choice.equals("1")) students.sort(Comparator.comparingDouble(Student::getGpa).reversed());
        else students.sort(Comparator.comparing(Student::getName));
        System.out.println("\n── Students ──");
        students.forEach(s -> System.out.printf("  %-20s GPA: %.2f%n", s.getName(), s.getGpa()));
    }

    /**
     * Displays all teachers.
     */
    private void viewTeachers() {
        List<Teacher> teachers = getAllTeachers();
        if (teachers.isEmpty()) { System.out.println("No teachers."); return; }
        System.out.println("\n── Teachers ──");
        teachers.forEach(t -> System.out.printf("  %-20s Position: %s%n", t.getName(), t.getPosition()));
    }

    /**
     * Generates a simple academic performance statistics report.
     */
    private void generateReport() {
        List<Student> students = getAllStudents();
        List<Mark> allMarks = getAllMarks();
        System.out.println("\n═══════════ ACADEMIC REPORT ═══════════");
        System.out.printf("Total students : %d%n", students.size());
        System.out.printf("Total marks    : %d%n", allMarks.size());
        if (!allMarks.isEmpty()) {
            double avg = allMarks.stream().mapToDouble(Mark::getTotal).average().orElse(0);
            long passed = allMarks.stream().filter(m -> !m.isFailed()).count();
            long failed = allMarks.stream().filter(Mark::isFailed).count();
            System.out.printf("Average score  : %.2f%n", avg);
            System.out.printf("Passed         : %d%n", passed);
            System.out.printf("Failed         : %d%n", failed);
        }
        System.out.println("════════════════════════════════════════");
        manager.createReport(students);
        Database.getInstance().addLog(manager.getName() + " generated academic report.");
    }
}