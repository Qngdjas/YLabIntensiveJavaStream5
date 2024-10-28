package ru.qngdjas.habitstracker.domain.model.user;

public class EmailException extends RuntimeException {

    public EmailException() {
        super("Неверный формат почты");
    }
}
