package ru.qngdjas.habitstracker.api.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.qngdjas.habitstracker.api.servlet.core.BaseServlet;
import ru.qngdjas.habitstracker.application.dto.habit.HabitCreateDTO;
import ru.qngdjas.habitstracker.application.dto.habit.HabitDTO;
import ru.qngdjas.habitstracker.application.dto.message.MultipleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserCreateDTO;
import ru.qngdjas.habitstracker.application.mapper.json.HabitJSONMapper;
import ru.qngdjas.habitstracker.application.utils.validator.ValidationException;
import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.model.user.EmailException;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.service.HabitService;
import ru.qngdjas.habitstracker.domain.service.core.AlreadyExistsException;
import ru.qngdjas.habitstracker.domain.service.core.RootlessException;

import java.io.IOException;

@WebServlet("/habits/*")
public class HabitServlet extends BaseServlet {

    private final HabitJSONMapper mapper;
    private final HabitService habitService;

    public HabitServlet() {
        mapper = new HabitJSONMapper();
        habitService = new HabitService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        if (session != null) {
            try {
                HabitCreateDTO habitDTO = mapper.toHabitCreateDTO(req.getInputStream());
                habitDTO.setUserId((long) session.getAttribute("userId"));
                Habit habit = habitService.add(habitDTO);
                resp.getWriter().write(messageMapper.toJson(
                        new SingleMessageDTO(String.format("Привычка %s успешно добавлена.", habit.getName()))
                ));
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
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println(messageMapper.toJson(new SingleMessageDTO("Действие доступно только аутентифицированным пользователям.")));
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        if (name == null || name.isEmpty()) {
            name = "world";
        }
        resp.setContentType("application/json");
        String[] split = req.getPathInfo().substring(1).split("/");
        resp.getWriter().print("{ \"hello\": \"" + name + "!\" }\n" + req.getPathInfo() + "<br>" + req.getQueryString());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        if (session != null) {
            String[] urlParts = req.getPathInfo().substring(1).split("/");
            if (urlParts.length == 1) {
                try {
                    HabitDTO habitDTO = mapper.toHabitDTO(req.getInputStream());
                    habitDTO.setId(Long.parseLong(urlParts[0]));
                    habitDTO.setUserId((long) session.getAttribute("userId"));
                    Habit habit = habitService.update(habitDTO);
                    resp.getWriter().write(messageMapper.toJson(
                            new SingleMessageDTO(String.format("Привычка %s успешно обновлена.", habit.getName()))
                    ));
                } catch (NumberFormatException exception) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO("Некорректный id привычки.")));
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
                resp.getWriter().write(messageMapper.toJson(new SingleMessageDTO("Неизвестный API URL.")));
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println(messageMapper.toJson(new SingleMessageDTO("Действие доступно только аутентифицированным пользователям.")));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
