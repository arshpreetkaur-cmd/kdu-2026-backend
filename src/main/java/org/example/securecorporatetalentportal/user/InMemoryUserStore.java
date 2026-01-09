package org.example.securecorporatetalentportal.user;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//Acts like a database
@Component
public class InMemoryUserStore {

    //key=username, value=user object
    private final Map<String, User> users = new ConcurrentHashMap<>();

    //finding user by username
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    //save a user
    public void save(User user) {
        users.put(user.getUserName(), user);
    }

    //return all users
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    //check if user already exists
    public boolean exists(String username) {
        return users.containsKey(username);
    }
}

