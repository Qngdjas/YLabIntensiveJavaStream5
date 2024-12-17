package ru.qngdjas.habitstracker.api.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.qngdjas.habitstracker.domain.service.core.RootlessException;

/**
 * Класс-перехватчик Http-сессии.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * Метод предварительной обработки запроса.
     *
     * @param request  Http запрос
     * @param response Http ответ
     * @param handler  Обработчик
     * @return {@code true}, если пользователь аутентифицирован.
     * @throws Exception         Ошибка выполнения.
     * @throws RootlessException Ошибка прав доступа.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception, RootlessException {
        if (isAuthenticated(request.getSession(false))) {
            return true;
        }
        throw new RootlessException();
    }

    /**
     * Метод проверки сессии пользователя.
     *
     * @param session http-сессия.
     * @return Возвращает true, если пользователь аутентифицирован.
     */
    private boolean isAuthenticated(HttpSession session) {
        return session != null
                && session.getAttribute("user") != null;
    }
}
