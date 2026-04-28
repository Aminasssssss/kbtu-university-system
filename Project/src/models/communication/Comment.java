package models.communication;

import models.users.User;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a comment left by a user on a news article.
 */
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique comment identifier. */
    private String commentId;

    /** The user who wrote the comment. */
    private User author;

    /** The comment text. */
    private String text;

    /** Date when the comment was posted. */
    private String date;

    /**
     * Creates a new comment.
     * @param commentId unique comment identifier
     * @param author the user writing the comment
     * @param text the comment text
     */
    public Comment(String commentId, User author, String text) {
        this.commentId = commentId;
        this.author = author;
        this.text = text;
        this.date = java.time.LocalDate.now().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(commentId, comment.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }

    @Override
    public String toString() {
        return String.format("Comment{id='%s', author='%s', date=%s, text='%s'}",
                commentId, author.getName(), date, text);
    }


    public String getCommentId() { return commentId; }

    public User getAuthor() { return author; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getDate() { return date; }
}