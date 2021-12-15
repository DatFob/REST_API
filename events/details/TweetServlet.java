package events.details;

import com.google.gson.Gson;
import dataBase.DBCPDataSource;
import events.EventConstants;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import dataBase.JDBC;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import login.LoginServerConstants;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;
import utilities.Config;
import utilities.TwitterConfig;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class was created to use Twitter 4J to tweet out an event's detail
 */
public class TweetServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        ClientInfo clientInfoObj = (ClientInfo) req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        StringBuilder message = new StringBuilder();
        if(clientInfoObj != null) {
            String eventName = req.getParameter("eventName");
            try(Connection connection = DBCPDataSource.getConnection()) {
                ResultSet results = JDBC.selectEventDetail(connection, eventName);
                if(!results.isBeforeFirst()){
                    resp.getWriter().println(EventConstants.NO_EVENT_HEADER);
                    resp.getWriter().println("<h1>The event you tried to tweet does not exist</h1>");
                }else {
                    while(results.next()) {
                        message.append("Event name: "+ results.getString("Name")+"\n");
                        message.append("Date of the event: "+ results.getDate("Date")+"\n");
                        message.append("Location of the event: "+ results.getString("Location")+"\n");
                        message.append("Creator of the event: "+ results.getString("Creator")+"\n");
                        message.append("Creator's Email of the event: "+ results.getString("CreatorEmail")+"\n");
                        message.append("Remaining tickets: "+ results.getString("TicketNum")+"\n");
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            Gson gson = new Gson();
            TwitterConfig config = gson.fromJson(new FileReader("twitterConfig.json"), TwitterConfig.class);

            try {
                Twitter twitter = new TwitterFactory().getInstance();

                twitter.setOAuthConsumer(config.getConsumer_key(), config.getConsumer_key_secret());
                AccessToken accessToken = new AccessToken(config.getAccess_token(),
                        config.getAccess_token_secret());

                twitter.setOAuthAccessToken(accessToken);

                twitter.updateStatus(String.valueOf(message));
                resp.setStatus(HttpStatus.OK_200);
                resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
                resp.getWriter().println("<h1>The event is tweeted! Go check your twitter!</h1>");
                resp.getWriter().println("<p><a href=\"/home\">Home Page</a>");
                resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
                System.out.println("Successfully updated the status in Twitter.");
            } catch (TwitterException te) {
                te.printStackTrace();
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
