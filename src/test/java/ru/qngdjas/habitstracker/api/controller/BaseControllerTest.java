package ru.qngdjas.habitstracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.qngdjas.habitstracker.config.ApplicationConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * НЕ ПОЛУЧИЛОСЬ НАСТРОИТЬ ТЕСТЫ - СОЗДАЕТСЯ ПУСТОЙ КОНТЕКСТ:
 * нояб. 04, 2024 3:45:29 AM org.springframework.mock.web.MockServletContext log
 * INFO: Initializing Spring TestDispatcherServlet ''
 * нояб. 04, 2024 3:45:29 AM org.springframework.web.servlet.FrameworkServlet initServletBean
 * INFO: Initializing Servlet ''
 * нояб. 04, 2024 3:45:29 AM org.springframework.web.servlet.FrameworkServlet initServletBean
 * INFO: Completed initialization in 2 ms
 */
@SpringJUnitWebConfig(classes = {ApplicationConfig.class}, loader = AnnotationConfigWebContextLoader.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BaseControllerTest {


    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean(AuthController.class));
    }

}