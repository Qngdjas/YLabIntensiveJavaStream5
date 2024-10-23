package ru.qngdjas.habitstracker.domain.service;

import ru.qngdjas.habitstracker.infrastructure.session.Session;

/**
 * Базовый класс сервисов.
 * <p>Представляет общие методы проверки аутентификации и прав доступа.
 */
abstract class Service {


    /**
     * Информационные сообщения, отображающиеся при попытке выполнить действия, требующих определенных прав доступа.
     */
    protected final static String AUTH_ONLY_MESSAGE = "Действие доступно только для зарегистрированных пользователей";
    protected final static String ADMIN_ONLY_MESSAGE = "Действие доступно только для администраторов";

    /**
     * Проверка авторизован ли пользователей.
     *
     * @return {@code true} если пользователей авторизован, иначе выводит информационное сообщение.
     */
    protected boolean isAuth() {
        boolean isAuth = Session.getInstance().isAuthenticated();
        if (!isAuth) {
            System.out.println(AUTH_ONLY_MESSAGE);
        }
        return isAuth;
    }

    /**
     * Проверка является ли пользователь администратор.
     *
     * @return {@code true} если пользователей является администратором, иначе выводит информационное сообщение.
     */
    protected boolean isAdmin() {
        boolean isAdmin = Session.getInstance().isAdmin();
        if (!isAdmin) {
            System.out.println(ADMIN_ONLY_MESSAGE);
        }
        return isAdmin;
    }
}
