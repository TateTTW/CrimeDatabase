import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseInterface {
    public static ObservableList<String> officerRankQuery() {
        ObservableList<String> list = FXCollections.observableArrayList();
        ResultSet rs = DatabaseInterface.databaseRetrieve("SELECT * FROM Ranks");
        try {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<CriminalRecord> criminalIdQuery(int criminalID) {
        ResultSet rs = DatabaseInterface.databaseRetrieve("SELECT * FROM Criminals WHERE CriminalID = " + criminalID);
        ObservableList<CriminalRecord> list = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                list.add(new CriminalRecord(Integer.valueOf(rs.getString(1)).intValue(), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), Integer.valueOf(rs.getString(7)).intValue(), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14).substring(0, 10)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<CriminalRecord> criminalNameQuery(String firstName, String lastName) {
        ResultSet rs = DatabaseInterface.databaseRetrieve("SELECT * FROM Criminals WHERE FirstName = \"" + firstName + "\" AND LastName = \"" + lastName + "\";");
        ObservableList<CriminalRecord> list = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                list.add(new CriminalRecord(Integer.valueOf(rs.getString(1)).intValue(), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), Integer.valueOf(rs.getString(7)).intValue(), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14).substring(0, 10)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<CriminalRecord> criminalsByCrimesQuery(String crime) {
        String sql = "SELECT Criminals.*, Crimes.Crime FROM Criminals INNER JOIN (Crimes INNER JOIN CriminalRecords ON Crimes.CrimeID = CriminalRecords.CrimeID) ON Criminals.CriminalID = CriminalRecords.CriminalID WHERE (((Crimes.Crime)=\"" + crime + "\"));";
        return DatabaseInterface.getCriminalRecords(DatabaseInterface.databaseRetrieve(sql));
    }

    public static ObservableList<CrimeHistory> crimeHistoryByIdQuery(int id) {
        ObservableList<CrimeHistory> list = FXCollections.observableArrayList();
        String sql = "SELECT Crimes.Crime, Crimes.Severity, CriminalRecords.CrimeDate, Officers.FirstName, Officers.LastName, CriminalRecords.Description , CriminalRecords.Address, CriminalRecords.City, CriminalRecords.States FROM Officers INNER JOIN (Criminals INNER JOIN (Crimes INNER JOIN CriminalRecords ON Crimes.CrimeID = CriminalRecords.CrimeID) ON Criminals.CriminalID = CriminalRecords.CriminalID) ON Officers.OfficerID = CriminalRecords.OfficerID WHERE (((Criminals.CriminalID)=" + id + "));";
        ResultSet rs = DatabaseInterface.databaseRetrieve(sql);
        try {
            while (rs.next()) {
                list.add(new CrimeHistory(rs.getString(1), rs.getString(2), rs.getString(3).substring(0, 10), String.valueOf(rs.getString(4)) + " " + rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<CriminalRecord> allCriminalsQuery() {
        return DatabaseInterface.getCriminalRecords(DatabaseInterface.databaseRetrieve("SELECT * FROM Criminals;"));
    }

    private static ObservableList<CriminalRecord> getCriminalRecords(ResultSet rs) {
        ObservableList<CriminalRecord> list = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                list.add(new CriminalRecord(Integer.valueOf(rs.getString(1)).intValue(), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), Integer.valueOf(rs.getString(7)).intValue(), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14).substring(0, 10)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<OfficerHistory> officerHistoryByIdQuery(int id) {
        ObservableList<OfficerHistory> list = FXCollections.observableArrayList();
        String sql = "SELECT Crimes.Crime, Crimes.Severity, CriminalRecords.CrimeDate, CriminalRecords.Description, Criminals.FirstName, Criminals.LastName, CriminalRecords.address, CriminalRecords.city, CriminalRecords.states FROM Criminals INNER JOIN (Officers INNER JOIN (Crimes INNER JOIN CriminalRecords ON Crimes.CrimeID = CriminalRecords.CrimeID) ON Officers.OfficerID = CriminalRecords.OfficerID) ON Criminals.CriminalID = CriminalRecords.CriminalID WHERE (((Officers.OfficerID)=" + id + "));";
        ResultSet rs = DatabaseInterface.databaseRetrieve(sql);
        try {
            while (rs.next()) {
                list.add(new OfficerHistory(rs.getString(1), rs.getString(2), rs.getString(3).substring(0, 10), rs.getString(4), String.valueOf(rs.getString(5)) + " " + rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<OfficerRecord> officersByNameQuery(String firstName, String lastName) {
        ResultSet rs = DatabaseInterface.databaseRetrieve("SELECT Officers.* FROM Officers WHERE (((Officers.FirstName)=\"" + firstName + "\") AND ((Officers.LastName)=\"" + lastName + "\"));");
        return DatabaseInterface.getOfficerRecords(rs);
    }

    public static ObservableList<OfficerRecord> allOfficersQuery() {
        return DatabaseInterface.getOfficerRecords(DatabaseInterface.databaseRetrieve("SELECT Officers.* FROM Officers;"));
    }

    public static ObservableList<OfficerRecord> officerByIdQuery(int id) {
        return DatabaseInterface.getOfficerRecords(DatabaseInterface.databaseRetrieve("SELECT Officers.* FROM Officers\r\nWHERE (((Officers.OfficerID)=" + id + "));"));
    }

    public static ObservableList<OfficerRecord> officerByRankQuery(String rank) {
        return DatabaseInterface.getOfficerRecords(DatabaseInterface.databaseRetrieve("SELECT Officers.* FROM Officers WHERE (((Officers.Ranks)= \"" + rank + "\"));"));
    }

    private static ObservableList<OfficerRecord> getOfficerRecords(ResultSet rs) {
        ObservableList<OfficerRecord> list = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                list.add(new OfficerRecord(Integer.valueOf(rs.getString(1)).intValue(), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15).substring(0, 10), rs.getString(16).substring(0, 10)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ResultSet databaseRetrieve(String sql) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://CriminalDatabase.accdb");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            conn.close();
            return rs;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean databaseInsert(String sql) {
        boolean success = false;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://CriminalDatabase.accdb");
            Statement st = conn.createStatement();
            success = st.execute(sql);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
}