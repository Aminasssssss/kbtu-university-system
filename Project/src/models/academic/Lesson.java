package models.academic;

import enums.LessonType;
import models.users.Teacher;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a single lesson (lecture or practice) within a course.
 * A course may have separate teachers for lecture and practice slots.
 */
public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique lesson identifier. */
    private String lessonId;

    /** The course this lesson belongs to. */
    private Course course;

    /** Type of lesson: LECTURE or PRACTICE. */
    private LessonType lessonType;

    /** The teacher conducting this lesson. */
    private Teacher instructor;

    /** Date and time of the lesson. */
    private LocalDateTime dateTime;

    /** Room where the lesson takes place. */
    private String room;

    /** Topic of the lesson. */
    private String topic;

    /** Whether the lesson has been attended. */
    private boolean attended;

    /**
     * Creates a new lesson.
     * @param lessonId unique lesson identifier
     * @param course the course this lesson belongs to
     * @param lessonType type of lesson
     * @param instructor the teacher
     * @param dateTime date and time
     * @param room room number
     */
    public Lesson(String lessonId, Course course, LessonType lessonType,
                  Teacher instructor, LocalDateTime dateTime, String room) {
        this.lessonId = lessonId;
        this.course = course;
        this.lessonType = lessonType;
        this.instructor = instructor;
        this.dateTime = dateTime;
        this.room = room;
    }

    /**
     * Returns the type of lesson.
     * @return LECTURE or PRACTICE
     */
    public LessonType getType() {
        return lessonType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson)) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(lessonId, lesson.lessonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId);
    }

    @Override
    public String toString() {
        return String.format("Lesson{id='%s', course='%s', type=%s, room='%s', dateTime=%s}",
                lessonId, course.getName(), lessonType, room, dateTime);
    }


    public String getLessonId() { return lessonId; }
    public void setLessonId(String lessonId) { this.lessonId = lessonId; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public LessonType getLessonType() { return lessonType; }
    public void setLessonType(LessonType lessonType) { this.lessonType = lessonType; }

    public Teacher getInstructor() { return instructor; }
    public void setInstructor(Teacher instructor) { this.instructor = instructor; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public boolean isAttended() { return attended; }
    public void setAttended(boolean attended) { this.attended = attended; }
}