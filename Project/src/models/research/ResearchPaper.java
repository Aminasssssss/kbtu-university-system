package models.research;

import enums.CitationFormat;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a research paper published by a researcher.
 * Supports citation in Plain Text and BibTeX formats.
 * Implements Comparable for sorting by citations.
 */
public class ResearchPaper implements Comparable<ResearchPaper>, Serializable {

    private static final long serialVersionUID = 1L;
    
    /** Title of the paper. */
    private String title;
    
    /** Number of citations. */
    private int citations;
    
    /** Journal where the paper was published. */
    private String journal;
    
    /** Digital Object Identifier. */
    private String doi;
    
    /** Authors of the paper. */
    private String authors;
    
    /** Publication date. */
    private String date;
    
    /** Number of pages. */
    private int pages;

    /**
     * Creates a new research paper.
     * @param title paper title
     * @param citations number of citations
     * @param journal journal name
     * @param doi DOI identifier
     * @param authors paper authors
     * @param date publication date
     * @param pages number of pages
     */
    public ResearchPaper(String title, int citations, String journal, 
                         String doi, String authors, String date, int pages) {
        this.title = title;
        this.citations = citations;
        this.journal = journal;
        this.doi = doi;
        this.authors = authors;
        this.date = date;
        this.pages = pages;
    }

    /**
     * Returns citation in the specified format.
     * @param f citation format (PLAIN_TEXT or BIBTEX)
     * @return formatted citation string
     */
    public String getCitation(CitationFormat f) {
        if (f == CitationFormat.PLAIN_TEXT) {
            return getPlainTextCitation();
        } else if (f == CitationFormat.BIBTEX) {
            return getBibtexCitation();
        }
        return "";
    }

    /**
     * Generates Plain Text citation.
     * @return plain text citation
     */
    private String getPlainTextCitation() {
        return authors + ". \"" + title + ".\" " + journal + " (" + date + "): " + pages + " pages.";
    }

    /**
     * Generates BibTeX citation.
     * @return bibtex format citation
     */
    private String getBibtexCitation() {
        return "@article{" + getFirstAuthor().toLowerCase() + date.substring(0, 4) + 
               ",\n  author = {" + authors + "},\n  title = {" + title + "}," +
               "\n  journal = {" + journal + "},\n  year = {" + date.substring(0, 4) + "}," +
               "\n  pages = {" + pages + "},\n  doi = {" + doi + "}\n}";
    }

    /**
     * Returns the first author's last name.
     * @return first author name
     */
    private String getFirstAuthor() {
        if (authors != null && authors.contains(" ")) {
            return authors.split(" ")[0];
        }
        return authors != null ? authors : "unknown";
    }

    // ── Getters & Setters ─────────────────────────────────────────────────

    public String getTitle() { return title; }
    public int getCitations() { return citations; }
    public String getJournal() { return journal; }
    public String getDoi() { return doi; }
    public String getAuthors() { return authors; }
    public String getDate() { return date; }
    public int getPages() { return pages; }

    public void setCitations(int citations) { this.citations = citations; }
    public void setJournal(String journal) { this.journal = journal; }

    @Override
    public int compareTo(ResearchPaper other) {
        return Integer.compare(this.citations, other.citations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchPaper that = (ResearchPaper) o;
        return Objects.equals(doi, that.doi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doi);
    }

    @Override
    public String toString() {
        return "ResearchPaper{" +
                "title='" + title + '\'' +
                ", citations=" + citations +
                ", journal='" + journal + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}