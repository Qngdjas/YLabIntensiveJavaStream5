package ru.qngdjas.habitstracker.api.servlet.user.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.qngdjas.habitstracker.api.servlet.user.BaseUserServlet;
import ru.qngdjas.habitstracker.application.dto.MessageDTO;
import ru.qngdjas.habitstracker.application.dto.user.LoginDTO;
import ru.qngdjas.habitstracker.application.mapper.json.UserJSONMapper;
import ru.qngdjas.habitstracker.domain.model.user.User;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends BaseUserServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginDTO loginDTO = mapper.toLoginDTO(req.getInputStream());
        User user = userService.login(loginDTO);
        MessageDTO messageDTO = new MessageDTO();
        if (user != null) {
            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute("email", user.getEmail());
            messageDTO.setMessage(String.format("Пользователь %s успешно аутентифицирован.", user.getEmail()));
        } else {
            messageDTO.setMessage("Неверные учетные данные.");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        resp.getWriter().write(messageMapper.toJson(messageDTO));
    }
}
