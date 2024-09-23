import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DatabaseConnector {

    //jeg definerer URL-en for databasene og bruker jdbc til å kunne
    //logge meg inn for å hente følgende databaser
    private static final String db1Url = "jdbc:mysql://localhost:3306/studentdb";
    private static final String db2Url = "jdbc:mysql://localhost:3306/votedb";

    // en metode for å koble til studentDb som er vår student database
    public Connection connectToStudentDb() throws SQLException {
        return DriverManager.getConnection(db1Url, "root", "sami2001");
    }

    //samme gjelder her men med voteDb(stemme databasen)
    public Connection connectToVoteDb() throws SQLException {
        //jeg lager samtidig som jeg returnerer tilkoblingen til stemmedatabasen
        return DriverManager.getConnection(db2Url, "root", "sami2001");
    }
}
