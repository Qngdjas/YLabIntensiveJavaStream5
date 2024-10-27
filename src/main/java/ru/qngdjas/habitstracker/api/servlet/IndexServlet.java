package ru.qngdjas.habitstracker.api.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/")
public class IndexServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        if (name == null || name.isEmpty()) {
            name = "world";
        }
        resp.setContentType("application/json");
        resp.getWriter().print("{ \"hello\": \"" + name + "!\" }\n" + req.getPathInfo());
    }
}
