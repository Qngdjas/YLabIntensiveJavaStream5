package ru.qngdjas.habitstracker.domain.service;

import ru.qngdjas.habitstracker.domain.model.user.EmailException;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.repository.IUserRepository;
import ru.qngdjas.habitstracker.infrastructure.persistance.UserRepository;
import ru.qngdjas.habitstracker.infrastructure.session.Session;

/**
 * Сервис обработки запросов управления пользователями.
 */
public class UserService extends Service {

    /**
     * Репозиторий CRUD-операций над моделями пользователей.
     */
    private final static IUserRepository userRepository = new UserRepository();

    /**
     * Метод аутентификации пользователя по почте и паролю.
     *
     * @param email    Адрес электронной почты пользователя.
     * @param password Пароль пользователя.
     * @return Аутентифицированный пользователь {@link User}, если учетные данные корректны, <p>иначе {@code null}.
     */
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

    /**
     * Метод регистрации нового пользователя.
     *
     * @param email    Адрес электронной почты пользователя.
     * @param password Пароль пользователя.
     * @param name     Имя пользователя.
     * @param isAdmin  Статус администратора.
     * @return Зарегистрированный пользователь {@link User},
     * <p>{@code null} если регистрация не удалась.
     */
    public User register(String email, String password, String name, Boolean isAdmin) {
        if (userRepository.isExists(email)) {
            System.out.println("Пользователь с таким email уже существует");
        } else {
            try {
                User user = userRepository.create(new User(email, password, name, isAdmin));
                Session.getInstance().setUser(user);
                return user;
            } catch (EmailException exception) {
                System.out.println(exception.getMessage());
            }
        }
        return null;
    }

    /**
     * Метод обновления профиля пользователя.
     *
     * @param email    Адрес электронной почты пользователя.
     * @param password Пароль пользователя.
     * @param name     Имя пользователя.
     * @return Обновленный пользователь {@link User},
     * <p>{@code null} если обновление не удалось.
     */
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

    /**
     * Метод удаления профиля пользователя.
     *
     * @param email Адрес электронной почты пользователя.
     * @return Удаленный пользователь {@link User},
     * <p>{@code null} если удаление не удалось.
     */
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
