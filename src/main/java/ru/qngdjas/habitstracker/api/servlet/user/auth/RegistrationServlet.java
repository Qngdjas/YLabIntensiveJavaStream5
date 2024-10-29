package ru.qngdjas.habitstracker.api.servlet.user.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.qngdjas.habitstracker.api.servlet.user.BaseUserServlet;
import ru.qngdjas.habitstracker.application.dto.message.MultipleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserCreateDTO;
import ru.qngdjas.habitstracker.application.utils.validator.ValidationException;
import ru.qngdjas.habitstracker.domain.model.user.EmailException;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.service.core.AlreadyExistsException;

import java.io.IOException;

@WebServlet("/register")
public class RegistrationServlet extends BaseUserServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            UserCreateDTO userDTO = mapper.toUserCreateDTO(req.getInputStream());
            User user = userService.register(userDTO);
            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute("userId", user.getId());
            resp.getWriter().write(messageMapper.toJson(
                    new SingleMessageDTO(String.format("Пользователь %s успешно зарегистрирован.", user.getEmail()))
            ));
        } catch (EmailException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO(exception.getMessage())));
        } catch (ValidationException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(messageMapper.toJson(new MultipleMessageDTO(exception.getErrors())));
        } catch (AlreadyExistsException exception) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO(exception.getMessage())));
        }
    }
}
