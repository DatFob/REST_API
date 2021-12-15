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
 * This class was created to show details of an event to client
 */
public class EventDetailServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        ClientInfo clientInfoObj = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            String eventName = req.getParameter("eventName");
            try(Connection connection = DBCPDataSource.getConnection()) {
                ResultSet results = JDBC.selectEventDetail(connection, eventName);
                //if nothing is returned, the event doesn't exist tell the user
                if(!results.isBeforeFirst()){
                    resp.getWriter().println(EventConstants.NO_EVENT_HEADER);
                    resp.getWriter().println("<h1>The event you tried to look up does not exist</h1>");
                }else{
                    //Append all information to stirng builder message and write to user
                    StringBuilder message = new StringBuilder();
                    while(results.next()) {
                        message.append("Event name: "+ results.getString("Name")+"\n");
                        message.append("Date of the event: "+ results.getDate("Date")+"\n");
                        message.append("Location of the event: "+ results.getString("Location")+"\n");
                        message.append("Creator of the event: "+ results.getString("Creator")+"\n");
                        message.append("Creator's Email of the event: "+ results.getString("CreatorEmail")+"\n");
                        message.append("Remaining tickets: "+ results.getString("TicketNum")+"\n");
                    }
                    System.out.println("The message is"+message);
                    resp.getWriter().println(EventConstants.EVENT_DETAIL_HEADER);
                    resp.getWriter().println("<h1>Event Detail is below</h1>");
                    resp.getWriter().println("<h1>"+message+"</h1>");
                }
                //Tweet an event's detail using twitter 4J
                resp.getWriter().println("<h2>Enter a event name below to tweet about an event!</h2>");
                resp.getWriter().println(EventConstants.EVENT_DETAIL_POST);
                resp.getWriter().println("<p><a href=\"/allEvents\">Return</a>");
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
