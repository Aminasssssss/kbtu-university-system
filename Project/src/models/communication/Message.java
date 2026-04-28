package models.communication;

import models.users.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a direct message sent between users.
 * Any user can send a message to any other user.
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique message identifier. */
    private String messageId;

    /** The user who sent the message. */
    private User sender;

    /** The user who receives the message. */
    private User receiver;

    /** Subject of the message. */
    private String subject;

    /** Body text of the message. */
    private String body;

    /** Date when the message was sent. */
    private String date;

    /** Whether the message has been read. */
    private boolean isRead;

    /**
     * Creates a new message.
     * @param messageId unique message identifier
     * @param sender the user sending the message
     * @param receiver the user receiving the message
     * @param subject message subject
     * @param body message body
     */
    public Message(String messageId, User sender, User receiver, String subject, String body) {
        this.messageId = messageId;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
        this.date = LocalDate.now().toString();
        this.isRead = false;
    }

    /**
     * Marks the message as read.
     */
    public void markAsRead() {
        this.isRead = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(messageId, message.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }

    @Override
    public String toString() {
        return String.format("Message{id='%s', from='%s', to='%s', subject='%s', date=%s, read=%b}",
                messageId, sender.getName(), receiver.getName(), subject, date, isRead);
    }


    public String getMessageId() { return messageId; }

    public User getSender() { return sender; }

    public User getReceiver() { return receiver; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getDate() { return date; }

    public boolean isRead() { return isRead; }
}