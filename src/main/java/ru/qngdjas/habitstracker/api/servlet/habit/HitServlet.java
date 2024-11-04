package ru.qngdjas.habitstracker.api.servlet.habit;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.qngdjas.habitstracker.application.dto.habit.NotedPeriodDTO;
import ru.qngdjas.habitstracker.application.dto.message.MultipleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.utils.logger.ApiLoggable;
import ru.qngdjas.habitstracker.domain.service.core.NotFoundException;
import ru.qngdjas.habitstracker.domain.service.core.RootlessException;

import java.io.IOException;
import java.time.format.DateTimeParseException;

//@ApiLoggable
abstract public class HitServlet extends BaseHabitServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        if (session != null) {
            String[] urlParts = req.getPathInfo() != null && !req.getPathInfo().substring(1).isBlank() ?
                    req.getPathInfo().substring(1).split("/") :
                    new String[0];
            if (urlParts.length <= 1) {
                try {
                    NotedPeriodDTO notedPeriodDTO = new NotedPeriodDTO(req.getParameter("beginDate"), req.getParameter("endDate"));
                    String result = urlParts.length == 0 ?
                            messageMapper.toJson(new MultipleMessageDTO(habitService.getHits((long) session.getAttribute("userId"), notedPeriodDTO))) :
                            messageMapper.toJson(new SingleMessageDTO(habitService.getHit((long) session.getAttribute("userId"), Long.parseLong(urlParts[0]), notedPeriodDTO)));
                    resp.getWriter().write(result);
                } catch (NumberFormatException exception) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO("Некорректный id привычки")));
                } catch (NotFoundException exception) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO(exception.getMessage())));
                } catch (RootlessException exception) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO(exception.getMessage())));
                } catch (DateTimeParseException exception) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO("Неверный формат даты")));
                } catch (IllegalArgumentException exception) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
