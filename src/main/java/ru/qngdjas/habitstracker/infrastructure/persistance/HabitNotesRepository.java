package ru.qngdjas.habitstracker.infrastructure.persistance;

import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.repository.IHabitNotesRepository;
import ru.qngdjas.habitstracker.infrastructure.external.postgres.ConnectionManager;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class HabitNotesRepository implements IHabitNotesRepository {

    @Override
    public LocalDate note(long habitID, LocalDate noteDate) {
        String sql = "INSERT INTO habit_notes (noted_date, habit_id) VALUES (?, ?)";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, Date.valueOf(noteDate));
            preparedStatement.setLong(2, habitID);
            preparedStatement.executeUpdate();
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
            callableStatement.setLong(1, habitID);
            ResultSet resultSet = callableStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException exception) {
            System.out.printf("Не удалось подсчитать серию выполнения:\n%s\n", exception);
        }
        return streak;
    }

    @Override
    public double getHit(long habitID, LocalDate beginDate, LocalDate endDate) {
        if (!beginDate.isAfter(endDate)) {
            double hit = 0.0;
            String sql = "CALL GetHabitHit(?, ?, ?)";
            try (Connection connection = ConnectionManager.getInstance().getConnection()) {
                CallableStatement callableStatement = connection.prepareCall(sql);
                callableStatement.setLong(1, habitID);
                ResultSet resultSet = callableStatement.executeQuery();
                resultSet.next();
                hit = resultSet.getDouble(1);
            } catch (SQLException exception) {
                System.out.printf("Не удалось подсчитать серию выполнения:\n%s\n", exception);
            }
            return hit;
        }
        throw new IllegalArgumentException("Дата конца периода не может быть меньше даты начала периода");
    }

}
