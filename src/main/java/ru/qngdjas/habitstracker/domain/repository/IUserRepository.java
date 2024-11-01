package ru.qngdjas.habitstracker.domain.repository;

import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.repository.core.model.IModelRepository;

/**
 * Интерфейс репозитория для обработки пользователей {@link User}.
 */
public interface IUserRepository extends IModelRepository<User> {

    /**
     * Метод получения модели {@link User} по адресу электронной почты.
     *
     * @param email Адрес электронной почты.
     * @return Объект модели {@link User}.
     */
    User retrieveByEmail(String email);

    /**
     * Проверка существования пользователя по адресу электронной почты.
     *
     * @param email Адрес электронной почты.
     * @return {@code true} если пользователь с указанным email существует.
     */
    boolean isExists(String email);
}
