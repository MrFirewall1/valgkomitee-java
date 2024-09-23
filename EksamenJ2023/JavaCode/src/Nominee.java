import java.util.ArrayList;
import java.util.List;

// nominee-klassen representerer en kandidat som kan nomineres og motta stemmer og kommentarer.
class Nominee {
    private final String name;          // kandidatens navn er uforanderlig (final).
    private int votes;
    private final List<String> comments; // en liste over kommentarer til kandidaten.


    //legger til konstruktør for å opprette en ny kandidat med gitt navn.
    public Nominee(String name) {
        this.name = name;
        this.votes = 0;
        this.comments = new ArrayList<>(); // her ppretter jeg en tom liste for kommentarer.
    }

    // metode for å hente kandidatens navn ved hjelp av gettere
    public String getName() {
        return name;
    }


    public int getVotes() {
        return votes;
    }


    public void addVote() {
        votes++;  // her øker antall stemmer med én med ++
    }


    public List<String> getComments() {
        return comments;
    }

    // metode for å legge til en kommentar til kandidaten.
    public void addComment(String comment) {
        comments.add(comment);  // legger til kommentaren i listen.
    }
}
