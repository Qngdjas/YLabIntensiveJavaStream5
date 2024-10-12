package ru.qngdjas.habitstracker.repository;

import ru.qngdjas.habitstracker.model.Habit;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class StatisticRepository {

    private static final Map<Habit, TreeSet<LocalDate>> habitStatistic = new HashMap<>();

    public void note(Habit habit, LocalDate noteDate) {
        if (!habitStatistic.containsKey(habit)) {
            habitStatistic.put(habit, new TreeSet<>());
        }
        habitStatistic.get(habit).add(noteDate);
    }

    public long getStreak(Habit habit) {
        int streak = 0;
        if (habitStatistic.containsKey(habit)) {
            Iterator<LocalDate> marks = habitStatistic.get(habit).descendingIterator();
            LocalDate previous = marks.next();
            streak = 1;
            while (marks.hasNext()) {
                LocalDate current = marks.next();
                if (Math.abs(ChronoUnit.DAYS.between(previous, current)) > habit.getRange()) {
                    break;
                }
                streak++;
                previous = current;
            }
        }
        return streak;
    }

    public double getHit(Habit habit, LocalDate beginDate, LocalDate endDate) {
        if (!beginDate.isAfter(endDate)) {
            long marked = 0, total = 0;
            for (LocalDate date = beginDate; !date.isAfter(endDate); date = date.plusDays(habit.getRange())) {
                if (isNoted(habit, date)) {
                    marked++;
                }
                total++;
            }
            return (double) marked / total * 100.0f;
        }
        throw new IllegalArgumentException("Дата конца периода не может быть меньше даты начала периода");
    }

    public boolean isNoted(Habit habit, LocalDate date) {
        if (habitStatistic.containsKey(habit)) {
            TreeSet<LocalDate> marks = habitStatistic.get(habit);
            // Если есть совпадения ключей, то значит отметка присутствует
            if (marks.contains(date)) {
                return true;
            }
            // Если дата попадает в период, то отметка засчитывается
            for (LocalDate mark : marks) {
                if (date.isAfter(mark) && date.isBefore(mark.plusDays(habit.getRange()))) {
                    return true;
                }
            }
        }
        return false;
    }
}
