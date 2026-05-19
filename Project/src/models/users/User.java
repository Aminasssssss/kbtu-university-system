package models.users;

import database.Database;
import enums.Language;
import utils.PasswordUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract base class for all users in the university system.
 * Passwords are stored as SHA-256 hashes — never in plain text.
 * Implements Serializable for database persistence.
 */
public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String email;
    private String passwordHash;
    private Language language;
    private boolean loggedIn;

    /**
     * Creates a new user. The password is hashed with SHA-256 before storing.
     *
     * @param id       unique user ID
     * @param name     full name
     * @param email    email address used for login
     * @param password plain text password — hashed immediately, never stored as-is
     * @param language preferred language
     */
    public User(String id, String name, String email, String password, Language language) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = PasswordUtils.hash(password);
        this.language = language;
        this.loggedIn = false;
    }

    /**
     * Constructor for cases where the password is already hashed.
     * Used by ResearcherDecorator to avoid double-hashing.
     *
     * @param id              unique user ID
     * @param name            full name
     * @param email           email address
     * @param password        value to store as password field
     * @param language        preferred language
     * @param alreadyHashed   if true, password is stored as-is without hashing
     */
    protected User(String id, String name, String email, String password,
                   Language language, boolean alreadyHashed) {
        this.id = id;
        this.name = name;
        this.email = email;
        if (alreadyHashed) {
            this.passwordHash = password;
        } else {
            this.passwordHash = PasswordUtils.hash(password);
        }
        this.language = language;
        this.loggedIn = false;
    }

    /**
     * Marks the user as logged in and records a log entry.
     * Password verification is handled by MainMenu before this is called.
     */
    public void login() {
        this.loggedIn = true;
        Database.getInstance().addLog("Login: " + email);
    }

    /**
     * Logs the user out and records a log entry.
     */
    public void logout() {
        this.loggedIn = false;
        Database.getInstance().addLog("Logout: " + email);
    }

    /**
     * Returns whether this user is currently logged in.
     *
     * @return true if logged in
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Sends a message to another user and records it in the log.
     *
     * @param receiver the recipient
     * @param content  the message text
     */
    public void sendMessage(User receiver, String content) {
        Database.getInstance().addLog("Message from " + email + " to " + receiver.getEmail() + ": " + content);
    }

    /**
     * Changes the user's password. The new password is hashed before storing.
     *
     * @param newRawPassword the new plain text password
     */
    public void changePassword(String newRawPassword) {
        this.passwordHash = PasswordUtils.hash(newRawPassword);
        Database.getInstance().addLog("Password changed: " + email);
    }

    /**
     * Sets the user's preferred language.
     *
     * @param language the new language
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * Returns the user's unique ID.
     *
     * @return user ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the user's full name.
     *
     * @return full name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the user's email address.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the user's preferred language.
     *
     * @return language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Returns the stored SHA-256 password hash.
     * Never returns the plain text password.
     *
     * @return password hash
     */
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
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
