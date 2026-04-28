package models.communication;

import models.users.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a news article published in the university system.
 *
 * Key rules:
 * - News with topic "Research" must be pinned/prioritized.
 * - When a Researcher publishes a paper, an announcement is auto-generated.
 * - News about the top-cited researcher is auto-generated.
 * - News items support comments.
 */
public class News implements Serializable, Comparable<News> {

    private static final long serialVersionUID = 1L;

    /** Topic constant that triggers automatic pinning. */
    public static final String TOPIC_RESEARCH = "Research";

    /** Unique news identifier. */
    private String newsId;

    /** News title. */
    private String title;

    /** News content. */
    private String content;

    /** News topic/category. */
    private String topic;

    /** The author of the news. */
    private User author;

    /** Timestamp when the news was published. */
    private LocalDateTime publishedAt;

    /** Whether the news is pinned to the top. */
    private boolean isPinned;

    /** List of comments on this news article. */
    private List<Comment> comments;

    /**
     * Creates a new news article.
     * Automatically pins the news if the topic is "Research".
     * @param newsId unique news identifier
     * @param title news title
     * @param content news content
     * @param topic news topic
     * @param author the author
     */
    public News(String newsId, String title, String content, String topic, User author) {
        this.newsId = newsId;
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.author = author;
        this.publishedAt = LocalDateTime.now();
        this.comments = new ArrayList<>();
        this.isPinned = TOPIC_RESEARCH.equalsIgnoreCase(topic);
    }

    /**
     * Adds a comment to this news article.
     * @param comment the comment to add
     */
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    /**
     * Removes a comment from this news article.
     * @param comment the comment to remove
     * @return true if removed
     */
    public boolean removeComment(Comment comment) {
        return comments.remove(comment);
    }

    /**
     * Compares news articles: pinned first, then by date descending (newest first).
     */
    @Override
    public int compareTo(News other) {
        if (this.isPinned && !other.isPinned) return -1;
        if (!this.isPinned && other.isPinned) return 1;
        return other.publishedAt.compareTo(this.publishedAt);
    }

    /**
     * Creates an auto-generated news item announcing a new research paper.
     * @param newsId unique news identifier
     * @param researcherName name of the researcher
     * @param paperTitle title of the published paper
     * @param systemUser the system user as author
     * @return the created news
     */
    public static News createPaperAnnouncement(String newsId, String researcherName,
                                               String paperTitle, User systemUser) {
        String title = "New Research Published by " + researcherName;
        String content = researcherName + " has published a new paper: \"" + paperTitle + "\".";
        return new News(newsId, title, content, TOPIC_RESEARCH, systemUser);
    }

    /**
     * Creates an auto-generated news item about the top-cited researcher.
     * @param newsId unique news identifier
     * @param researcherName name of the researcher
     * @param citations total number of citations
     * @param systemUser the system user as author
     * @return the created news
     */
    public static News createTopCitedAnnouncement(String newsId, String researcherName,
                                                   int citations, User systemUser) {
        String title = "Top Cited Researcher: " + researcherName;
        String content = researcherName + " is the most cited researcher this year with "
                + citations + " total citations.";
        return new News(newsId, title, content, TOPIC_RESEARCH, systemUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof News)) return false;
        News news = (News) o;
        return Objects.equals(newsId, news.newsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsId);
    }

    @Override
    public String toString() {
        return String.format("News{id='%s', title='%s', topic='%s', pinned=%b, comments=%d}",
                newsId, title, topic, isPinned, comments.size());
    }


    public String getNewsId() { return newsId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) {
        this.topic = topic;
        this.isPinned = TOPIC_RESEARCH.equalsIgnoreCase(topic);
    }

    public User getAuthor() { return author; }

    public LocalDateTime getPublishedAt() { return publishedAt; }

    public boolean isPinned() { return isPinned; }
    public void setPinned(boolean pinned) { isPinned = pinned; }

    public List<Comment> getComments() { return comments; }
}