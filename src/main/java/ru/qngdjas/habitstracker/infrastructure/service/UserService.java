package ru.qngdjas.habitstracker.infrastructure.service;

import ru.qngdjas.habitstracker.domain.model.user.EmailException;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.repository.IUserRepository;
import ru.qngdjas.habitstracker.infrastructure.persistance.UserRepository;
import ru.qngdjas.habitstracker.infrastructure.session.Session;

public class UserService extends Service {

    private final static IUserRepository userRepository = new UserRepository();

    public User login(String email, String password) {
        User user = userRepository.retrieveByEmail(email);
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
                User user = userRepository.create(new User(email, password, name));
                Session.getInstance().setUser(user);
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
                User user = userRepository.retrieveByEmail(Session.getInstance().getUser().getEmail());
                if (user != null) {
                    try {
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setName(name);
                        user = userRepository.update(user);
                        Session.getInstance().setUser(user);
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
                User user = userRepository.retrieveByEmail(email);
                if (user != null) {
                    userRepository.delete(user.getID());
                    if (Session.getInstance().getUser().getID() == user.getID()) {
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
