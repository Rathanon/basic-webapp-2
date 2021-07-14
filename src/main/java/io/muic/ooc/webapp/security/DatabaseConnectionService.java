package io.muic.ooc.webapp.security;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnectionService {

    final HikariDataSource ds;

    public DatabaseConnectionService() {
        ds = new HikariDataSource();
        final HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(20);

        // TODO : hard coded, not convenient, insecure
        // change to use configuration file or environment variable
        ds.setDriverClassName("org.mariadb.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mariadb://localhost:3306/login_webapp");
        ds.addDataSourceProperty("user", "mansahej");
        ds.addDataSourceProperty("password", "mansahej20");
        ds.setAutoCommit(false);
    }

    public static void main(String[] args) {
        final HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(20);
        ds.setDriverClassName("org.mariadb.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mariadb://localhost:3306/login_webapp");
        ds.addDataSourceProperty("user", "mansahej");
        ds.addDataSourceProperty("password", "mansahej20");
        ds.setAutoCommit(false);


        try {
            Connection connection = ds.getConnection();
            String sql = "INSERT INTO tbl_user(username, password, display_name) VALUES (?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql);
            // setting username column
            ps.setString(1, "my_username");
            // setting password column
            ps.setString(2, "my_password");
            // setting display name column
            ps.setString(3, "my_display_name");
            ps.executeUpdate();
            //manually commit the change
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
