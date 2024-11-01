package ru.qngdjas.habitstracker.domain.service;

import org.junit.jupiter.api.*;
import ru.qngdjas.habitstracker.application.dto.user.UserCreateDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserLoginDTO;
import ru.qngdjas.habitstracker.domain.model.user.EmailException;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.service.core.AlreadyExistsException;
import ru.qngdjas.habitstracker.domain.service.core.IncorrectPasswordException;
import ru.qngdjas.habitstracker.domain.service.core.NotFoundException;

class UserServiceTest {

    private final static UserService userService = new UserService();

    @BeforeAll
    static void init() {
        userService.register(new UserCreateDTO("admin@domain", "admin", "admin", true));
        userService.register(new UserCreateDTO("user@domain", "user", "user", false));
        userService.register(new UserCreateDTO("user_for_update@domain", "user_for_update", "user_for_update", false));
        userService.register(new UserCreateDTO("user_for_wrong_update@domain", "user_for_wrong_update", "user_for_wrong_update", false));
        userService.register(new UserCreateDTO("user_for_delete@domain", "user_for_delete", "user_for_delete", false));
        userService.register(new UserCreateDTO("user_for_delete_by_admin@domain", "user_for_delete_by_admin", "user_for_delete_by_admin", false));
    }

    @Test
    @DisplayName("Тестирование аутентификации с корректными учетными данными")
    void testSuccessfulLogin() {
        User user = userService.login(new UserLoginDTO("admin@domain", "admin"));
        Assertions.assertNotNull(user);
        Assertions.assertEquals("admin", user.getName());
    }

    @Test
    @DisplayName("Тестирование аутентификации с неверным паролем")
    void testWrongPasswordLogin() {
        try {
            userService.login(new UserLoginDTO("user@domain", "wrongPassword"));
        } catch (IncorrectPasswordException e) {
            Assertions.assertEquals("Пароль введен не верно", e.getMessage());
        }
    }

    @Test
    @DisplayName("Тестирование аутентификации с несуществующим пользователем")
    void testNotExistingUserLogin() {
        try {
            userService.login(new UserLoginDTO("notExistingUser@domain", "pass"));
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь notExistingUser@domain не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Тестирование регистрации с корректными учетными данными")
    void testSuccessfulRegister() {
        User user = userService.register(new UserCreateDTO("new_user@domain", "new_user_pass", "new_user_name", false));
        Assertions.assertNotNull(user);
        Assertions.assertNotEquals(0, user.getId());
    }

    @Test
    @DisplayName("Тестирование регистрации с некорректно указанным адресом электронной почты")
    void testWrongEmailRegister() {
        try {
            userService.register(new UserCreateDTO("new_user_without_domain", "new_user_pass", "new_user_name", false));
        } catch (EmailException e) {
            Assertions.assertEquals("Неверный формат почты", e.getMessage());
        }
    }

    @Test
    @DisplayName("Тестирование регистрации с уже имеющимся адресом электронной почты")
    void testExistingUserRegister() {
        try {
            userService.register(new UserCreateDTO("user@domain", "user", "user", false));
        } catch (AlreadyExistsException e) {
            Assertions.assertEquals("Пользователь с таким email уже существует", e.getMessage());
        }
    }

    @Test
    @DisplayName("Тестирование обновления с корректными учетными данными")
    void testSuccessfulUpdate() {
        User user = userService.login(new UserLoginDTO("user_for_update@domain", "user_for_update"));
        Assertions.assertEquals("user_for_update@domain", user.getEmail());
        Assertions.assertEquals("user_for_update", user.getPassword());
        Assertions.assertEquals("user_for_update", user.getName());
        User updatedUser = userService.update(user.getId(), new UserDTO(user.getId(), "update_user@domain", "update_user_pass", "update_user_name", false));
        Assertions.assertEquals(updatedUser.getId(), user.getId());
        Assertions.assertEquals("update_user@domain", updatedUser.getEmail());
        Assertions.assertEquals("update_user_pass", updatedUser.getPassword());
        Assertions.assertEquals("update_user_name", updatedUser.getName());
    }

    @Test
    @DisplayName("Тестирование обновления с некорректно указанным адресом электронной почты")
    void testWrongEmailUpdate() {
        User user = userService.login(new UserLoginDTO("user_for_wrong_update@domain", "user_for_wrong_update"));
        Assertions.assertEquals("user_for_wrong_update@domain", user.getEmail());
        Assertions.assertEquals("user_for_wrong_update", user.getPassword());
        Assertions.assertEquals("user_for_wrong_update", user.getName());
        try {
            userService.update(user.getId(), new UserDTO(user.getId(), "update_user_without_domain", "update_user_pass", "update_user_name", false));
        } catch (EmailException e) {
            Assertions.assertEquals("Неверный формат почты", e.getMessage());
        }
    }

    @Test
    @DisplayName("Тестирование обновления с уже имеющимся адресом электронной почты")
    void testExistingUserUpdate() {
        User user = userService.login(new UserLoginDTO("user_for_wrong_update@domain", "user_for_wrong_update"));
        Assertions.assertEquals("user_for_wrong_update@domain", user.getEmail());
        Assertions.assertEquals("user_for_wrong_update", user.getPassword());
        Assertions.assertEquals("user_for_wrong_update", user.getName());
        try {
            userService.update(user.getId(), new UserDTO(user.getId(), "user@domain", "update_user_pass", "update_user_name", false));
        } catch (AlreadyExistsException e) {
            Assertions.assertEquals("Email занят", e.getMessage());
        }
    }

    @Test
    @DisplayName("Тестирование успешного удаления пользователя")
    void testSuccessfulDelete() {
        User user = userService.login(new UserLoginDTO("user_for_delete@domain", "user_for_delete"));
        user = userService.delete(user.getId(), user.getId());
        Assertions.assertNotNull(user);
    }

    @Test
    @DisplayName("Тестирование успешного удаления пользователя администратором")
    void testSuccessfulDeleteByAdmin() {
        User userForDeleteByAdmin = userService.login(new UserLoginDTO("user_for_delete_by_admin@domain", "user_for_delete_by_admin"));
        User user = userService.login(new UserLoginDTO("admin@domain", "admin"));
        user = userService.delete(user.getId(), userForDeleteByAdmin.getId());
        Assertions.assertNotNull(user);
        Assertions.assertEquals("user_for_delete_by_admin@domain", user.getEmail());
        Assertions.assertEquals("user_for_delete_by_admin", user.getPassword());
        Assertions.assertEquals("user_for_delete_by_admin", user.getName());
        try {
            userService.login(new UserLoginDTO("user_for_delete_by_admin@domain", "user_for_delete_by_admin"));
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь user_for_delete_by_admin@domain не найден", e.getMessage());
        }
    }
}