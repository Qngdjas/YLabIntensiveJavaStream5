package ru.qngdjas.habitstracker.domain.model.core;

/**
 * Исключение для обработки некорректных идентификаторов.
 */
public class IdException extends RuntimeException {

    /**
     * Стандартный конструктор.
     */
    public IdException() {
        super("Идентификатор не может быть отрицательным");
    }
}
