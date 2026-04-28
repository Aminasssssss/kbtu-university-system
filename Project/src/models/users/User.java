package models.users;

import database.Database;
import enums.Language;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract base class for all users in the university system.
 * Provides authentication, messaging, and basic user information.
 * Implements Serializable for database persistence.
 */
public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique user identifier. */
    private String id;

    /** User's full name. */
    private String name;

    /** User's email address (used as login). */
    private String email;

    /** User's password for authentication. */
    private String password;

    /** User's preferred language. */
    private Language language;

    /** Whether the user is currently logged in. */
    private boolean loggedIn;

    /**
     * Creates a new User.
     * @param id unique user ID
     * @param name full name
     * @param email email address
     * @param password login password
     * @param language preferred language
     */
    public User(String id, String name, String email, String password, Language language) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.language = language;
    }

    /**
     * Logs the user in by authenticating against the Database.
     * @return true if login succeeded
     */
    public boolean login() {
        boolean success = Database.getInstance().authenticate(email, password);
        this.loggedIn = success;
        if (success) {
            Database.getInstance().addLog("User logged in: " + email);
        }
        return success;
    }

    /**
     * Logs the user out.
     */
    public void logout() {
        this.loggedIn = false;
        Database.getInstance().addLog("User logged out: " + email);
    }

    /**
     * Returns whether the user is currently logged in.
     * @return true if logged in
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Sends a message to another user.
     * @param receiver the recipient
     * @param content the message content
     */
    public void sendMessage(User receiver, String content) {
        Database.getInstance().addLog("Message sent from " + email + " to " + receiver.getEmail());
    }

    /**
     * Returns the user's ID.
     * @return user ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the user's name.
     * @return full name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the user's email.
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the user's preferred language.
     * @return language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Returns the user's password.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}