package ru.qngdjas.habitstracker.application.utils.logger.aspectj;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ApiLogger {

    @Pointcut("within(@ru.qngdjas.habitstracker.application.utils.logger.ApiLoggable *) && execution(* * (..))")
    public void apiLogging() {
    }

    @Before("apiLogging()")
    public void doApiLogging(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = null;
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                request = (HttpServletRequest) arg;
                break;
            }
        }
        System.out.printf("Вызов метода %s пользователем %s.\n", joinPoint.getSignature(), getCurrentUser(request));
    }

    private String getCurrentUser(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                return (String) session.getAttribute("email");
            }
        }
        return "Anonymous";
    }
}
