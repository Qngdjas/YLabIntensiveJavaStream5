package ru.qngdjas.habitstracker.infrastructure.service;

import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.repository.IHabitRepository;
import ru.qngdjas.habitstracker.domain.repository.IHabitNotesRepository;
import ru.qngdjas.habitstracker.infrastructure.persistance.HabitRepository;
import ru.qngdjas.habitstracker.infrastructure.persistance.HabitNotesRepository;
import ru.qngdjas.habitstracker.infrastructure.session.Session;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class HabitService extends Service {

    private static final IHabitRepository habitRepository = new HabitRepository();
    private static final IHabitNotesRepository statisticRepository = new HabitNotesRepository();

    public Habit add(String habitName, String description, boolean isDaily) {
        if (isAuth()) {
            User user = Session.getInstance().getUser();
            if (!habitRepository.isExists(user.getID(), habitName)) {
                Habit habit = new Habit(habitName, description, isDaily, user.getID());
                habitRepository.create(habit);
                return habit;
            }
            System.out.printf("Привычка %s у пользователя %s уже существует", habitName, user.getEmail());
        }
        return null;
    }

    public Habit update(String oldHabitName, String habitName, String description, boolean isDaily) {
        if (isAuth()) {
            User user = Session.getInstance().getUser();
            if (!habitRepository.isExists(user.getID(), habitName)) {
                Habit habit = habitRepository.retrieveByUserIDAndName(user.getID(), oldHabitName);
                if (habit != null) {
                    habit.setName(habitName);
                    habit.setDescription(description);
                    habit.setDaily(isDaily);
                    habitRepository.update(habit);
                    System.out.printf("Привычка %s обновлена\n", habit.getName());
                    return habit;
                }
                System.out.printf("Привычка %s у пользователя %s не найдена", oldHabitName, user.getEmail());
            } else {
                System.out.printf("Привычка %s уже существует", habitName);
            }
        }
        return null;
    }

    public Habit delete(String habitName) {
        if (isAuth()) {
            User user = Session.getInstance().getUser();
            Habit habit = habitRepository.retrieveByUserIDAndName(user.getID(), habitName);
            if (habit != null) {
                habitRepository.delete(habit.getID());
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
            Habit habit = habitRepository.retrieveByUserIDAndName(user.getID(), habitName);
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
                List<Habit> habits = habitRepository.listByUserID(user.getID());
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
                    return statisticRepository.note(habit.getID(), noteDate);
                }
            } catch (DateTimeParseException exception) {
                System.out.println("Неверный формат даты");
            }
        }
        return null;
    }

    public Map<String, Long> getStreak() {
        Map<String, Long> streaks = new HashMap<>();
        if (isAuth()) {
            List<Habit> habits = getAll();
            for (Habit habit : habits) {
                streaks.put(habit.getName(), statisticRepository.getStreak(habit.getID()));
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
                    return statisticRepository.getHit(habit.getID(), noteBeginDate, noteEndDate);
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
                    result.put(habit, statisticRepository.getHit(habit.getID(), noteBeginDate, noteEndDate));
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
