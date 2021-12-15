package events.createEvents;

import events.EventConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import login.LoginServerConstants;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;
import java.io.IOException;

/**
 * This class was created to serve as main page of "event" menu
 */
public class EventCreateWelcomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        ClientInfo clientInfoObj = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            resp.getWriter().println(EventConstants.WELCOME_PAGE_HEADER);
            resp.getWriter().println("<p><a href=\"/createEvent\">Create Event</a>");
            resp.getWriter().println("<p><a href=\"/allEvents\">See All Events</a>");
            resp.getWriter().println("<p><a href=\"/transfer\">Transfer your tickets</a>");
            resp.getWriter().println("<p><a href=\"/home\">Back</a>");
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
        }else{
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>Hello, Please Login again using link below</h1>");
            resp.getWriter().println("<p><a href=\"/\">Click to login</a>");
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
        }
    }

}
