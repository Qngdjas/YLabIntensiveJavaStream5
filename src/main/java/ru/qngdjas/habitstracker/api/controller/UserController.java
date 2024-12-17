package ru.qngdjas.habitstracker.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserDTO;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.service.UserService;

/**
 * Контроллер операций действий пользователя.
 */
@RestController
@RequestMapping("/users/{id}")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Маршрут редактирования данных пользователя.
     *
     * @param id      Идентификатор пользователя.
     * @param userDTO Данные пользователя.
     * @return Сообщение о статусе выполнения обновления с поясняющим текстом.
     */
    @PutMapping
    public ResponseEntity<SingleMessageDTO> update(HttpSession httpSession, @PathVariable long id, @RequestBody UserDTO userDTO) {
        System.out.println(userDTO.getId());
        userDTO.setId(id);
        User user = userService.update((long) httpSession.getAttribute("id"), userDTO);
        return ResponseEntity.ok(
                new SingleMessageDTO(
                        String.format("Пользователь %s успешно обновлен", user.getEmail())
                )
        );
    }

    /**
     * Маршрут удаления пользователя.
     *
     * @param id Идентификатор пользователя.
     * @return Сообщение о статусе выполнения удаления с поясняющим текстом.
     */
    @DeleteMapping
    public ResponseEntity<SingleMessageDTO> delete(HttpSession httpSession, @PathVariable long id) {
        User user = userService.delete((long) httpSession.getAttribute("id"), id);
        return ResponseEntity.ok(
                new SingleMessageDTO(
                        String.format("Удаление пользователя %d", id)
                )
        );
    }

}
