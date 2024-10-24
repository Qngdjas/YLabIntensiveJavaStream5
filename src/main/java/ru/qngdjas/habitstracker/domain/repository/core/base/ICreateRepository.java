package ru.qngdjas.habitstracker.domain.repository.core.base;

/**
 * Интерфейс создания объектов репозитория.
 *
 * @param <T> Тип создаваемого объекта.
 */
public interface ICreateRepository<T> {

    /**
     * Метод создания объекта в БД.
     *
     * @param instance Объект для создания.
     * @return Созданный объект с присвоенным ID.
     */
    T create(T instance);
}
