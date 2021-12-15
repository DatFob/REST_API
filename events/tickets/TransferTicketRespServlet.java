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
 * This class was created to serve as responding page after a user transfer owned ticket to another user
 */
public class TransferTicketRespServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        ClientInfo clientInfoObj = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in
            String email = clientInfoObj.getEmail();

            String eventName = req.getParameter("eventName");
            int ticketNum = Integer.parseInt(req.getParameter("ticketNum"));
            String recipientEmail = req.getParameter("recipientEmail");

            try(Connection connection = DBCPDataSource.getConnection()) {
                int senderID = JDBC.selectUserID(connection,email);
                int ticketPurchased = JDBC.selectTicketLeftOrders(connection,senderID);
                int ticketLeft = ticketPurchased - ticketNum;
                //if enough ticket,execute purchase else don't
                if(ticketLeft >= 0){
                    int recipientID = JDBC.selectUserID(connection,recipientEmail);
                    System.out.println("Recipient ID:" + recipientID);
                    int eventID = JDBC.selectEventID(connection, eventName);
                    System.out.println("Event ID:" + eventID);
                    JDBC.insertPurchase(connection,eventID,recipientID,ticketNum);
                    JDBC.updateTicketNumOrders(connection, eventID, senderID, ticketLeft);
                    resp.setStatus(HttpStatus.OK_200);
                    resp.getWriter().println(EventConstants.TICKET_PURCHASE_SUCCESSFULLY);
                    resp.getWriter().println("<h1>Ticket transferred successfully</h1>");
                }else{
                    resp.setStatus(HttpStatus.OK_200);
                    resp.getWriter().println(EventConstants.TICKET_PURCHASE_UNSUCCESSFULLY);
                    resp.getWriter().println("<h1>You do not own that many tickets</h1>");
                }
                resp.getWriter().println("<p><a href=\"/home\">Home Page</a>");
                resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                resp.setStatus(HttpStatus.OK_200);
                resp.getWriter().println(EventConstants.TICKET_TRANSFER_HEADER);
                resp.getWriter().println("<h1>Ooops something went wrong please try again</h1>");
                resp.getWriter().println("<p><a href=\"/home\">Home Page</a>");
                resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
            }
        }else{
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>Hello, Please Login again using link below</h1>");
            resp.getWriter().println("<p><a href=\"/\">Click to login</a>");

        }

    }
}
