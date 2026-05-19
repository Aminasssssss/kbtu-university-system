# KBTU University Management System

A console-based university information system built in Java for the Object-Oriented Programming and Design course at KBTU. The system supports 8 user roles, implements 4 design patterns, and covers the full lifecycle of academic processes — from course registration to research paper management.

---

## What It Does

**Students** register for courses (maximum 21 credits), view marks and transcripts, rate teachers, join student organizations, subscribe to academic journals, and submit tech support requests.

**Graduate students** have all student features plus access to supervisor information, diploma project management, and h-index verification for their supervisor.

**Teachers** put marks using the att1 (30%) + att2 (30%) + final (40%) formula, file complaints to the Dean with urgency levels, mark attendance, send messages to employees, and access researcher tools.

**Managers** assign courses to teachers, approve student registrations, add new courses, generate academic reports, and manage the university news feed.

**Admins** add and remove users, search by email, and view full system logs.

**Deans** view and resolve complaints filed by teachers, sign tech support requests, and view system event logs.

**Tech Support Specialists** process requests through the full status lifecycle: VIEWED → ACCEPTED → DONE.

**Researchers** (any user decorated with ResearcherDecorator) calculate h-index, sort papers by citations, date, or pages, get citations in Plain Text or BibTeX format, publish papers with automatic news announcements, create and join research projects, and find the top cited researcher.

---

## Design Patterns

| Pattern | Implementation |
|---------|---------------|
| Singleton | Database, CourseRegistry, ResearcherRegistry |
| Observer | Journal notifies all subscribers when a paper is published |
| Decorator | ResearcherDecorator — any User can become a Researcher at runtime |
| Factory | UserFactory creates the correct User subtype by role |

---

## Technical Details

- **Authentication** — passwords stored as SHA-256 hashes, never in plain text
- **Serialization** — Database, CourseRegistry, and ResearcherRegistry are serialized to `.ser` files on exit and loaded on startup, so all data persists between sessions
- **Language support** — full menu translation to English, Russian, and Kazakh via the Translator utility class
- **Exceptions** — CourseOverloadException, FailLimitException, LowHIndexException, NotResearcherException
- **Enumerations** — TeacherPosition, CourseType, UrgencyLevel, ManagerType, Language, DegreeType, RequestStatus, LessonType, CitationFormat, AttendanceStatus, ReportType, UserType
- **Documentation** — 96-page Javadoc HTML generated from source

---

## Project Structure

```
src/
├── models/
│   ├── users/          User, Employee, Student, GraduateStudent, Teacher,
│   │                   Manager, Admin, Dean, TechSupportSpecialist
│   ├── academic/       Course, Mark, Transcript, Attendance, Lesson,
│   │                   Schedule, DiplomaProject, StudentOrganization,
│   │                   RecommendationLetter, Report
│   ├── research/       Researcher, ResearchPaper, ResearchProject,
│   │                   TopCitedResearcher
│   └── communication/  Message, News, Comment, Complaint, Journal,
│                       Notification, Request
├── enums/              12 enumerations
├── exceptions/         4 custom exceptions
├── patterns/           ResearcherDecorator, ResearcherRegistry, UserFactory
├── database/           Database, CourseRegistry, Log
├── menu/               AdminMenu, DeanMenu, ManagerMenu, TeacherMenu,
│                       StudentMenu, TechSupportMenu, ResearcherMenu, MainMenu
├── utils/              PasswordUtils, Translator
└── main/               Main.java
```

---

## How to Run

**In Eclipse:**

1. Create a new Java project
2. Copy the contents of `src/` into your project's `src` folder
3. Right-click `src/main/Main.java` → Run As → Java Application
4. Enter credentials in the Console tab at the bottom

**From the command line:**

```
javac -d out -encoding UTF-8 $(find src -name "*.java")
java -cp out main.Main
```

To save data correctly, always exit by pressing `0` twice — once to logout, once to exit the program.

---

## Test Accounts

| Email | Password | Role |
|-------|----------|------|
| admin@kbtu.kz | admin123 | Admin |
| dean@kbtu.kz | dean123 | Dean |
| aigerim@kbtu.kz | teach123 | Professor + Researcher |
| bekzat@kbtu.kz | bekzat123 | Senior Lector |
| manager@kbtu.kz | mgr123 | Manager |
| tech@kbtu.kz | tech123 | Tech Support |
| gala@kbtu.kz | 12345 | Student |
| arman@kbtu.kz | arman123 | Student |
| zhanel@kbtu.kz | grad123 | Master Student |
| ruslan@kbtu.kz | phd123 | PhD Student + Researcher |

---

## Team

| Name | Contribution |
|------|-------------|
| Жуматаева Амина Ғаниқызы | Team lead, research system, design patterns, UML diagrams |
| Бекмуратов Галымжомарт Максатович | User hierarchy, enumerations, authentication |
| Нуртуганов Альфараби Нурлыбекович | Academic system, console menus |

---

CSE212 Object-Oriented Programming and Design · KBTU · Spring 2026
