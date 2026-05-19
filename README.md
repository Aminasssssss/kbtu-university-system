# KBTU University Management System

A console-based university information system built in Java for the Object-Oriented Programming and Design course at KBTU. The system supports 8 user roles, implements 4 design patterns, and covers the full lifecycle of academic processes — from course registration to research paper management.

---

## How to Run

**In Eclipse:**

1. Create a new Java project
2. Copy the contents of `src/` into your project's `src` folder
3. Right-click `src/main/Main.java` → Run As → Java Application
4. Enter credentials in the Console tab at the bottom

**Important — Cyrillic characters (RU/KZ menus):**

To display Russian and Kazakh menus correctly, add this to Eclipse VM Arguments:

```
-Dfile.encoding=UTF-8
```

How: Run → Run Configurations → Arguments tab → VM Arguments → paste `-Dfile.encoding=UTF-8` → Apply → Run.

**From the command line:**

```
javac -d out -encoding UTF-8 $(find src -name "*.java")
java -Dfile.encoding=UTF-8 -cp out main.Main
```

Always exit by pressing `0` twice (logout, then exit) to save data.

---

## What It Does

**Students** register for courses (max 21 credits), view marks and transcripts, rate teachers (current average shown before rating), join student organizations, subscribe to journals, submit tech requests, view and comment on news, drop courses.

**Graduate students** — all Student features plus supervisor info, diploma project management, and h-index verification for supervisor.

**Teachers** put marks (duplicate detection — overwrites with warning), file complaints with urgency levels, mark attendance, send messages, view inbox, comment on news, and access researcher tools.

**Managers** assign courses, approve registrations (rollback if credit limit exceeded), add courses, generate reports, manage news.

**Admins** add/remove users, search by email, view system logs.

**Deans** view and resolve complaints, sign tech requests, view logs.

**Tech Support Specialists** process requests: VIEWED → ACCEPTED → DONE.

**Researchers** calculate h-index, sort papers by citations/date/pages, get citations in Plain Text or BibTeX, publish papers with auto-announcements, create/join research projects.

---

## Design Patterns

| Pattern | Implementation |
|---------|---------------|
| Singleton | Database, CourseRegistry, ResearcherRegistry |
| Observer | Journal notifies subscribers when a paper is published |
| Decorator | ResearcherDecorator — any User becomes a Researcher at runtime |
| Factory | UserFactory creates the correct User subtype by role |

---

## Technical Details

- **Authentication** — SHA-256 password hashing via MessageDigest
- **Serialization** — 3 files saved on exit, loaded on startup; all data persists
- **Language support** — EN / RU / KZ, 80+ translation keys; requires `-Dfile.encoding=UTF-8` for Cyrillic in Eclipse
- **Enumerations** — 12 enums: TeacherPosition, CourseType, UrgencyLevel, ManagerType, Language, DegreeType, RequestStatus, LessonType, CitationFormat, AttendanceStatus, ReportType, UserType
- **Exceptions** — CourseOverloadException, FailLimitException, LowHIndexException, NotResearcherException
- **Documentation** — 96-page Javadoc HTML in `javadoc/` folder

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
├── menu/               8 role-based menus
├── utils/              PasswordUtils, Translator
└── main/               Main.java
```

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
| ruslan@kbtu.kz | phd123 | PhD + Researcher |

---

## Known Limitations

- Cyrillic display requires `-Dfile.encoding=UTF-8` in Eclipse VM Arguments
- Schedule generation — class is implemented but not connected to any menu
- RecommendationLetter — class is complete but not accessible through any menu
- No automatic credit reset between semesters

---

## Team

| Name | Contribution |
|------|-------------|
| Жуматаева Амина Ғаниқызы | Team lead, research system, design patterns, UML diagrams |
| Бекмуратов Галымжомарт Максатович | User hierarchy, enumerations, authentication |
| Нуртуганов Альфараби Нурлыбекович | Academic system, console menus |

---

CSE212 Object-Oriented Programming and Design · KBTU · Spring 2026
