package events.tickets;

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
import java.sql.SQLException;

/**
 * This class was created to allow user to purchase tickets fo ran event
 */
public class EventPurchaseSevlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        ClientInfo clientInfoObj = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in
            String email = clientInfoObj.getEmail();

            String eventName = req.getParameter("eventName");
            int ticketNum = Integer.parseInt(req.getParameter("eventTicketNum"));
            String userEmail = clientInfoObj.getEmail();
            try(Connection connection = DBCPDataSource.getConnection()) {
                int eventID = JDBC.selectEventID(connection,eventName);
                int userID = JDBC.selectUserID(connection,userEmail);
                int ticketRemaining = JDBC.selectTicketLeft(connection,eventID);
                int ticketLeft = ticketRemaining - ticketNum;
                //if we have enough ticket for this purchase, else we don't execute purchase
                if(ticketLeft >=0){
                    JDBC.insertPurchase(connection,eventID,userID,ticketNum);
                    JDBC.updateTicketNum(connection,eventID,ticketLeft);
                    resp.setStatus(HttpStatus.OK_200);
                    resp.getWriter().println(EventConstants.TICKET_PURCHASE_SUCCESSFULLY);
                    resp.getWriter().println("<h1>Ticket Purchased Successfully</h1>");
                }else{
                    resp.setStatus(HttpStatus.OK_200);
                    resp.getWriter().println(EventConstants.TICKET_PURCHASE_UNSUCCESSFULLY);
                    resp.getWriter().println("<h1>Sorry, do not have enough tickets left for your purchase</h1>");
                }
                resp.getWriter().println("<p><a href=\"/home\">Main Page</a>");
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
