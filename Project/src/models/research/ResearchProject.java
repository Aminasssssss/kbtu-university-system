package models.research;

import exceptions.NotResearcherException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a research project with participants and published papers.
 * If someone who is not a Researcher tries to join, a NotResearcherException is thrown.
 */
public class ResearchProject implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /** The project topic. */
    private String topic;
    
    /** List of researcher participants. */
    private List<Researcher> participants;
    
    /** List of published papers in this project. */
    private List<ResearchPaper> publishedPapers;

    /**
     * Creates a new research project.
     * @param topic the project topic
     */
    public ResearchProject(String topic) {
        this.topic = topic;
        this.participants = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }

    /**
     * Adds a researcher as participant.
     * @param researcher the researcher to add
     * @throws NotResearcherException if the user is not a Researcher
     */
    public void addParticipant(Researcher researcher) throws NotResearcherException {
        if (researcher == null) {
            throw new NotResearcherException("User is not a researcher and cannot join the project!");
        }
        participants.add(researcher);
        System.out.println("Researcher added to project: " + topic);
    }

    /**
     * Adds a published paper to the project.
     * @param paper the paper to add
     */
    public void addPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
        System.out.println("Paper added to project: " + paper.getTitle());
    }

    /**
     * Returns all participants.
     * @return list of researchers
     */
    public List<Researcher> getParticipants() {
        return participants;
    }

    /**
     * Returns all published papers.
     * @return list of research papers
     */
    public List<ResearchPaper> getPublishedPapers() {
        return publishedPapers;
    }

    /**
     * Returns the project topic.
     * @return topic
     */
    public String getTopic() {
        return topic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchProject that = (ResearchProject) o;
        return Objects.equals(topic, that.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic);
    }

    @Override
    public String toString() {
        return "ResearchProject{" +
                "topic='" + topic + '\'' +
                ", participants=" + participants.size() +
                ", publishedPapers=" + publishedPapers.size() +
                '}';
    }
}