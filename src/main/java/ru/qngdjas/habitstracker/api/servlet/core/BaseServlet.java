package ru.qngdjas.habitstracker.api.servlet.core;

import jakarta.servlet.http.HttpServlet;
import ru.qngdjas.habitstracker.application.mapper.json.MessageJSONMapper;

abstract public class BaseServlet extends HttpServlet {

    protected final MessageJSONMapper messageMapper;

    public BaseServlet() {
        messageMapper = new MessageJSONMapper();
    }
}
