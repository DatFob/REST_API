package userInfo;

import com.mysql.cj.xdevapi.Client;
import dataBase.DBCPDataSource;
import dataBase.JDBC;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import login.LoginServerConstants;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;
import utilities.Config;
import utilities.HTTPFetcher;
import utilities.LoginUtilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class UserInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        ClientInfo clientInfoObj = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in
            String[] nameSplit = clientInfoObj.getName().split("\\s+");
            String firstName = nameSplit[0];
            String lastName = nameSplit[1];
            try(Connection connection = DBCPDataSource.getConnection()) {
                //ResultSet names = JDBC.selectUserName(connection, clientInfoObj.getEmail());
                ResultSet results = JDBC.selectUserWithLastname(connection,lastName);
                int userID = JDBC.selectUserID(connection, clientInfoObj.getEmail());
                ResultSet orderResults = JDBC.selectUserOrderHistory(connection,userID);
                StringBuilder message = new StringBuilder();
                StringBuilder purchaseHistory = new StringBuilder();
                while(results.next()) {
                    message.append("UserID: "+ results.getString("ID")+"\n");
                    message.append("Last Name: "+ results.getString("LastName")+"\n");
                    message.append("First Name: "+ results.getString("FirstName")+"\n");
                }
                System.out.println("The message is"+message);
                while(orderResults.next()) {
                    purchaseHistory.append("Event Name:"+ orderResults.getString("Name")+"\n");
                    purchaseHistory.append("Tickets bought:"+ orderResults.getInt("TicketPurchased")+"\n");
                }
                System.out.println("The message is"+message);
                resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
                resp.getWriter().println("<h1>Your account info is below: " + clientInfoObj.getName()+" </h1>");
                resp.getWriter().println("<h1>"+message+"</h1>");
                if(purchaseHistory.isEmpty()){
                    resp.getWriter().println("<h1>You don't own any tickets yet!</h1>");
                }else{
                    resp.getWriter().println("<h1>Your ticket purchase history is below</h1>");
                    resp.getWriter().println("<h1>"+purchaseHistory+"</h1>");
                }
                resp.getWriter().println("<h2>Want to change your name? fill out this name changing form</h2>");
                resp.getWriter().println(UserInfoConsstants.NAME_FORM);
                resp.getWriter().println("<p><a href=\"/home\">Home Page</a>");
                resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>Hello, Please Login again using link below</h1>");
            resp.getWriter().println("<p><a href=\"/\">Click to login</a>");
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
        }
    }


}
