package ru.qngdjas.habitstracker.infrastructure.persistance;

import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.repository.IStatisticRepository;
import ru.qngdjas.habitstracker.infrastructure.external.postgres.ConnectionManager;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class StatisticRepository implements IStatisticRepository {

    private static final Map<Habit, TreeSet<LocalDate>> habitStatistic = new HashMap<>();

    @Override
    public LocalDate note(long habitID, LocalDate noteDate) {
        String sql = "INSERT INTO habit_notes (noted_date, habit_id) VALUES (?, ?)";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, Date.valueOf(noteDate));
            preparedStatement.setLong(2, habitID);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            System.out.println("Привычка отмечена");
            return noteDate;
        } catch (SQLException exception) {
            System.out.printf("Не удалось добавить привычку:\n%s\n", exception);
        }
        return null;
    }

    @Override
    public long getStreak(long habitID) {
        int streak = 0;
        String sql = "CALL getactualhabitstreak(?)";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            CallableStatement callableStatement = connection.prepareCall(sql);

        } catch (SQLException exception) {
            System.out.printf("Не удалось подсчитать серию выполнения:\n%s\n", exception);
        }
        return streak;
    }

    @Override
    public double getHit(long habitID, LocalDate beginDate, LocalDate endDate) {
//        if (!beginDate.isAfter(endDate)) {
//            long marked = 0, total = 0;
//            for (LocalDate date = beginDate; !date.isAfter(endDate); date = date.plusDays(habit.getRange())) {
//                if (isNoted(habit, date)) {
//                    marked++;
//                }
//                total++;
//            }
//            return (double) marked / total * 100.0f;
//        }
        throw new IllegalArgumentException("Дата конца периода не может быть меньше даты начала периода");
    }

    @Override
    public boolean isNoted(long habitID, LocalDate date) {
//        if (habitStatistic.containsKey(habit)) {
//            TreeSet<LocalDate> marks = habitStatistic.get(habit);
//            // Если есть совпадения ключей, то значит отметка присутствует
//            if (marks.contains(date)) {
//                return true;
//            }
//            // Если дата попадает в период, то отметка засчитывается
//            for (LocalDate mark : marks) {
//                if (date.isAfter(mark) && date.isBefore(mark.plusDays(habit.getRange()))) {
//                    return true;
//                }
//            }
//        }
        return false;
    }


}
