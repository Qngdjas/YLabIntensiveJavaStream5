package ru.qngdjas.habitstracker.api.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String email = (String) request.getSession().getAttribute("email");
        if (email == null) {
            response.sendRedirect("api/v1/login");
            return false;
        }
        return true;
    }
}
