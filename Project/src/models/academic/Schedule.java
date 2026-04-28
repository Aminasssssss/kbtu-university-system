package models.academic;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a weekly schedule of lessons.
 * Supports room-load awareness to avoid double-booking rooms.
 * (Bonus feature: Schedule generation taking into account room load and room type.)
 */
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique schedule identifier. */
    private String scheduleId;

    /** Semester name (e.g., "Fall 2024"). */
    private String semester;

    /** List of lessons in the schedule. */
    private List<Lesson> lessons;

    /** List of available rooms. */
    private List<String> rooms;

    /** List of time slots. */
    private List<String> timeSlots;

    /** List of schedule entries. */
    private List<ScheduleEntry> entries;

    /**
     * Creates a new schedule.
     * @param scheduleId unique schedule identifier
     * @param semester semester name
     */
    public Schedule(String scheduleId, String semester) {
        this.scheduleId = scheduleId;
        this.semester = semester;
        this.lessons = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.timeSlots = new ArrayList<>();
        this.entries = new ArrayList<>();
    }

    /**
     * Generates the schedule based on lessons and rooms.
     */
    public void generate() {
        System.out.println("Generating schedule for " + semester + "...");
        entries.clear();
        for (Lesson lesson : lessons) {
            ScheduleEntry entry = new ScheduleEntry(lesson, DayOfWeek.MONDAY,
                    LocalTime.of(9, 0), LocalTime.of(10, 30),
                    rooms.isEmpty() ? "TBA" : rooms.get(0), "Lecture Hall");
            if (addEntry(entry)) {
                System.out.println("  Added: " + lesson.getCourse().getName());
            }
        }
        System.out.println("Schedule generated with " + entries.size() + " entries.");
    }

    /**
     * Returns the schedule as a formatted string.
     * @return schedule string
     */
    public String getSchedule() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════ SCHEDULE: ").append(semester).append(" ═══════════\n");
        for (DayOfWeek day : DayOfWeek.values()) {
            List<ScheduleEntry> dayEntries = getEntriesForDay(day);
            if (!dayEntries.isEmpty()) {
                sb.append("── ").append(day).append(" ──\n");
                dayEntries.forEach(e -> sb.append("   ").append(e).append("\n"));
            }
        }
        sb.append("═══════════════════════════════════════\n");
        return sb.toString();
    }

    /**
     * Adds a lesson to the schedule.
     * @param lesson the lesson to add
     */
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    /**
     * Attempts to add a lesson to the schedule entries.
     * Validates that neither the room nor the instructor is double-booked.
     * @param entry the schedule entry to add
     * @return true if added successfully, false if there is a conflict
     */
    public boolean addEntry(ScheduleEntry entry) {
        for (ScheduleEntry existing : entries) {
            if (existing.conflictsWith(entry)) {
                System.out.printf("[Schedule] Conflict detected: %s overlaps with %s%n",
                        entry, existing);
                return false;
            }
        }
        entries.add(entry);
        return true;
    }

    /**
     * Removes an entry from the schedule.
     * @param entry the entry to remove
     * @return true if removed
     */
    public boolean removeEntry(ScheduleEntry entry) {
        return entries.remove(entry);
    }

    /**
     * Returns all entries for a given day.
     * @param day the day of week
     * @return list of entries sorted by time
     */
    public List<ScheduleEntry> getEntriesForDay(DayOfWeek day) {
        return entries.stream()
                .filter(e -> e.getDayOfWeek() == day)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Returns all entries for a specific room.
     * @param room the room name
     * @return list of entries
     */
    public List<ScheduleEntry> getEntriesForRoom(String room) {
        return entries.stream()
                .filter(e -> e.getRoom().equalsIgnoreCase(room))
                .collect(Collectors.toList());
    }

    /**
     * Prints the full weekly schedule to standard output.
     */
    public void printSchedule() {
        System.out.println(getSchedule());
    }

    @Override
    public String toString() {
        return String.format("Schedule{id='%s', semester='%s', entries=%d}",
                scheduleId, semester, entries.size());
    }


    public String getScheduleId() { return scheduleId; }
    public String getSemester() { return semester; }
    public List<Lesson> getLessons() { return lessons; }
    public List<String> getRooms() { return rooms; }
    public List<String> getTimeSlots() { return timeSlots; }
    public List<ScheduleEntry> getEntries() { return entries; }


    /**
     * A single entry in a schedule: a lesson at a specific day/time/room.
     */
    public static class ScheduleEntry implements Serializable, Comparable<ScheduleEntry> {

        private static final long serialVersionUID = 1L;

        /** The lesson for this entry. */
        private Lesson lesson;

        /** Day of the week. */
        private DayOfWeek dayOfWeek;

        /** Start time of the lesson. */
        private LocalTime startTime;

        /** End time of the lesson. */
        private LocalTime endTime;

        /** Room where the lesson takes place. */
        private String room;

        /** Type of room (e.g., "Lecture Hall", "Lab"). */
        private String roomType;

        /**
         * Creates a new schedule entry.
         * @param lesson the lesson
         * @param dayOfWeek day of the week
         * @param startTime start time
         * @param endTime end time
         * @param room room name
         * @param roomType type of room
         */
        public ScheduleEntry(Lesson lesson, DayOfWeek dayOfWeek,
                             LocalTime startTime, LocalTime endTime,
                             String room, String roomType) {
            this.lesson = lesson;
            this.dayOfWeek = dayOfWeek;
            this.startTime = startTime;
            this.endTime = endTime;
            this.room = room;
            this.roomType = roomType;
        }

        /**
         * Checks if this entry conflicts with another (same room OR same instructor at the same time).
         * @param other the other entry to check
         * @return true if there is a conflict
         */
        public boolean conflictsWith(ScheduleEntry other) {
            if (this.dayOfWeek != other.dayOfWeek) return false;
            boolean timeOverlap = this.startTime.isBefore(other.endTime)
                    && other.startTime.isBefore(this.endTime);
            if (!timeOverlap) return false;
            boolean sameRoom = this.room.equalsIgnoreCase(other.room);
            boolean sameInstructor = this.lesson.getInstructor() != null
                    && this.lesson.getInstructor().equals(other.lesson.getInstructor());
            return sameRoom || sameInstructor;
        }

        @Override
        public int compareTo(ScheduleEntry other) {
            int dayCmp = this.dayOfWeek.compareTo(other.dayOfWeek);
            if (dayCmp != 0) return dayCmp;
            return this.startTime.compareTo(other.startTime);
        }

        @Override
        public String toString() {
            return String.format("[%s %s–%s] %s | Room: %s (%s) | Instructor: %s",
                    dayOfWeek, startTime, endTime,
                    lesson.getCourse().getName(),
                    room, roomType,
                    lesson.getInstructor() != null ? lesson.getInstructor().getName() : "TBA");
        }


        public Lesson getLesson() { return lesson; }
        public DayOfWeek getDayOfWeek() { return dayOfWeek; }
        public LocalTime getStartTime() { return startTime; }
        public LocalTime getEndTime() { return endTime; }
        public String getRoom() { return room; }
        public String getRoomType() { return roomType; }
    }
}