package io.muic.ooc.webapp.service;


import io.muic.ooc.webapp.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;


public class UserService {

    private static final String INSERT_USER_SQL = "INSERT INTO tbl_user(username, password, display_name) VALUES (?, ?, ?);";
    private static final String SELECT_USER_SQL = "SELECT * FROM tbl_user WHERE username = ?;";


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
    // find user by username
    public User findByUsername(String username) {
        try {
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECT_USER_SQL);
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("display_Name")
            );
        }  catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        UserService userService = new UserService();
        userService.setDatabaseConnectionService(new DatabaseConnectionService());
        User user = userService.findByUsername("mansahej");
        System.out.println(user.getUsername());

    }


}

