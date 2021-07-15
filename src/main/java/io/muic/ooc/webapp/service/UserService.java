package io.muic.ooc.webapp.service;


import io.muic.ooc.webapp.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Make user service singleton
 */

public class UserService {

    private static final String INSERT_USER_SQL = "INSERT INTO tbl_user(username, password, display_name) VALUES (?, ?, ?);";
    private static final String SELECT_USER_SQL = "SELECT * FROM tbl_user WHERE username = ?;";
    private static final String SELECT_ALL_USER_SQL = "SELECT * FROM tbl_user;";
    private static final String DELETE_USER_SQL = "DELETE FROM tbl_user WHERE username = ?;";


    private static UserService service;

    private DatabaseConnectionService databaseConnectionService;


    private UserService(){
    }

    public static UserService getInstance() {
        if (service == null){
            service = new UserService();
            service.setDatabaseConnectionService(DatabaseConnectionService.getInstance());
        }
        return service;
    }

    public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }

    //create new user
    public void createUser(String username, String password, String displayName) throws UserServiceException {
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL);

                ){

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
        try(
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_USER_SQL);

                ) {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("display_Name")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    // list users

    /**
     * list all users in the database
     *
     * @return list of users, never return null
     */
    public List<User> findALl() {
        List<User> users = new ArrayList<>();
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_ALL_USER_SQL);

                ){

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                users.add(new User(
                                resultSet.getLong("id"),
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getString("display_Name")
                        )
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    /**
     * Delete user by user id
     * @return true if pass
     */
    public boolean deleteUserByUsername(String username){
        try(
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_USER_SQL);

        ) {
            ps.setString(1, username);
            int deleteCount = ps.executeUpdate();
            connection.commit();
            return deleteCount > 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }


    }

    // update user

    /**
     * User can only change their display name when updating profile.
     * @param id
     * @param displayName
     */
    public void updateUserById(long id, String displayName ){
        throw new UnsupportedOperationException("not yet implemented");

    }

    /**
     * Change password method is not in update method
     *
     * @param newPassword
     */
    public void changePassword(String newPassword){
        throw new UnsupportedOperationException("not yet implemented");

    }




    public static void main(String[] args) {
        UserService userService = UserService.getInstance();
        try {
            userService.createUser("admin", "12345","Admin");
        } catch (UserServiceException e) {
            e.printStackTrace();
        }
//        List<User> users = userService.findALl();
//        for (User user : users) {
//            System.out.println(user.getUsername());
//        }
    }

}

