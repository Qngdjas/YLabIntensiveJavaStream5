package ru.qngdjas.habitstracker.api.servlet.habit;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.qngdjas.habitstracker.application.dto.habit.NotedDateDTO;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.utils.logger.ApiLoggable;
import ru.qngdjas.habitstracker.domain.service.core.NotFoundException;
import ru.qngdjas.habitstracker.domain.service.core.RootlessException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@ApiLoggable
@WebServlet("/notes/*")
public class NoteServlet extends BaseHabitServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        if (session != null) {
            String[] urlParts = req.getPathInfo() != null && !req.getPathInfo().substring(1).isBlank() ?
                    req.getPathInfo().substring(1).split("/") :
                    new String[0];
            if (urlParts.length == 1) {
                try {
                    NotedDateDTO notedDate = mapper.toNotedDateDTO(req.getInputStream());
                    LocalDate date = habitService.note((long) session.getAttribute("userId"), Long.parseLong(urlParts[0]), notedDate);
                    resp.getWriter().write(messageMapper.toJson(
                            new SingleMessageDTO(String.format("Привычка отмечена %s", date))
                    ));
                } catch (NumberFormatException exception) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO("Некорректный id привычки")));
                } catch (DateTimeParseException exception) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO("Неверный формат даты")));
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
