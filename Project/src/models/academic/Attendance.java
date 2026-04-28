package models.academic;

import enums.AttendanceStatus;
import models.users.Student;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an attendance record for a student in a specific lesson.
 * Tracks whether the student was present or absent on a given date.
 */
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The student being tracked. */
    private Student student;

    /** The lesson the student attended or missed. */
    private Lesson lesson;

    /** Attendance status: PRESENT or ABSENT. */
    private AttendanceStatus status;

    /** The date of the lesson. */
    private LocalDate date;

    /**
     * Creates an attendance record.
     * @param student the student
     * @param lesson the lesson
     * @param status the attendance status
     * @param date the date of the lesson
     */
    public Attendance(Student student, Lesson lesson, AttendanceStatus status, LocalDate date) {
        this.student = student;
        this.lesson = lesson;
        this.status = status;
        this.date = date;
    }

    /**
     * Marks the student as present.
     */
    public void markPresent() {
        this.status = AttendanceStatus.PRESENT;
    }

    /**
     * Marks the student as absent.
     */
    public void markAbsent() {
        this.status = AttendanceStatus.ABSENT;
    }

    /**
     * Returns the student.
     * @return the student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Returns the lesson.
     * @return the lesson
     */
    public Lesson getLesson() {
        return lesson;
    }

    /**
     * Returns the attendance status.
     * @return PRESENT or ABSENT
     */
    public AttendanceStatus getStatus() {
        return status;
    }

    /**
     * Returns the date of the lesson.
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the student.
     * @param student the student
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * Sets the lesson.
     * @param lesson the lesson
     */
    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    /**
     * Sets the attendance status.
     * @param status the status
     */
    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    /**
     * Sets the date.
     * @param date the date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(student, that.student)
                && Objects.equals(lesson, that.lesson)
                && status == that.status
                && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, lesson, status, date);
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "student=" + student +
                ", lesson=" + lesson +
                ", status=" + status +
                ", date=" + date +
                '}';
    }
}