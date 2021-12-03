package dataBase;
import com.google.gson.Gson;
import utilities.Config;

import java.io.FileNotFoundException;
import java.io.FileReader;
public class Utilities {

    public static final String configFileName = "dbConfig.json";

    /**
     * Read in the configuration file.
     * @return
     */
    public static DBConfig readConfig() {

        DBConfig config = null;
        Gson gson = new Gson();
        try {
            config = gson.fromJson(new FileReader(configFileName), DBConfig.class);
            System.out.println(config.getDatabase());

        } catch (FileNotFoundException e) {
            System.err.println("Config file config.json not found: " + e.getMessage());
        }
        return config;
    }
}
