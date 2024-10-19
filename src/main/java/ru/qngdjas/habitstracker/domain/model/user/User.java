package ru.qngdjas.habitstracker.domain.model.user;

import ru.qngdjas.habitstracker.domain.model.core.Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User extends Model {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("[^@\\s]+@[^@\\s]+");

    private String email;
    private String password;
    private String name;
    private boolean isAdmin;


    public User(String email, String password, String name) throws EmailException {
        this(email, password, name, false);
    }

    public User(String email, String password, String name, boolean isAdmin) throws EmailException {
        this(-1, email, password, name, isAdmin);
    }

    public User(long id, String email, String password, String name, boolean isAdmin) throws EmailException {
        super(id);
        setEmail(email);
        this.password = password;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws EmailException {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new EmailException("Неверный формат почты");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return String.format("User %s %s", email, name);
    }

}