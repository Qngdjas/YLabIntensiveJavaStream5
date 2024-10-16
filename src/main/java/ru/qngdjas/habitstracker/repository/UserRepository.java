package ru.qngdjas.habitstracker.repository;

import ru.qngdjas.habitstracker.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private static final Map<String, User> users = new HashMap<>();

    public User getUser(String email) {
        return users.get(email);
    }

    public void addUser(User user) {
        users.put(user.getEmail(), user);
    }

    public boolean isExists(String email) {
        return users.containsKey(email);
    }

    public User removeUser(String email)  {
        return users.remove(email);
    }
}
