package ru.qngdjas.habitstracker.domain.service.core;

public class RootlessException extends RuntimeException {

    public RootlessException() {
        super("Недостаточно прав для выполнения действия.");
    }
}
