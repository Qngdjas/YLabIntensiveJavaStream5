package ru.qngdjas.habitstracker.api.servlet.user.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.qngdjas.habitstracker.api.servlet.user.BaseUserServlet;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends BaseUserServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
