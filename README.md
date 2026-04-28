# University Management System

A console-based university system built in Java for our OOP and Design course at KBTU. It supports 8 user roles, 4 design patterns, 12 enums, 4 custom exceptions, and covers course registration, research paper management, journal subscriptions, and more.

## What it does

Students can register for courses (max 21 credits), view grades, get transcripts, rate teachers, and join organizations. Graduate students can also submit diploma projects and view recommendations.

Teachers put marks, manage courses, send complaints to the dean with urgency levels, and view attendance. Managers assign courses to teachers, approve registrations, generate academic reports, and manage schedules.

Admins manage users and view system logs. Deans resolve complaints and sign requests. Tech support specialists view and update request statuses. Researchers publish papers, calculate h-index, and get citations in Plain Text or BibTeX format.

There is a journal subscription system where users get notified when new papers are published.

## Key features

- Journal subscription with notifications
- Automatic report generation with statistics
- Complaint system sent to dean
- Student organizations
- Schedule generation with room conflict detection
- Inter-user messaging
- Multi-language support (KZ/EN/RU)
- Data persistence via Java serialization
- Top cited researcher auto-detection

## Design Patterns

- Singleton for Database and CourseRegistry
- Observer for Journal subscriptions and notifications
- Decorator for Researcher role any user can become a researcher
- Factory for creating user objects

## Project structure
```

src/
├── models/
│ ├── users/
│ ├── academic/
│ ├── research/
│ └── communication/
├── enums/
├── exceptions/
├── patterns/
├── database/
├── menu/
└── Main.java

```

## How to run
git clone https://github.com/Aminasssssss/kbtu-university-system.git
cd kbtu-university-system
javac -d out src/**/*.java
java -cp out main.Main

Test login: gala@kbtu.kz / 12345

## Team

| Name | Part |
|------|------|
| Жуматаева Амина Ғаниқызы | Team lead — research system, patterns, UML diagrams |
| Бекмуратов Галымжомарт Максатович | User hierarchy, enums, authentication |
| Нуртуганов Альфараби Нурлыбекович | Academic system, console menu |

---


CSE212 Object-Oriented Programming and Design · KBTU · Spring 2026
