package ru.qngdjas.habitstracker.domain.service;

import org.junit.jupiter.api.*;
import ru.qngdjas.habitstracker.application.dto.habit.HabitCreateDTO;
import ru.qngdjas.habitstracker.application.dto.habit.HabitDTO;
import ru.qngdjas.habitstracker.application.dto.habit.NotedDateDTO;
import ru.qngdjas.habitstracker.application.dto.habit.NotedPeriodDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserCreateDTO;
import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.service.core.AlreadyExistsException;
import ru.qngdjas.habitstracker.domain.service.core.NotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

class HabitServiceTest {

    private final static UserService userService = new UserService();
    private final static HabitService habitService = new HabitService();
    private final static User existingUser = userService.register(new UserCreateDTO("habits_manager@domain", "habits_manager", "habits_manager", false));

    @BeforeAll
    static void init() {
        habitService.add(new HabitCreateDTO("Привычка 1", "Описание привычки 1", true, existingUser.getId()));
        habitService.add(new HabitCreateDTO("Привычка 2", "Описание привычки 2", false, existingUser.getId()));
        habitService.add(new HabitCreateDTO("Привычка для обновления", "Описание привычки для обновления", true, existingUser.getId()));
        habitService.add(new HabitCreateDTO("Привычка для удаления", "Описание привычки для удаления", false, existingUser.getId()));
    }

    @Test
    @DisplayName("Тестирование добавления новой привычки с корректными исходными данными")
    void testSuccessfulAddHabit() {
        Habit habit = habitService.add(new HabitCreateDTO("Новая привычка", "Описание новой привычки", true, existingUser.getId()));
        Assertions.assertNotNull(habit);
        Assertions.assertEquals("Новая привычка", habit.getName());
        Assertions.assertEquals("Описание новой привычки", habit.getDescription());
        Assertions.assertEquals("ежедневная", habit.getPeriodicity());
    }

    @Test
    @DisplayName("Тестирование добавления новой привычки с существующим наименованием")
    void testAddExistingHabit() {
        try {
            habitService.add(new HabitCreateDTO("Привычка 1", "Описание привычки 1", true, existingUser.getId()));
        } catch (AlreadyExistsException e) {
            Assertions.assertEquals("Привычка Привычка 1 у пользователя уже существует", e.getMessage());
        }
    }

    @Test
    @DisplayName("Тестирование обновления новой привычки с корректными исходными данными")
    void testSuccessfulUpdateHabit() {
        Habit habit = habitService.get(existingUser.getId(), 3);
        habit = habitService.update(new HabitDTO(habit.getId(), "Обновленная привычка", "Обновленное описание", false, existingUser.getId()));
        Assertions.assertNotNull(habit);
        Assertions.assertEquals("Обновленная привычка", habit.getName());
        Assertions.assertEquals("Обновленное описание", habit.getDescription());
        Assertions.assertEquals("еженедельная", habit.getPeriodicity());
    }

    @Test
    @DisplayName("Тестирование обновления несуществующей привычки")
    void testUpdateNotExistingHabit() {
        try {
            habitService.update(new HabitDTO(999, "Обновленное наименование", "Обновленное описание", false, existingUser.getId()));
        } catch (NotFoundException e) {
            Assertions.assertEquals("Привычки с id=999 не существует", e.getMessage());
        }
    }

    @Test
    @DisplayName("Тестирование удаления привычки")
    void testSuccessfulDelete() {
        Habit habit = habitService.delete(existingUser.getId(), 4);
        Assertions.assertNotNull(habit);
        Assertions.assertEquals("Привычка для удаления", habit.getName());
        Assertions.assertEquals("Описание привычки для удаления", habit.getDescription());
        Assertions.assertEquals("еженедельная", habit.getPeriodicity());
    }

    @Test
    @DisplayName("Тестирование удаления несуществующей привычки")
    void testDeleteNotExistingHabit() {
        try {
            habitService.delete(existingUser.getId(), 999);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Привычки с id=999 не существует", e.getMessage());
        }
    }

    @Test
    @DisplayName("Тестирование получения привычки")
    void testGet() {
        Habit habit = habitService.get(existingUser.getId(), 1);
        Assertions.assertNotNull(habit);
        Assertions.assertEquals("Привычка 1", habit.getName());
        Assertions.assertEquals("Описание привычки 1", habit.getDescription());
        Assertions.assertEquals("ежедневная", habit.getPeriodicity());
    }


    @Test
    @DisplayName("Тестирование получения несуществующей привычки")
    void testGetNotExistingHabit() {
        try {
            habitService.get(existingUser.getId(), 999);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Привычки с id=999 не существует", e.getMessage());
        }
    }

    @Test
    @DisplayName("Тестирование получения всех привычек пользователя")
    void testGetAll() {
        List<Habit> habits = habitService.getAll(existingUser.getId());
        Assertions.assertFalse(habits.isEmpty());
        Assertions.assertTrue(habits.size() >= 3);
    }

    @Test
    @DisplayName("Тестирование отметки выполнения привычки без указания даты")
    void testNoteWithEmptyDate() {
        LocalDate date = habitService.note(existingUser.getId(), 1, new NotedDateDTO(""));
        Assertions.assertEquals(LocalDate.now(), date);
    }

    @Test
    @DisplayName("Тестирование отметки выполнения привычки с указанием даты")
    void testNoteWithDate() {
        LocalDate date = habitService.note(existingUser.getId(), 1, new NotedDateDTO("2023-10-10"));
        Assertions.assertEquals(LocalDate.parse("2023-10-10"), date);
    }

    @Test
    @DisplayName("Тестирование отметки выполнения привычки с указанием некорректной даты")
    void testNoteWithWrongDate() {
        try {
            habitService.note(existingUser.getId(), 1, new NotedDateDTO("20224-10-10"));
        } catch (DateTimeParseException e) {
            Assertions.assertEquals(0, e.getErrorIndex());
        }
    }

    @Test
    @DisplayName("Тестирование получения актуальной серии выполнения ежедневной привычки")
    void testGetDailyStreak() {
        LocalDate now = LocalDate.now().plusYears(1);
        habitService.note(existingUser.getId(), 1, new NotedDateDTO(now.plusDays(1).toString()));
        habitService.note(existingUser.getId(), 1, new NotedDateDTO(now.plusDays(2).toString()));
        habitService.note(existingUser.getId(), 1, new NotedDateDTO(now.plusDays(3).toString()));
        Map<String, String> streaks = habitService.getStreak(existingUser.getId());
        Assertions.assertNotNull(streaks);
        Habit habit = habitService.get(existingUser.getId(), 1);
        Assertions.assertEquals("3", streaks.get(habit.getName()));
    }

    @Test
    @DisplayName("Тестирование получения актуальной серии выполнения еженедельной привычки")
    void testGetWeeklyStreak() {
        LocalDate now = LocalDate.now().plusYears(1);
        habitService.note(existingUser.getId(), 2, new NotedDateDTO(now.plusWeeks(1).toString()));
        habitService.note(existingUser.getId(), 2, new NotedDateDTO(now.plusWeeks(2).toString()));
        habitService.note(existingUser.getId(), 2, new NotedDateDTO(now.plusWeeks(3).toString()));
        Map<String, String> streaks = habitService.getStreak(existingUser.getId());
        Assertions.assertNotNull(streaks);
        Habit habit = habitService.get(existingUser.getId(), 2);
        Assertions.assertEquals("3", streaks.get(habit.getName()));
    }

    @Test
    @DisplayName("Тестирование получения процента выполнения ежедневной привычки")
    void testGetDailyHit() {
        habitService.note(existingUser.getId(), 1, new NotedDateDTO("2024-10-11"));
        habitService.note(existingUser.getId(), 1, new NotedDateDTO("2024-10-10"));
        habitService.note(existingUser.getId(), 1, new NotedDateDTO("2024-10-12"));
        String hit = habitService.getHit(existingUser.getId(), 1, new NotedPeriodDTO("2024-10-07", "2024-10-13"));
        Assertions.assertEquals(42.86, Double.parseDouble(hit), 0.01);
    }

    @Test
    @DisplayName("Тестирование получения процента выполнения еженедельной привычки")
    void testGetWeeklyHit() {
        habitService.note(existingUser.getId(), 2, new NotedDateDTO("2024-10-17"));
        habitService.note(existingUser.getId(), 2, new NotedDateDTO("2024-10-14"));
        habitService.note(existingUser.getId(), 2, new NotedDateDTO("2024-10-24"));
        String hit = habitService.getHit(existingUser.getId(), 2, new NotedPeriodDTO("2024-10-07", "2024-10-27"));
        Assertions.assertEquals(50.00, Double.parseDouble(hit), 0.01);
    }
}