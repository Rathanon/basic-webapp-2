package io.muic.ooc.webapp.service;


import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


public class UserService {

    private static final String INSERT_USER_SQL = "INSERT INTO tbl_user(username, password, display_name) VALUES (?, ?, ?);";


    private DatabaseConnectionService databaseConnectionService;

    public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }

    //create new user
    public void createUser(String username, String password, String displayName) throws UserServiceException {
        try {
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL);
            ps.setString(1, username);
            //password -> hashed and salted, bcrypt library
            ps.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            ps.setString(3, displayName);
            ps.executeUpdate();
            //manually commit the change
            connection.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UsernameNotUniqueException(String.format("Username %s. has already been taken.", username));

        } catch (SQLException throwables) {
            throw new UserServiceException(throwables.getMessage());
        }
    }

}

