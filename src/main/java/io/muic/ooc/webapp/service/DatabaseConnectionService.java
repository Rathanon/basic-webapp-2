package io.muic.ooc.webapp.service;

import com.zaxxer.hikari.HikariDataSource;
import io.muic.ooc.webapp.config.ConfigProperties;
import io.muic.ooc.webapp.config.ConfigurationLoader;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Make this singleton
 */

public class DatabaseConnectionService {

    private static DatabaseConnectionService service;

    private final HikariDataSource ds;



    /**
     * Database connection pool using hikari library
     * The secret and variables are loaded from disk
     * config.properties is not committed to git repository
     */

    private DatabaseConnectionService() {
        ds = new HikariDataSource();
        ds.setMaximumPoolSize(20);
        ConfigProperties configProperties = ConfigurationLoader.load();
        if(configProperties == null) {
            throw new RuntimeException("Unable to read the config.properties.");
        }
        ds.setDriverClassName(configProperties.getDatabaseDriverClassName());
        ds.setJdbcUrl(configProperties.getDatabaseConnectionUrl());
        ds.addDataSourceProperty("user", configProperties.getDatabaseUsername());
        ds.addDataSourceProperty("password", configProperties.getDatabasePassword());
        ds.setAutoCommit(false);
    }

    public Connection getConnection() throws SQLException{
        return ds.getConnection();
    }

    public static DatabaseConnectionService getInstance(){
        if (service == null){
            service = new DatabaseConnectionService();
        }
        return service;
    }


}
