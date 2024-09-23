import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// main klassen som implementerer liste over studenter
// Map over kandidater
// databaseforbindesler for både studenter og stemmer
class VotingSystem {
    private final List<Student> students;
    private final Map<String, Nominee> nominees;
    private final Connection studentDbConnection;
    private Connection voteDbConnection;

    // Konstruktør som setter opp egenskapene til objektet
    public VotingSystem(Connection studentDbConnection, Connection voteDbConnection) {
        this.students = new ArrayList<>();
        this.nominees = new HashMap<>();
        this.studentDbConnection = studentDbConnection;
        this.voteDbConnection = voteDbConnection;
    }


// metoder som håndterer studenter og kandidater
    public void addStudent(Student student) {
        students.add(student);
    }

    public void nominate(Nominee nominee) {
        nominees.put(nominee.getName(), nominee);
    }

    public List<String> getCurrentNominees() {
        return new ArrayList<>(nominees.keySet());
    }

    public boolean vote(Student student, String nomineeName) {
        if (!student.hasVoted() && nominees.containsKey(nomineeName)) {
            Nominee nominee = nominees.get(nomineeName);
            nominee.addVote();
            student.markVoted();

            saveVoteToDatabase(student, nominee); // Save vote to the database

            return true;
        }
        return false;
    }

    public Nominee getWinner() {
        Nominee winner = null;
        int maxVotes = -1;
        for (Nominee nominee : nominees.values()) {
            if (nominee.getVotes() > maxVotes) {
                maxVotes = nominee.getVotes();
                winner = nominee;
            }
        }
        return winner;
    }

    public Student getStudentById(int studentId) { //henter student basert på ID
        for (Student student : students) {
            if (student.getStudentId() == studentId) {
                return student;
            }
        }
        return null;
    }

    public void addCommentToNominee(String nomineeName, String comment) {
        if (nominees.containsKey(nomineeName)) {
            Nominee nominee = nominees.get(nomineeName);
            nominee.addComment(comment);
            saveCommentToDatabase(nominee, comment); // Save comment to the database
        }
    }

    public List<Student> getStudents() {
        return students;
    }

    private void saveVoteToDatabase(Student student, Nominee nominee) {
        //metode for å lagre stemmer og kommentarer
        // i data basen
        try {
            String query = "INSERT INTO votes (student_id, nominee_name) VALUES (?, ?)";
            PreparedStatement preparedStatement = voteDbConnection.prepareStatement(query);
            preparedStatement.setInt(1, student.getStudentId());
            preparedStatement.setString(2, nominee.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveCommentToDatabase(Nominee nominee, String comment) {
        try {
            String query = "INSERT INTO comments (nominee_name, comment) VALUES (?, ?)";
            PreparedStatement preparedStatement = voteDbConnection.prepareStatement(query);
            preparedStatement.setString(1, nominee.getName());
            preparedStatement.setString(2, comment);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
