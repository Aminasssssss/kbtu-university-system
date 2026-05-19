package menu;

import database.CourseRegistry;
import utils.Translator;
import database.Database;
import enums.Language;
import exceptions.CourseOverloadException;
import models.academic.*;
import models.communication.Journal;
import models.users.GraduateStudent;
import models.users.Student;
import models.users.Teacher;
import models.users.User;
import patterns.ResearcherDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for Student and GraduateStudent roles.
 * If the student is also a researcher, "Researcher Tools" option appears.
 * GraduateStudent gets extra options: supervisor info, diploma projects.
 */
public class StudentMenu {

    private final Student student;
    private final Scanner scanner;
    private final ResearcherDecorator researcherProfile; // null if not a researcher

    /** Shared list of student organizations across all students. */
    private static final List<StudentOrganization> organizations = new ArrayList<>();
    static {
        organizations.add(new StudentOrganization("ORG001", "Programming Club"));
        organizations.add(new StudentOrganization("ORG002", "Math Society"));
        organizations.add(new StudentOrganization("ORG003", "Robotics Team"));
        organizations.add(new StudentOrganization("ORG004", "Research Circle"));
    }

    /** Returns the shared journal list from TeacherMenu for Observer pattern. */
    private List<Journal> getJournals() {
        return database.Database.getInstance().getJournals();
    }

    public StudentMenu(Student student, Scanner scanner) {
        this(student, scanner, null);
    }

    public StudentMenu(Student student, Scanner scanner, ResearcherDecorator rd) {
        this.student = student;
        this.scanner = scanner;
        this.researcherProfile = rd;
    }

    public void show() {
        boolean running = true;
        while (running) {
            boolean isGrad = student instanceof GraduateStudent;

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          STUDENT MENU                ║");
            System.out.println("║  " + student.getName());
            System.out.printf("║  GPA: %.2f  |  Lang: %s%n", student.getGpa(), student.getLanguage());
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║ " + Translator.get("view_courses", student.getLanguage()));
            System.out.println("║ " + Translator.get("register_course", student.getLanguage()));
            System.out.println("║ " + Translator.get("view_marks", student.getLanguage()));
            System.out.println("║ " + Translator.get("view_transcript", student.getLanguage()));
            System.out.println("║ " + Translator.get("rate_teacher", student.getLanguage()));
            System.out.println("║ " + Translator.get("teacher_info", student.getLanguage()));
            System.out.println("║ " + Translator.get("organizations", student.getLanguage()));
            System.out.println("║ " + Translator.get("journals", student.getLanguage()));
            System.out.println("║ " + Translator.get("tech_request", student.getLanguage()));
            System.out.println("║ " + Translator.get("change_language", student.getLanguage()));
            System.out.println("║ 13. Drop a course                    ║");
            System.out.println("║ 14. View university news             ║");
            if (isGrad)
                System.out.println("║ 11. Graduate student info        [G]║");
            if (researcherProfile != null)
                System.out.println("║ 12. Researcher tools             [R]║");
            System.out.println("║ 0.  Logout                           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                viewCourses();
            } else if (choice.equals("2")) {
                registerForCourse();
            } else if (choice.equals("3")) {
                viewMarks();
            } else if (choice.equals("4")) {
                viewTranscript();
            } else if (choice.equals("5")) {
                rateTeacher();
            } else if (choice.equals("6")) {
                viewCourseTeacherInfo();
            } else if (choice.equals("7")) {
                manageOrganizations();
            } else if (choice.equals("8")) {
                manageJournals();
            } else if (choice.equals("9")) {
                submitTechRequest();
            } else if (choice.equals("10")) {
                changeLanguage();
            } else if (choice.equals("11")) {
                if (student instanceof GraduateStudent gs) {
                    showGradStudentInfo(gs);
                } else {
                    System.out.println("This option is only for graduate students.");
                }
            } else if (choice.equals("12")) {
                if (researcherProfile != null) {
                    new ResearcherMenu(researcherProfile, student, scanner).show();
                } else {
                    System.out.println("You are not registered as a researcher.");
                }
            } else if (choice.equals("13")) {
                dropCourse();
            } else if (choice.equals("14")) {
                viewNews();
            } else if (choice.equals("0")) {
                student.logout();
                running = false;
                System.out.println(Translator.get("logout", student.getLanguage()));
            } else {
                System.out.println("Invalid option.");
            }
        }
    }


    private void viewCourses() {
        List<Course> courses = CourseRegistry.getInstance().getAllCourses();
        if (courses.isEmpty()) { System.out.println(Translator.get("no_courses", student.getLanguage())); return; }
        System.out.println("\n── Available Courses ──");
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            System.out.printf("%2d. %-30s %d cr  [%-12s]  %s%n",
                    i + 1, c.getName(), c.getCredits(), c.getCourseType(), c.getSchool());
        }
    }

    /**
     * Registers the student for a selected course.
     * Prevents duplicate enrollment and enforces the 21-credit limit.
     */
    private void registerForCourse() {
        List<Course> courses = CourseRegistry.getInstance().getAllCourses();
        if (courses.isEmpty()) {
            System.out.println(Translator.get("no_courses", student.getLanguage()));
            return;
        }
        viewCourses();
        System.out.print("Enter course number: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= courses.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            Course selected = courses.get(idx);
            if (student.isEnrolledIn(selected)) {
                System.out.println(Translator.get("already_enrolled", student.getLanguage()));
                return;
            }
            student.registerForCourse(selected.getCredits());
            selected.addStudent(student);
            student.addRegisteredCourse(selected);
            System.out.println(Translator.get("registered", student.getLanguage()) + selected.getName());
            System.out.println("Total credits: " + student.getCredits() + "/21");
            Database.getInstance().addLog(student.getName() + " registered for " + selected.getName());
        } catch (CourseOverloadException e) {
            System.out.println("Cannot register: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
        }
    }

    /**
     * Allows the student to drop a course they are enrolled in.
     * Reduces credit count accordingly.
     */
    private void dropCourse() {
        List<Course> myCourses = student.getRegisteredCourses();
        if (myCourses.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
            return;
        }
        System.out.println("\n── Your Enrolled Courses ──");
        for (int i = 0; i < myCourses.size(); i++) {
            System.out.printf("%d. %s (%d credits)%n",
                    i + 1, myCourses.get(i).getName(), myCourses.get(i).getCredits());
        }
        System.out.print("Select course to drop: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= myCourses.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            Course toDrop = myCourses.get(idx);
            student.dropCourse(toDrop);
            toDrop.removeStudent(student);
            System.out.println("Dropped: " + toDrop.getName());
            System.out.println("Remaining credits: " + student.getCredits() + "/21");
            Database.getInstance().addLog(student.getName() + " dropped course: " + toDrop.getName());
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
        }
    }

    /**
     * Shows news articles visible to all users.
     * Research news is shown first (pinned).
     */
    private void viewNews() {
        List<models.communication.News> newsList = menu.ManagerMenu.getNewsList();
        if (newsList.isEmpty()) {
            System.out.println("No news published yet.");
            return;
        }
        List<models.communication.News> sorted = new java.util.ArrayList<>(newsList);
        java.util.Collections.sort(sorted);
        System.out.println("\n── University News ──");
        System.out.println();
        for (int i = 0; i < sorted.size(); i++) {
            models.communication.News n = sorted.get(i);
            String pin;
            if (n.isPinned()) {
                pin = "[PINNED] ";
            } else {
                pin = "";
            }
            System.out.printf("%d. %s[%s] %s%n", i + 1, pin, n.getTopic(), n.getTitle());
            System.out.println("   " + n.getContent());
            if (!n.getComments().isEmpty()) {
                System.out.println("   Comments:");
                for (models.communication.Comment comm : n.getComments()) {
                    System.out.println("     - " + comm.getAuthor().getName() + ": " + comm.getText());
                }
            }
            System.out.println();
        }
        System.out.print("Enter news number to comment (or 0 to skip): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim());
            if (idx > 0 && idx <= sorted.size()) {
                System.out.print("Your comment: ");
                String text = scanner.nextLine().trim();
                if (!text.isBlank()) {
                    String cid = "CMT-" + System.currentTimeMillis();
                    models.communication.Comment comment = new models.communication.Comment(cid, student, text);
                    sorted.get(idx - 1).addComment(comment);
                    System.out.println("Comment added.");
                }
            }
        } catch (NumberFormatException e) {
        }
    }


    private List<Mark> getMyMarks() {
        List<Mark> marks = new ArrayList<>();
        for (Course c : CourseRegistry.getInstance().getAllCourses())
            for (Mark m : c.getMarks())
                if (m.getStudent().equals(student)) marks.add(m);
        return marks;
    }

    private void viewMarks() {
        List<Mark> marks = getMyMarks();
        if (marks.isEmpty()) { System.out.println("No marks yet."); return; }
        System.out.println("\n── Your Marks ──");
        for (Mark m : marks)
            System.out.printf("%-28s  Att1:%4.1f  Att2:%4.1f  Final:%4.1f  → %5.2f (%s)%n",
                    m.getCourse().getName(),
                    m.getFirstAttestation(), m.getSecondAttestation(), m.getFinalExam(),
                    m.getTotal(), m.getLetterGrade());
    }

    private void viewTranscript() {
        Transcript transcript = new Transcript(student);
        List<Mark> myMarks = getMyMarks();
        for (Mark mark : myMarks) {
            transcript.addMark(mark);
        }
        transcript.generate();
    }


    private void rateTeacher() {
        List<Teacher> teachers = getTeachers();
        if (teachers.isEmpty()) { System.out.println("No teachers found."); return; }
        System.out.println("\n── Teachers ──");
        for (int i = 0; i < teachers.size(); i++)
            System.out.printf("%d. %-25s [%s]%n", i + 1,
                    teachers.get(i).getName(), teachers.get(i).getPosition());
        System.out.print("Select teacher: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= teachers.size()) { System.out.println("Invalid."); return; }
            System.out.print("Rating (1-5): ");
            int rating = Integer.parseInt(scanner.nextLine().trim());
            if (rating < 1 || rating > 5) {
                System.out.println("Rating must be between 1 and 5.");
                return;
            }
            Teacher t = teachers.get(idx);
            Database.getInstance().addTeacherRating(t.getEmail(), rating);
            double avg = Database.getInstance().getAverageRating(t.getEmail());
            int count = Database.getInstance().getRatingCount(t.getEmail());
            System.out.printf("You rated %s: %d/5.%n", t.getName(), rating);
            System.out.printf("Their average rating is now %.1f/5 (%d ratings).%n", avg, count);
            Database.getInstance().addLog(student.getName() + " rated "
                    + t.getName() + ": " + rating + "/5");
        } catch (NumberFormatException e) { System.out.println("Invalid."); }
    }

    private void viewCourseTeacherInfo() {
        List<Course> courses = CourseRegistry.getInstance().getAllCourses();
        if (courses.isEmpty()) { System.out.println("No courses."); return; }
        viewCourses();
        System.out.print("Select course: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= courses.size()) { System.out.println("Invalid."); return; }
            Course c = courses.get(idx);
            List<Teacher> instructors = c.getInstructors();
            if (instructors.isEmpty()) { System.out.println("No teacher assigned yet."); return; }
            System.out.println("\n── Instructors for: " + c.getName() + " ──");
            for (Teacher t : instructors) {
                System.out.println("  Name       : " + t.getName());
                System.out.println("  Position   : " + t.getPosition());
                System.out.println("  Department : " + t.getDepartment());
                System.out.println("  Email      : " + t.getEmail());
                System.out.println();
            }
        } catch (NumberFormatException e) { System.out.println("Invalid."); }
    }

    private List<Teacher> getTeachers() {
        List<Teacher> list = new ArrayList<>();
        for (User u : Database.getInstance().getUsers().values())
            if (u instanceof Teacher t) list.add(t);
        return list;
    }


    private void manageOrganizations() {
        boolean back = false;
        while (!back) {
            System.out.println("\n── Student Organizations ──");
            System.out.println("1. View all");
            System.out.println("2. Join as member");
            System.out.println("3. Become head");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> listOrgs();
                case "2" -> joinOrg(false);
                case "3" -> joinOrg(true);
                case "0" -> back = true;
                default  -> System.out.println("Invalid.");
            }
        }
    }

    private void listOrgs() {
        System.out.println();
        for (int i = 0; i < organizations.size(); i++) {
            StudentOrganization o = organizations.get(i);
            System.out.printf("%d. %-25s Members: %d  Head: %s%n",
                    i + 1, o.getName(), o.getMemberCount(), getHeadName(o));
        }
    }

    private void joinOrg(boolean asHead) {
        listOrgs();
        System.out.print("Select: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= organizations.size()) { System.out.println("Invalid."); return; }
            StudentOrganization org = organizations.get(idx);
            if (asHead) {
                org.setHead(student);
                System.out.println("You are now head of \"" + org.getName() + "\".");
            } else {
                boolean added = org.addMember(student);
                if (added) {
                    System.out.println("Joined \"" + org.getName() + "\".");
                } else {
                    System.out.println("You are already a member of this organization.");
                }
            }
        } catch (NumberFormatException e) { System.out.println("Invalid."); }
    }


    private void manageJournals() {
        boolean back = false;
        while (!back) {
            System.out.println("\n── Journal Subscriptions ──");
            System.out.println("1. View journals");
            System.out.println("2. Subscribe");
            System.out.println("3. Unsubscribe");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> listJournals();
                case "2" -> subscribeJournal();
                case "3" -> unsubscribeJournal();
                case "0" -> back = true;
                default  -> System.out.println("Invalid.");
            }
        }
    }

    private void listJournals() {
        List<Journal> journals = getJournals();
        System.out.println();
        for (int i = 0; i < journals.size(); i++) {
            Journal j = journals.get(i);
            boolean sub = j.getSubscribers().contains(student);
            System.out.printf("%d. %-35s %s%n",
                    i + 1, j.getName(), getSubscribedText(sub));
        }
    }

    private void subscribeJournal() {
        List<Journal> journals = getJournals();
        listJournals();
        System.out.print("Select journal: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= journals.size()) { System.out.println("Invalid."); return; }
            boolean ok = journals.get(idx).subscribe(student);
            if (ok) {
                System.out.println("Subscribed successfully!");
            } else {
                System.out.println("You are already subscribed to this journal.");
            }
        } catch (NumberFormatException e) { System.out.println("Invalid."); }
    }

    private void unsubscribeJournal() {
        List<Journal> journals = getJournals();
        listJournals();
        System.out.print("Select journal to unsubscribe: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= journals.size()) { System.out.println("Invalid."); return; }
            boolean ok = journals.get(idx).unsubscribe(student);
            if (ok) {
                System.out.println("Unsubscribed successfully.");
            } else {
                System.out.println("You were not subscribed to this journal.");
            }
        } catch (NumberFormatException e) { System.out.println("Invalid."); }
    }


    private void changeLanguage() {
        System.out.println("\nSelect language:");
        System.out.println("1. English (EN)");
        System.out.println("2. Қазақша (KZ)");
        System.out.println("3. Русский (RU)");
        System.out.print("Choose: ");
        String c = scanner.nextLine().trim();
        Language lang;
        if (c.equals("2")) {
            lang = Language.KZ;
        } else if (c.equals("3")) {
            lang = Language.RU;
        } else {
            lang = Language.EN;
        };
        student.setLanguage(lang);
        System.out.println("Language set to: " + lang);
        Database.getInstance().addLog(student.getName() + " changed language to " + lang);
    }


    private void submitTechRequest() {
        System.out.print("Describe your issue: ");
        String desc = scanner.nextLine().trim();
        if (desc.isBlank()) { System.out.println("Cannot be empty."); return; }
        TechSupportMenu.submitRequest(desc, student);
    }


    private void showGradStudentInfo(GraduateStudent gs) {
        boolean back = false;
        while (!back) {
            System.out.println("\n── Graduate Student Options ──");
            System.out.println("1. View supervisor info");
            System.out.println("2. View diploma projects");
            System.out.println("3. View supervisor's h-index");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String gsChoice = scanner.nextLine().trim();
            if (gsChoice.equals("1")) {
                viewSupervisor(gs);
            } else if (gsChoice.equals("2")) {
                viewDiplomaProjects(gs);
            } else if (gsChoice.equals("3")) {
                viewSupervisorHIndex(gs);
            } else if (gsChoice.equals("0")) {
                back = true;
            } else {
                System.out.println("Invalid.");
            }
        }
    }

    private void viewSupervisor(GraduateStudent gs) {
        if (gs.getSupervisor() == null) {
            System.out.println("No supervisor assigned yet.");
            return;
        }
        System.out.println("\n── Your Supervisor ──");
        if (gs.getSupervisor() instanceof User u) {
            System.out.println("  Name    : " + u.getName());
            System.out.println("  Email   : " + u.getEmail());
            System.out.println("  H-Index : " + gs.getSupervisor().calculateHIndex());
        }
        System.out.println("  Degree  : " + gs.getDegree());
    }

    /**
     * Displays diploma projects and allows adding a new one.
     *
     * @param gs the graduate student
     */
    private void viewDiplomaProjects(GraduateStudent gs) {
        List<DiplomaProject> projects = gs.getDiplomaProjects();
        System.out.println("\n── Diploma Projects ──");
        if (projects.isEmpty()) {
            System.out.println("  No diploma projects yet.");
        }
        for (DiplomaProject dp : projects) {
            System.out.println("  Title    : " + dp.getTitle());
            System.out.println("  Approved : " + dp.isApproved());
            System.out.println();
        }
        System.out.println("A. Add a new diploma project");
        System.out.println("0. Back");
        System.out.print(Translator.get("choose", student.getLanguage()));
        String choice = scanner.nextLine().trim();
        if (choice.equalsIgnoreCase("A")) {
            addDiplomaProject(gs);
        }
    }

    /**
     * Prompts the graduate student to create a new diploma project.
     *
     * @param gs the graduate student
     */
    private void addDiplomaProject(GraduateStudent gs) {
        System.out.print("Project title: ");
        String title = scanner.nextLine().trim();
        if (title.isBlank()) {
            System.out.println("Title cannot be empty.");
            return;
        }
        String id = "DP-" + gs.getId() + "-" + (gs.getDiplomaProjects().size() + 1);
        DiplomaProject project = new DiplomaProject(id, title, gs, gs.getSupervisor());
        gs.addDiplomaProject(project);
        System.out.println("Diploma project added: " + title);
        Database.getInstance().addLog(gs.getName() + " added diploma project: " + title);
    }

    private void viewSupervisorHIndex(GraduateStudent gs) {
        if (gs.getSupervisor() == null) {
            System.out.println("No supervisor assigned.");
            return;
        }
        int h = gs.getSupervisor().calculateHIndex();
        System.out.println("Supervisor h-index: " + h);
        if (h >= 3) {
            System.out.println("This supervisor meets the minimum h-index requirement.");
        } else {
            System.out.println("Warning: h-index is below the required minimum of 3.");
        }
    }

    /**
     * Returns the name of the head of an organization, or "None" if there is no head.
     *
     * @param org the student organization
     * @return head name or "None"
     */
    private String getHeadName(models.academic.StudentOrganization org) {
        if (org.getHead() != null) {
            return org.getHead().getName();
        }
        return "None";
    }

    /**
     * Returns subscription status label for display.
     *
     * @param subscribed whether the student is subscribed
     * @return "[SUBSCRIBED]" or empty string
     */
    private String getSubscribedText(boolean subscribed) {
        if (subscribed) {
            return "[SUBSCRIBED]";
        }
        return "";
    }

    /**
     * Allows the graduate student to add a diploma project.
     *
     * @param gs the graduate student
     */

}
