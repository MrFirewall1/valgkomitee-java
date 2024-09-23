
// Jeg definerer grensesnitt som brukes til å representere
// egenskapene til noe som kan bli stemt på
interface Votable {
    String getName();
    boolean hasVoted();
    void markVoted();
}

// klasse som definerer en student og implemtenterer votable-grensesnittet
// for å definere regler metoden må ha for å håndtere denne kjøringen
class Student implements Votable {
    private int studentId;
    private String name;
    private String major;
    private boolean voted;

    public Student(int studentId, String name, String major) {
        this.studentId = studentId;
        this.name = name;
        this.major = major;
        this.voted = false;
    }

    public int getStudentId() {
        return studentId;
    }
    // jeg bruker ovveride til å holde styr på mine underklasser
    //det gjør det enklere med vedlikehold og struktur.
    @Override
    public String getName() {
        return name;
    }

    public String getMajor() {
        return major;
    }

    @Override
    public boolean hasVoted() {
        return voted;
    }

    @Override
    public void markVoted() {
        voted = true;
    }
}