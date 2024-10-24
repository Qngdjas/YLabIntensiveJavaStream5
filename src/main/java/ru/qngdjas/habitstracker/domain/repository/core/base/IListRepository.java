package ru.qngdjas.habitstracker.domain.repository.core.base;

import java.util.List;

/**
 * Интерфейс получения списка объектов репозитория.
 *
 * @param <T> Тип получаемых объектов.
 */
public interface IListRepository<T> {

    /**
     * Метод получения списка объектов из БД.
     *
     * @return Список объектов.
     */
    List<T> list();
}
