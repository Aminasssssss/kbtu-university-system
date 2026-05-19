package menu;

import database.Database;
import enums.CitationFormat;
import exceptions.NotResearcherException;
import models.communication.News;
import models.research.ResearchPaper;
import models.research.ResearchProject;
import models.research.TopCitedResearcher;
import models.users.User;
import patterns.ResearcherDecorator;
import patterns.ResearcherRegistry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Sub-menu for users who are registered as researchers.
 * Accessible from TeacherMenu and StudentMenu when the logged-in user
 * is found in the ResearcherRegistry.
 *
 * Covers h-index calculation, paper management, citations,
 * research project management, and top cited researcher announcements.
 */
public class ResearcherMenu {

    private final ResearcherDecorator researcher;
    private final Scanner scanner;
    private final User baseUser;

    private static final List<ResearchProject> projects = new ArrayList<>();
    private static int projectCounter = 1;

    /**
     * Creates a ResearcherMenu.
     *
     * @param researcher the researcher decorator with papers and h-index
     * @param baseUser   the original user (Teacher or Student)
     * @param scanner    the shared input scanner
     */
    public ResearcherMenu(ResearcherDecorator researcher, User baseUser, Scanner scanner) {
        this.researcher = researcher;
        this.baseUser = baseUser;
        this.scanner = scanner;
    }

    /**
     * Returns the shared list of research projects.
     * Other menus can read this list if needed.
     *
     * @return list of all research projects
     */
    public static List<ResearchProject> getProjects() {
        return projects;
    }

    /**
     * Displays the researcher menu and handles input until the user goes back.
     */
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         RESEARCHER TOOLS             ║");
            System.out.println("║  " + baseUser.getName());
            System.out.println("║  H-Index: " + researcher.calculateHIndex());
            System.out.println("║  Papers : " + researcher.getPapers().size());
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║ 1. View my papers (sorted)           ║");
            System.out.println("║ 2. Get citation for a paper          ║");
            System.out.println("║ 3. Publish new paper                 ║");
            System.out.println("║ 4. Create research project           ║");
            System.out.println("║ 5. Join existing project             ║");
            System.out.println("║ 6. View all university papers        ║");
            System.out.println("║ 7. Top cited researcher              ║");
            System.out.println("║ 0. Back                              ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                viewPapersSorted();
            } else if (choice.equals("2")) {
                getCitation();
            } else if (choice.equals("3")) {
                addPaper();
            } else if (choice.equals("4")) {
                createProject();
            } else if (choice.equals("5")) {
                joinProject();
            } else if (choice.equals("6")) {
                viewAllUniversityPapers();
            } else if (choice.equals("7")) {
                showTopCited();
            } else if (choice.equals("0")) {
                back = true;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Displays this researcher's papers in a chosen sort order.
     */
    private void viewPapersSorted() {
        List<ResearchPaper> papers = researcher.getPapers();
        if (papers.isEmpty()) {
            System.out.println("You have not published any papers yet.");
            return;
        }
        System.out.println("Sort by:");
        System.out.println("1. Citations (highest first)");
        System.out.println("2. Date (newest first)");
        System.out.println("3. Length in pages (longest first)");
        System.out.print("Choose: ");
        String choice = scanner.nextLine().trim();

        Comparator<ResearchPaper> comparator;
        if (choice.equals("2")) {
            comparator = Comparator.comparing(ResearchPaper::getDate).reversed();
        } else if (choice.equals("3")) {
            comparator = Comparator.comparingInt(ResearchPaper::getPages).reversed();
        } else {
            comparator = Comparator.comparingInt(ResearchPaper::getCitations).reversed();
        }

        System.out.println("\n── Your Papers ──");
        researcher.printPapers(comparator);
    }

    /**
     * Displays a formatted citation for one of this researcher's papers.
     * Supports Plain Text and BibTeX formats.
     */
    private void getCitation() {
        List<ResearchPaper> papers = researcher.getPapers();
        if (papers.isEmpty()) {
            System.out.println("No papers to cite.");
            return;
        }
        System.out.println("\n── Select Paper ──");
        for (int i = 0; i < papers.size(); i++) {
            System.out.printf("%d. %s (%d citations)%n",
                    i + 1, papers.get(i).getTitle(), papers.get(i).getCitations());
        }
        System.out.print("Select: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= papers.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            System.out.println("Format:");
            System.out.println("1. Plain Text");
            System.out.println("2. BibTeX");
            System.out.print("Choose: ");
            String formatChoice = scanner.nextLine().trim();
            CitationFormat format;
            if (formatChoice.equals("2")) {
                format = CitationFormat.BIBTEX;
            } else {
                format = CitationFormat.PLAIN_TEXT;
            }
            System.out.println("\n── Citation ──");
            System.out.println(papers.get(idx).getCitation(format));
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    /**
     * Adds a new research paper to this researcher's list.
     * Also automatically generates a news announcement and adds it to the news feed.
     */
    private void addPaper() {
        System.out.println("\n── Publish New Paper ──");
        System.out.print("Title   : ");
        String title = scanner.nextLine().trim();
        System.out.print("Journal : ");
        String journal = scanner.nextLine().trim();
        System.out.print("Authors : ");
        String authors = scanner.nextLine().trim();
        System.out.print("DOI     : ");
        String doi = scanner.nextLine().trim();
        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();
        System.out.print("Pages   : ");
        System.out.print("Citations: ");

        try {
            int pages = Integer.parseInt(scanner.nextLine().trim());
            int citations = Integer.parseInt(scanner.nextLine().trim());

            ResearchPaper paper = new ResearchPaper(title, citations, journal, doi, authors, date, pages);
            researcher.addPaper(paper);

            System.out.println("Paper published successfully!");
            System.out.println("New h-index: " + researcher.calculateHIndex());

            String newsId = "NEWS-PUB-" + System.currentTimeMillis();
            News announcement = News.createPaperAnnouncement(newsId, baseUser.getName(), title, baseUser);
            ManagerMenu.getNewsList().add(announcement);
            System.out.println("Research news auto-published: " + announcement.getTitle());

            Database.getInstance().addLog(baseUser.getName() + " published paper: " + title);

        } catch (NumberFormatException e) {
            System.out.println("Invalid number input.");
        }
    }

    /**
     * Creates a new research project and adds this researcher as a participant.
     */
    private void createProject() {
        System.out.print("Project topic: ");
        String topic = scanner.nextLine().trim();
        if (topic.isBlank()) {
            System.out.println("Topic cannot be empty.");
            return;
        }
        String projectId = "PROJ-" + String.format("%03d", projectCounter++);
        ResearchProject project = new ResearchProject(topic);
        try {
            project.addParticipant(researcher);
            projects.add(project);
            System.out.println("Research project created [" + projectId + "]: " + topic);
            Database.getInstance().addLog(baseUser.getName() + " created research project " + projectId + ": " + topic);
        } catch (NotResearcherException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Allows this researcher to join an existing research project.
     * Throws NotResearcherException if the user is not a researcher — but since
     * only ResearcherDecorators reach this menu, that should never happen here.
     */
    private void joinProject() {
        if (projects.isEmpty()) {
            System.out.println("No research projects available to join.");
            return;
        }
        System.out.println("\n── Research Projects ──");
        for (int i = 0; i < projects.size(); i++) {
            ResearchProject p = projects.get(i);
            System.out.printf("%d. %-35s  Participants: %d%n",
                    i + 1, p.getTopic(), p.getParticipants().size());
        }
        System.out.print("Select project: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= projects.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            ResearchProject project = projects.get(idx);
            project.addParticipant(researcher);
            System.out.println("Joined project: " + project.getTopic());
            Database.getInstance().addLog(baseUser.getName() + " joined project: " + project.getTopic());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (NotResearcherException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays all research papers from all researchers in the university,
     * sorted by the user's choice.
     */
    private void viewAllUniversityPapers() {
        List<ResearcherDecorator> allResearchers = ResearcherRegistry.getInstance().getAll();
        if (allResearchers.isEmpty()) {
            System.out.println("No researchers registered in the system.");
            return;
        }
        System.out.println("Sort by:");
        System.out.println("1. Citations (highest first)");
        System.out.println("2. Date (newest first)");
        System.out.println("3. Length in pages (longest first)");
        System.out.print("Choose: ");
        String choice = scanner.nextLine().trim();

        Comparator<ResearchPaper> comparator;
        if (choice.equals("2")) {
            comparator = Comparator.comparing(ResearchPaper::getDate).reversed();
        } else if (choice.equals("3")) {
            comparator = Comparator.comparingInt(ResearchPaper::getPages).reversed();
        } else {
            comparator = Comparator.comparingInt(ResearchPaper::getCitations).reversed();
        }

        List<ResearchPaper> allPapers = new ArrayList<>();
        for (ResearcherDecorator rd : allResearchers) {
            allPapers.addAll(rd.getPapers());
        }
        allPapers.sort(comparator);

        System.out.println("\n── All University Papers ──");
        for (ResearchPaper p : allPapers) {
            System.out.printf("  %-40s  Cited: %3d  Journal: %s%n",
                    p.getTitle(), p.getCitations(), p.getJournal());
        }
    }

    /**
     * Finds the top cited researcher in the university and generates
     * an auto-pinned news article about them.
     */
    private void showTopCited() {
        List<ResearcherDecorator> allResearchers = ResearcherRegistry.getInstance().getAll();
        if (allResearchers.isEmpty()) {
            System.out.println("No researchers registered in the system.");
            return;
        }
        TopCitedResearcher finder = new TopCitedResearcher(new ArrayList<>(), new ArrayList<>(allResearchers));
        News news = finder.generateNews();
        if (news == null) {
            System.out.println("Could not determine the top cited researcher.");
            return;
        }
        System.out.println("\n── Top Cited Researcher ──");
        System.out.println("Title  : " + news.getTitle());
        System.out.println("Content: " + news.getContent());
        System.out.println("Pinned : " + news.isPinned());

        ManagerMenu.getNewsList().add(news);
        System.out.println("This announcement has been auto-published to the news feed.");
        Database.getInstance().addLog("Top cited researcher news generated.");
    }
}
