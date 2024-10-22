package ru.qngdjas.habitstracker.infrastructure.session;

import ru.qngdjas.habitstracker.domain.model.user.User;

/**
 * Класс-одиночка управления сессией пользователей.
 */
public class Session {

    /**
     * Экземпляр сессии.
     */
    private static Session instance;
    /**
     * Активный пользователь.
     */
    private User user;

    /**
     * Получение экземпляра сессии.
     *
     * @return Экземпляр сессии {@link #instance}.
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    /**
     * Метод установки активного пользователя.
     *
     * @param user Пользователь, который будет установлен в качестве текущего.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Метод получения активного пользователя.
     *
     * @return Текущий пользователь сессии {@link #user}.
     */
    public User getUser() {
        return user;
    }

    /**
     * Метод проверки наличия активного пользователя сессии.
     *
     * @return {@code true} если пользователь аутентифицирован.
     */
    public boolean isAuthenticated() {
        return user != null;
    }

    /**
     * Метод проверки прав администратора активного пользователя сессии.
     *
     * @return {@code true} если пользователь является администратором.
     */
    public boolean isAdmin() {
        return isAuthenticated() && user.isAdmin();
    }
}
