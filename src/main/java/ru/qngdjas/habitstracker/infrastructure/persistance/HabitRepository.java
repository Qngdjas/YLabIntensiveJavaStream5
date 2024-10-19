package ru.qngdjas.habitstracker.infrastructure.persistance;

import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.repository.IHabitRepository;
import ru.qngdjas.habitstracker.infrastructure.external.postgres.ConnectionManager;

import java.sql.*;
import java.util.*;

public class HabitRepository implements IHabitRepository {

    @Override
    public Habit create(Habit instance) {
        String sql = "INSERT INTO entity.habits (habit_name, description, daily_flag, user_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, instance.getName());
            preparedStatement.setString(2, instance.getDescription());
            preparedStatement.setBoolean(3, instance.isDaily());
            preparedStatement.setLong(4, instance.getUserID());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            instance.setID(resultSet.getLong(1));
            System.out.println("Привычка добавлена");
        } catch (SQLException exception) {
            System.out.printf("Не удалось добавить привычку:\n%s\n", exception);
        }
        return instance;
    }

    @Override
    public Habit update(Habit instance) {
        String sql = "UPDATE entity.habits AS h SET (habit_name, description, daily_flag) = (?, ?, ?) WHERE h.habit_id = ?";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, instance.getName());
            preparedStatement.setString(2, instance.getDescription());
            preparedStatement.setBoolean(3, instance.isDaily());
            preparedStatement.setLong(4, instance.getID());
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                System.out.println("Привычка обновлена");
                return instance;
            }
        } catch (SQLException exception) {
            System.out.printf("Не удалось обновить привычку:\n%s\n", exception);
        }
        return null;
    }

    @Override
    public Habit retrieve(long id) {
        String sql = "SELECT * FROM entity.habits AS h WHERE h.habit_id = ?";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return new Habit(
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getBoolean(4),
                        result.getDate(5).toLocalDate(),
                        result.getLong(6)
                );
            }
        } catch (SQLException exception) {
            System.out.printf("Не удалось получить привычку:\n%s\n", exception);
        }
        return null;
    }

    @Override
    public Habit retrieveByUserIDAndName(long userID, String name) {
        String sql = "SELECT * FROM entity.habits AS h WHERE h.user_id = ? AND h.habit_name = ?)";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userID);
            preparedStatement.setString(2, name);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return new Habit(
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getBoolean(4),
                        result.getDate(5).toLocalDate(),
                        result.getLong(6)
                );
            }
        } catch (SQLException exception) {
            System.out.printf("Не удалось получить привычку:\n%s\n", exception);
        }
        return null;
    }


    @Override
    public List<Habit> list() {
        throw new UnsupportedOperationException("Метод не поддерживается");
    }

    @Override
    public List<Habit> listByUserID(long userID) {
        List<Habit> habits = new ArrayList<>();
        String sql = "SELECT * FROM entity.habits AS h WHERE h.user_id = ?";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userID);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                habits.add(new Habit(
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getBoolean(4),
                        result.getDate(5).toLocalDate(),
                        result.getLong(6)
                ));
            }
        } catch (SQLException exception) {
            System.out.printf("Не удалось получить привычки:\n%s\n", exception);
        }
        return habits;
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM entity.habits AS h WHERE h.habit_id = ?";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            System.out.printf("Не удалось удалить привычку:\n%s\n", exception);
        }
    }

    @Override
    public boolean isExists(long userID, String habitName) {
        boolean isExists = false;
        String sql = "SELECT EXISTS (SELECT h.habit_id FROM entity.habits AS h WHERE h.user_id = ? AND h.habit_name = ?)";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userID);
            preparedStatement.setString(2, habitName);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            isExists = resultSet.getBoolean(1);
        } catch (SQLException exception) {
            System.out.printf("Не удалось получить данные о привычке:\n%s\n", exception);
        }
        return isExists;
    }

}
