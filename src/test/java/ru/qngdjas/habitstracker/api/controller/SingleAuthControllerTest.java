package ru.qngdjas.habitstracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.qngdjas.habitstracker.application.dto.user.UserLoginDTO;
import ru.qngdjas.habitstracker.domain.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SingleAuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(post("/api/v1/login", objectMapper.writeValueAsString(new UserLoginDTO("user@mail.ru", "user"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
//                .andExpect(jsonPath("$.message").value("Пользователь %s успешно аутентифицирован"));
    }

//    @Test
//    void register() {
//    }
}