package ru.qngdjas.habitstracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.qngdjas.habitstracker.api.handler.GlobalExceptionHandler;
import ru.qngdjas.habitstracker.application.dto.user.UserLoginDTO;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.service.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

//Пример модульного теста для контроллера с мокированием
class SingleAuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(GlobalExceptionHandler.class)
//                .addInterceptors(new AuthInterceptor())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Тестирование аутентификации с корректными учетными данными")
    void testSuccessfulLogin() throws Exception {
        when(userService.login(getValidUserCredentials())).thenReturn(getUser());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidUserCredentials()))
                )
//                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
//                .andExpect(content().(""))
                .andDo(print());
//        String id = "1";
//        String content = "new updated content";
//        MockHttpServletRequestBuilder builder =
//                MockMvcRequestBuilders.post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .characterEncoding("UTF-8")
//                        .content(objectMapper.writeValueAsString(new UserLoginDTO("user@mail.ru", "user")));
//        this.mockMvc.perform(builder)
//                .andExpect(MockMvcResultMatchers.status()
//                        .isOk())
//                .andExpect(MockMvcResultMatchers.content()
//                        .string("Article updated with content: " + content))
//                .andDo(MockMvcResultHandlers.print());
//                .andExpect(jsonPath("$.message").value("Пользователь %s успешно аутентифицирован"));
    }

//    @Test
//    void login() throws Exception {
//        mockMvc.perform(post("/login", objectMapper.writeValueAsString(new UserLoginDTO("user@mail.ru", "user"))))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"));
//                .andExpect(jsonPath("$.message").value("Пользователь %s успешно аутентифицирован"));
//    }

//    @Test
//    void register() {
//    }

    private UserLoginDTO getValidUserCredentials() {
        return new UserLoginDTO("ivan@mail.ru", "ivan");
    }

    private User getUser() {
        return new User(1, "ivan@mail.ru", "ivan", "Ivan", false);
    }
}