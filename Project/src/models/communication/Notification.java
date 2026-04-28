package models.communication;

import models.users.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a system notification delivered to a user.
 * Used for journal subscription alerts, paper publication announcements,
 * top-cited researcher announcements, and other system events.
 */
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique notification identifier. */
    private String notificationId;

    /** The notification message text. */
    private String message;

    /** The recipient of the notification. */
    private User recipient;

    /** Date when the notification was created. */
    private LocalDate date;

    /** Whether the notification has been read. */
    private boolean isRead;

    /**
     * Creates a new notification.
     * @param notificationId unique notification identifier
     * @param message the notification message
     * @param recipient the recipient user
     */
    public Notification(String notificationId, String message, User recipient) {
        this.notificationId = notificationId;
        this.message = message;
        this.recipient = recipient;
        this.date = LocalDate.now();
        this.isRead = false;
    }

    /**
     * Sends the notification to the recipient.
     */
    public void send() {
        System.out.println("Notification sent to " + recipient.getName() + ": " + message);
        this.isRead = false;
    }

    /**
     * Marks this notification as read.
     */
    public void markAsRead() {
        this.isRead = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return Objects.equals(notificationId, that.notificationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationId);
    }

    @Override
    public String toString() {
        return String.format("Notification{id='%s', recipient='%s', read=%b, msg='%s'}",
                notificationId, recipient.getName(), isRead, message);
    }


    public String getNotificationId() { return notificationId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public User getRecipient() { return recipient; }
    public void setRecipient(User recipient) { this.recipient = recipient; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public boolean isRead() { return isRead; }
}