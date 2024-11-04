package ru.qngdjas.habitstracker.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.mapper.model.UserMapper;
import ru.qngdjas.habitstracker.domain.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{id}")
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @PutMapping
    public ResponseEntity<SingleMessageDTO> update(@PathVariable long id) {
        return ResponseEntity.ok(
                new SingleMessageDTO(
                        String.format("Обновление пользователя %d", id)
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<SingleMessageDTO> delete(@PathVariable long id) {
        return ResponseEntity.ok(
                new SingleMessageDTO(
                        String.format("Удаление пользователя %d", id)
                )
        );
    }

}
