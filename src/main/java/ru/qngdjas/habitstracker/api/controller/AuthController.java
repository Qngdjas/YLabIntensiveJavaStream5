package ru.qngdjas.habitstracker.api.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserLoginDTO;
import ru.qngdjas.habitstracker.application.mapper.model.UserMapper;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping(value = "/info")
    public ResponseEntity<SingleMessageDTO> info(HttpSession session) {
        String email = session.getAttribute("email") == null ? "anonymous" : (String) session.getAttribute("email");
        SingleMessageDTO message = new SingleMessageDTO("Добро пожаловать на информационную страницу, " + email + "!");
        return ResponseEntity.ok(message);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<SingleMessageDTO> login(HttpSession httpSession, @RequestBody UserLoginDTO userDTO) {
        User user = userService.login(userDTO);
        httpSession.setAttribute("userId", user.getId());
        httpSession.setAttribute("email", user.getEmail());
        return ResponseEntity.ok(new SingleMessageDTO(String.format("Пользователь %s успешно аутентифицирован", user.getEmail())));
    }
}
