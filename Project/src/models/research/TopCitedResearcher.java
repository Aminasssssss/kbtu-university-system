package models.research;

import models.communication.News;
import patterns.ResearcherDecorator;
import java.util.List;

/**
 * Automatically finds and generates news about the top cited researcher.
 * Used to create auto-generated announcements in the university system.
 */
public class TopCitedResearcher {

    /** The top cited researcher. */
    private Researcher researcher;
    
    /** Total citations of the top researcher. */
    private int citations;

    /**
     * Creates a new TopCitedResearcher and automatically finds the top researcher.
     * @param papers list of all research papers
     * @param researchers list of all researchers
     */
    public TopCitedResearcher(List<ResearchPaper> papers, List<Researcher> researchers) {
        this.researcher = findTopResearcher(researchers);
        this.citations = countCitations(this.researcher);
    }

    /**
     * Finds the researcher with the highest total citations.
     */
    private Researcher findTopResearcher(List<Researcher> researchers) {
        if (researchers == null || researchers.isEmpty()) {
            return null;
        }

        Researcher top = null;
        int maxCitations = 0;

        for (Researcher r : researchers) {
            int total = countCitations(r);
            if (total > maxCitations) {
                maxCitations = total;
                top = r;
            }
        }
        return top;
    }

    /**
     * Counts total citations for a researcher.
     * Uses ResearcherDecorator's papers if available.
     */
    private int countCitations(Researcher researcher) {
        if (researcher == null) return 0;
        
        if (researcher instanceof ResearcherDecorator) {
            ResearcherDecorator decorator = (ResearcherDecorator) researcher;
            int total = 0;
            for (ResearchPaper paper : decorator.getPapers()) {
                total += paper.getCitations();
            }
            return total;
        }
        
        return 0;
    }

    /**
     * Returns the top cited researcher.
     * @return top researcher or null
     */
    public Researcher getTopResearcher() {
        return researcher;
    }

    /**
     * Generates a news article about the top cited researcher.
     * @return the generated news, or null if no researcher found
     */
    public News generateNews() {
        if (researcher == null) {
            return null;
        }

        String newsId = "TOP-" + System.currentTimeMillis();
        String researcherName = getResearcherName(researcher);
        String title = "Top Cited Researcher Announcement";
        String content = "Congratulations to " + researcherName + 
                        " — our top cited researcher with " + citations + " total citations!";

        News news = new News(newsId, title, content, "Research", null);
        news.setPinned(true);
        return news;
    }

    /**
     * Returns the name of a researcher.
     */
    private String getResearcherName(Researcher r) {
        if (r instanceof models.users.User) {
            return ((models.users.User) r).getName();
        }
        return "Researcher";
    }

    /**
     * Returns the top researcher.
     */
    public Researcher getResearcher() {
        return researcher;
    }

    /**
     * Returns total citations.
     */
    public int getCitations() {
        return citations;
    }

    @Override
    public String toString() {
        return "TopCitedResearcher{" +
                "researcher=" + getResearcherName(researcher) +
                ", citations=" + citations +
                '}';
    }
}