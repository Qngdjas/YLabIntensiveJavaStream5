package ru.qngdjas.habitstracker.session;

import ru.qngdjas.habitstracker.model.User;

public class Session {

    private static Session instance;
    private User user;

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean isAuthenticated() {
        return user != null;
    }

    public boolean isAdmin() {
        return user != null && user.isAdmin();
    }
}
