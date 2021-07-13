package io.muic.ooc.webapp.security;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        final HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(20);
        ds.setDriverClassName("org.mariadb.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mariadb://localhost:3306/login_webapp");
        ds.addDataSourceProperty("user", "root");
        ds.addDataSourceProperty("password", "mansahej");
        ds.setAutoCommit(false);


        try {
            Connection connection = ds.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
