package models.academic;

import enums.AttendanceStatus;
import models.users.Student;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Attendance implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Student student;
    private Object lesson;
    private AttendanceStatus status;
    private LocalDate date;

    /**
     * Creates attendance.
     */
    public Attendance(Student student, Object lesson, AttendanceStatus status, LocalDate date) {
        this.student = student;
        this.lesson = lesson;
        this.status = status;
        this.date = date;
    }

    /**
     * Marks present.
     */
    public void markPresent() {
        this.status = AttendanceStatus.PRESENT;
    }

    /**
     * Marks absent.
     */
    public void markAbsent() {
        this.status = AttendanceStatus.ABSENT;
    }

    /**
     * Returns student.
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Returns lesson.
     */
    public Object getLesson() {
        return lesson;
    }

    /**
     * Returns status.
     */
    public AttendanceStatus getStatus() {
        return status;
    }

    /**
     * Returns date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets student.
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * Sets lesson.
     */
    public void setLesson(Object lesson) {
        this.lesson = lesson;
    }

    /**
     * Sets status.
     */
    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    /**
     * Sets date.
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