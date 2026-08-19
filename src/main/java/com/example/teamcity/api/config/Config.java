package com.example.teamcity.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String CONFIG_PROPERTIES = "config.properties";
    private static Config config;
    private Properties properties;

    private Config() {
        properties = new Properties();
        loadProperties(CONFIG_PROPERTIES);
    }

    public static Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }


    /*
    После выполнения метода все проперти станут доступны:
       properties.getProperty("url");
       properties.getProperty("username");
     */
    private void loadProperties(String fileName) {
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream(fileName)) {
            if (stream == null) {
                throw new IllegalArgumentException("File not found: " + fileName);
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file: " + fileName, e);
        }
    }

    public static String getProperty(String key){
        return getConfig().properties.getProperty(key);
    }

}
