package models.research;

import enums.CitationFormat;
import java.util.Comparator;

/**
 * Interface for researchers in the university system.
 * Teachers and GraduateStudents can implement this interface.
 * Can also be added via the ResearcherDecorator pattern.
 */
public interface Researcher {

    /**
     * Calculates the h-index of the researcher.
     * @return h-index value
     */
    int calculateHIndex();

    /**
     * Prints research papers sorted by the given comparator.
     * @param c Comparator for sorting papers (by date, citations, or length)
     */
    void printPapers(Comparator<? super ResearchPaper> c);

    /**
     * Returns citation in the specified format.
     * @param f Citation format (PLAIN_TEXT or BIBTEX)
     * @return formatted citation string
     */
    String getCitation(CitationFormat f);
}