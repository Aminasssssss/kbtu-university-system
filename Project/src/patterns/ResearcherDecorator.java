package patterns;

import enums.CitationFormat;
import models.research.Researcher;
import models.research.ResearchPaper;
import models.users.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Decorator pattern implementation.
 * Adds Researcher functionality to any existing User without modifying
 * the original class hierarchy. A Teacher or Student can become a
 * Researcher simply by wrapping them in a ResearcherDecorator.
 */
public class ResearcherDecorator extends User implements Researcher {

    private static final long serialVersionUID = 1L;

    private User decoratedUser;
    private List<ResearchPaper> papers;

    /**
     * Creates a ResearcherDecorator that wraps an existing user.
     * The password is passed with alreadyHashed=true to avoid double-hashing,
     * since the wrapped user's password is already a SHA-256 hash.
     *
     * @param user the user to decorate with Researcher functionality
     */
    public ResearcherDecorator(User user) {
        super(user.getId(), user.getName(), user.getEmail(),
              user.getPassword(), user.getLanguage(), true);
        this.decoratedUser = user;
        this.papers = new ArrayList<>();
    }

    /**
     * Returns the original user that was wrapped by this decorator.
     *
     * @return the decorated user
     */
    public User getDecoratedUser() {
        return decoratedUser;
    }

    /**
     * Adds a research paper to this researcher's list.
     *
     * @param paper the paper to add
     */
    public void addPaper(ResearchPaper paper) {
        papers.add(paper);
    }

    /**
     * Returns all research papers published by this researcher.
     *
     * @return list of research papers
     */
    public List<ResearchPaper> getPapers() {
        return papers;
    }

    /**
     * Calculates the h-index based on published papers.
     * The h-index is the maximum value h such that h papers
     * have at least h citations each.
     *
     * @return h-index value
     */
    @Override
    public int calculateHIndex() {
        if (papers.isEmpty()) {
            return 0;
        }
        for (int i = 1; i < papers.size(); i++) {
            ResearchPaper key = papers.get(i);
            int j = i - 1;
            while (j >= 0 && papers.get(j).getCitations() < key.getCitations()) {
                papers.set(j + 1, papers.get(j));
                j--;
            }
            papers.set(j + 1, key);
        }
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
     * Prints all research papers sorted according to the given comparator.
     *
     * @param c the comparator to sort by (date, citations, or page count)
     */
    @Override
    public void printPapers(Comparator<? super ResearchPaper> c) {
        List<ResearchPaper> sorted = new ArrayList<>(papers);
        sorted.sort(c);
        for (ResearchPaper paper : sorted) {
            System.out.println(paper);
        }
    }

    /**
     * Returns a formatted citation for the first paper in the list.
     *
     * @param f the citation format (PLAIN_TEXT or BIBTEX)
     * @return formatted citation string
     */
    @Override
    public String getCitation(CitationFormat f) {
        if (papers.isEmpty()) {
            return getName() + " has no publications yet.";
        }
        return papers.get(0).getCitation(f);
    }

    @Override
    public String toString() {
        return "ResearcherDecorator{user=" + decoratedUser + ", papers=" + papers.size() + "}";
    }
}
