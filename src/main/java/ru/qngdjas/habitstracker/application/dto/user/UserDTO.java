package ru.qngdjas.habitstracker.application.dto.user;

public class UserDTO {

    private long id;
    private String email;
    private String password;
    private String name;
    private boolean isAdmin;

    public UserDTO(long id, String email, String password, String name, boolean isAdmin) {
        setId(id);
        setEmail(email);
        setPassword(password);
        setName(name);
        setAdmin(isAdmin);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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
}
