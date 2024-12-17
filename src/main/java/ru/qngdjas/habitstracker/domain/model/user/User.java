package ru.qngdjas.habitstracker.domain.model.user;

import lombok.Getter;
import lombok.Setter;
import ru.qngdjas.habitstracker.domain.model.core.Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class User extends Model {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("[^@\\s]+@[^@\\s]+");

    private String email;
    @Setter
    private String password;
    @Setter
    private String name;
    @Setter
    private boolean isAdmin;

    public User(long id, String email, String password, String name, boolean isAdmin) throws EmailException {
        super(id);
        setEmail(email);
        setPassword(password);
        setName(name);
        setAdmin(isAdmin);
    }

    public void setEmail(String email) throws EmailException {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new EmailException();
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("User %s %s", email, name);
    }

}
