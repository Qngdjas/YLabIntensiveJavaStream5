package ru.qngdjas.habitstracker.service;

import ru.qngdjas.habitstracker.session.Session;

abstract class Service {

    protected final static String AUTH_ONLY_MESSAGE = "Действие доступно только для зарегистрированных пользователей";
    protected final static String ADMIN_ONLY_MESSAGE = "Действие доступно только для администраторов";

    protected boolean isAuth() {
        boolean isAuth = Session.getInstance().isAuthenticated();
        if (!isAuth) {
            System.out.println(AUTH_ONLY_MESSAGE);
        }
        return isAuth;
    }

    protected boolean isAdmin() {
        boolean isAdmin = Session.getInstance().isAdmin();
        if (!isAdmin) {
            System.out.println(ADMIN_ONLY_MESSAGE);
        }
        return isAdmin;
    }
}
