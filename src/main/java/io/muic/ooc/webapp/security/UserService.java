package io.muic.ooc.webapp.security;


import java.util.HashMap;
import java.util.Map;

public class UserService {

    private Map<String, User> users = new HashMap<>();

    {
        users.put("mansahej", new User("mansahej", "12345"));
        users.put("mansahej20", new User("mansahej20", "12345"));
    }

    public User findByUsername(String username){
        return users.get(username);
    }

    public boolean checkIfUserExists(String username){
        return users.containsKey(username);
    }
}
