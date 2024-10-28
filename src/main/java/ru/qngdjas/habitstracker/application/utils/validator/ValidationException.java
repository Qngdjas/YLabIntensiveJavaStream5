package ru.qngdjas.habitstracker.application.utils.validator;

import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        this.errors = Map.copyOf(errors);
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
