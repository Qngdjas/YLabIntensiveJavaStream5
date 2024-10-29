package ru.qngdjas.habitstracker.application.utils.validator;

abstract public class Validator {

    protected static final String REQUIRED = "Поле обязательно";

    protected static boolean isEmpty(String field) {
        return field == null || field.isBlank();
    }
}
