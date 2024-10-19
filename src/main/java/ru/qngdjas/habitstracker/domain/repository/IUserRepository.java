package ru.qngdjas.habitstracker.domain.repository;

import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.repository.core.model.IModelRepository;

public interface IUserRepository extends IModelRepository<User> {

    User retrieveByEmail(String email);

    boolean isExists(String email);
}
