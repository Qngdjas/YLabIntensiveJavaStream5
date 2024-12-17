package ru.qngdjas.habitstracker.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.qngdjas.habitstracker.application.dto.habit.HabitDTO;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.mapper.model.HabitMapper;
import ru.qngdjas.habitstracker.domain.service.HabitService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users/{userId}/habits", produces = MediaType.APPLICATION_JSON_VALUE)
public class HabitController {

    private final HabitMapper habitMapper;
    private final HabitService habitService;

    @GetMapping
    public ResponseEntity<List<HabitDTO>> list(@PathVariable long userId) {
        List<HabitDTO> habits = habitService.getAll(userId)
                .stream()
                .map(habitMapper::toDto)
                .toList();
        return ResponseEntity.ok(habits);
    }

    @PostMapping
    public ResponseEntity<SingleMessageDTO> create(@PathVariable long userId) {
        return ResponseEntity.ok(
                new SingleMessageDTO(
                        String.format("Создание привычки для пользователя %d", userId)
                )
        );
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<HabitDTO> retrieve(@PathVariable long userId, @PathVariable long id) {
        return ResponseEntity.ok(new HabitDTO(id, "Тестовая привычка", "Описание тестовой привычки", true, userId));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<SingleMessageDTO> update(@PathVariable long userId, @PathVariable long id) {
        return ResponseEntity.ok(
                new SingleMessageDTO(
                        String.format("Обновление привычки %d для пользователя %d", id, userId)
                )
        );
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SingleMessageDTO> delete(@PathVariable long userId, @PathVariable long id) {
        return ResponseEntity.ok(
                new SingleMessageDTO(
                        String.format("Удаление привычки %d для пользователя %d", id, userId)
                )
        );
    }
}
