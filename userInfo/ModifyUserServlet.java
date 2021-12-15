package userInfo;

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
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModifyUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        ClientInfo clientInfoObj = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in
            String email = clientInfoObj.getEmail();

            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");

            try(Connection connection = DBCPDataSource.getConnection()) {
                JDBC.updateUserName(connection,firstName,lastName,email);
                JDBC.executeSelectPrintUsers(connection);
                resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
                resp.getWriter().println("<h1>Your name is updated successfully</h1>");
                resp.getWriter().println("<p><a href=\"/home\">Click to login</a>");
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
