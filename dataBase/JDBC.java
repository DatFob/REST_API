package dataBase;
import com.google.gson.Gson;
import utilities.Config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
public class JDBC {
    public static void main(String[] args){
        DBConfig config = Utilities.readConfig();
        if(config == null) {
            System.exit(1);
        }

        // Make sure that mysql-connector-java is added as a dependency.
        // Force Maven to Download Sources and Documentation
        //jdbc:mysql://localhost:3306/user009
        System.out.println(config.getDatabase());
        try (Connection con = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/" + config.getDatabase(), config.getUsername(), config.getPassword())) {

            executeCreateUserTable(con);
            executeCreateEventTable(con);
            executeShowTables(con);
            System.out.println("*****************");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void executeInsertUser(Connection con, String LastName, String FirstName, String email) throws SQLException {
        String insertContactSql = "INSERT INTO Users (LastName, FirstName,email) VALUES (?, ?,?);";
        PreparedStatement insertContactStmt = con.prepareStatement(insertContactSql);
        insertContactStmt.setString(1, LastName);
        insertContactStmt.setString(2, FirstName);
        insertContactStmt.setString(3, email);
        insertContactStmt.executeUpdate();
    }

    public static void executeInsertEvent(Connection con, String name, Date date, String location, String creator, String creatorEmail, int ticketNum) throws SQLException {
        String insertContactSql = "INSERT INTO Events (Name, Date, Location, Creator, CreatorEmail, TicketNum) VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement insertContactStmt = con.prepareStatement(insertContactSql);
        insertContactStmt.setString(1, name);
        insertContactStmt.setDate(2, date);
        insertContactStmt.setString(3, location);
        insertContactStmt.setString(4, creator);
        insertContactStmt.setString(5, creatorEmail);
        insertContactStmt.setInt(6, ticketNum);
        insertContactStmt.executeUpdate();
    }

    public static void updateUserName(Connection con, String lastName, String firstName,String email) throws SQLException{
        PreparedStatement stmt = con.prepareStatement("UPDATE Users SET LastName = ?, FirstName = ? WHERE Email = ?;");
        stmt.setString(1,lastName);
        stmt.setString(2,firstName);
        stmt.setString(3,email);

        int recordNum = stmt.executeUpdate();
        System.out.println(recordNum + " records updated");
    }

    /**
     * A method to demonstrate using a PrepareStatement to execute a database select
     * @param con
     * @throws SQLException
     */
    public static void executeSelectPrintUsers(Connection con) throws SQLException {
        String selectAllContactsSql = "SELECT * FROM Users;";
        PreparedStatement selectAllContactsStmt = con.prepareStatement(selectAllContactsSql);
        ResultSet results = selectAllContactsStmt.executeQuery();
        while(results.next()) {
            System.out.printf("UserId %s\n", results.getString("UserId"));
            System.out.printf("LastName: %s\n", results.getString("LastName"));
            System.out.printf("FirstName: %s\n", results.getString("FirstName"));
        }
    }

    /**
     * A method to demonstrate using a PrepareStatement to execute a database select
     * @param con
     * @throws SQLException
     */
    public static ResultSet selectUserWithLastname(Connection con, String lastName) throws SQLException {
        String selectAllContactsSql = "SELECT * FROM Users WHERE LastName=\'"+lastName+"\';";
        PreparedStatement selectAllContactsStmt = con.prepareStatement(selectAllContactsSql);
        ResultSet results = selectAllContactsStmt.executeQuery();
        return results;
    }

    public static void executeCreateUserTable(Connection con) throws SQLException {
        String createTable = "CREATE TABLE IF NOT EXISTS Users(\n" +
                "    UserId int AUTO_INCREMENT,\n" +
                "    LastName varchar(255),\n" +
                "    FirstName varchar(255),\n" +
                "    Email varchar(225) NOT NULL UNIQUE,\n" +
                "    PRIMARY KEY (UserId)\n" +
                ");";
        PreparedStatement statement = con.prepareStatement(createTable);
        statement.execute();
    }

    public static void executeCreateEventTable(Connection con) throws SQLException {
        String createTable = "CREATE TABLE IF NOT EXISTS Events(\n" +
                "    EventId int AUTO_INCREMENT,\n" +
                "    Name varchar(255),\n" +
                "    Date Date,\n" +
                "    Location varchar(255),\n" +
                "    Creator varchar(225),\n" +
                "    CreatorEmail varchar(225),\n" +
                "    TicketNum int(255),\n" +
                "    PRIMARY KEY (EventId)\n" +
                ");";
        PreparedStatement statement = con.prepareStatement(createTable);
        statement.execute();
    }

    public static void executeShowTables(Connection con) throws SQLException {
        String createTable = "SHOW TABLES;";
        PreparedStatement selectAllContactsStmt = con.prepareStatement(createTable);
        ResultSet results = selectAllContactsStmt.executeQuery();
        while(results.next()) {
            System.out.printf("Table: %s\n", results.getString(1));
        }
    }

}

