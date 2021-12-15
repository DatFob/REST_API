package events.details;

import dataBase.DBCPDataSource;
import dataBase.JDBC;
import events.EventConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import login.LoginServerConstants;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class was created to list out all events in the current database
 */
public class ListEventServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        ClientInfo clientInfoObj = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            try(Connection connection = DBCPDataSource.getConnection()) {
                ResultSet results = JDBC.selectAllEvents(connection);
                //If results empty, means no events are in the data base
                if(!results.isBeforeFirst()){
                    resp.getWriter().println(EventConstants.NO_EVENT_HEADER);
                    resp.getWriter().println("<h1>No events have been created</h1>");
                    resp.getWriter().println("<p><a href=\"/home\">Home Page</a>");
                }else{
                    StringBuilder message = new StringBuilder();
                    while(results.next()) {
                        message.append(" "+ results.getString("Name")+"\n");
                    }
                    System.out.println("The message is"+message);
                    resp.getWriter().println(EventConstants.DISPLAY_EVENT_HEADER);
                    resp.getWriter().println("<h2>Look up detail of an event:</h2>");
                    resp.getWriter().println(EventConstants.EVENT_LOOKUP_FORM);
                    resp.getWriter().println("<h2>Want to buy tickets? Fill out the form: </h2>");
                    resp.getWriter().println(EventConstants.EVENT_PURCHASE_FORM);
                    resp.getWriter().println("<h2>Visiting a new city? Look up events happening at that city!</h2>");
                    resp.getWriter().println(EventConstants.SEARCH_EVENTS_FORM);
                    resp.getWriter().println("<h1>List of Events</h1>");
                    resp.getWriter().println("<h1>"+message+"</h1>");
                }
                resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
            } catch (SQLException e) {
                e.printStackTrace();
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
