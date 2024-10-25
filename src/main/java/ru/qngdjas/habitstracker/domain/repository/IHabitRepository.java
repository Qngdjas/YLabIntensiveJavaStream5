package ru.qngdjas.habitstracker.domain.repository;

import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.repository.core.model.IModelRepository;

import java.util.List;

/**
 * Интерфейс репозитория для обработки привычек {@link Habit}.
 */
public interface IHabitRepository extends IModelRepository<Habit> {

    /**
     * Метод получения модели {@link Habit} по идентификатору пользователя и наименованию привычки.
     *
     * @param userID    Идентификатор пользователя.
     * @param habitName Наименование привычки.
     * @return Объект модели {@link Habit}.
     */
    Habit retrieveByUserIDAndName(long userID, String habitName);

    /**
     * Метод получения списка привычек конкретного пользователя.
     *
     * @param userID Идентификатор пользователя.
     * @return Список привычек пользователя.
     */
    List<Habit> listByUserID(long userID);

    /**
     * Проверка существования привычки по идентификатору пользователя и наименованию привычки.
     *
     * @param userID    Идентификатор пользователя.
     * @param habitName Наименование привычки.
     * @return {@code true} если привычка с указанными параметрами существует.
     */
    boolean isExists(long userID, String habitName);
}
