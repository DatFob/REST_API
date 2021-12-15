package events.tickets;


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
 * This class was created to transfer ticket to another user
 */
public class TransferTicketServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        ClientInfo clientInfoObj = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            resp.getWriter().println(EventConstants.CREATE_EVENT_HEADER);
            resp.getWriter().println("<h2>Event Transfer Form</h2>");
            resp.getWriter().println(EventConstants.TICKET_TRANSFER_FORM);
            resp.getWriter().println("<p><a href=\"/home\">Home page</a>");
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
