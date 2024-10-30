package ru.qngdjas.habitstracker.domain.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.service.UserService;
import ru.qngdjas.habitstracker.infrastructure.session.Session;

class UserServiceTest {

    private final static UserService userService = new UserService();
    private static User existingUser;

    @BeforeAll
    static void init() {
//        User admin = userService.register("admin@domain", "admin", "admin", true);
//        userService.register("user@domain", "user", "user", false);
//        userService.register("user_for_update@domain", "user_for_update", "user_for_update", false);
//        userService.register("user_for_delete@domain", "user_for_delete", "user_for_delete", false);
//        userService.register("user_for_delete_by_admin@domain", "user_for_delete_by_admin", "user_for_delete_by_admin", false);
    }

    @BeforeEach
//    void setUp() {
//        existingUser = userService.login("user@domain", "user");
//    }

    @Test
    void testSuccessfulLogin() {
//        User user = userService.login("admin@domain", "admin");
//        Assertions.assertEquals(Session.getInstance().getUser(), user);
    }

    @Test
    void testWrongPasswordLogin() {
//        User user = userService.login("user@domain", "wrongPassword");
//        Assertions.assertNull(user);
    }

    @Test
    void testNotExistingUserLogin() {
//        User user = userService.login("notExistingUser@domain", "pass");
//        Assertions.assertNull(user);
    }

    @Test
    void testSuccessfulRegister() {
//        User user = userService.register("new_user@domain", "new_user_pass", "new_user_name", false);
//        Assertions.assertEquals(Session.getInstance().getUser(), user);
    }

    @Test
    void testWrongEmailRegister() {
//        User user = userService.register("new_user_without_domain", "new_user_pass", "new_user_name", false);
//        Assertions.assertNull(user);
    }

    @Test
    void testExistingUserRegister() {
//        User user = userService.register("user@domain", "user", "user", false);
//        Assertions.assertNull(user);
    }

    @Test
    void testSuccessfulUpdate() {
//        existingUser = userService.login("user_for_update@domain", "user_for_update");
        Assertions.assertEquals("user_for_update@domain", existingUser.getEmail());
        Assertions.assertEquals("user_for_update", existingUser.getPassword());
        Assertions.assertEquals("user_for_update", existingUser.getName());
//        existingUser = userService.update("update_user@domain", "update_user_pass", "update_user_name");
        Assertions.assertEquals(Session.getInstance().getUser(), existingUser);
        Assertions.assertEquals("update_user@domain", existingUser.getEmail());
        Assertions.assertEquals("update_user_pass", existingUser.getPassword());
        Assertions.assertEquals("update_user_name", existingUser.getName());
    }

    @Test
    void testWrongEmailUpdate() {
//        User user = userService.register("new_user_without_domain", "new_user_pass", "new_user_name", false);
//        Assertions.assertNull(user);
    }

    @Test
    void testExistingUserUpdate() {
//        User user = userService.register("user@domain", "user", "user", false);
//        Assertions.assertNull(user);
    }

    @Test
    void testSuccessfulDelete() {
//        existingUser = userService.login("user_for_delete@domain", "user_for_delete");
        Assertions.assertEquals(Session.getInstance().getUser(), existingUser);
//        existingUser = userService.delete("user_for_delete@domain");
        Assertions.assertNull(Session.getInstance().getUser());
    }

    @Test
    void testSuccessfulDeleteByAdmin() {
//        existingUser = userService.login("admin@domain", "admin");
        Assertions.assertEquals(Session.getInstance().getUser(), existingUser);
//        User user = userService.delete("user_for_delete_by_admin@domain");
//        Assertions.assertNotNull(user);
//        Assertions.assertEquals("user_for_delete_by_admin@domain", user.getEmail());
//        Assertions.assertEquals("user_for_delete_by_admin", user.getPassword());
//        Assertions.assertEquals("user_for_delete_by_admin", user.getName());
//        existingUser = userService.login("user_for_delete_by_admin@domain", "user_for_delete_by_admin");
        Assertions.assertNull(existingUser);
    }
}