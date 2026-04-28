package models.communication;

import models.research.ResearchPaper;
import models.users.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a university research journal.
 * All users (not only researchers) can subscribe to journals.
 * When a new paper is published, all subscribers are notified.
 *
 * This uses the Observer (Publish-Subscribe) design pattern:
 * - Journal is the Subject/Publisher
 * - Subscribers are Observers
 * New journals can appear dynamically.
 */
public class Journal implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique journal identifier. */
    private String journalId;

    /** Journal name. */
    private String name;

    /** International Standard Serial Number. */
    private String issn;

    /** Journal description. */
    private String description;

    /** List of published research papers in this journal. */
    private List<ResearchPaper> publishedPapers;

    /** Subscribers who will be notified when a new paper is published. */
    private List<User> subscribers;

    /**
     * Creates a new journal.
     * @param journalId unique journal identifier
     * @param name journal name
     * @param issn ISSN number
     */
    public Journal(String journalId, String name, String issn) {
        this.journalId = journalId;
        this.name = name;
        this.issn = issn;
        this.publishedPapers = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    /**
     * Subscribes a user to this journal.
     * @param user the user to subscribe
     * @return false if the user is already subscribed
     */
    public boolean subscribe(User user) {
        if (!subscribers.contains(user)) {
            subscribers.add(user);
            return true;
        }
        return false;
    }

    /**
     * Unsubscribes a user from this journal.
     * @param user the user to unsubscribe
     * @return true if the user was subscribed
     */
    public boolean unsubscribe(User user) {
        return subscribers.remove(user);
    }

    /**
     * Publishes a new research paper in this journal.
     * Automatically notifies all subscribers (Observer pattern).
     * @param paper the research paper to publish
     */
    public void publishPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
        notifySubscribers(paper);
    }

    /**
     * Notifies all subscribers that a new paper has been published.
     * @param paper the published paper
     */
    public void notifySubscribers(ResearchPaper paper) {
        String msg = "[JOURNAL NOTIFICATION] New paper in \"" + name + "\": " + paper.getTitle();
        for (User subscriber : subscribers) {
            System.out.println("  → Notifying " + subscriber.getName() + ": " + msg);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Journal)) return false;
        Journal journal = (Journal) o;
        return Objects.equals(journalId, journal.journalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(journalId);
    }

    @Override
    public String toString() {
        return String.format("Journal{id='%s', name='%s', issn='%s', papers=%d, subscribers=%d}",
                journalId, name, issn, publishedPapers.size(), subscribers.size());
    }


    public String getJournalId() { return journalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIssn() { return issn; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<ResearchPaper> getPublishedPapers() { return publishedPapers; }

    public List<User> getSubscribers() { return subscribers; }
}