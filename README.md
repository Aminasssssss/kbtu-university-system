# University Management System

A console-based university system built in Java for our OOP & Design course at KBTU. It supports multiple user roles and covers most things a real university system would need — from course registration to research paper management.

## What it does

Depending on your role, you can do different things:

- **Students** can register for courses, view grades, get transcripts and rate teachers
- **Teachers** can put marks, manage their courses and send complaints to the dean
- **Managers** can assign courses, approve registrations and generate reports
- **Admins** manage users and view system logs
- **Researchers** can publish papers, calculate h-index and manage research projects

There's also a journal subscription system where users get notified when new papers come out.

## Tech stuff

Built with pure Java — no frameworks, just OOP. We used four design patterns:

- **Singleton** for the database (one instance, handles serialization)
- **Observer** for journal subscriptions and notifications
- **Decorator** for the Researcher role — any user can become a researcher
- **Factory** for creating user objects

Data is saved between sessions using Java serialization.

## Project structure

```
src/
├── models/
│   ├── users/         User, Student, Teacher, Admin, Manager...
│   ├── academic/      Course, Lesson, Mark, Transcript
│   ├── research/      ResearchPaper, ResearchProject
│   └── communication/ News, Message
├── enums/
├── exceptions/
├── patterns/
├── database/
└── Main.java
```

## How to run

```bash
git clone https://github.com/YOUR_USERNAME/kbtu-university-system.git
cd kbtu-university-system
javac -d out src/**/*.java
java -cp out Main
```

## Team

| Name | Part |
|------|------|
| Жуматаева Амина Ғаниқызы | Team lead — research system, patterns, UML diagrams |
| Бекмуратов Галымжомарт Максатович | User hierarchy, enums, authentication |
| Нуртуганов Альфараби Нурлыбекович | Academic system, console menu |

---

*CSE212 Object-Oriented Programming & Design · KBTU · Spring 2026*
