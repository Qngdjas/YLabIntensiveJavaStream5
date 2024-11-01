package ru.qngdjas.habitstracker.domain.service.core;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
