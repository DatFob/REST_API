package events.createEvents;

import dataBase.DBCPDataSource;
import dataBase.JDBC;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import login.LoginServerConstants;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

/**
 * This class was created to show response after creating a new event by a user
 */
public class CreateEventRespServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        ClientInfo clientInfoObj = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in
            String email = clientInfoObj.getEmail();

            String eventName = req.getParameter("eventName");
            String eventDate = req.getParameter("eventDate");
            Date date = java.sql.Date.valueOf(eventDate);
            String eventLocation = req.getParameter("eventLocation");
            String eventCreator = req.getParameter("eventCreator");
            String eventCreatorEmail = req.getParameter("eventCreatorEmail");
            int eventTicketNum = Integer.parseInt(req.getParameter("eventTicketNum"));

            try(Connection connection = DBCPDataSource.getConnection()) {
                //Insert new event to Events table
                JDBC.executeInsertEvent(connection,eventName, date, eventLocation, eventCreator, eventCreatorEmail, eventTicketNum);
                resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
                resp.getWriter().println("<h1>Event Has been created</h1>");
                resp.getWriter().println("<p><a href=\"/home\">Main Page</a>");
                resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
            } catch (SQLException throwables) {
                resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
                resp.getWriter().println("<h1>Oops something went wrong, please try again</h1>");
                resp.getWriter().println("<p><a href=\"/home\">Main Page</a>");
                resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
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
