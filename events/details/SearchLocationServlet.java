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
 * This class was created to search for an event's name where the location of such event is partially matched
 */
public class SearchLocationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        ClientInfo clientInfoObj = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in
            String location = req.getParameter("location");

            try(Connection connection = DBCPDataSource.getConnection()) {
                ResultSet results = JDBC.selectEventsFromLocation(connection,location);
                if(!results.isBeforeFirst()){
                    resp.getWriter().println(EventConstants.NO_EVENT_HEADER);
                    resp.getWriter().println("<h1>No parties in your city, time to move.</h1>");
                }else{
                    StringBuilder message = new StringBuilder();
                    int i = 1;
                    while(results.next()) {
                        message.append("Event "+i+" " + results.getString("Name")+"\n");
                        i++;
                    }
                    System.out.println("The message is"+message);
                    resp.getWriter().println(EventConstants.EVENT_DETAIL_HEADER);
                    resp.getWriter().println("<h1>Search results are below</h1>");
                    resp.getWriter().println("<h1>"+message+"</h1>");
                }
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
