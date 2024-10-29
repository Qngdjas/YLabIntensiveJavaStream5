package ru.qngdjas.habitstracker.api.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.qngdjas.habitstracker.domain.service.HabitService;

import java.io.IOException;

@WebServlet("/habits/*")
public class HabitServlet extends HttpServlet {

    private final ObjectMapper objectMapper;
    private final HabitService habitService;

    public HabitServlet() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.habitService = new HabitService();
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

}
