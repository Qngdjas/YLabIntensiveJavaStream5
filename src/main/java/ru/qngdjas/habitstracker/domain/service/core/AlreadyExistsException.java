package ru.qngdjas.habitstracker.domain.service.core;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String message) {
        super(message);
    }
}
