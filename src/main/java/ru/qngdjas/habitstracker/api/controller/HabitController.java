package ru.qngdjas.habitstracker.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.qngdjas.habitstracker.application.dto.habit.HabitDTO;
import ru.qngdjas.habitstracker.application.mapper.model.HabitMapper;
import ru.qngdjas.habitstracker.domain.service.HabitService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/habits")
public class HabitController {

    private HabitMapper habitMapper;
    private HabitService habitService;

    @GetMapping(value = "/habits", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HabitDTO>> list() {
        List<HabitDTO> habits = habitService.getAll(1)
                .stream()
                .map(habitMapper::toDto)
                .toList();
        return ResponseEntity.ok(habits);
    }
}
