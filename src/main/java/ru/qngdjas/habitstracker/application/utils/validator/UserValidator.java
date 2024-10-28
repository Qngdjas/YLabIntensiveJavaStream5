package ru.qngdjas.habitstracker.application.utils.validator;

import ru.qngdjas.habitstracker.application.dto.user.UserCreateDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserLoginDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserUpdateDTO;

import java.util.HashMap;
import java.util.Map;

public class UserValidator {

    private static final String REQUIRED = "Поле обязательно";

    public static void validate(UserLoginDTO userDTO) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        if (isEmpty(userDTO.getEmail())) {
            errors.put("email", REQUIRED);
        }

        if (isEmpty(userDTO.getPassword())) {
            errors.put("password", REQUIRED);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public static void validate(UserCreateDTO userDTO) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        if (isEmpty(userDTO.getEmail())) {
            errors.put("email", REQUIRED);
        }

        if (isEmpty(userDTO.getPassword())) {
            errors.put("password", REQUIRED);
        }

        if (isEmpty(userDTO.getName())) {
            errors.put("name", REQUIRED);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public static void validate(UserUpdateDTO userDTO) {
        Map<String, String> errors = new HashMap<>();

        if (userDTO.getId() <= 0) {
            errors.put("id", REQUIRED);
        }

        if (isEmpty(userDTO.getEmail())) {
            errors.put("email", REQUIRED);
        }

        if (isEmpty(userDTO.getPassword())) {
            errors.put("password", REQUIRED);
        }

        if (isEmpty(userDTO.getName())) {
            errors.put("name", REQUIRED);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private static boolean isEmpty(String field) {
        return field == null || field.isBlank();
    }
}