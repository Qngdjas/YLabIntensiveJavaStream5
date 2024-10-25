package ru.qngdjas.habitstracker.domain.repository.core.base;

/**
 * Интерфейс получения конкретных объектов репозитория.
 *
 * @param <T> Тип получаемых объектов.
 */
public interface IRetrieveRepository<T> {

    /**
     * Метод получения конкретного объекта из БД.
     *
     * @return Объект, найденный по идентификатору.
     */
    T retrieve(long id);
}
