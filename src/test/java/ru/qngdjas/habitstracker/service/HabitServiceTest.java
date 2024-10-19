package ru.qngdjas.habitstracker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.infrastructure.service.HabitService;
import ru.qngdjas.habitstracker.infrastructure.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

class HabitServiceTest {

    private final static UserService userService = new UserService();
    private final static HabitService habitService = new HabitService();
    private User existingUser;

    @BeforeAll
    static void init() {
        userService.register("user@domain", "user", "user");
        habitService.add("Привычка 1", "Описание привычки 1", true);
        habitService.add("Привычка 2", "Описание привычки 2", false);
        habitService.add("Привычка для обновления", "Описание привычки для обновления", true);
        habitService.add("Привычка для удаления", "Описание привычки для удаления", false);
        habitService.getAll();
    }

    @BeforeEach
    void setUp() {
        existingUser = userService.login("user@domain", "user");
    }

    @Test
    void testSuccessfulAddHabit() {
        Habit habit = habitService.add("Новая привычка", "Описание новой привычки", true);
        Assertions.assertNotNull(habit);
        Assertions.assertEquals("Новая привычка", habit.getName());
        Assertions.assertEquals("Описание новой привычки", habit.getDescription());
        Assertions.assertEquals("ежедневная", habit.getPeriodicity());
    }

    @Test
    void testAddExistingHabit() {
        Habit habit = habitService.add("Привычка 1", "Описание привычки 1", true);
        Assertions.assertNull(habit);
    }

    @Test
    void testSuccessfulUpdateHabit() {
        habitService.get("Привычка для обновления");
        Habit habit = habitService.update("Привычка для обновления", "Обновленная привычка", "Обновленное описание", false);
        Assertions.assertNotNull(habit);
        Assertions.assertEquals("Обновленная привычка", habit.getName());
        Assertions.assertEquals("Обновленное описание", habit.getDescription());
        Assertions.assertEquals("еженедельная", habit.getPeriodicity());
    }

    @Test
    void testUpdateNotExistingHabit() {
        Habit habit = habitService.update("Несуществующая привычка", "Обновленное наименование", "Обновленное описание", false);
        Assertions.assertNull(habit);
    }

    @Test
    void testSuccessfulDelete() {
        Habit habit = habitService.delete("Привычка для удаления");
        Assertions.assertNotNull(habit);
        Assertions.assertEquals("Привычка для удаления", habit.getName());
        Assertions.assertEquals("Описание привычки для удаления", habit.getDescription());
        Assertions.assertEquals("еженедельная", habit.getPeriodicity());
    }

    @Test
    void testDeleteNotExistingHabit() {
        Habit habit = habitService.delete("Несуществующая привычка");
        Assertions.assertNull(habit);
    }

    @Test
    void testGet() {
        Habit habit = habitService.get("Привычка 1");
        Assertions.assertNotNull(habit);
        Assertions.assertEquals("Привычка 1", habit.getName());
        Assertions.assertEquals("Описание привычки 1", habit.getDescription());
        Assertions.assertEquals("ежедневная", habit.getPeriodicity());
    }


    @Test
    void testGetNotExistingHabit() {
        Habit habit = habitService.get("Несуществующая привычка");
        Assertions.assertNull(habit);
    }

    @Test
    void testGetAll() {
        List<Habit> habits = habitService.getAll();
        Assertions.assertFalse(habits.isEmpty());
        Assertions.assertTrue(habits.size() >= 3);
    }

    @Test
    void testNoteWithEmptyDate() {
        LocalDate date = habitService.note("Привычка 1", "");
        Assertions.assertEquals(LocalDate.now(), date);
    }

    @Test
    void testNoteWithDate() {
        LocalDate date = habitService.note("Привычка 1", "2024-10-10");
        Assertions.assertEquals(LocalDate.parse("2024-10-10"), date);
    }

    @Test
    void testNoteWithWrongDate() {
        LocalDate date = habitService.note("Привычка 1", "20224-10-10");
        Assertions.assertNull(date);
    }

    @Test
    void testGetDailyStreak() {
        habitService.note("Привычка 1", "2024-10-11");
        habitService.note("Привычка 1", "2024-10-10");
        habitService.note("Привычка 1", "2024-10-12");
        Map<Habit, Long> streaks = habitService.getStreak();
        Assertions.assertNotNull(streaks);
        Assertions.assertEquals(3, streaks.get(habitService.get("Привычка 1")));
    }

    @Test
    void testGetWeeklyStreak() {
        habitService.note("Привычка 2", "2024-10-17");
        habitService.note("Привычка 2", "2024-10-14");
        habitService.note("Привычка 2", "2024-10-24");
        Map<Habit, Long> streaks = habitService.getStreak();
        Assertions.assertNotNull(streaks);
        Assertions.assertEquals(3, streaks.get(habitService.get("Привычка 2")));
    }

    @Test
    void testGetDailyHit() {
        habitService.note("Привычка 1", "2024-10-11");
        habitService.note("Привычка 1", "2024-10-10");
        habitService.note("Привычка 1", "2024-10-12");
        double hit = habitService.getHit("Привычка 1", "2024-10-07", "2024-10-13");
        Assertions.assertEquals(42.86, hit, 0.01);
    }

    @Test
    void testGetWeeklyHit() {
        habitService.note("Привычка 2", "2024-10-17");
        habitService.note("Привычка 2", "2024-10-14");
        habitService.note("Привычка 2", "2024-10-24");
        double hit = habitService.getHit("Привычка 2", "2024-10-07", "2024-10-27");
        Assertions.assertEquals(66.67, hit, 0.01);
    }
}