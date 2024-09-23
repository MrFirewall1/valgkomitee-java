import java.util.Scanner;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {

            // Jeg legger til DatabaseConnector for å koble til følgende databaser

            DatabaseConnector dbConnector = new DatabaseConnector();
            Connection studentDbConnection = dbConnector.connectToStudentDb();
            Connection voteDbConnection = dbConnector.connectToVoteDb();

            // Jeg oppretter et VotingSystem-objekt for å administrere stemmesystemet

            VotingSystem votingSystem = new VotingSystem(studentDbConnection, voteDbConnection);

            // Jeg legger til en Student og legger til i stemmesystemet

            Student student = new Student(1, "Karius", "Informatikk");
            votingSystem.addStudent(student);

            // jeg legger til nominerte og legger dem ogsp til stemmesystemet

            Nominee nominee1 = new Nominee("Per");
            Nominee nominee2 = new Nominee("Emely");
            Nominee nominee3 = new Nominee("karl");
            votingSystem.nominate(nominee1);
            votingSystem.nominate(nominee2);
            votingSystem.nominate(nominee3);

            System.out.println("Velkommen til vår avslutningsseremoni!");

            while (true) {
                System.out.println("Hei, jeg heter Nils, jeg skal hjelpe deg her:");
                System.out.println("1. Registrer deg, vær så snill.");
                System.out.println("2. Sjekk ut våre toppnominerte for øyeblikket.");
                System.out.println("3. Avslutt - takk for at du deltok!");
                System.out.print("-> ");
                int valg = scanner.nextInt();

                switch (valg) {
                    case 1:
                        // Her så bruker jeg if else statement for å håndtere registreringen
                        // hvis vedkommende ikke taster riktig id, må den prøve igjen
                        System.out.print("Student-ID: ");
                        int studentId = scanner.nextInt();
                        Student nåværendeStudent = votingSystem.getStudentById(studentId);
                        if (nåværendeStudent != null) {
                            håndterStudentValg(votingSystem, nåværendeStudent, scanner);
                        } else {
                            System.out.println("Feil student-ID, prøv igjen.");
                        }
                        break;
                    case 2:
                        // som variabelen sier, den viser toppnominerte fra voting systenet
                        visToppNominerte(votingSystem);
                        break;
                    case 3:
                        //her avslutter vi programmet ved å bruke scanner.close
                        System.out.println("Avslutter...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Feil valg, vennligst velg et gyldig alternativ.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void håndterStudentValg(VotingSystem votingSystem, Student student, Scanner scanner) {
        System.out.println("Velkommen, " + student.getName() + "! Oi, du har ennå ikke stemt, sjekk ut menyen vår.");

        while (true) {
            System.out.println("1. Se alle nominerte.");
            System.out.println("2. Forslå en ny kandidat.");
            System.out.println("3. Velg og legg til en kommentar for en kandidat under:");
            System.out.println("4. Gå tilbake til hovedmenyen.");
            System.out.print("-> ");
            int studentValg = scanner.nextInt();

            switch (studentValg) {
                case 1:
                    //viser alle nominerte i systemet
                    visNominerte(votingSystem);
                    break;
                case 2:
                    //her så kjører det en foreslå kommando slik at vedkommende
                    //kan foreslå en ny kandidat
                    foreslåNyKandidat(votingSystem, student, scanner);
                    break;
                case 3:
                    // metoden kjører stemme og kommentar
                    stemOgKommenter(votingSystem, student, scanner);
                    break;
                case 4:
                    System.out.println("Går tilbake til hovedmenyen...");
                    return;
                default:
                    System.out.println("Ugyldig valg. Vennligst velg et gyldig alternativ.");
            }
        }
    }


    // På metoden vis ToppNominerte og antall stemmere
    private static void visToppNominerte(VotingSystem votingSystem) {
        Nominee vinner = votingSystem.getWinner();
        if (vinner != null) {
            System.out.println("Toppnominert: " + vinner.getName() + " med " + vinner.getVotes() + " stemmer.");
        } else {
            System.out.println("Ingen vinner ennå.");
        }
    }

    private static void visNominerte(VotingSystem votingSystem) {
        // metoden her viser hvem som er nominerte nå-tid
        System.out.println("Nåværende nominerte:");
        List<String> nominerte = votingSystem.getCurrentNominees();
        for (int i = 0; i < nominerte.size(); i++) { //navn med valg poeng skal metoden kjøre
            System.out.println((i + 1) + ". " + nominerte.get(i));
        }
    }


    // metode med if statement brukes for å forhindre at vedkommende
    // kan stemme gjentatte ganger
    private static void foreslåNyKandidat(VotingSystem votingSystem, Student student, Scanner scanner) {
        if (student.hasVoted()) {
            System.out.println("Du har allerede stemt og kan ikke foreslå en ny kandidat.");
            return;
        }

        System.out.print("Skriv inn navnet på den nye kandidaten: ");
        String kandidatNavn = scanner.next();
        Nominee nyKandidat = new Nominee(kandidatNavn);
        votingSystem.nominate(nyKandidat);
        System.out.println("Kandidat '" + kandidatNavn + "' har blitt foreslått.");
    }

    private static void stemOgKommenter(VotingSystem votingSystem, Student student, Scanner scanner) {
        if (student.hasVoted()) {
            System.out.println("Du har allerede stemt og kan ikke stemme igjen.");
            return;
        }

        System.out.println("Nåværende nominerte:");
        List<String> nominerte = votingSystem.getCurrentNominees();
        for (int i = 0; i < nominerte.size(); i++) {
            System.out.println((i + 1) + ". " + nominerte.get(i));
        }

        System.out.print("Skriv inn nummeret på kandidaten du vil stemme på: ");
        int kandidatNummer = scanner.nextInt(); //scanner har jeg brukt for å lese linjeskift

        if (kandidatNummer < 1 || kandidatNummer > nominerte.size()) {
            System.out.println("Ugyldig kandidatnummer.");
            return;
        }

        String valgtKandidat = nominerte.get(kandidatNummer - 1);
        scanner.nextLine();  // Leser inn linjeskift
        System.out.print("Skriv inn kommentaren din for " + valgtKandidat + ": ");
        String kommentar = scanner.nextLine();

        boolean stemmeSuksess = votingSystem.vote(student, valgtKandidat);
        if (stemmeSuksess) {
            votingSystem.addCommentToNominee(valgtKandidat, kommentar);
            System.out.println("Stemmen og kommentaren for " + valgtKandidat + " er blitt registrert.");
        } else {
            System.out.println("Ugyldig valg av kandidat eller du har allerede stemt.");
        }
    }
}
