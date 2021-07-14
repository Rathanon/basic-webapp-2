package io.muic.ooc.webapp.config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationLoader {

    // static method for loading config from disk
    // default Location: config.properties (same folder)
    public static ConfigProperties load(){
        String configFileName = "config.properties";
        try(FileInputStream fin = new FileInputStream(configFileName)) {
            Properties prop = new Properties();
            prop.load(fin);
            // get the property value and print it out
            String driverClassName = prop.getProperty("database.driverClassName");
            String connectionUrl = prop.getProperty("database.connectionUrl");
            String username = prop.getProperty("database.username");
            String password = prop.getProperty("database.password");
            return new ConfigProperties.ConfigPropertiesBuilder()
                    .databaseDriverClassName(driverClassName)
                    .databaseConnectionUrl(connectionUrl)
                    .databaseUsername(username)
                    .databasePassword(password)
                    .build();

        } catch (Exception e) {
            return null;
        }
    }
}
