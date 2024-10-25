package ru.qngdjas.habitstracker.domain.repository.core.base;

/**
 * Интерфейс обновления объектов репозитория.
 *
 * @param <T> Тип обновляемого объекта.
 */
public interface IUpdateRepository<T> {

    /**
     * Метод обновления объекта в БД.
     *
     * @param instance Обновленный объект.
     * @return Обновленный объект с ранее присвоенным ID.
     */
    T update(T instance);

}
