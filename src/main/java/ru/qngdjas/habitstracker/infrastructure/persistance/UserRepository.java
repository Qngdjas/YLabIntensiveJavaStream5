package ru.qngdjas.habitstracker.infrastructure.persistance;

import ru.qngdjas.habitstracker.domain.model.user.EmailException;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.repository.IUserRepository;
import ru.qngdjas.habitstracker.infrastructure.external.postgres.ConnectionManager;

import java.sql.*;
import java.util.List;

public class UserRepository implements IUserRepository {

    @Override
    public User create(User instance) {
        String sql = "INSERT INTO entity.users (email, password, user_name, admin_flag) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, instance.getEmail());
            preparedStatement.setString(2, instance.getPassword());
            preparedStatement.setString(3, instance.getName());
            preparedStatement.setBoolean(4, instance.isAdmin());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            instance.setID(resultSet.getLong(1));
            System.out.printf("Пользователь %s зарегистрирован\n", instance.getEmail());
        } catch (SQLException exception) {
            System.out.printf("Не удалось добавить пользователя:\n%s\n", exception);
        }
        return instance;
    }

    @Override
    public User update(User instance) {
        String sql = "UPDATE entity.users AS u SET (email, password, user_name, admin_flag) = (?, ?, ?, ?) WHERE u.user_id = ?";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, instance.getEmail());
            preparedStatement.setString(2, instance.getPassword());
            preparedStatement.setString(3, instance.getName());
            preparedStatement.setBoolean(4, instance.isAdmin());
            preparedStatement.setLong(5, instance.getID());
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                System.out.printf("Пользователь %s обновлен\n", instance.getEmail());
                return instance;
            }
        } catch (SQLException exception) {
            System.out.printf("Не удалось обновить пользователя:\n%s\n", exception);
        }
        return null;
    }

    @Override
    public User retrieve(long id) {
        String sql = "SELECT * FROM entity.users AS u WHERE u.user_id = ?";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return new User(
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getBoolean(5)
                );
            }
        } catch (SQLException | EmailException exception) {
            System.out.printf("Не удалось получить пользователя:\n%s\n", exception);
        }
        return null;
    }

    @Override
    public User retrieveByEmail(String email) {
        String sql = "SELECT * FROM entity.users AS u WHERE u.email = ?";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getBoolean(5));
            }
        } catch (SQLException | EmailException exception) {
            System.out.printf("Не удалось получить пользователя:\n%s\n", exception);
        }
        return null;
    }

    @Override
    public List<User> list() {
        throw new UnsupportedOperationException("Метод не поддерживается");
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM entity.users AS u WHERE u.user_id = ?";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            System.out.printf("Не удалось удалить пользователя:\n%s\n", exception);
        }
    }

    @Override
    public boolean isExists(String email) {
        boolean isExists = false;
        String sql = "SELECT EXISTS (SELECT u.user_id FROM entity.users AS u WHERE u.email = ?)";
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            isExists = resultSet.getBoolean(1);
        } catch (SQLException exception) {
            System.out.printf("Не удалось получить данные о пользователе:\n%s\n", exception);
        }
        return isExists;
    }
}
