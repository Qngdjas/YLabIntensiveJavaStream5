package ru.qngdjas.habitstracker.api.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/users/*")
public class UserServlet extends BaseUserServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        if (name == null || name.isEmpty()) {
            name = "world";
        }
        resp.setContentType("application/json");
        resp.getWriter().print("{ \"hello\": \"" + name + "!\" }\n" + req.getPathInfo());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
}
