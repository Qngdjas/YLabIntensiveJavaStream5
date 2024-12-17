package ru.qngdjas.habitstracker.api.controller;

import org.junit.jupiter.api.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserDTO;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.infrastructure.persistance.UserRepository;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Класс интеграционных тестов контроллера аутентификации.
 */
class UserControllerTest extends BaseControllerTest {

    /**
     * Дополнительная конфигурация для мокирования репозиториев.
     */
    @Configuration
    static class Config {

        @Bean
        public UserRepository userRepository() {
            UserRepository userRepository = mock(UserRepository.class);
            when(userRepository.retrieve(getUser().getId())).thenReturn(getUser());
            when(userRepository.isExists(getUpdatedUser().getEmail())).thenReturn(false);
            when(userRepository.update(getUpdatedUser())).thenReturn(getUpdatedUser());
            return userRepository;
        }
    }

    @Override
    @Test
    @BeforeEach
    void setUp() {
        super.setUp();
        setSession();
    }

    @Test
    @DisplayName("Тестирование обновления с корректными учетными данными")
    void testSuccessfulLogin() throws Exception {
        UserDTO userDTO = new UserDTO(1, "vova@mail.ru", "vova", "Vova", false);
        MvcResult result = mockMvc.perform(put("/users/1")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsBytes(userDTO))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print())
                .andReturn();

        SingleMessageDTO messageDTO = objectMapper.readValue(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8), SingleMessageDTO.class
        );
        Assertions.assertEquals(
                String.format("Пользователь %s успешно обновлен", userDTO.getEmail()),
                messageDTO.getMessage()
        );
    }

//    @Test
//    @DisplayName("Тестирование аутентификации с неверным паролем")
//    void testUnsuccessfulLogin() throws Exception {
//        MvcResult result = mockMvc.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .content(objectMapper.writeValueAsBytes(getInvalidUserCredentials()))
//                )
//                .andExpect(status().isUnauthorized())
//                .andExpect(content().contentType("application/json"))
//                .andDo(print())
//                .andReturn();
//
//        SingleMessageDTO messageDTO = objectMapper.readValue(
//                result.getResponse().getContentAsString(StandardCharsets.UTF_8), SingleMessageDTO.class
//        );
//        Assertions.assertEquals("Пароль введен не верно", messageDTO.getMessage());
//    }

    protected static User getUpdatedUser() {
        return new User(1, "vova@mail.ru", "vova", "Vova", false);
    }

}