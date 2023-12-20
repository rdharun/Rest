package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {


    public static String getProperty(String propertyName) {
        String configPropertyFilePath = "src/main/resources/config.properties";
        Properties properties = new Properties();
        try {
            FileInputStream configFile = new FileInputStream(configPropertyFilePath);
            properties.load(configFile);
            return properties.getProperty(propertyName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

