package ru.qngdjas.habitstracker.api.servlet.habit;

import ru.qngdjas.habitstracker.api.servlet.core.BaseServlet;
import ru.qngdjas.habitstracker.application.mapper.json.HabitJSONMapper;
import ru.qngdjas.habitstracker.domain.service.HabitService;

public class BaseHabitServlet extends BaseServlet {

    protected final HabitService habitService;
    protected final HabitJSONMapper mapper;

    public BaseHabitServlet() {
        mapper = new HabitJSONMapper();
        habitService = new HabitService();
    }
}
