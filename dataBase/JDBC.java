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
            executeCreateOrderTable(con);
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

    public static void updateTicketNum(Connection con, int eventID, int ticketNum) throws SQLException{
        PreparedStatement stmt = con.prepareStatement("UPDATE Events\n" +
                "SET TicketNum = ?\n" +
                "WHERE ID = ?;");
        stmt.setInt(1,ticketNum);
        stmt.setInt(2,eventID);

        int recordNum = stmt.executeUpdate();
        System.out.println(recordNum + " records updated");
    }

    public static void updateTicketNumOrders(Connection con, int eventID,int buyerID, int ticketNum) throws SQLException{
        PreparedStatement stmt = con.prepareStatement("UPDATE Orders\n" +
                "SET TicketPurchased = ?\n" +
                "WHERE BuyerId = ? AND EventId = ?;");
        stmt.setInt(1,ticketNum);
        stmt.setInt(2,buyerID);
        stmt.setInt(3,eventID);

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
            System.out.printf("UserId %s\n", results.getString("ID"));
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

    /**
     * A method to Select Event Name and Ticket Purchased from Events and Orders
     * @param con
     * @throws SQLException
     */
    public static ResultSet selectUserOrderHistory(Connection con, int userID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("SELECT Events.Name, Orders.TicketPurchased from Events \n" +
                "INNER JOIN Orders ON Orders.EventId = Events.ID\n" +
                "WHERE Orders.BuyerId = ?;");
        stmt.setInt(1,userID);
        ResultSet results = stmt.executeQuery();
        return results;
    }

    /**
     * A method to select all events' names
     * @param con
     * @throws SQLException
     */
    public static ResultSet selectAllEvents(Connection con) throws SQLException {
        String selectAllContactsSql = "SELECT Name FROM Events;";
        PreparedStatement selectAllContactsStmt = con.prepareStatement(selectAllContactsSql);
        ResultSet results = selectAllContactsStmt.executeQuery();
        return results;
    }

    /**
     * A method to partially match location string and return the name of such event
     * @param con
     * @throws SQLException
     */
    public static ResultSet selectEventsFromLocation(Connection con, String location) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("SELECT Name FROM Events WHERE Location LIKE ? OR Location LIKE ? OR Location LIKE ?;");
        String param1 = "%"+location;
        String param2 = location+"%";
        String param3 = "%"+location+"%";
        stmt.setString(1,param1);
        stmt.setString(2,param2);
        stmt.setString(3,param3);
        ResultSet results = stmt.executeQuery();
        return results;
    }

    /**
     * A method to Select all columns of events based on such event's name
     * @param con
     * @throws SQLException
     */
    public static ResultSet selectEventDetail(Connection con, String eventName) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("Select * from Events WHERE Name = ?;");
        stmt.setString(1,eventName);
        ResultSet results = stmt.executeQuery();
        return results;
    }

    /**
     * A method to select ID of an event where event name matches input
     * @param con
     * @throws SQLException
     */
    public static int selectEventID(Connection con, String eventName) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("SELECT ID FROM Events WHERE Name =?;");
        stmt.setString(1,eventName);
        ResultSet results = stmt.executeQuery();
        results.next();
        return results.getInt("ID");
    }

    /**
     * A method to select ID of a user where email matches input
     * @param con
     * @throws SQLException
     */
    public static int selectUserID(Connection con, String userEmail) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("SELECT ID FROM Users WHERE Email =?;");
        stmt.setString(1,userEmail);
        ResultSet results = stmt.executeQuery();
        results.next();
        return results.getInt("ID");
    }

    /**
     * A method to select total tickets an event has where ID of event matches input
     * @param con
     * @throws SQLException
     */
    public static int selectTicketLeft(Connection con, int eventID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("SELECT TicketNum FROM Events WHERE ID =?;");
        stmt.setInt(1,eventID);
        ResultSet results = stmt.executeQuery();
        results.next();
        return results.getInt("TicketNum");
    }

    /**
     * A method to select total ticket purchased from Orders where ID of user matches input
     * @param con
     * @throws SQLException
     */
    public static int selectTicketLeftOrders(Connection con, int userID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("SELECT TicketPurchased FROM Orders WHERE BuyerId =?;");
        stmt.setInt(1,userID);
        ResultSet results = stmt.executeQuery();
        results.next();
        return results.getInt("TicketPurchased");
    }

    /**
     * A method to insert purchase order into Orders table
     * @param con
     * @throws SQLException
     */
    public static void insertPurchase(Connection con, int eventID, int userID, int ticketNum) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("INSERT INTO Orders (BuyerId, EventId, TicketPurchased) VALUES (?,?,?);");
        stmt.setInt(1,userID);
        stmt.setInt(2,eventID);
        stmt.setInt(3,ticketNum);

        int recordNum = stmt.executeUpdate();
        System.out.println(recordNum + " rows inserted");
    }

    /**
     * A method to create user table
     * @param con
     * @throws SQLException
     */
    public static void executeCreateUserTable(Connection con) throws SQLException {
        String createTable = "CREATE TABLE IF NOT EXISTS Users(\n" +
                "    ID int AUTO_INCREMENT,\n" +
                "    LastName varchar(255),\n" +
                "    FirstName varchar(255),\n" +
                "    Email varchar(225) NOT NULL UNIQUE,\n" +
                "    PRIMARY KEY (ID)\n" +
                ");";
        PreparedStatement statement = con.prepareStatement(createTable);
        statement.execute();
    }

    /**
     * A method to create Events table
     * @param con
     * @throws SQLException
     */
    public static void executeCreateEventTable(Connection con) throws SQLException {
        String createTable = "CREATE TABLE IF NOT EXISTS Events(\n" +
                "    ID int AUTO_INCREMENT,\n" +
                "    Name varchar(255) NOT NULL UNIQUE,\n" +
                "    Date Date,\n" +
                "    Location varchar(255),\n" +
                "    Creator varchar(225),\n" +
                "    CreatorEmail varchar(225),\n" +
                "    TicketNum int(255),\n" +
                "    PRIMARY KEY (ID)\n" +
                ");";
        PreparedStatement statement = con.prepareStatement(createTable);
        statement.execute();
    }

    /**
     * A method to create Order (transaction) table
     * @param con
     * @throws SQLException
     */
    public static void executeCreateOrderTable(Connection con) throws SQLException {
        String createTable = "CREATE TABLE IF NOT EXISTS Orders(\n" +
                "    OrderId int AUTO_INCREMENT,\n" +
                "    BuyerId int NOT NULL,\n" +
                "    EventId int NOT NULL,\n" +
                "    TicketPurchased int(255) NOT NULL,\n" +
                "    PRIMARY KEY (OrderId),\n" +
                "    FOREIGN KEY (BuyerId) REFERENCES Users(ID),\n" +
                "    FOREIGN KEY (EventId) REFERENCES Events(ID)\n" +
                ");";
        PreparedStatement statement = con.prepareStatement(createTable);
        statement.execute();
    }

    /**
     * A method to show all tables in the database
     * @param con
     * @throws SQLException
     */
    public static void executeShowTables(Connection con) throws SQLException {
        String createTable = "SHOW TABLES;";
        PreparedStatement selectAllContactsStmt = con.prepareStatement(createTable);
        ResultSet results = selectAllContactsStmt.executeQuery();
        while(results.next()) {
            System.out.printf("Table: %s\n", results.getString(1));
        }
    }

}

