package ru.qngdjas.habitstracker.api.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserCreateDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserLoginDTO;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.service.UserService;

/**
 * Контроллер операций аутентификации с поддержкой JSON.
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Маршрут аутентификации.
     *
     * @param httpSession Сессия пользователя.
     * @param userDTO     Данные авторизации пользователя.
     * @return Сообщение о статусе выполнения аутентификации с поясняющим текстом.
     */
    @PostMapping(value = "/login")
    public ResponseEntity<SingleMessageDTO> login(HttpSession httpSession, @RequestBody UserLoginDTO userDTO) {
        User user = userService.login(userDTO);
        httpSession.setAttribute("user", user);
        return ResponseEntity.ok(
                new SingleMessageDTO(String.format("Пользователь %s успешно аутентифицирован", user.getEmail()))
        );
    }

    /**
     * Маршрут регистрации.
     *
     * @param httpSession Сессия пользователя.
     * @param userDTO     Данные регистрации пользователя.
     * @return Сообщение о статусе выполнения регистрации с поясняющим текстом.
     */
    @PostMapping(value = "/register")
    public ResponseEntity<SingleMessageDTO> register(HttpSession httpSession, @RequestBody UserCreateDTO userDTO) {
        User user = userService.register(userDTO);
        httpSession.setAttribute("user", user);
        return ResponseEntity.ok(new SingleMessageDTO(String.format("Пользователь %s успешно зарегистрирован", user.getEmail())));
    }

    /**
     * Маршрут выхода из системы.
     *
     * @param httpSession Сессия пользователя.
     * @return Сообщение о статусе выполнения операции выхода из системы с поясняющим текстом.
     */
    @GetMapping(value = "/logout")
    public ResponseEntity<SingleMessageDTO> logout(HttpSession httpSession) {
        httpSession.invalidate();
        return ResponseEntity.ok(
                new SingleMessageDTO("Выполнен выход из системы")
        );
    }
}
