package database;

import models.academic.Attendance;
import models.users.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Database instance;

    private Map<String, User> users;
    private List<String> logs;
    private List<Attendance> attendances;

    /**
     * Creates database.
     */
    private Database() {
        users = new HashMap<>();
        logs = new ArrayList<>();
        attendances = new ArrayList<>();
    }

    /**
     * Returns database instance.
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Adds user.
     */
    public void addUser(User user) {
        users.put(user.getEmail(), user);
        addLog("User added: " + user.getEmail());
    }

    /**
     * Removes user.
     */
    public void removeUser(String email) {
        users.remove(email);
        addLog("User removed: " + email);
    }

    /**
     * Returns user by email.
     */
    public User getUser(String email) {
        return users.get(email);
    }

    /**
     * Checks login data.
     */
    public boolean authenticate(String email, String password) {
        User user = users.get(email);
        return user != null && user.getPassword().equals(password);
    }

    /**
     * Adds log message.
     */
    public void addLog(String log) {
        logs.add(log);
    }

    /**
     * Returns logs.
     */
    public List<String> getLogs() {
        return logs;
    }

    /**
     * Returns all users.
     */
    public Map<String, User> getUsers() {
        return users;
    }

    /**
     * Adds attendance record.
     */
    public void addAttendance(Attendance attendance) {
        attendances.add(attendance);
        addLog("Attendance added for student: " + attendance.getStudent().getEmail());
    }

    /**
     * Returns attendance list.
     */
    public List<Attendance> getAttendances() {
        return attendances;
    }

    /**
     * Saves database to file.
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
     * Loads database from file.
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
