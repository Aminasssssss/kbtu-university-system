package database;

import models.users.User;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a log entry for user actions in the system.
 * Records the action, the user who performed it, and the timestamp.
 */
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /** Description of the action performed. */
    private String action;
    
    /** The user who performed the action. */
    private User user;
    
    /** Timestamp of when the action occurred. */
    private String timestamp;

    /**
     * Creates a new log entry.
     * @param action description of the action
     * @param user the user who performed the action
     */
    public Log(String action, User user) {
        this.action = action;
        this.user = user;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Returns the action description.
     * @return action string
     */
    public String getAction() {
        return action;
    }

    /**
     * Returns the user who performed the action.
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the timestamp of the action.
     * @return formatted timestamp string
     */
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + user.getName() + " (" + user.getEmail() + "): " + action;
    }
}