package ru.qngdjas.habitstracker.api.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserLoginDTO;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.infrastructure.persistance.UserRepository;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Класс интеграционных тестов контроллера аутентификации.
 */
class AuthControllerTest extends BaseControllerTest {

    /**
     * Дополнительная конфигурация для мокирования репозиториев.
     */
    @Configuration
    static class Config {

        @Bean
        public UserRepository userRepository() {
            UserRepository userRepository = mock(UserRepository.class);
            when(userRepository.retrieveByEmail("ivan@mail.ru")).thenReturn(getUser());
            return userRepository;
        }

        private User getUser() {
            return new User(1, "ivan@mail.ru", "ivan", "Ivan", false);
        }
    }

    @Test
    @DisplayName("Тестирование аутентификации с корректными учетными данными")
    void testSuccessfulLogin() throws Exception {
        UserLoginDTO userDTO = getValidUserCredentials();
        MvcResult result = mockMvc.perform(post("/login")
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
                String.format("Пользователь %s успешно аутентифицирован", userDTO.getEmail()),
                messageDTO.getMessage()
        );
    }

    @Test
    @DisplayName("Тестирование аутентификации с неверным паролем")
    void testUnsuccessfulLogin() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsBytes(getInvalidUserCredentials()))
                )
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json"))
                .andDo(print())
                .andReturn();

        SingleMessageDTO messageDTO = objectMapper.readValue(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8), SingleMessageDTO.class
        );
        Assertions.assertEquals("Пароль введен не верно", messageDTO.getMessage());
    }

}