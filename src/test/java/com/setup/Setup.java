package com.setup;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Setup {
    public static Properties properties;
    public final String propertyFilePath= "./src/test/java/com/configs/config.properties";

    public Setup() {
        // implement code to load and read the properties
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }

    public String getAuthURL() {
        return properties.getProperty("authURL");
    }

    public String getBaseURL() {
        return properties.getProperty("baseURL");
    }
}
