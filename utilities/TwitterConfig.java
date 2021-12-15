package utilities;


/**
 * Twitter class to store twitter information
 */
public class TwitterConfig {
    private String access_token;
    private String access_token_secret;
    private String consumer_key;
    private String consumer_key_secret;

    public String getAccess_token() {
        return access_token;
    }

    public String getAccess_token_secret() {
        return access_token_secret;
    }

    public String getConsumer_key() {
        return consumer_key;
    }

    public String getConsumer_key_secret() {
        return consumer_key_secret;
    }

    public TwitterConfig(String access_token, String access_token_secret, String consumer_key, String consumer_key_secret) {
        this.access_token = access_token;
        this.access_token_secret = access_token_secret;
        this.consumer_key = consumer_key;
        this.consumer_key_secret = consumer_key_secret;
    }
}
