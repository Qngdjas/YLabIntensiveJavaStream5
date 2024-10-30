package ru.qngdjas.habitstracker.infrastructure.persistance;

import ru.qngdjas.habitstracker.domain.repository.IHabitNotesRepository;
import ru.qngdjas.habitstracker.infrastructure.external.postgres.ConnectionManager;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;

/**
 *
 */
public class HabitNotesRepository implements IHabitNotesRepository {

    @Override
    public LocalDate note(long habitID, LocalDate noteDate) {
        String sql = "INSERT INTO habit_notes (noted_date, habit_id) VALUES (?, ?)";
        Connection connection = ConnectionManager.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(noteDate));
            preparedStatement.setLong(2, habitID);
            preparedStatement.executeUpdate();
            System.out.println("Привычка отмечена");
            return noteDate;
        } catch (SQLException exception) {
            System.out.printf("Не удалось отметить привычку:\n%s\n", exception);
        }
        return null;
    }

    @Override
    public String getStreak(long habitID) {
        int streak = 0;
        String sql = "CALL getactualhabitstreak(?, ?)";
        Connection connection = ConnectionManager.getInstance().getConnection();
        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.setInt(1, (int) habitID);
            callableStatement.setInt(2, streak);
            callableStatement.registerOutParameter(2, Types.INTEGER);
            callableStatement.execute();
            streak = callableStatement.getInt(2);
        } catch (SQLException exception) {
            System.out.printf("Не удалось подсчитать серию выполнения:\n%s\n", exception);
        }
        return String.valueOf(streak);
    }

    @Override
    public double getHit(long habitID, LocalDate beginDate, LocalDate endDate) {
        if (!beginDate.isAfter(endDate)) {
            double hit = 0.0;
            String sql = "CALL gethabithit(?, ?, ?, ?)";
            Connection connection = ConnectionManager.getInstance().getConnection();
            try (CallableStatement callableStatement = connection.prepareCall(sql)) {
                callableStatement.setInt(1, (int) habitID);
                callableStatement.setDate(2, Date.valueOf(beginDate));
                callableStatement.setDate(3, Date.valueOf(endDate));
                callableStatement.setDouble(4, hit);
                callableStatement.registerOutParameter(4, Types.DOUBLE);
                callableStatement.execute();
                hit = callableStatement.getDouble(4);
            } catch (SQLException exception) {
                System.out.printf("Не удалось подсчитать серию выполнения:\n%s\n", exception);
            }
            return hit;
        }
        throw new IllegalArgumentException("Дата конца периода не может быть меньше даты начала периода");
    }

}
