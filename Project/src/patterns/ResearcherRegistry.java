package patterns;

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
 * Singleton registry that keeps track of all ResearcherDecorators in the system.
 * Maps user email to their ResearcherDecorator so any menu can check
 * whether a logged-in user is also a researcher.
 *
 * Supports serialization so researcher profiles and papers persist between sessions.
 */
public class ResearcherRegistry implements Serializable {

    private static final long serialVersionUID = 1L;

    private static ResearcherRegistry instance;

    private Map<String, ResearcherDecorator> byEmail;

    private ResearcherRegistry() {
        byEmail = new HashMap<>();
    }

    /**
     * Returns the singleton instance of ResearcherRegistry.
     *
     * @return the single instance
     */
    public static ResearcherRegistry getInstance() {
        if (instance == null) {
            instance = new ResearcherRegistry();
        }
        return instance;
    }

    /**
     * Registers a researcher by their email address.
     * If a researcher with this email is already registered, it is replaced.
     *
     * @param decorator the researcher decorator to register
     */
    public void register(ResearcherDecorator decorator) {
        byEmail.put(decorator.getEmail(), decorator);
    }

    /**
     * Returns the ResearcherDecorator for the given email,
     * or null if this user is not a researcher.
     *
     * @param email the user's email
     * @return the researcher decorator or null
     */
    public ResearcherDecorator getResearcher(String email) {
        return byEmail.get(email);
    }

    /**
     * Returns true if the user with this email is registered as a researcher.
     *
     * @param email the user's email
     * @return true if researcher
     */
    public boolean isResearcher(String email) {
        return byEmail.containsKey(email);
    }

    /**
     * Returns all registered researchers as a list.
     *
     * @return list of all researcher decorators
     */
    public List<ResearcherDecorator> getAll() {
        return new ArrayList<>(byEmail.values());
    }

    /**
     * Saves the ResearcherRegistry to a file so researcher profiles
     * and papers are preserved between sessions.
     *
     * @param fileName the file path to save to
     */
    public static void saveToFile(String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(instance);
        } catch (IOException e) {
            System.out.println("Could not save researcher registry: " + e.getMessage());
        }
    }

    /**
     * Loads the ResearcherRegistry from a file.
     * If the file does not exist, the registry stays empty.
     *
     * @param fileName the file path to load from
     */
    public static void loadFromFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            instance = (ResearcherRegistry) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not load researcher registry: " + e.getMessage());
        }
    }
}
