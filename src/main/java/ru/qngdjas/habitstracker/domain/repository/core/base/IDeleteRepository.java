package ru.qngdjas.habitstracker.domain.repository.core.base;

/**
 * Интерфейс удаления объектов репозитория.
 */
public interface IDeleteRepository {

    /**
     * Метод удаления объекта в БД.
     *
     * @param id Идентификатор объекта для удаления.
     */
    void delete(long id);

}
