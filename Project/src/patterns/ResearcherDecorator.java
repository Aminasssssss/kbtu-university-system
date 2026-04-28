package patterns;

import enums.CitationFormat;
import models.research.Researcher;
import models.research.ResearchPaper;
import models.users.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Decorator pattern — adds Researcher functionality to any User.
 * Any user (Student, Teacher, etc.) can become a Researcher
 * without modifying existing classes.
 */
public class ResearcherDecorator extends User implements Researcher {

    private static final long serialVersionUID = 1L;

    /** The original user being decorated. */
    private User decoratedUser;
    
    /** List of research papers for h-index calculation. */
    private List<ResearchPaper> papers;

    /**
     * Creates a ResearcherDecorator wrapping an existing user.
     * @param user the user to decorate with Researcher functionality
     */
    public ResearcherDecorator(User user) {
        super(user.getId(), user.getName(), user.getEmail(), 
              user.getPassword(), user.getLanguage());
        this.decoratedUser = user;
        this.papers = new ArrayList<>();
    }

    /**
     * Returns the original user being decorated.
     * @return the decorated user
     */
    public User getDecoratedUser() {
        return decoratedUser;
    }
    
    /**
     * Adds a research paper to this researcher's list.
     * @param paper the paper to add
     */
    public void addPaper(ResearchPaper paper) {
        papers.add(paper);
    }
    
    /**
     * Returns all papers of this researcher.
     * @return list of research papers
     */
    public List<ResearchPaper> getPapers() {
        return papers;
    }

    /**
     * Calculates the h-index based on published papers.
     * h-index = max number h such that h papers have at least h citations.
     * @return h-index value
     */
    @Override
    public int calculateHIndex() {
        if (papers.isEmpty()) return 0;
        
        // Sort papers by citations in descending order
        papers.sort((a, b) -> Integer.compare(b.getCitations(), a.getCitations()));
        
        int hIndex = 0;
        for (int i = 0; i < papers.size(); i++) {
            if (papers.get(i).getCitations() >= i + 1) {
                hIndex = i + 1;
            } else {
                break;
            }
        }
        return hIndex;
    }

    /**
     * Prints research papers sorted by the given comparator.
     * @param c Comparator for sorting papers
     */
    @Override
    public void printPapers(Comparator<? super ResearchPaper> c) {
        papers.sort(c);
        for (ResearchPaper paper : papers) {
            System.out.println(paper);
        }
    }

    /**
     * Returns citation in the specified format.
     * @param f Citation format (PLAIN_TEXT or BIBTEX)
     * @return formatted citation string
     */
    @Override
    public String getCitation(CitationFormat f) {
        if (papers.isEmpty()) {
            return getName() + " has no publications.";
        }
        return papers.get(0).getCitation(f);
    }

    // ========== Delegated User methods ==========

    @Override
    public boolean login() {
        return decoratedUser.login();
    }

    @Override
    public void logout() {
        decoratedUser.logout();
    }

    @Override
    public void sendMessage(User receiver, String content) {
        decoratedUser.sendMessage(receiver, content);
    }

    @Override
    public String toString() {
        return "ResearcherDecorator{" + decoratedUser + ", papers=" + papers.size() + "}";
    }
}