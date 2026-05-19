package main;

import database.CourseRegistry;
import database.Database;
import enums.*;
import menu.MainMenu;
import models.academic.Course;
import models.research.ResearchPaper;
import models.users.*;
import patterns.ResearcherDecorator;
import patterns.ResearcherRegistry;

/**
 * Entry point for the KBTU University Information System.
 *
 * On first run, test data is created (all user accounts and courses).
 * On subsequent runs, saved data is loaded automatically and setup is skipped.
 *
 * Passwords are plain text here — the User constructor hashes them with SHA-256.
 */
public class Main {

    public static void main(String[] args) {
        Database.loadFromFile("university_data.ser");
        CourseRegistry.loadFromFile("course_registry.ser");

        if (Database.getInstance().getUsers().isEmpty()) {
            setupTestData();
        }

        printCredentials();
        new MainMenu().start();
    }

    /**
     * Creates all test users, courses, and researcher profiles.
     * Only called on the very first run when the database is empty.
     */
    private static void setupTestData() {
        Database db = Database.getInstance();
        CourseRegistry registry = CourseRegistry.getInstance();
        ResearcherRegistry resRegistry = ResearcherRegistry.getInstance();

        Admin admin = new Admin("A001", "Nursultan Abenov",
                "admin@kbtu.kz", "admin123", Language.EN, 500000.0, "Administration");
        db.addUser(admin);

        Dean dean = new Dean("D001", "Aigul Bekova",
                "dean@kbtu.kz", "dean123", Language.EN, 700000.0, "School of IT", "SCHOOL");
        db.addUser(dean);

        Teacher professor = new Teacher("T001", "Aigerim Seitkali",
                "aigerim@kbtu.kz", "teach123", Language.EN,
                350000.0, "CS Department", TeacherPosition.PROFESSOR);
        db.addUser(professor);

        ResearcherDecorator profResearcher = new ResearcherDecorator(professor);
        profResearcher.addPaper(new ResearchPaper(
                "Machine Learning in Education", 20, "IJCSE",
                "10.1000/ml2023", "Seitkali A.", "2023-01-15", 12));
        profResearcher.addPaper(new ResearchPaper(
                "OOP Design Patterns Survey", 10, "JTSE",
                "10.1000/oop2022", "Seitkali A., Ivanov B.", "2022-06-10", 8));
        profResearcher.addPaper(new ResearchPaper(
                "Data Structures Overview", 6, "ACM Computing",
                "10.1000/ds2021", "Seitkali A.", "2021-03-20", 10));
        profResearcher.addPaper(new ResearchPaper(
                "Algorithms in Practice", 5, "IEEE Trans.",
                "10.1000/alg2020", "Seitkali A.", "2020-09-05", 15));
        resRegistry.register(profResearcher);

        Teacher seniorLector = new Teacher("T002", "Bekzat Nurmagambetov",
                "bekzat@kbtu.kz", "bekzat123", Language.RU,
                220000.0, "Mathematics", TeacherPosition.SENIOR_LECTOR);
        db.addUser(seniorLector);

        Manager manager = new Manager("M001", "Daniyar Abenov",
                "manager@kbtu.kz", "mgr123", Language.EN,
                400000.0, "OR Department", ManagerType.OR);
        db.addUser(manager);

        TechSupportSpecialist techSupport = new TechSupportSpecialist(
                "TS001", "Marat Aliev", "tech@kbtu.kz", "tech123",
                Language.EN, 250000.0, "IT Department");
        db.addUser(techSupport);

        Student student1 = new Student("S001", "Gala Nurlanovna",
                "gala@kbtu.kz", "12345", Language.EN, 3.5, 0, 0);
        db.addUser(student1);

        Student student2 = new Student("S002", "Arman Bekzhanov",
                "arman@kbtu.kz", "arman123", Language.RU, 2.8, 0, 0);
        db.addUser(student2);

        GraduateStudent masterStudent = new GraduateStudent(
                "GS001", "Zhanel Kassenova", "zhanel@kbtu.kz", "grad123",
                Language.EN, 3.8, 0, 0, DegreeType.MASTER, null);
        try {
            masterStudent.setSupervisor(profResearcher);
        } catch (exceptions.LowHIndexException e) {
            System.err.println("Supervisor error: " + e.getMessage());
        }
        db.addUser(masterStudent);

        GraduateStudent phdStudent = new GraduateStudent(
                "GS002", "Ruslan Dzhaksybekov", "ruslan@kbtu.kz", "phd123",
                Language.EN, 3.9, 0, 0, DegreeType.PHD, null);
        try {
            phdStudent.setSupervisor(profResearcher);
        } catch (exceptions.LowHIndexException e) {
            System.err.println("Supervisor error: " + e.getMessage());
        }
        db.addUser(phdStudent);

        ResearcherDecorator phdResearcher = new ResearcherDecorator(phdStudent);
        phdResearcher.addPaper(new ResearchPaper(
                "Deep Learning for NLP", 4, "ACL",
                "10.1000/nlp2024", "Dzhaksybekov R.", "2024-02-01", 9));
        phdResearcher.addPaper(new ResearchPaper(
                "Transfer Learning Survey", 3, "EMNLP",
                "10.1000/tl2023", "Dzhaksybekov R.", "2023-05-10", 14));
        resRegistry.register(phdResearcher);

        Course cs101   = new Course("CS101",   "Introduction to Programming", 3, CourseType.MAJOR, "SITE");
        Course cs201   = new Course("CS201",   "Object-Oriented Programming", 3, CourseType.MAJOR, "SITE");
        Course math101 = new Course("MATH101", "Calculus I",                  3, CourseType.MAJOR, "Mathematics");
        Course pe101   = new Course("PE101",   "Physical Education",          2, CourseType.FREE_ELECTIVE, "General");
        Course eng101  = new Course("ENG101",  "Technical English",           2, CourseType.MINOR, "Languages");
        Course ai301   = new Course("AI301",   "Artificial Intelligence",     3, CourseType.MAJOR, "SITE");

        registry.addCourse(cs101);
        registry.addCourse(cs201);
        registry.addCourse(math101);
        registry.addCourse(pe101);
        registry.addCourse(eng101);
        registry.addCourse(ai301);

        registry.assignCourseToTeacher(cs101,   professor);
        registry.assignCourseToTeacher(cs201,   professor);
        registry.assignCourseToTeacher(ai301,   professor);
        registry.assignCourseToTeacher(math101, seniorLector);

        System.out.println("Test data created successfully.");
    }

    /**
     * Prints the list of test accounts to the console.
     */
    private static void printCredentials() {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║    KBTU University — Test Accounts           ║");
        System.out.println("║    Passwords stored as SHA-256 hashes        ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  admin@kbtu.kz      / admin123  (Admin)      ║");
        System.out.println("║  dean@kbtu.kz       / dean123   (Dean)       ║");
        System.out.println("║  aigerim@kbtu.kz    / teach123  (Professor)  ║");
        System.out.println("║  bekzat@kbtu.kz     / bekzat123 (Sr.Lector)  ║");
        System.out.println("║  manager@kbtu.kz    / mgr123    (Manager)    ║");
        System.out.println("║  tech@kbtu.kz       / tech123   (TechSupport)║");
        System.out.println("║  gala@kbtu.kz       / 12345     (Student)    ║");
        System.out.println("║  arman@kbtu.kz      / arman123  (Student)    ║");
        System.out.println("║  zhanel@kbtu.kz     / grad123   (Master)     ║");
        System.out.println("║  ruslan@kbtu.kz     / phd123    (PhD+Res)    ║");
        System.out.println("╚══════════════════════════════════════════════╝\n");
    }
}
