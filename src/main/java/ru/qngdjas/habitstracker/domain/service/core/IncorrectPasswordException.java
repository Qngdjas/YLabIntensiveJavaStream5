package ru.qngdjas.habitstracker.domain.service.core;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException() {
        super("Пароль введен не верно.");
    }
}
