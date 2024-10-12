package ru.qngdjas.habitstracker.repository;

import ru.qngdjas.habitstracker.model.Habit;
import ru.qngdjas.habitstracker.model.User;

import java.util.*;

public class HabitRepository {

    private final static Map<User, Map<String, Habit>> habits = new HashMap<>();

    public void addHabit(User user, Habit habit) {
        if (!habits.containsKey(user)) {
            habits.put(user, new HashMap<>());
        }
        habits.get(user).put(habit.getName(), habit);
    }

    public Habit getHabit(User user, String habitName) {
        if (habits.containsKey(user)) {
            return habits.get(user).get(habitName);
        }
        return null;
    }

    public List<Habit> getHabits(User user) {
        if (habits.containsKey(user)) {
            return new ArrayList<>(habits.get(user).values());
        }
        return new ArrayList<>();
    }

    public Habit deleteHabit(User user, String habitName) {
        if (habits.containsKey(user)) {
            return habits.get(user).remove(habitName);
        }
        return null;
    }

    public boolean isExists(User user, String habit) {
        return habits.containsKey(user) && habits.get(user).containsKey(habit);
    }

}
