package utilities;

public class ClientInfo {

    private String name;
    private String email;

    public String getEmail() {
        return email;
    }

    /**
     * Constructor
     * @param name
     */
    public ClientInfo(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * return name
     * @return
     */
    public String getName() {
        return name;
    }
}
