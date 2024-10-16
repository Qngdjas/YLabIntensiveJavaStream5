package ru.qngdjas.habitstracker.service;

import ru.qngdjas.habitstracker.model.Habit;
import ru.qngdjas.habitstracker.model.User;
import ru.qngdjas.habitstracker.repository.HabitRepository;
import ru.qngdjas.habitstracker.repository.StatisticRepository;
import ru.qngdjas.habitstracker.session.Session;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class HabitService extends Service {

    private static final HabitRepository habitRepository = new HabitRepository();
    private static final StatisticRepository statisticRepository = new StatisticRepository();

    public Habit add(String habitName, String description, boolean isDaily) {
        if (isAuth()) {
            User user = Session.getInstance().getUser();
            if (!habitRepository.isExists(user, habitName)) {
                Habit habit = new Habit(habitName, description, isDaily);
                habitRepository.addHabit(user, habit);
                System.out.println("Привычка добавлена");
                return habit;
            }
            System.out.printf("Привычка %s у пользователя %s уже существует", habitName, user.getEmail());
        }
        return null;
    }

    public Habit update(String oldHabitName, String habitName, String description, boolean isDaily) {
        if (isAuth()) {
            User user = Session.getInstance().getUser();
            Habit habit = oldHabitName.equals(habitName) ? habitRepository.getHabit(user, habitName) : habitRepository.deleteHabit(user, oldHabitName);
            if (habit != null) {
                habit.setName(habitName);
                habit.setDescription(description);
                habit.setDaily(isDaily);
                habitRepository.addHabit(user, habit);
                System.out.printf("Привычка %s обновлена\n", habit.getName());
                return habit;
            }
            System.out.printf("Привычка %s у пользователя %s не найдена", oldHabitName, user.getEmail());
        }
        return null;
    }

    public Habit delete(String habitName) {
        if (isAuth()) {
            User user = Session.getInstance().getUser();
            Habit habit = habitRepository.deleteHabit(user, habitName);
            if (habit != null) {
                System.out.printf("Привычка %s у пользователя %s удалена\n", habitName, user.getEmail());
                return habit;
            }
            System.out.printf("Привычка %s у пользователя %s не найдена", habitName, user.getEmail());
        }
        return null;
    }

    public Habit get(String habitName) {
        if (isAuth()) {
            User user = Session.getInstance().getUser();
            Habit habit = habitRepository.getHabit(user, habitName);
            if (habit != null) {
                System.out.println(habit);
                return habit;
            }
            System.out.printf("Привычка %s у пользователя %s не найдена", habitName, user.getEmail());
        }
        return null;
    }

    public List<Habit> getAll() {
        if (isAuth()) {
            User user = Session.getInstance().getUser();
            try {
                List<Habit> habits = habitRepository.getHabits(user);
                System.out.printf("Привычки пользователя:\n%s\n", habits.toString());
                return habits;
            } catch (NullPointerException exception) {
                System.out.printf("Привычки у пользователя %s не найдены", user.getEmail());
            }
        }
        return null;
    }

    public LocalDate note(String habitName, String date) {
        if (isAuth()) {
            try {
                Habit habit = get(habitName);
                if (habit != null) {
                    LocalDate noteDate = date.isBlank() ? LocalDate.now() : LocalDate.parse(date);
                    statisticRepository.note(habit, noteDate);
                    System.out.println("Привычка отмечена");
                    return noteDate;
                }
            } catch (DateTimeParseException exception) {
                System.out.println("Неверный формат даты");
            }
        }
        return null;
    }

    public Map<Habit, Long> getStreak() {
        Map<Habit, Long> streaks = new HashMap<>();
        if (isAuth()) {
            List<Habit> habits = getAll();
            for (Habit habit : habits) {
                streaks.put(habit, statisticRepository.getStreak(habit));
            }
        }
        return streaks;
    }

    public double getHit(String habitName, String beginDate, String endDate) {
        if (isAuth()) {
            try {
                Habit habit = get(habitName);
                if (habit != null) {
                    LocalDate noteBeginDate = beginDate.isBlank() ? LocalDate.now() : LocalDate.parse(beginDate);
                    LocalDate noteEndDate = beginDate.isBlank() ? LocalDate.now() : LocalDate.parse(endDate);
                    System.out.printf("Процент успеха по привычке %s:\n", habit.getName());
                    return statisticRepository.getHit(habit, noteBeginDate, noteEndDate);
                }
            } catch (DateTimeParseException exception) {
                System.out.println("Неверный формат даты");
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
        return 0.0f;
    }

    public Map<Habit, Double> getHits(String beginDate, String endDate) {
        Map<Habit, Double> result = new HashMap<>();
        if (isAuth()) {
            try {
                List<Habit> habits = getAll();
                LocalDate noteBeginDate = beginDate.isBlank() ? LocalDate.now() : LocalDate.parse(beginDate);
                LocalDate noteEndDate = beginDate.isBlank() ? LocalDate.now() : LocalDate.parse(endDate);
                for (Habit habit : habits) {
                    result.put(habit, statisticRepository.getHit(habit, noteBeginDate, noteEndDate));
                }
                System.out.printf("Отчёт по привычкам:\n%s\n", result);
                return result;
            } catch (DateTimeParseException exception) {
                System.out.println("Неверный формат даты");
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
        return result;
    }
}
