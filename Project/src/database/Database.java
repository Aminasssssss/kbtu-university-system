package database;

import models.academic.Attendance;
import models.users.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton database for the university system.
 * Stores all users, logs, and attendance records.
 * Supports serialization for saving and loading data.
 */
public class Database implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The single instance of Database. */
    private static Database instance;

    /** Users stored by email as key. */
    private Map<String, User> users;
    
    /** System activity logs as strings (for compatibility). */
    private List<String> logs;
    
    /** System activity logs as Log objects. */
    private List<Log> logEntries;
    
    /** Attendance records. */
    private List<Attendance> attendances;

    /**
     * Private constructor for Singleton pattern.
     * Initializes empty collections.
     */
    private Database() {
        users = new HashMap<>();
        logs = new ArrayList<>();
        logEntries = new ArrayList<>();
        attendances = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of Database.
     * @return the single instance
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Adds a user to the database.
     * @param user the user to add
     */
    public void addUser(User user) {
        users.put(user.getEmail(), user);
        addLog("User added: " + user.getEmail());
    }

    /**
     * Removes a user by email.
     * @param email the email of the user to remove
     */
    public void removeUser(String email) {
        users.remove(email);
        addLog("User removed: " + email);
    }

    /**
     * Returns a user by email.
     * @param email the email of the user
     * @return the user or null if not found
     */
    public User getUser(String email) {
        return users.get(email);
    }

    /**
     * Authenticates a user by email and password.
     * @param email the user's email
     * @param password the user's password
     * @return true if authentication succeeds
     */
    public boolean authenticate(String email, String password) {
        User user = users.get(email);
        return user != null && user.getPassword().equals(password);
    }

    /**
     * Adds a log message to the system logs.
     * @param log the log message
     */
    public void addLog(String log) {
        logs.add(log);
        logEntries.add(new Log(log, null));
    }
    
    /**
     * Adds a log entry with the user who performed the action.
     * @param action the action description
     * @param user the user who performed the action
     */
    public void addLog(String action, User user) {
        logs.add("[" + user.getName() + "] " + action);
        logEntries.add(new Log(action, user));
    }

    /**
     * Returns all system logs as strings.
     * @return list of log messages
     */
    public List<String> getLogs() {
        return logs;
    }
    
    /**
     * Returns all system logs as Log objects.
     * @return list of Log entries
     */
    public List<Log> getLogEntries() {
        return logEntries;
    }

    /**
     * Returns all users in the database.
     * @return map of users by email
     */
    public Map<String, User> getUsers() {
        return users;
    }

    /**
     * Adds an attendance record to the database.
     * @param attendance the attendance record
     */
    public void addAttendance(Attendance attendance) {
        attendances.add(attendance);
        addLog("Attendance added for student: " + attendance.getStudent().getEmail());
    }

    /**
     * Returns all attendance records.
     * @return list of attendance records
     */
    public List<Attendance> getAttendances() {
        return attendances;
    }

    /**
     * Saves the database to a file using serialization.
     * @param fileName the file name to save to
     */
    public void saveToFile(String fileName) {
        try (ObjectOutputStream outputStream =
                     new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(this);
            addLog("Database saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }

    /**
     * Loads the database from a file using deserialization.
     * @param fileName the file name to load from
     */
    public static void loadFromFile(String fileName) {
        try (ObjectInputStream inputStream =
                     new ObjectInputStream(new FileInputStream(fileName))) {
            instance = (Database) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Load error: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Database{" +
                "users=" + users.size() +
                ", logs=" + logs.size() +
                ", attendances=" + attendances.size() +
                '}';
    }
}
