package ru.qngdjas.habitstracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.qngdjas.habitstracker.application.dto.user.UserLoginDTO;
import ru.qngdjas.habitstracker.config.WebMvcConfig;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Базовый класс интеграционных тестов.
 */
@SpringJUnitWebConfig(classes = WebMvcConfig.class)
class BaseControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Тестирование корректной конфигурации контроллеров")
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
        assertNotNull(webApplicationContext.getBean(AuthController.class));
        assertNotNull(webApplicationContext.getBean(UserController.class));
        assertNotNull(webApplicationContext.getBean(HabitController.class));
    }

    protected UserLoginDTO getValidUserCredentials() {
        return new UserLoginDTO("ivan@mail.ru", "ivan");
    }

    protected UserLoginDTO getInvalidUserCredentials() {
        return new UserLoginDTO("ivan@mail.ru", "navi");
    }

}