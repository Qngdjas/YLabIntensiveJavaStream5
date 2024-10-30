package ru.qngdjas.habitstracker.domain.repository;

import ru.qngdjas.habitstracker.domain.model.Habit;

import java.time.LocalDate;

/**
 * Интерфейс репозитория для обработки статистики выполнения привычек {@link Habit}.
 */
public interface IHabitNotesRepository {

    /**
     * Метод отметки о выполнении привычки {@link Habit}.
     *
     * @param habitID Идентификатор привычки.
     * @param date    Дата выполнения.
     * @return Дата выполнения.
     */
    LocalDate note(long habitID, LocalDate date);

    /**
     * Метод получения текущей серии выполнения привычек.
     *
     * @param habitID Идентификатор привычки.
     * @return Количество подряд выполненных привычек за последнюю серию.
     */
    String getStreak(long habitID);

    /**
     * Метод получения процента успешного выполнения конкретной привычки за период.
     *
     * @param habitID   Идентификатор привычки.
     * @param beginDate Дата начала периода.
     * @param endDate   Дата завершения периода.
     * @return Процент успешного выполнения привычки.
     */
    double getHit(long habitID, LocalDate beginDate, LocalDate endDate);

}
