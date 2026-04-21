package models.users;

import database.Database;
import enums.Language;

import java.io.Serializable;
import java.util.Objects;

public abstract class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
    private String name;
    private String email;
    private String password;
    private Language language;
    private boolean loggedIn;

    /**
     * Creates user.
     */
    public User(String id, String name, String email, String password, Language language) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.language = language;
    }

    /**
     * Logs user in.
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
     * Logs user out.
     */
    public void logout() {
        this.loggedIn = false;
        Database.getInstance().addLog("User logged out: " + email);
    }
    
    /**
     * Returns login status.
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Sends message.
     */
    public void sendMessage(User receiver, String content) {
        Database.getInstance().addLog("Message sent from " + email + " to " + receiver.getEmail());
    }

    /**
     * Returns id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns language.
     */
    public Language getLanguage() {
        return language;
    }
    
    /**
     * Returns password.
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