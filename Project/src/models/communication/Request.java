package models.communication;

import enums.RequestStatus;
import models.users.User;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a tech support request in the university system.
 * Statuses: VIEWED, ACCEPTED, REJECTED, DONE.
 * Example requests: fixing a projector, repairing a printer, etc.
 */
public class Request implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** Unique request identifier. */
    private String requestId;
    
    /** Description of the request. */
    private String description;
    
    /** Current status of the request. */
    private RequestStatus status;
    
    /** The user who submitted the request. */
    private User sender;
    
    /** Timestamp when the request was created. */
    private LocalDateTime createdAt;

    /**
     * Creates a new request with status VIEWED.
     * @param requestId unique request identifier
     * @param description description of the request
     * @param sender the user submitting the request
     */
    public Request(String requestId, String description, User sender) {
        this.requestId = requestId;
        this.description = description;
        this.sender = sender;
        this.status = RequestStatus.VIEWED;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Returns the request ID.
     * @return request ID
     */
    public String getRequestId() { return requestId; }

    /**
     * Returns the description.
     * @return description
     */
    public String getDescription() { return description; }

    /**
     * Returns the current status.
     * @return request status
     */
    public RequestStatus getStatus() { return status; }

    /**
     * Updates the request status.
     * @param status new status
     */
    public void setStatus(RequestStatus status) { this.status = status; }

    /**
     * Returns the sender.
     * @return the user who sent the request
     */
    public User getSender() { return sender; }

    /**
     * Returns the creation timestamp.
     * @return creation time
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "Request{" + requestId + ", " + description + ", " + status + "}";
    }
}