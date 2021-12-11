package login;

import com.google.gson.Gson;
import events.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import userInfo.ModifyUserServlet;
import userInfo.UserInfoServlet;
import utilities.Config;

import java.io.FileReader;

public class LoginServer {
    public static final int PORT = 8080;
    private static final String configFilename = "config.json";
    public static void main(String[] args) {
        try {
            startup();
        } catch(Exception e) {
            // catch generic Exception as that is what is thrown by server start method
            e.printStackTrace();
        }
    }

    /**
     * A helper method to start the server.
     * @throws Exception -- generic Exception thrown by server start method
     */
    public static void startup() throws Exception {

        // read the client id and secret from a config file
        Gson gson = new Gson();
        Config config = gson.fromJson(new FileReader(configFilename), Config.class);

        // create a new server
        Server server = new Server(PORT);

        // make the config information available across servlets by setting an
        // attribute in the context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setAttribute(LoginServerConstants.CONFIG_KEY, config);

        // the default path will direct to a landing page with
        // "Login with Slack" button
        context.addServlet(LandingServlet.class, "/");

        // Once authenticated, Slack will redirect the user
        // back to /login
        context.addServlet(LoginServlet.class, "/login");

        // handle logout
        context.addServlet(LogoutServlet.class, "/logout");


        context.addServlet(UserInfoServlet.class,"/user");

        //
        context.addServlet(UserInfoServlet.class,"/eventSearch");
        context.addServlet(ModifyUserServlet.class,"/modifyUser");
        context.addServlet(EventCreateWelcomeServlet.class,"/events");
        context.addServlet(CreateEventServlet.class,"/createEvent");
        context.addServlet(CreateEventRespServlet.class,"/createEventResp");
        context.addServlet(HomePageServlet.class,"/home");
        context.addServlet(EventPurchaseSevlet.class,"/buyTix");
        context.addServlet(ListEventServlet.class,"/allEvents");
        // start it up!
        server.setHandler(context);
        server.start();
    }
}
