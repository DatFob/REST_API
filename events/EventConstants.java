package events;

/**
 * Public class used to store constant strings
 */
public class EventConstants {
    public static final String WELCOME_PAGE_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Welcome to event page</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";
    public static final String CREATE_EVENT_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Create your own event!</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";
    public static final String NO_EVENT_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>There is no record of any events</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";
    public static final String DISPLAY_EVENT_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Events list</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";
    public static final String EVENT_DETAIL_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Events detail</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";
    public static final String TICKET_TRANSFER_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Ticket Transfer Page</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";
    public static final String TICKET_PURCHASE_SUCCESSFULLY = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Yay! Get ready for your new event!</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";
    public static final String TICKET_PURCHASE_UNSUCCESSFULLY = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Ooops, something went wrong!</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";
    public static final String CREATE_EVENT_FORM = "<form name=\"createEventForm\" method=\"post\" action=\"createEventResp\">\n" +
            "    Event Name: <input type=\"text\" name=\"eventName\"/> <br/>\n" +
            "    Date (Ex. 2013-09-04): <input type=\"text\" name=\"eventDate\"/> <br/>\n" +
            "    Location: <input type=\"text\" name=\"eventLocation\"/> <br/>\n" +
            "    Creator: <input type=\"text\" name=\"eventCreator\"/> <br/>\n" +
            "    Creator Email: <input type=\"text\" name=\"eventCreatorEmail\"/> <br/>\n" +
            "    Total tickets Number: <input type=\"number\" min=\"0\" step=\"1\" name=\"eventTicketNum\"/> <br/>\n" +
            "    <input type=\"submit\" value=\"Submit\" />\n" +
            "</form>";

    public static final String EVENT_PURCHASE_FORM = "<form name=\"eventPurchaseForm\" method=\"post\" action=\"buyTix\">\n" +
            "    Event Name: <input type=\"text\" name=\"eventName\"/> <br/>\n" +
            "    How many tickets?: <input type=\"number\" min=\"0\" step=\"1\" name=\"eventTicketNum\"/> <br/>\n" +
            "    <input type=\"submit\" value=\"Submit\" />\n" +
            "</form>";

    public static final String EVENT_LOOKUP_FORM = "<form name=\"eventLookupForm\" method=\"post\" action=\"detail\">\n" +
            "    Event Name: <input type=\"text\" name=\"eventName\"/> <br/>\n" +
            "    <input type=\"submit\" value=\"Submit\" />\n" +
            "</form>";

    public static final String TICKET_TRANSFER_FORM = "<form name=\"ticketTransferForm\" method=\"post\" action=\"transferResp\">\n" +
            "    Event Name: <input type=\"text\" name=\"eventName\"/> <br/>\n" +
            "    How many tickets?: <input type=\"number\" min=\"1\" step=\"1\" name=\"ticketNum\"/> <br/>\n" +
            "    Recipient email: <input type=\"text\" name=\"recipientEmail\"/> <br/>\n" +
            "    <input type=\"submit\" value=\"Submit\" />\n" +
            "</form>";

    public static final String EVENT_DETAIL_POST = "<form name=\"eventDetailForm\" method=\"post\" action=\"tweet\">\n" +
            "    Event Name: <input type=\"text\" name=\"eventName\"/> <br/>\n" +
            "    <input type=\"submit\" value=\"Tweet about it!\" />\n" +
            "</form>";

    public static final String SEARCH_EVENTS_FORM = "<form name=\"eventDetailForm\" method=\"post\" action=\"search\">\n" +
            "    Full or partial city name:<input type=\"text\" name=\"location\"/> <br/>\n" +
            "    <input type=\"submit\" value=\"Search\" />\n" +
            "</form>";
}
