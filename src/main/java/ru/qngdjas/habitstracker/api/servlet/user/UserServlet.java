package ru.qngdjas.habitstracker.api.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.qngdjas.habitstracker.application.dto.message.MultipleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserDTO;
import ru.qngdjas.habitstracker.application.utils.logger.ApiLoggable;
import ru.qngdjas.habitstracker.application.utils.validator.ValidationException;
import ru.qngdjas.habitstracker.domain.model.user.EmailException;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.service.core.AlreadyExistsException;
import ru.qngdjas.habitstracker.domain.service.core.NotFoundException;
import ru.qngdjas.habitstracker.domain.service.core.RootlessException;

import java.io.IOException;

//@ApiLoggable
abstract class UserServlet extends BaseUserServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        if (session != null) {
            String[] urlParts = req.getPathInfo() != null && !req.getPathInfo().substring(1).isBlank() ?
                    req.getPathInfo().substring(1).split("/") :
                    new String[0];
            if (urlParts.length == 1) {
                try {
                    UserDTO userDTO = mapper.toUserDTO(req.getInputStream());
                    userDTO.setId(Long.parseLong(urlParts[0]));
                    User user = userService.update((long) session.getAttribute("userId"), userDTO);
                    resp.getWriter().write(messageMapper.toJson(
                            new SingleMessageDTO(String.format("Пользователь %s успешно обновлен", user.getEmail()))
                    ));
                } catch (NumberFormatException exception) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO("Некорректный id пользователя")));
                } catch (EmailException exception) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO(exception.getMessage())));
                } catch (NotFoundException exception) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO(exception.getMessage())));
                } catch (ValidationException exception) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write(messageMapper.toJson(new MultipleMessageDTO(exception.getErrors())));
                } catch (AlreadyExistsException exception) {
                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO(exception.getMessage())));
                } catch (RootlessException exception) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO(exception.getMessage())));
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO("Неизвестный API URL")));
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println(messageMapper.toJson(new SingleMessageDTO("Действие доступно только аутентифицированным пользователям")));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        if (session != null) {
            String[] urlParts = req.getPathInfo() != null && !req.getPathInfo().substring(1).isBlank() ?
                    req.getPathInfo().substring(1).split("/") :
                    new String[0];
            if (urlParts.length == 1) {
                try {
                    User user = userService.delete((long) session.getAttribute("userId"), Long.parseLong(urlParts[0]));
                    session.invalidate();
                    Cookie sessionCookie = new Cookie("JSESSIONID", null);
                    sessionCookie.setMaxAge(0);
                    resp.addCookie(sessionCookie);
                    resp.getWriter().write(messageMapper.toJson(
                            new SingleMessageDTO(String.format("Пользователь %s успешно удален", user.getEmail()))
                    ));
                } catch (NumberFormatException exception) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO("Некорректный id пользователя")));
                } catch (RootlessException exception) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO(exception.getMessage())));
                } catch (NotFoundException exception) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO(exception.getMessage())));
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO("Неизвестный API URL")));
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println(messageMapper.toJson(new SingleMessageDTO("Действие доступно только аутентифицированным пользователям")));
        }
    }
}
