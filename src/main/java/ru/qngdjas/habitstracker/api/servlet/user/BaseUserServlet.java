package ru.qngdjas.habitstracker.api.servlet.user;

import ru.qngdjas.habitstracker.api.servlet.core.BaseServlet;
import ru.qngdjas.habitstracker.application.mapper.json.UserJSONMapper;
import ru.qngdjas.habitstracker.domain.service.UserService;

public class BaseUserServlet extends BaseServlet {

    protected final UserService userService;
    protected final UserJSONMapper mapper;

    public BaseUserServlet() {
        userService = new UserService();
        mapper = new UserJSONMapper();
    }
}
