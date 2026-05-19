package menu;

import database.CourseRegistry;
import database.Database;
import enums.CourseType;
import enums.Language;
import models.academic.Course;
import models.academic.Mark;
import models.communication.Comment;
import models.communication.Complaint;
import models.communication.News;
import models.users.Manager;
import models.users.Student;
import models.users.Teacher;
import models.users.User;

import java.util.*;

/**
 * Console menu for Manager role.
 * Covers: course assignment, approvals, news management (with comments),
 * statistics, tech request overview, complaint review, language switch.
 */
public class ManagerMenu {

    private final Manager manager;
    private final Scanner scanner;

    private static int newsCounter = 1;
    private static int commentCounter = 1;

    public ManagerMenu(Manager manager, Scanner scanner) {
        this.manager = manager;
        this.scanner = scanner;
    }

    public static List<News> getNewsList() {
        return database.Database.getInstance().getNewsFeed();
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║         MANAGER MENU             ║");
            System.out.println("║  " + manager.getName());
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║ 1.  Assign course to teacher     ║");
            System.out.println("║ 2.  Approve student registration ║");
            System.out.println("║ 3.  Add new course               ║");
            System.out.println("║ 4.  View students (sorted)       ║");
            System.out.println("║ 5.  View teachers                ║");
            System.out.println("║ 6.  Statistics report            ║");
            System.out.println("║ 7.  Manage news                  ║");
            System.out.println("║ 8.  View tech requests           ║");
            System.out.println("║ 9.  View complaints              ║");
            System.out.println("║ 10. Change language              ║");
            System.out.println("║ 0.  Logout                       ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                assignCourse();
            } else if (choice.equals("2")) {
                approveRegistration();
            } else if (choice.equals("3")) {
                addCourse();
            } else if (choice.equals("4")) {
                viewStudentsSorted();
            } else if (choice.equals("5")) {
                viewTeachers();
            } else if (choice.equals("6")) {
                generateReport();
            } else if (choice.equals("7")) {
                manageNews();
            } else if (choice.equals("8")) {
                viewRequests();
            } else if (choice.equals("9")) {
                viewComplaints();
            } else if (choice.equals("10")) {
                changeLanguage();
            } else if (choice.equals("0")) {
                manager.logout();
                running = false;
                System.out.println("Logged out.");
            } else {
                System.out.println("Invalid option.");
            }
        }
    }


    private List<Teacher> getAllTeachers() {
        List<Teacher> list = new ArrayList<>();
        for (User u : Database.getInstance().getUsers().values())
            if (u instanceof Teacher t) list.add(t);
        return list;
    }

    private List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        for (User u : Database.getInstance().getUsers().values())
            if (u instanceof Student s) list.add(s);
        return list;
    }


    private void assignCourse() {
        List<Course> courses = CourseRegistry.getInstance().getAllCourses();
        List<Teacher> teachers = getAllTeachers();
        if (courses.isEmpty() || teachers.isEmpty()) {
            System.out.println("Need at least one course and one teacher.");
            return;
        }
        System.out.println("\n── Courses ──");
        for (int i = 0; i < courses.size(); i++)
            System.out.printf("%d. %s%n", i + 1, courses.get(i).getName());
        System.out.print("Select course: ");
        try {
            int cIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (cIdx < 0 || cIdx >= courses.size()) { System.out.println("Invalid."); return; }
            System.out.println("\n── Teachers ──");
            for (int i = 0; i < teachers.size(); i++)
                System.out.printf("%d. %-25s [%s]%n", i + 1,
                        teachers.get(i).getName(), teachers.get(i).getPosition());
            System.out.print("Select teacher: ");
            int tIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (tIdx < 0 || tIdx >= teachers.size()) { System.out.println("Invalid."); return; }
            Course c = courses.get(cIdx);
            Teacher t = teachers.get(tIdx);
            CourseRegistry.getInstance().assignCourseToTeacher(c, t);
            manager.assignCourse(c, t);
            System.out.printf("'%s' → %s%n", c.getName(), t.getName());
            Database.getInstance().addLog(manager.getName() + " assigned " + c.getName() + " → " + t.getName());
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private void approveRegistration() {
        List<Student> students = getAllStudents();
        List<Course> courses = CourseRegistry.getInstance().getAllCourses();
        if (students.isEmpty() || courses.isEmpty()) {
            System.out.println("No students or courses."); return;
        }
        System.out.println("\n── Students ──");
        for (int i = 0; i < students.size(); i++)
            System.out.printf("%d. %s%n", i + 1, students.get(i).getName());
        System.out.print("Select student: ");
        try {
            int sIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (sIdx < 0 || sIdx >= students.size()) { System.out.println("Invalid."); return; }
            System.out.println("\n── Courses ──");
            for (int i = 0; i < courses.size(); i++)
                System.out.printf("%d. %s (%d cr)%n", i + 1,
                        courses.get(i).getName(), courses.get(i).getCredits());
            System.out.print("Select course: ");
            int cIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (cIdx < 0 || cIdx >= courses.size()) { System.out.println("Invalid."); return; }
            manager.approveRegistration(students.get(sIdx), courses.get(cIdx));
            Database.getInstance().addLog(manager.getName() + " approved "
                    + students.get(sIdx).getName() + " for " + courses.get(cIdx).getName());
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private void addCourse() {
        System.out.println("\n── Add New Course ──");
        System.out.print("Code (e.g. CS301): "); String code = scanner.nextLine().trim();
        System.out.print("Name             : "); String name = scanner.nextLine().trim();
        System.out.print("School           : "); String school = scanner.nextLine().trim();
        System.out.print("Credits          : ");
        try {
            int credits = Integer.parseInt(scanner.nextLine().trim());
            System.out.println("Type: 1=MAJOR  2=MINOR  3=FREE_ELECTIVE");
            System.out.print("Choose: ");
            int t = Integer.parseInt(scanner.nextLine().trim());
            CourseType type;
            if (t == 2) {
                type = CourseType.MINOR;
            } else if (t == 3) {
                type = CourseType.FREE_ELECTIVE;
            } else {
                type = CourseType.MAJOR;
            };
            System.out.print("Target year of study (1-4): ");
            int year = Integer.parseInt(scanner.nextLine().trim());
            Course c = new Course(code, name, credits, type, school);
            c.setTargetYear(year);
            CourseRegistry.getInstance().addCourse(c);
            System.out.println("Course added: " + name);
            Database.getInstance().addLog(manager.getName() + " added course: " + name);
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private void viewStudentsSorted() {
        System.out.println("Sort: 1=GPA desc   2=Name A→Z");
        System.out.print("Choose: ");
        String c = scanner.nextLine().trim();
        List<Student> students = getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students.");
            return;
        }
        if (c.equals("1")) {
            students.sort(Comparator.comparingDouble(Student::getGpa).reversed());
        } else {
            students.sort(Comparator.comparing(Student::getName));
        }
        System.out.println("\n── Students ──");
        for (Student s : students) {
            System.out.printf("  %-25s  GPA: %.2f%n", s.getName(), s.getGpa());
        }
    }

    private void viewTeachers() {
        List<Teacher> teachers = getAllTeachers();
        if (teachers.isEmpty()) {
            System.out.println("No teachers.");
            return;
        }
        teachers.sort(Comparator.comparing(Teacher::getName));
        System.out.println("\n── Teachers (A→Z) ──");
        for (Teacher t : teachers) {
            System.out.printf("  %-25s  %-14s  %s%n",
                    t.getName(), t.getPosition(), t.getDepartment());
        }
    }

    private void generateReport() {
        List<Student> students = getAllStudents();
        List<Mark> allMarks = new ArrayList<>();
        for (Course course : CourseRegistry.getInstance().getAllCourses()) {
            allMarks.addAll(course.getMarks());
        }
        System.out.println("\n═══════════ ACADEMIC REPORT ═══════════");
        System.out.printf("Students  : %d%n", students.size());
        System.out.printf("Marks     : %d%n", allMarks.size());
        if (!allMarks.isEmpty()) {
            double total = 0;
            long passed = 0;
            for (Mark m : allMarks) {
                total += m.getTotal();
                if (!m.isFailed()) {
                    passed++;
                }
            }
            double avg = total / allMarks.size();
            System.out.printf("Average   : %.2f%n", avg);
            System.out.printf("Passed    : %d  |  Failed: %d%n", passed, allMarks.size() - passed);
        }
        System.out.println("════════════════════════════════════════");
        manager.createReport(students);
        Database.getInstance().addLog(manager.getName() + " generated report.");
    }


    private void manageNews() {
        boolean back = false;
        while (!back) {
            System.out.println("\n── News Management ──");
            System.out.println("1. View all news");
            System.out.println("2. Add news article");
            System.out.println("3. Delete news article");
            System.out.println("4. Add comment to news");  // new
            System.out.println("0. Back");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> viewNews();
                case "2" -> addNews();
                case "3" -> deleteNews();
                case "4" -> addCommentToNews();
                case "0" -> back = true;
                default  -> System.out.println("Invalid.");
            }
        }
    }

    private void viewNews() {
        List<News> feed = database.Database.getInstance().getNewsFeed();
        if (feed.isEmpty()) { System.out.println("No news yet."); return; }
        List<News> sorted = new ArrayList<>(feed);
        Collections.sort(sorted); // pinned first, then newest
        System.out.println("\n── News Feed ──");
        for (int i = 0; i < sorted.size(); i++) {
            News n = sorted.get(i);
            System.out.printf("%d. %s[%s] %s%n",
                    i + 1, getPinLabel(n), n.getTopic(), n.getTitle());
            System.out.println("   " + n.getContent());
            if (!n.getComments().isEmpty()) {
                System.out.println("   Comments (" + n.getComments().size() + "):");
                for (Comment c : n.getComments()) {
                    System.out.println("     [" + c.getAuthor().getName() + "] " + c.getText());
                }
            }
            System.out.println();
        }
    }

    private void addNews() {
        System.out.print("Title  : "); String title = scanner.nextLine().trim();
        System.out.print("Content: "); String content = scanner.nextLine().trim();
        System.out.print("Topic (Research / Events / Announcements): ");
        String topic = scanner.nextLine().trim();
        if (title.isBlank() || content.isBlank()) {
            System.out.println("Title and content required."); return;
        }
        String id = "NEWS-" + String.format("%03d", newsCounter++);
        News news = new News(id, title, content, topic, manager);
        database.Database.getInstance().addNews(news);
        if (news.isPinned())
            System.out.println("Auto-pinned (Research topic).");
        System.out.println("News added: " + title);
        Database.getInstance().addLog(manager.getName() + " added news: " + title);
    }

    private void deleteNews() {
        List<News> feedRef = database.Database.getInstance().getNewsFeed();
        if (feedRef.isEmpty()) { System.out.println("No news."); return; }
        viewNews();
        List<News> sorted = new ArrayList<>(feedRef);
        Collections.sort(sorted);
        System.out.print("Enter number to delete: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= sorted.size()) { System.out.println("Invalid."); return; }
            News toDelete = sorted.get(idx);
            feedRef.remove(toDelete);
            System.out.println("Deleted: " + toDelete.getTitle());
            Database.getInstance().addLog(manager.getName() + " deleted news: " + toDelete.getTitle());
        } catch (NumberFormatException e) { System.out.println("Invalid."); }
    }

    /** Add a comment to a news article — uses Comment class */
    private void addCommentToNews() {
        List<News> feed = database.Database.getInstance().getNewsFeed();
        if (feed.isEmpty()) { System.out.println("No news to comment on."); return; }
        viewNews();
        List<News> sorted = new ArrayList<>(feed);
        Collections.sort(sorted);
        System.out.print("Select news to comment on: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= sorted.size()) { System.out.println("Invalid."); return; }
            System.out.print("Your comment: ");
            String text = scanner.nextLine().trim();
            if (text.isBlank()) { System.out.println("Cannot be empty."); return; }
            String cid = "CMT-" + String.format("%03d", commentCounter++);
            Comment comment = new Comment(cid, manager, text);
            sorted.get(idx).addComment(comment);
            System.out.println("Comment added.");
            Database.getInstance().addLog(manager.getName() + " commented on: " + sorted.get(idx).getTitle());
        } catch (NumberFormatException e) { System.out.println("Invalid."); }
    }


    private void viewRequests() {
        List<models.communication.Request> reqs = TechSupportMenu.getRequestPool();
        if (reqs.isEmpty()) {
            System.out.println("No tech requests.");
            return;
        }
        System.out.println("\n── Tech Requests ──");
        for (models.communication.Request r : reqs) {
            System.out.printf("[%-8s] %s | %-20s | %s%n",
                    r.getStatus(), r.getRequestId(), r.getSender().getName(), r.getDescription());
        }
    }

    private void viewComplaints() {
        List<Complaint> complaints = TeacherMenu.getComplaints();
        if (complaints.isEmpty()) {
            System.out.println("No complaints.");
            return;
        }
        System.out.println("\n── Complaints ──");
        for (Complaint c : complaints) {
            System.out.printf("[%-6s] %s | Teacher: %-20s → Student: %s%n",
                    c.getUrgencyLevel(), c.getComplaintId(),
                    c.getSubmittedBy().getName(), c.getReportedStudent().getName());
            System.out.println("         " + c.getDescription());
        }
    }


    private void changeLanguage() {
        System.out.println("\n1. English (EN)\n2. Қазақша (KZ)\n3. Русский (RU)");
        System.out.print("Choose: ");
        Language lang = switch (scanner.nextLine().trim()) {
            case "2" -> Language.KZ;
            case "3" -> Language.RU;
            default  -> Language.EN;
        };
        manager.setLanguage(lang);
        System.out.println("Language changed to: " + lang);
        Database.getInstance().addLog(manager.getName() + " changed language → " + lang);
    }

    /**
     * Returns a pin indicator for a news article.
     *
     * @param news the news article
     * @return "📌 " if pinned, otherwise spaces
     */
    private String getPinLabel(News news) {
        if (news.isPinned()) {
            return "📌 ";
        }
        return "   ";
    }
}
