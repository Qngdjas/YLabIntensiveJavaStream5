package ru.qngdjas.habitstracker.api.servlet.user.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.qngdjas.habitstracker.api.servlet.user.BaseUserServlet;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserLoginDTO;
import ru.qngdjas.habitstracker.application.utils.logger.ApiLoggable;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.service.core.IncorrectPasswordException;
import ru.qngdjas.habitstracker.domain.service.core.NotFoundException;

import java.io.IOException;

//@ApiLoggable
abstract public class LoginServlet extends BaseUserServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            UserLoginDTO userDTO = mapper.toLoginDTO(req.getInputStream());
            User user = userService.login(userDTO);
            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute("userId", user.getId());
            httpSession.setAttribute("email", user.getEmail());
            resp.getWriter()
                    .write(messageMapper.toJson(
                            new SingleMessageDTO(
                                    String.format("Пользователь %s успешно аутентифицирован", user.getEmail())
                            )
                    ));
        } catch (IncorrectPasswordException exception) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO(exception.getMessage())));
        } catch (NotFoundException exception) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO(exception.getMessage())));
        }
    }
}
