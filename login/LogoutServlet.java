package login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utilities.HTTPFetcher;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // log out by invalidating the session
        req.getSession().invalidate();
        resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
        resp.getWriter().println("<h1>Thanks for playing</h1>");
        resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);

    }
}
