package ru.qngdjas.habitstracker.api.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.qngdjas.habitstracker.api.servlet.core.BaseServlet;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.utils.logger.ApiLoggable;

import java.io.IOException;

//@ApiLoggable
abstract public class IndexServlet extends BaseServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.getWriter().print(messageMapper.toJson(new SingleMessageDTO("Информационная страница!")));
    }
}
