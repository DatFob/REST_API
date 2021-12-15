package login;

import dataBase.DBCPDataSource;
import dataBase.JDBC;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;

import java.io.IOException;

/**
 * Main Page of the server.
 */
public class HomePageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        ClientInfo clientInfoObj = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            req.getSession().setAttribute(LoginServerConstants.CLIENT_INFO_KEY, clientInfoObj);
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);

            resp.getWriter().println("<p><a href=\"/user\">Account Info</a>");
            resp.getWriter().println("<p><a href=\"/events\">Events</a>");
            resp.getWriter().println("<p><a href=\"/logout\">Signout</a>");
        }else{
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>Hello, Please Login again using link below</h1>");
            resp.getWriter().println("<p><a href=\"/\">Click to login</a>");
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
        }

    }
}
