package ru.qngdjas.habitstracker.application.utils.validator;

import ru.qngdjas.habitstracker.application.dto.user.UserCreateDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserLoginDTO;

import java.util.HashMap;
import java.util.Map;

public class UserValidator extends Validator {

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

    public static void validate(UserDTO userDTO) {
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
}