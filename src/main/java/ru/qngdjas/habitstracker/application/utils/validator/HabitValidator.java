package ru.qngdjas.habitstracker.application.utils.validator;

import ru.qngdjas.habitstracker.application.dto.habit.HabitCreateDTO;
import ru.qngdjas.habitstracker.application.dto.habit.HabitDTO;

import java.util.HashMap;
import java.util.Map;

public class HabitValidator extends Validator {

    public static void validate(HabitCreateDTO habitDTO) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        if (isEmpty(habitDTO.getName())) {
            errors.put("name", REQUIRED);
        }

        if (habitDTO.getUserId() <= 0) {
            errors.put("userId", REQUIRED);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public static void validate(HabitDTO habitDTO) {
        Map<String, String> errors = new HashMap<>();

        if (habitDTO.getId() <= 0) {
            errors.put("id", REQUIRED);
        }

        if (isEmpty(habitDTO.getName())) {
            errors.put("name", REQUIRED);
        }

        if (habitDTO.getUserId() <= 0) {
            errors.put("userId", REQUIRED);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
