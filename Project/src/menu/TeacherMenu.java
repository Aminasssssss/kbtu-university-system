package menu;

import database.CourseRegistry;
import database.Database;
import enums.Language;
import enums.UrgencyLevel;
import models.academic.Attendance;
import models.academic.Course;
import models.academic.Mark;
import models.communication.Complaint;
import models.communication.Journal;
import models.communication.Message;
import models.users.Student;
import models.users.Teacher;
import models.users.User;
import utils.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for the Teacher role.
 * Teachers can view courses, put marks, file complaints,
 * send messages, generate reports, and use researcher tools.
 */
public class TeacherMenu {

    private final Teacher teacher;
    private final Scanner scanner;
    private static int complaintCounter = 1;

    /**
     * Creates a TeacherMenu for the given teacher.
     *
     * @param teacher the logged-in teacher
     * @param scanner the shared input scanner
     */
    public TeacherMenu(Teacher teacher, Scanner scanner) {
        this.teacher = teacher;
        this.scanner = scanner;
    }

    /**
     * Returns all complaints from the Database.
     *
     * @return list of all complaints
     */
    public static List<Complaint> getComplaints() {
        return Database.getInstance().getComplaints();
    }

    /**
     * Displays the teacher menu and handles input until logout.
     */
    public void show() {
        Language lang = teacher.getLanguage();
        boolean running = true;
        while (running) {
            lang = teacher.getLanguage();
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║  " + Translator.get("teacher_menu_title", lang));
            System.out.println("║  " + teacher.getName() + " | " + teacher.getPosition());
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║ " + Translator.get("my_courses", lang));
            System.out.println("║ " + Translator.get("put_mark", lang));
            System.out.println("║ " + Translator.get("send_complaint", lang));
            System.out.println("║ " + Translator.get("view_students", lang));
            System.out.println("║ " + Translator.get("send_message", lang));
            System.out.println("║ " + Translator.get("inbox", lang));
            System.out.println("║ " + Translator.get("marks_report", lang));
            System.out.println("║ " + Translator.get("journal_sub", lang));
            System.out.println("║ " + Translator.get("tech_req", lang));
            System.out.println("║ " + Translator.get("lang", lang));
            System.out.println("║ " + Translator.get("researcher", lang));
            System.out.println("║ " + Translator.get("attendance", lang));
            System.out.println("║ " + Translator.get("logout_option", lang));
            System.out.println("╚══════════════════════════════════╝");
            System.out.print(Translator.get("choose", lang));

            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                viewMyCourses();
            } else if (choice.equals("2")) {
                putMark();
            } else if (choice.equals("3")) {
                sendComplaint();
            } else if (choice.equals("4")) {
                viewStudents();
            } else if (choice.equals("5")) {
                sendMessage();
            } else if (choice.equals("6")) {
                viewInbox();
            } else if (choice.equals("7")) {
                generateMarksReport();
            } else if (choice.equals("8")) {
                manageJournals();
            } else if (choice.equals("9")) {
                submitTechRequest();
            } else if (choice.equals("10")) {
                changeLanguage();
            } else if (choice.equals("11")) {
                patterns.ResearcherDecorator rd = patterns.ResearcherRegistry.getInstance()
                        .getResearcher(teacher.getEmail());
                if (rd != null) {
                    new ResearcherMenu(rd, teacher, scanner).show();
                } else {
                    System.out.println("You are not registered as a researcher.");
                }
            } else if (choice.equals("12")) {
                markAttendance();
            } else if (choice.equals("0")) {
                teacher.logout();
                running = false;
                System.out.println(Translator.get("logout", lang));
            } else {
                System.out.println(Translator.get("invalid", lang));
            }
        }
    }

    private void viewMyCourses() {
        List<Course> courses = CourseRegistry.getInstance().getCoursesForTeacher(teacher);
        if (courses.isEmpty()) {
            System.out.println("No courses assigned.");
            return;
        }
        System.out.println("\n── " + Translator.get("my_courses", teacher.getLanguage()) + " ──");
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            System.out.printf("%d. %-30s %d credits  [%s]%n",
                    i + 1, c.getName(), c.getCredits(), c.getCourseType());
        }
    }

    private void putMark() {
        List<Course> courses = CourseRegistry.getInstance().getCoursesForTeacher(teacher);
        if (courses.isEmpty()) {
            System.out.println("No courses assigned.");
            return;
        }
        viewMyCourses();
        System.out.print(Translator.get("select_course", teacher.getLanguage()));
        try {
            int cIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (cIdx < 0 || cIdx >= courses.size()) {
                System.out.println(Translator.get("invalid", teacher.getLanguage()));
                return;
            }
            Course course = courses.get(cIdx);
            List<Student> enrolled = course.getStudents();
            if (enrolled.isEmpty()) {
                System.out.println(Translator.get("no_students_enrolled", teacher.getLanguage()));
                return;
            }
            System.out.println("\n── Students in " + course.getName() + " ──");
            for (int i = 0; i < enrolled.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, enrolled.get(i).getName());
            }
            System.out.print(Translator.get("select_student", teacher.getLanguage()));
            int sIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (sIdx < 0 || sIdx >= enrolled.size()) {
                System.out.println(Translator.get("invalid", teacher.getLanguage()));
                return;
            }
            Student student = enrolled.get(sIdx);

            System.out.print(Translator.get("enter_att1", teacher.getLanguage()));
            double att1 = Double.parseDouble(scanner.nextLine().trim());
            System.out.print(Translator.get("enter_att2", teacher.getLanguage()));
            double att2 = Double.parseDouble(scanner.nextLine().trim());
            System.out.print(Translator.get("enter_final", teacher.getLanguage()));
            double fin = Double.parseDouble(scanner.nextLine().trim());

            Mark mark = new Mark(student, course, teacher);
            mark.setFirstAttestation(att1);
            mark.setSecondAttestation(att2);
            mark.setFinalExam(fin);
            course.addMark(mark);
            teacher.putMark(student, course, mark);

            System.out.printf("%s Total: %.2f → %s%n",
                    Translator.get("mark_saved", teacher.getLanguage()),
                    mark.getTotal(), mark.getLetterGrade());

            recalculateGpa(student, course);

            if (mark.isFailed()) {
                try {
                    student.incrementFailCount();
                    System.out.println("Note: student failed this course.");
                } catch (exceptions.FailLimitException e) {
                    System.out.println("Note: student failed this course.");
                    System.out.println("WARNING: " + e.getMessage());
                }
            }

            Database.getInstance().addLog(teacher.getName() + " graded " +
                    student.getName() + " in " + course.getName() +
                    ": " + mark.getTotal());

        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
        }
    }

    private void sendComplaint() {
        List<Student> students = getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("\n── Select student ──");
        for (int i = 0; i < students.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, students.get(i).getName());
        }
        System.out.print("Student number: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= students.size()) {
                System.out.println(Translator.get("invalid", teacher.getLanguage()));
                return;
            }
            Student student = students.get(idx);
            System.out.println("Urgency: 1-LOW  2-MEDIUM  3-HIGH");
            System.out.print("Select: ");
            String urg = scanner.nextLine().trim();
            UrgencyLevel level;
            if (urg.equals("2")) {
                level = UrgencyLevel.MEDIUM;
            } else if (urg.equals("3")) {
                level = UrgencyLevel.HIGH;
            } else {
                level = UrgencyLevel.LOW;
            }
            System.out.print(Translator.get("enter_description", teacher.getLanguage()));
            String description = scanner.nextLine().trim();
            String id = "COMP-" + String.format("%03d", complaintCounter++);
            Complaint complaint = new Complaint(id, teacher, student, description, level);
            Database.getInstance().addComplaint(complaint);
            System.out.printf("Complaint %s filed against %s [%s].%n",
                    id, student.getName(), level);
            Database.getInstance().addLog(teacher.getName() +
                    " filed complaint " + id + " against " + student.getName());
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
        }
    }

    private void viewStudents() {
        List<Student> students = getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students in the system.");
            return;
        }
        for (int i = 0; i < students.size() - 1; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < students.size(); j++) {
                if (students.get(j).getGpa() > students.get(maxIdx).getGpa()) {
                    maxIdx = j;
                }
            }
            Student temp = students.get(i);
            students.set(i, students.get(maxIdx));
            students.set(maxIdx, temp);
        }
        System.out.println("\n── All Students (by GPA) ──");
        for (Student s : students) {
            System.out.printf("  %-25s  GPA: %.2f%n", s.getName(), s.getGpa());
        }
        double avgRating = Database.getInstance().getAverageRating(teacher.getEmail());
        int ratingCount = Database.getInstance().getRatingCount(teacher.getEmail());
        System.out.println();
        if (ratingCount > 0) {
            System.out.printf("Your average student rating: %.1f/5 (%d ratings)%n",
                    avgRating, ratingCount);
        } else {
            System.out.println("You have not been rated by students yet.");
        }
    }

    private void sendMessage() {
        List<User> employees = getAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        System.out.println("\n── Select recipient ──");
        for (int i = 0; i < employees.size(); i++) {
            System.out.printf("%d. %-25s [%s]%n",
                    i + 1, employees.get(i).getName(),
                    employees.get(i).getClass().getSimpleName());
        }
        System.out.print("Recipient number: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= employees.size()) {
                System.out.println(Translator.get("invalid", teacher.getLanguage()));
                return;
            }
            User recipient = employees.get(idx);
            System.out.print("Message: ");
            String text = scanner.nextLine().trim();
            if (text.isBlank()) {
                System.out.println("Message cannot be empty.");
                return;
            }
            Message message = new Message("MSG-" + System.currentTimeMillis(), teacher, recipient, "(message)", text);
            Database.getInstance().addMessage(message);
            System.out.println("Message sent to " + recipient.getName() + ".");
            Database.getInstance().addLog(teacher.getName() +
                    " sent message to " + recipient.getName());
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
        }
    }

    private void viewInbox() {
        List<Message> inbox = Database.getInstance().getMessagesFor(teacher.getEmail());
        if (inbox.isEmpty()) {
            System.out.println(Translator.get("no_messages", teacher.getLanguage()));
            return;
        }
        System.out.println("\n── " + Translator.get("inbox", teacher.getLanguage()) + " ──");
        for (int i = 0; i < inbox.size(); i++) {
            Message m = inbox.get(i);
            System.out.printf("%d. %s%s%n",
                    i + 1,
                    Translator.get("message_from", teacher.getLanguage()),
                    m.getSender().getName());
            System.out.println("   " + m.getBody());
        }
    }

    private void generateMarksReport() {
        List<Course> courses = CourseRegistry.getInstance().getCoursesForTeacher(teacher);
        System.out.println("\n═══════ Marks Report ═══════");
        for (Course c : courses) {
            System.out.println("Course: " + c.getName());
            List<Mark> marks = c.getMarks();
            if (marks.isEmpty()) {
                System.out.println("  No marks yet.");
            } else {
                for (Mark m : marks) {
                    System.out.printf("  %-20s %.2f (%s)%n",
                            m.getStudent().getName(), m.getTotal(), m.getLetterGrade());
                }
            }
        }
        System.out.println("════════════════════════════");
        Database.getInstance().addLog(teacher.getName() + " generated marks report");
        Database.getInstance().addLog(teacher.getName() + " generated marks report");
    }

    private void manageJournals() {
        List<Journal> journals = database.Database.getInstance().getJournals();
        if (journals.isEmpty()) {
            System.out.println("No journals available.");
            return;
        }
        boolean back = false;
        while (!back) {
            System.out.println("\n── Journals ──");
            System.out.println("1. View journals");
            System.out.println("2. Subscribe");
            System.out.println("3. Unsubscribe");
            System.out.println("0. Back");
            System.out.print(Translator.get("choose", teacher.getLanguage()));
            String c = scanner.nextLine().trim();
            if (c.equals("1")) {
                listJournals(journals);
            } else if (c.equals("2")) {
                subscribeJournal(journals);
            } else if (c.equals("3")) {
                unsubscribeJournal(journals);
            } else if (c.equals("0")) {
                back = true;
            } else {
                System.out.println(Translator.get("invalid", teacher.getLanguage()));
            }
        }
    }

    private void listJournals(List<Journal> journals) {
        for (int i = 0; i < journals.size(); i++) {
            Journal j = journals.get(i);
            boolean subscribed = j.getSubscribers().contains(teacher);
            System.out.printf("%d. %-20s ISSN:%s  %s%n",
                    i + 1, j.getName(), j.getIssn(),
                    getSubscribedLabel(subscribed));
        }
    }

    private void subscribeJournal(List<Journal> journals) {
        listJournals(journals);
        System.out.print("Journal number: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= journals.size()) {
                System.out.println(Translator.get("invalid", teacher.getLanguage()));
                return;
            }
            boolean ok = journals.get(idx).subscribe(teacher);
            if (ok) {
                System.out.println("Subscribed successfully!");
            } else {
                System.out.println("Already subscribed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
        }
    }

    private void unsubscribeJournal(List<Journal> journals) {
        listJournals(journals);
        System.out.print("Journal number: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= journals.size()) {
                System.out.println(Translator.get("invalid", teacher.getLanguage()));
                return;
            }
            boolean ok = journals.get(idx).unsubscribe(teacher);
            if (ok) {
                System.out.println("Unsubscribed successfully.");
            } else {
                System.out.println("You were not subscribed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
        }
    }

    private void submitTechRequest() {
        System.out.print("Describe your issue: ");
        String description = scanner.nextLine().trim();
        if (description.isBlank()) {
            System.out.println("Description cannot be empty.");
            return;
        }
        TechSupportMenu.submitRequest(description, teacher);
    }

    private void changeLanguage() {
        System.out.println(Translator.get("lang_select", teacher.getLanguage()));
        System.out.print(Translator.get("choose", teacher.getLanguage()));
        String choice = scanner.nextLine().trim();
        Language lang;
        if (choice.equals("2")) {
            lang = Language.KZ;
        } else if (choice.equals("3")) {
            lang = Language.RU;
        } else {
            lang = Language.EN;
        }
        teacher.setLanguage(lang);
        System.out.println(Translator.get("lang_changed", lang));
        Database.getInstance().addLog(teacher.getName() + " changed language to " + lang);
    }

    private void markAttendance() {
        List<Course> courses = CourseRegistry.getInstance().getCoursesForTeacher(teacher);
        if (courses.isEmpty()) {
            System.out.println("No courses assigned.");
            return;
        }
        viewMyCourses();
        System.out.print(Translator.get("select_course", teacher.getLanguage()));
        try {
            int cIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (cIdx < 0 || cIdx >= courses.size()) {
                System.out.println(Translator.get("invalid", teacher.getLanguage()));
                return;
            }
            Course course = courses.get(cIdx);
            List<Student> enrolled = course.getStudents();
            if (enrolled.isEmpty()) {
                System.out.println(Translator.get("no_students_enrolled", teacher.getLanguage()));
                return;
            }
            System.out.println("\n── Mark attendance for " + course.getName() + " ──");
            for (int i = 0; i < enrolled.size(); i++) {
                Student student = enrolled.get(i);
                System.out.print(student.getName() + " — 1." +
                        Translator.get("present", teacher.getLanguage()) +
                        "  2." + Translator.get("absent", teacher.getLanguage()) + ": ");
                String ans = scanner.nextLine().trim();
                boolean present = !ans.equals("2");
                enums.AttendanceStatus status;
                if (present) {
                    status = enums.AttendanceStatus.PRESENT;
                } else {
                    status = enums.AttendanceStatus.ABSENT;
                }
                Database.getInstance().addLog(teacher.getName() +
                        " marked " + student.getName() + " as " + status +
                        " in " + course.getName());
            }
            System.out.println(Translator.get("attendance_marked", teacher.getLanguage()));
            Database.getInstance().addLog(teacher.getName() +
                    " marked attendance for " + course.getName());
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
        }
    }

    private void recalculateGpa(Student student, Course ignoredCourse) {
        double totalWeighted = 0;
        int totalCredits = 0;
        for (Course c : CourseRegistry.getInstance().getAllCourses()) {
            for (Mark m : c.getMarks()) {
                if (m.getStudent().equals(student)) {
                    totalWeighted += m.getGpaPoints() * c.getCredits();
                    totalCredits += c.getCredits();
                }
            }
        }
        if (totalCredits > 0) {
            double newGpa = totalWeighted / totalCredits;
            User userFromDb = Database.getInstance().getUser(student.getEmail());
            if (userFromDb instanceof Student dbStudent) {
                dbStudent.setGpa(newGpa);
                System.out.printf("Student GPA updated to: %.2f%n", newGpa);
            }
        }
    }

    List<Student> getAllStudents() {
        List<Student> result = new ArrayList<>();
        for (User u : Database.getInstance().getUsers().values()) {
            if (u instanceof Student) {
                result.add((Student) u);
            }
        }
        return result;
    }

    private List<User> getAllEmployees() {
        List<User> result = new ArrayList<>();
        for (User u : Database.getInstance().getUsers().values()) {
            if (!(u instanceof Student) && !u.getEmail().equals(teacher.getEmail())) {
                result.add(u);
            }
        }
        return result;
    }

    private String getSubscribedLabel(boolean subscribed) {
        if (subscribed) {
            return "[SUBSCRIBED]";
        }
        return "";
    }
}
