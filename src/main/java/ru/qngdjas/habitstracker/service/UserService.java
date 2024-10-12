package ru.qngdjas.habitstracker.service;

import ru.qngdjas.habitstracker.exception.EmailException;
import ru.qngdjas.habitstracker.model.User;
import ru.qngdjas.habitstracker.repository.UserRepository;
import ru.qngdjas.habitstracker.session.Session;

public class UserService extends Service {

    private final static UserRepository userRepository = new UserRepository();

    public User login(String email, String password) {
        User user = userRepository.getUser(email);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                Session.getInstance().setUser(user);
                System.out.printf("Пользователь %s авторизован\n", user.getEmail());
                return user;
            }
            System.out.println("Пароль введен не верно");
        } else {
            System.out.println("Пользователь не найден");
        }
        return null;
    }

    public User register(String email, String password, String name) {
        if (userRepository.isExists(email)) {
            System.out.println("Пользователь с таким email уже существует");
        } else {
            try {
                User user = new User(email, password, name);
                userRepository.addUser(user);
                Session.getInstance().setUser(user);
                System.out.printf("Пользователь %s зарегистрирован\n", user.getEmail());
                return user;
            } catch (EmailException exception) {
                System.out.println(exception.getMessage());
            }
        }
        return null;
    }

    public User update(String email, String password, String name) {
        if (isAuth()) {
            if (!userRepository.isExists(email)) {
                String old_email = Session.getInstance().getUser().getEmail();
                User user = old_email.equals(email) ? userRepository.getUser(email) : userRepository.removeUser(old_email);
                if (user != null) {
                    try {
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setName(name);
                        userRepository.addUser(user);
                        System.out.printf("Пользователь %s обновлен\n", user.getEmail());
                        return user;
                    } catch (EmailException exception) {
                        System.out.println(exception.getMessage());
                    }
                }
                System.out.println("Пользователь не найден");
            } else {
                System.out.println("Email занят");
            }
        }
        return null;
    }

    public User delete(String email) {
        if (isAuth()) {
            if (Session.getInstance().getUser().getEmail().equals(email) || isAdmin()) {
                User user = userRepository.removeUser(email);
                if (user != null) {
                    if (Session.getInstance().getUser().equals(user)) {
                        Session.getInstance().setUser(null);
                    }
                    System.out.printf("Пользователь %s удален\n", user.getEmail());
                    return user;
                }
                System.out.println("Пользователь не найден");
            }
        }
        return null;
    }
}
