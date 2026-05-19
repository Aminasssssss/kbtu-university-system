package database;

import models.academic.Attendance;

import models.communication.Complaint;
import models.communication.Message;
import models.communication.Message;
import models.communication.News;
import models.communication.Request;
import models.users.User;
import utils.PasswordUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton database for the university system.
 * Stores users, logs, attendance records, tech support requests,
 * and teacher ratings. Supports serialization between sessions.
 */
public class Database implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Database instance;

    private Map<String, User> users;
    private List<String> logs;
    private List<Log> logEntries;
    private List<Attendance> attendances;

    /** Tech support requests — persisted so they survive restarts. */
    private List<Request> techRequests;

    /** Teacher ratings stored by teacher email. */
    private Map<String, List<Integer>> teacherRatings;

    /** Complaints filed by teachers, persisted between sessions. */
    private List<Complaint> complaints;

    /** University news feed, persisted between sessions. */
    private List<News> newsFeed;

    /** Messages between users, persisted between sessions. */
    private List<Message> messages;

    /** Academic journals for subscription. */
    private List<models.communication.Journal> journals;

    /** Messages sent between users. */

    private Database() {
        users = new HashMap<>();
        logs = new ArrayList<>();
        logEntries = new ArrayList<>();
        attendances = new ArrayList<>();
        techRequests = new ArrayList<>();
        teacherRatings = new HashMap<>();
        complaints = new ArrayList<>();
        newsFeed = new ArrayList<>();
        messages = new ArrayList<>();
        journals = new ArrayList<>();
        journals.add(new models.communication.Journal("J001", "KBTU Research Journal", "2222-3333"));
        journals.add(new models.communication.Journal("J002", "Computer Science Review", "1111-2222"));
        journals.add(new models.communication.Journal("J003", "IEEE Transactions", "0018-9340"));
    }

    /**
     * Returns the singleton instance of Database.
     *
     * @return the single instance
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Adds a user to the database, keyed by email.
     *
     * @param user the user to add
     */
    public void addUser(User user) {
        users.put(user.getEmail(), user);
        addLog("User added: " + user.getEmail());
    }

    /**
     * Removes a user from the database by email.
     *
     * @param email the email of the user to remove
     */
    public void removeUser(String email) {
        users.remove(email);
        addLog("User removed: " + email);
    }

    /**
     * Returns a user by email, or null if not found.
     *
     * @param email the email to look up
     * @return the user or null
     */
    public User getUser(String email) {
        return users.get(email);
    }

    /**
     * Verifies a login attempt using SHA-256 password hashing.
     *
     * @param email       the user's email
     * @param rawPassword the plain text password entered by the user
     * @return true if credentials are valid
     */
    public boolean authenticate(String email, String rawPassword) {
        User user = users.get(email);
        if (user == null) {
            return false;
        }
        return PasswordUtils.verify(rawPassword, user.getPassword());
    }

    /**
     * Adds a simple log message.
     *
     * @param log the message to log
     */
    public void addLog(String log) {
        logs.add(log);
        logEntries.add(new Log(log, null));
    }

    /**
     * Adds a log entry with the user who performed the action.
     *
     * @param action the description of what happened
     * @param user   the user who performed the action
     */
    public void addLog(String action, User user) {
        String entry = "[" + user.getName() + "] " + action;
        logs.add(entry);
        logEntries.add(new Log(action, user));
    }

    /**
     * Returns all log messages as plain strings.
     *
     * @return list of log strings
     */
    public List<String> getLogs() {
        return logs;
    }

    /**
     * Returns all log entries as Log objects.
     *
     * @return list of Log entries
     */
    public List<Log> getLogEntries() {
        return logEntries;
    }

    /**
     * Returns all users in the database, keyed by email.
     *
     * @return map of users
     */
    public Map<String, User> getUsers() {
        return users;
    }

    /**
     * Adds an attendance record.
     *
     * @param attendance the attendance record to add
     */
    public void addAttendance(Attendance attendance) {
        attendances.add(attendance);
        addLog("Attendance recorded for: " + attendance.getStudent().getEmail());
    }

    /**
     * Returns all attendance records.
     *
     * @return list of attendance records
     */
    public List<Attendance> getAttendances() {
        return attendances;
    }


    /**
     * Adds a tech support request to the database so it persists between sessions.
     *
     * @param request the request to add
     */
    public void addTechRequest(Request request) {
        techRequests.add(request);
    }

    /**
     * Returns all tech support requests.
     *
     * @return list of tech requests
     */
    public List<Request> getTechRequests() {
        return techRequests;
    }


    /**
     * Records a student's rating for a teacher.
     * Ratings are stored by teacher email so they persist between sessions.
     *
     * @param teacherEmail the teacher's email
     * @param rating       rating from 1 to 5
     */
    public void addTeacherRating(String teacherEmail, int rating) {
        if (!teacherRatings.containsKey(teacherEmail)) {
            teacherRatings.put(teacherEmail, new ArrayList<>());
        }
        teacherRatings.get(teacherEmail).add(rating);
    }

    /**
     * Returns the average rating for a teacher, or 0.0 if no ratings yet.
     *
     * @param teacherEmail the teacher's email
     * @return average rating
     */
    public double getAverageRating(String teacherEmail) {
        List<Integer> ratings = teacherRatings.get(teacherEmail);
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        int sum = 0;
        for (int r : ratings) {
            sum += r;
        }
        return (double) sum / ratings.size();
    }

    /**
     * Returns how many ratings a teacher has received.
     *
     * @param teacherEmail the teacher's email
     * @return number of ratings
     */
    public int getRatingCount(String teacherEmail) {
        List<Integer> ratings = teacherRatings.get(teacherEmail);
        if (ratings == null) {
            return 0;
        }
        return ratings.size();
    }


    /**
     * Adds a complaint to the database so it persists between sessions.
     *
     * @param complaint the complaint to store
     */
    public void addComplaint(Complaint complaint) {
        complaints.add(complaint);
    }

    /**
     * Returns all complaints stored in the database.
     *
     * @return list of all complaints
     */
    public List<Complaint> getComplaints() {
        return complaints;
    }


    /**
     * Adds a news article to the persistent news feed.
     *
     * @param news the news article to add
     */
    public void addNews(News news) {
        newsFeed.add(news);
    }

    /**
     * Returns all news articles in the university news feed.
     *
     * @return list of news articles
     */
    public List<News> getNewsFeed() {
        return newsFeed;
    }



    /**
     * Sends a message from one user to another and stores it.
     *
     * @param message the message to store
     */
    public void sendMessage(Message message) {
        messages.add(message);
        addLog("Message from " + message.getSender().getEmail()
                + " to " + message.getReceiver().getEmail());
    }

    /**
     * Returns all messages received by a specific user.
     *
     * @param email the recipient's email
     * @return list of messages for that user
     */
    public List<Message> getMessagesFor(String email) {
        List<Message> inbox = new ArrayList<>();
        for (Message m : messages) {
            if (m.getReceiver().getEmail().equals(email)) {
                inbox.add(m);
            }
        }
        return inbox;
    }

    /**
     * Stores a message between users.
     *
     * @param message the message to store
     */
    public void addMessage(Message message) {
        messages.add(message);
    }

    /**
     * Returns all messages received by a user.
     *
     * @param email the recipient email
     * @return list of received messages
     */

    /**
     * Returns the list of academic journals in the system.
     *
     * @return list of journals
     */
    public List<models.communication.Journal> getJournals() {
        return journals;
    }


    /**
     * Saves the database to a file.
     *
     * @param fileName the file path to save to
     */
    public void saveToFile(String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
            System.out.println("Data saved to: " + fileName);
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    /**
     * Loads the database from a file. Starts fresh if the file does not exist.
     *
     * @param fileName the file path to load from
     */
    public static void loadFromFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("No saved data found, starting with a fresh database.");
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            instance = (Database) in.readObject();
            System.out.println("Data loaded from: " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Database{users=" + users.size() + ", logs=" + logs.size() + "}";
    }
}
